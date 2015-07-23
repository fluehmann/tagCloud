package tagcloud.server.controller;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import tagcloud.core.Adapter;
import tagcloud.indexer.IndexAdapter;
import tagcloud.webcrawler.WebCrawler;

public class CrawlController {
	IndexAdapter adapter;
	

	public CrawlController(){
		adapter = new Adapter("elasticsearch", "127.0.0.1");
	}
	

	
	public void crawl(String hostname){
		// Prevent XSS-Attacks/unwanted input by cleaning user input
		String safeUrl = Jsoup.clean(hostname, Whitelist.basic());
		new WebCrawler(safeUrl).crawl(safeUrl);
	}
}
