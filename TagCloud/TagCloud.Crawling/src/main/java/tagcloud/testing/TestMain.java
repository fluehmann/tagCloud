package tagcloud.testing;

import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import tagcloud.webcrawler.CrawlCallable;
import tagcloud.webcrawler.WebCrawler;
import tagcloud.webcrawler.webPage;

public class TestMain {

	public static void main(String[] args) {
		
		
		final String url = "http://www.fhnw.ch/";
	
//		try {
//			System.out.println(new CrawlCallable("http://www.fhnw.ch/").call());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
//		try {
//			Document doc = new CrawlCallable(url).call().get(url);
//			webPage x = new webPage(url, doc);
//			List<String> links = x.extractLinks();
//			
//			for (String link : links ){
//				System.out.println(link);
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
//		List<String> links = null;
//		
//		try {
//			links = new CrawlCallable(url).call();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		for (String link : links){
//			System.out.println(link);
//		}
		
		
		WebCrawler x = new WebCrawler();
		List<String> result = x.crawl(url);
		
		
		for (String entry : result){
			System.out.println(entry);
		}
		System.out.println(result.size());
		
		
	}

}