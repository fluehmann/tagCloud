package tagcloud.webcrawler;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.concurrent.Callable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * A task that returns a result and may throw an exception. A Callable returns a
 * result and can throw a checked exception.
 * 
 * @author Simon Flühmann
 * @param <V>
 *            the result type of method {@code call}
 */

public class CrawlCallable implements Callable {

	private final String startURL;

	public CrawlCallable(String startURL) {
		this.startURL = startURL;
	}

	// nimmt als Parameter eine URL
	// gibt die URL zurück und die Page als Document
	public HashMap<String, Document> call() throws Exception {

		// Open connection to startURL
		URL url = new URL(startURL);
		URLConnection conn = url.openConnection();
		conn.setRequestProperty("User-Agent", "TagCloudCrawler/0.1 Mozilla/5.0");

		// Initialize Hashmap to store websites & to check progress in URLs
		HashMap<String, Document> content = new HashMap<String, Document>();

		// check if content of URL is HTML
		String contentType = conn.getContentType();
		if (contentType != null && contentType.startsWith("text/html")) {
			BufferedInputStream pageInputStream = null;

			try {
				pageInputStream = new BufferedInputStream(conn.getInputStream());

				Document doc = Jsoup.parse(pageInputStream, null, startURL);

				content.put(startURL, doc);

			} finally {
				pageInputStream.close();
			}
		}

		return content;
	}
}
