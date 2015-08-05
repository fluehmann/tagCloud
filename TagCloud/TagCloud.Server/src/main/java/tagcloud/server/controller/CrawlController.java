package tagcloud.server.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import tagcloud.core.Adapter;
import tagcloud.doccrawler.FileCrawler;
import tagcloud.indexer.IIndexer;
import tagcloud.indexer.IndexerImpl;
import tagcloud.webcrawler.WebCrawler;

public class CrawlController {
//	IIndexer adapter;
	protected IIndexer indexer;

	public CrawlController(HttpServletResponse response, String uri, String type){
//		adapter = new Adapter("elasticsearch", "127.0.0.1");
		this.indexer = new IndexerImpl("elasticsearch", "127.0.0.1");

		if (uri.equals("") || uri.equals(null)) {
			uri = "empty uri";
		}
		
		// Prevent XSS-Attacks/unwanted input by cleaning user input
		String safeUri = Jsoup.clean(uri, Whitelist.basic()).toLowerCase();
		
		// crawl only website or file exist
		if (type.equals("website") && (websiteExists(safeUri))) {
			new WebCrawler(safeUri).crawl(safeUri);
			success(response);
			
		} else if (type.equals("file") && typeExists(safeUri) && new File(safeUri).exists()){
			new FileCrawler(safeUri).crawl(safeUri);
			success(response);
			
		} else {
			// open error page jsp
			error(response);
		}
	}
	
	
	// check if a entered url is a valid website
	public boolean websiteExists(String url) {
		boolean exists = true;
		try {
			Jsoup.connect(url).get();
		} catch (Exception e) {
			exists = false;
			System.err.println("URI not valid: " + e.getMessage());
		}
		return exists;
	}
	
	//check if a entered filetype is implemented
	public boolean typeExists(String path){
		boolean exists = true;
		if(!path.endsWith(".txt")){
			exists = false;
		}
		return exists;
	}
	
	// display success page
	public void success(HttpServletResponse res) {
		try {
			res.sendRedirect("success.jsp");
		} catch (IOException e) {
			System.err.println("Website Error: " + e.getMessage());		
		}
	}
	
	// display error page
	public void error(HttpServletResponse res) {
		try {
			res.sendRedirect("error.jsp");
		} catch (IOException e) {
			System.err.println("Input Error: " + e.getMessage());		
		}
	}
}
