package org.Crawler;

import java.util.ArrayList;

import org.Interfaces.CrawledContent;
import org.Interfaces.Crawler;
import org.Interfaces.CrawlerType;



public class WebCrawler implements Crawler {
	
	private String StartURL;
	private ArrayList<String> urlsVisited;
	private ArrayList<String> urlsToVisit;
	
	// constructor
	public WebCrawler (String startURL){
		this.StartURL = StartURL;
		crawl(StartURL);
	}
	
	public CrawledContent crawl(String uri) {
		
		// start new crawler and crawl each page
		// while hasnext crawl
		
		
		return null;
	}

}
