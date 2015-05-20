package tagcloud.webcrawler;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import tagcloud.preprocessing.*;

/**
 * A task that returns a result and may throw an exception. The Callable returns
 * a Hashmap with the specified URL as the ID and the a doc that containing the
 * source of the webpage.
 * 
 * @author Simon Flühmann
 * @param <V>
 */

public class CrawlCallable implements Callable<List<String>> {

	private final String startURL;

	public CrawlCallable(String startURL) {
		this.startURL = startURL;
	}

	// one crawling task - executed by a thread
	// nimmt als Parameter eine URL
	// gibt die URL zurück und die Page als Document
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
			BufferedInputStream pageInputStream = null;

			try {
				pageInputStream = new BufferedInputStream(conn.getInputStream());
				Document doc = Jsoup.parse(pageInputStream, null, startURL);
				Elements urls = doc.select("a[href]");
				for (Element link : urls) {
					String linkString = link.absUrl("href");
					if (linkString.startsWith("http")) {
						extractedLinks.add(linkString);
//						System.out.println("new Link added to queue: " + linkString);
						}
				}
				
				new Cleaner(doc,startURL);
				
				
			} finally {
				pageInputStream.close();
			}
		}

		return extractedLinks;
	}
}
