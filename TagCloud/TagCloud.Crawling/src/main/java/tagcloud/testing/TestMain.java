package tagcloud.testing;

import java.util.HashSet;
import java.util.List;

import tagcloud.core.Adapter;
import tagcloud.indexer.IndexAdapter;
import tagcloud.webcrawler.SingleWebPageCrawler;
import tagcloud.webcrawler.WebCrawler;

public class TestMain {

	public static void main(String[] args) throws Exception {
		
		final String url = "http://www.fhnw.ch/";
		IndexAdapter idxAdapter = new Adapter("elasticserach", "127.0.0.1");
//		try {
//			new SingleWebPageCrawler().crawl(url);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		
		List<String> result = new WebCrawler(url, idxAdapter).crawl(url);	
		System.out.println(result);
		System.out.println("nr of results: " + result.size());
		
//		for (String entry : result){
//			System.out.println(entry);
//		}
//		System.out.println(".. all these links have been crawled: " + result.size());
		
	}

}