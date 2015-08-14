package tagcloud.webcrawler;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import tagcloud.indexer.IIndexer;
import tagcloud.preprocessing.Cleaner;

public class SimplePageParser {

	private final IIndexer indexer;
	private final String hostname;
	
	public SimplePageParser(String hostname, IIndexer indexer) {
		this.indexer = indexer;
		this.hostname = hostname;
	}
	
	/**
	 * @deprecated - use @CrawlCallable with @Webcrawler concurrent implementation instead
	 * Encapsulates task of a single-threaded webcrawler.
	 * 
	 * @param startURL
	 */
	public Set<String> parsePageByUrlAndGetLinks(String startURL) throws Exception {

		// open connection to startURL
		URL url = new URL(startURL);
		URLConnection conn = url.openConnection();
		conn.setRequestProperty("User-Agent", "TagCloudWebCrawler/0.1 Mozilla/5.0");

		// list to store links
		Set<String> extractedLinks = new HashSet<String>();

		// check if content of URL is HTML
		String contentType = conn.getContentType();
		if (contentType != null && contentType.startsWith("text/html")) {

			BufferedInputStream pageInputStream = null;
			try {
				pageInputStream = new BufferedInputStream(conn.getInputStream());
				Document doc = Jsoup.parse(pageInputStream, null, startURL);
				
				// extract all links out of src
				Elements urls = doc.select("a[href]");
				for (Element link : urls) {
					String linkString = link.absUrl("href");
					if (linkString.startsWith("http")) {
						linkString.toLowerCase();
						extractedLinks.add(linkString);
					}
				}
			
			// start cleaning content and send data over to Elasticsearch
			new Cleaner(indexer, doc,startURL, hostname);
			
			} catch (Exception e){
				System.err.println("Crawling Error in SimplePageParser: " + startURL + ": " + e.getMessage());
				
			} finally {
				pageInputStream.close();
			}
		}
		return extractedLinks;
	}
}
