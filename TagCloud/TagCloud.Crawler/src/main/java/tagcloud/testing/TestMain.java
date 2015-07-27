package tagcloud.testing;

import java.util.List;

import tagcloud.indexer.IndexAdapter;
import tagcloud.webcrawler.CrawlCallable;
import tagcloud.webcrawler.WebCrawler;

public class TestMain {

	private static IndexAdapter x;

	public static void main(String[] args) throws Exception {
		
		// testSites
//		final String url = "http://www.test.fhnw.ch/";
//		final String url = "http://www.fhnw.ch/";
//		final String url = "https://www.bfh.ch/";
		final String url = "http://emmental.ch/";
//		final String url = "http://emmental-tours.ch/";
//		final String url = "http://www.20min.ch/";
//		final String url = "http://www.blick.ch/";
//		final String url = "http://www.greenpeace.org/";
//		final String url = "http://www.himmelblau.ch/";
//		final String url = "http://www.pro-senectute.ch/";
//		final String url = "http://www.lungenliga.ch/";
//		final String url = "http://www.zermatt.ch/";
		
		// measure execution time of crawler
		long startTime = System.nanoTime();
		new WebCrawler(url).crawl(url);
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
		double seconds = (double)duration / 1000000000.0;
		System.out.println(seconds/60);
	}
}