package tagcloud.webcrawler;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class SingleWebPageCrawler {

	int i = 0;
	private PageParser parser = new SimplePageParser();

	public SingleWebPageCrawler() {

	}

	public void crawl(String startUrl) throws Exception {
		long startTime = System.currentTimeMillis();

		Set<String> urlsCrawled = new HashSet<String>();
		Queue<String> urlsToCrawl = new PriorityQueue<String>();
		Set<String> extractedLinks = new HashSet<String>();

		urlsToCrawl.add(startUrl);

		String nextUrl;
		//		while ((nextUrl = urlsToCrawl.poll()) != null) {
		while ((nextUrl = urlsToCrawl.poll()) != null && (!urlsCrawled.contains(nextUrl))) {

			extractedLinks = parser.parsePageByUrlAndGetLinks(nextUrl);
			urlsCrawled.add(nextUrl);

			// add links below site entry point
			Iterator<String> iter = extractedLinks.iterator();
			while (iter.hasNext()) {
				String link = iter.next();
				if (link.startsWith(startUrl) && (!urlsToCrawl.contains(link) && (!urlsCrawled.contains(link)))) {
					if (link.startsWith(startUrl)) {
						urlsToCrawl.add(link); // test if true
						System.out.println("in the Queue: " + link);
					}
				}
			}


		}
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("execution time: " + elapsedTime / 60 + " ms");
		
		for (String s : urlsCrawled) {
			System.out.println("crawled: " + s);
		}
	}
}
