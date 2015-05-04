package org.Crawling;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.concurrent.Callable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlCallable implements Callable {
	
	private final String startURL;
	
	public CrawlCallable(String startURL) {
		this.startURL = startURL;
	}

	public HashMap<String, Document> call() throws Exception {
		
		// Open connection to startURL
		URL url = new URL(startURL);
		URLConnection conn = url.openConnection();
		conn.setRequestProperty("User-Agent", "TagCloudCrawler/0.1 Mozilla/5.0");
		
		// Initialize Hashmap to store websites
		HashMap<String, Document> content = new HashMap<String, Document>();
		
		// check if content of URL is HTML
		String contentType = conn.getContentType();
		if (contentType != null && contentType.startsWith("text/html")) {
			BufferedInputStream webInputStream = null;
		
			try {
				webInputStream = new BufferedInputStream(conn.getInputStream());
				Document doc = Jsoup.parse(webInputStream, null, startURL);
				
				Elements links = doc.select("a[href]");
//				for (Element link : links) {
//				String linkString = link.absUrl("href"); if (linkString.startsWith("http")) {
//				content.add(linkString); }
				

		
	}  finally {
//		IOUtils.closeQuietly(is); }
	}
	}
		return content;
}
}
