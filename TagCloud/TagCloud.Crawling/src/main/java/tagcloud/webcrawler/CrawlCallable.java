package tagcloud.webcrawler;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.concurrent.Callable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * A task that returns a result and may throw an exception. The Callable returns
 * a Hashmap with the specified URL as the ID and the a doc that containing the
 * source of the webpage.
 * 
 * @author Simon Flühmann
 * @param <V>
 */

public class CrawlCallable implements Callable<Object> {

	private final String startURL;

	public CrawlCallable(String startURL) {
		this.startURL = startURL;
	}

	// nimmt als Parameter eine URL
	// gibt die URL zurück und die Page als Document
	public HashMap<String, webPage> call() throws Exception {

		// Open connection to startURL
		URL url = new URL(startURL);
		URLConnection conn = url.openConnection();
		conn.setRequestProperty("User-Agent", "TagCloudWebCrawler/0.1 Mozilla/5.0");

		// Initialize Hashmap to store websites & to check progress in URLs
		HashMap<String, webPage> entry = new HashMap<String, webPage>();

		// check if content of URL is HTML
		String contentType = conn.getContentType();
		if (contentType != null && contentType.startsWith("text/html")) {
			BufferedInputStream pageInputStream = null;

			try {
				pageInputStream = new BufferedInputStream(conn.getInputStream());

				// fetch source from URL into doc
				Document doc = Jsoup.parse(pageInputStream, null, startURL);

				// create new webpage Object to put into hashmap
				webPage pageX = new webPage(startURL, doc);
				entry.put(startURL, pageX);				
				

			} finally {
				pageInputStream.close();
			}
		}

		return entry;
	}
}
