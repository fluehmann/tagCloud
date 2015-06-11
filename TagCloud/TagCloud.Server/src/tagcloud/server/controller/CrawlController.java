package tagcloud.server.controller;

import tagcloud.core.Adapter;
import tagcloud.indexer.IndexAdapter;
import tagcloud.webcrawler.WebCrawler;

public class CrawlController {
	IndexAdapter adapter;
	

	public CrawlController(){
		adapter = new Adapter("elasticsearch", "127.0.0.1");
	}
	
	public void crawl(String hostname){
		new WebCrawler().crawl(hostname);
	}
}
