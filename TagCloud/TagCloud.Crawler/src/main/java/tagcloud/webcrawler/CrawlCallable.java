package tagcloud.webcrawler;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import tagcloud.indexer.IndexAdapter;
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
	private final IndexAdapter x;
	private final String hostname;

	public CrawlCallable(String hostname, String startURL, IndexAdapter x) {
		this.startURL = startURL;
		this.x = x;
		this.hostname = hostname;
	}

	/**
	 * Method to crawl a specific website.
	 * @returns: A list with new links to be crawled
	 */
	public List<String> call() throws Exception {

		// Open connection to startURL
		URL url = new URL(startURL);
		URLConnection conn = url.openConnection();
		conn.setRequestProperty("User-Agent", "TagCloudWebCrawler/0.1 Mozilla/5.0");

		// list to store links
		List<String> extractedLinks = new LinkedList<String>();

		// check if content of URL is HTML
		String contentType = conn.getContentType();
		if (contentType != null && contentType.startsWith("text/html")) {

			// Build up InputStream to start loading source
			BufferedInputStream pageInputStream = null;

			try {
				pageInputStream = new BufferedInputStream(conn.getInputStream());
				Document doc = Jsoup.parse(pageInputStream, null, startURL);
				// remove useless parts of website
				doc.select("head, script, style, link, .hidden").remove();

				// extract all links out of the websites source and add them to a list
				Elements urls = doc.select("a[href]");
				for (Element link : urls) {
					String linkString = link.absUrl("href");
					if (linkString.startsWith("http")) {
						extractedLinks.add(linkString);
					}
				}

				// Send the source to the cleaner to clean it and store it in the ES index
				new Cleaner(x,doc,startURL,hostname);


			} catch (Exception e){
				System.err.println("Error ocurred" + e.getMessage());


			} finally {
				// IOUtils closes the stream without caring about exceptions and errors
				IOUtils.closeQuietly(pageInputStream);
				
				//pageInputStream.close();
			}
		}

		// return the list with all the extracted links from the website
		return extractedLinks;
	}
}
