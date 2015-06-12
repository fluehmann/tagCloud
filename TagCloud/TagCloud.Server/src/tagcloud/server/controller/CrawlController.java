package tagcloud.server.controller;

import java.util.List;

import tagcloud.core.Adapter;
import tagcloud.indexer.IndexAdapter;
import tagcloud.webcrawler.WebCrawler;

public class CrawlController {
	IndexAdapter adapter;
	

	public CrawlController(){
		adapter = new Adapter("elasticsearch", "127.0.0.1");
	}
	
	public List<String> crawl(String hostname){
		return new WebCrawler().crawl(hostname);
	}
}
