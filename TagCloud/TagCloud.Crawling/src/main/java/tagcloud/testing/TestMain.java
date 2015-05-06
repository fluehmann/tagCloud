package tagcloud.testing;

import tagcloud.crawling.CrawlCallable;

public class TestMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			System.out.println(new CrawlCallable("http://www.fhnw.ch/").call());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
