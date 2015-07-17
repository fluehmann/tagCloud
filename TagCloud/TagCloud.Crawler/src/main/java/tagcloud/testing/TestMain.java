package tagcloud.testing;

import java.util.List;

import tagcloud.indexer.IndexAdapter;
import tagcloud.webcrawler.CrawlCallable;
import tagcloud.webcrawler.WebCrawler;

public class TestMain {

	private static IndexAdapter x;

	public static void main(String[] args) throws Exception {
		
//		final String url = "http://www.test.fhnw.ch/";
		final String url = "http://www.fhnw.ch/";
//		final String url = "http://www.bfh.ch/";
		
//		List<String> links = new CrawlCallable(url, url, x).call();
		
//		if(links.isEmpty()){
//			System.out.println("nothing in there");
//		}
//		System.out.println("------------------------------------------");
//		for (String x : links){
//			System.out.println(x);
//		}

//		CrawlCallable huerenutte = new CrawlCallable("dini mer", url,  x);
//		huerenutte.call();
		new WebCrawler(url).crawl(url);
		
//		List<String> result = new WebCrawler(url).crawl(url);	
//		System.out.println(result);
//		
//		for (String entry : result){
//			System.out.println(entry);
//		}
//		System.out.println(".. all these links have been crawled: " + result.size());
		
	}

}