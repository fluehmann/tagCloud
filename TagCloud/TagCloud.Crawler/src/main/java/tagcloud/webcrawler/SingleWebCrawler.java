package tagcloud.webcrawler;

import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import tagcloud.crawler.CrawlerShell;

// implementation of a single threaded WebCrawler as an alternative to the mutlithreaded solution 
public class SingleWebCrawler extends CrawlerShell {


	// call super constructor
	public SingleWebCrawler(String hostname) {
		super(hostname);
	}

	public void crawl(String startURL) {

		String nextUrl = null;
		SimplePageParser parser = new SimplePageParser(hostname, idxAdptr);
		
		Set<String> urlsCrawled = new HashSet<String>();
		Queue<String> urlsToCrawl = new PriorityQueue<String>();
		Set<String> extractedLinks = new HashSet<String>();

		urlsToCrawl.add(startURL);


		// take first element of the queue and start crawling
		while ((nextUrl = urlsToCrawl.poll()) != null && (!urlsCrawled.contains(nextUrl))) {

			try {
				extractedLinks = parser.parsePageByUrlAndGetLinks(nextUrl);
			} catch (Exception e) {
				System.err.println("Error: failed to access website " + nextUrl
						+ ": " + e.getMessage());
			}
			// failed urls will be skipped
			urlsToCrawl.remove(nextUrl);
			urlsCrawled.add(nextUrl);

			// add newly crawled links from the website to the @urlsToCrawl queue
			Iterator<String> iter = extractedLinks.iterator();
			while (iter.hasNext()) {
				String link = iter.next();
				if (link.startsWith(startURL)
						&& (!urlsToCrawl.contains(link) && (!urlsCrawled.contains(link)))) {
						urlsToCrawl.add(link); // test if true					
				}
			}
		}
	}
}
