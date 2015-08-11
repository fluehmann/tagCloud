package tagcloud.webcrawler;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import tagcloud.indexer.IIndexer;
import tagcloud.preprocessing.Cleaner;

/**
 * The CrawlCallable contains the workload that has to be executed by one thread.
 * The Callable returns a Hashmap with all the outgoing links it has found on a website.
 * The list of Links will then be inserted into the queue of links to visit.
 * The CrawlCallable will transmit the content of a website to the cleaner class
 * where it will be cleaned and stored to the ElasticSearch Index.
 * 
 * @param hostname: the name of the website
 * @param startURL: the URL that needs to be crawled
 * @param x: the IndexAdapter object that specifies the index where the site has to be stored
 * 
 * @author: Simon Flühmann, FHWN, June 2015
 */
public class CrawlCallable implements Callable<List<String>> {

	private final String startURL;
	private final IIndexer indexer;
	private final String hostname;

	public CrawlCallable(String hostname, String startURL, IIndexer indexer) {
		this.startURL = startURL;
		this.indexer = indexer;
		this.hostname = hostname;
	}

	/**
	 * Call-Method of the Callable representing the task to crawl a specific website.
	 * @returns: A list with new links to be crawled
	 */
	public List<String> call() throws Exception {

		// open connection to startURL
		URL url = new URL(startURL);
		URLConnection conn = url.openConnection();
		conn.setRequestProperty("User-Agent", "TagCloudWebCrawler/0.1 Mozilla/5.0");

		// set to store links that have been found on website
		Set<String> extractedLinks = new HashSet<String>();

		// check if content of URL is HTML
		String contentType = conn.getContentType();
		if (contentType != null && contentType.startsWith("text/html")) {

			// Build up InputStream to start loading source
			BufferedInputStream pageInputStream = null;
			
			try {
				pageInputStream = new BufferedInputStream(conn.getInputStream());
				Document doc = Jsoup.parse(pageInputStream, null, startURL);

				// remove useless parts of website
				doc.select("img, link, input, form, nav, header, footer, iframe, head, script, style, link, .hidden").remove();
				
				// extract all links out of the websites source and add them to a list
				Elements urls = doc.select("a[href]");
				for (Element link : urls) {
					String linkString = link.absUrl("href");
					if (linkString.startsWith("http") &&
							!linkString.endsWith(".jpg") &&
							!linkString.endsWith(".png") &&
							!linkString.endsWith(".gif") &&
							!linkString.contains("#") &&
							!linkString.contains("@") &&
							!linkString.contains("publikationen") &&
							!linkString.startsWith("http://www.fhnw.ch/ph") &&
							!linkString.endsWith("accessibility-info") &&
							!linkString.startsWith("http://www.fhnw.ch/ppt"))
							{
						extractedLinks.add(linkString);
					}
				}
				
				// Remove Menu-Elements
				doc.select("li a[href]").remove();
				doc.select("span").remove();
				
				// Send the source to the cleaner to clean it and store it in the ES index
				new Cleaner(indexer,doc,startURL,hostname);
				
			} catch (Exception e){
				System.err.println("Error ocurred while crawling in CrawlCallable: " + e.getMessage());
				extractedLinks = null;

			} finally {
				// IOUtils closes the stream without caring about exceptions and errors
				IOUtils.closeQuietly(pageInputStream);

				// alternative way to close the stream
				// pageInputStream.close();
			}
		}

		// convert Set to ArrayList to conform to Callable-Interface
		// return the list with all the extracted links from the website
		List<String> linkList = new ArrayList<String>(extractedLinks);
		return linkList;
	}
}
