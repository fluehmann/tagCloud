package org.CrawlerService;

import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.Callable;

import org.jsoup.nodes.Document;

public class CrawlCallable implements Callable {
	
	private final String startURL;
	
	public CrawlCallable(String startURL) {
		this.startURL = startURL;
	}

	public HashMap<String, Document> call() throws Exception {
		
		URL url = new URL(startURL);
		
		
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
