package tagcloud.testing;

import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import tagcloud.webcrawler.CrawlCallable;
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
//			Document doc = new CrawlCallable("http://www.emmental.ch/").call().get("http://www.emmental.ch/");
//			webPage x = new webPage("http://www.emmental.ch/", doc);
//			List<String> links = x.extractLinks();
//			
//			for (String link : links ){
//				System.out.println(link);
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		try {
			System.out.println(new CrawlCallable(url).call().get(url).extractLinks());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}

}