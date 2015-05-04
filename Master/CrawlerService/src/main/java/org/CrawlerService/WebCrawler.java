/**
 * 
 */
package org.CrawlerService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Implementation of a Webcrawler
 * @author simonfluhmann
 *
 */
public class WebCrawler implements Crawler{

	private final int POOL_SIZE = 50;
	
	public WebCrawler(String url){
		
		// put this in webinterface!! not THReadsafe
		if(!url.startsWith("http")) {
			url =  "http://" + url;
			crawl(url);
		}
		
	}
	
//	return a hashmap => url als ID und page als 'Site' Objekt
	
	
	/**
     * Crawls the www starting at {@code src} 
     * @param src the URI to start crawling any resource
     * @return a list of uris that are reachable from {@code src}
     */
	public List<String> crawl(final String startUrl) {
		
		/* Contains all discovered urls. */
		final Set<String> urlsFound = new HashSet<String>();
		
		/* Contains the urls to be visited. */
		final Queue<String> urlsToVisit = new LinkedList<String>();
				
		urlsToVisit.add(startUrl);
		
		try {
			Document doc = Jsoup.connect("http://www.mit.edu/").get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(doc);

		
		
		return new ArrayList<String>();
		
	}
}
