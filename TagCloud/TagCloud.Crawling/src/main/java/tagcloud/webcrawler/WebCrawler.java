/**
 * 
 */
package tagcloud.webcrawler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import tagcloud.webcrawler.Crawler;

/**
 * Implementation of a Webcrawler
 * 
 * @author simonfluhmann
 *
 */
public class WebCrawler implements Crawler {

	private final int POOL_SIZE = 10;

	/**
	 * Crawls the www starting at {@code startURL}
	 * 
	 * @param startURL
	 *            the URI to start crawling any resource
	 * @return a hashmap with URLs as Key and webPage Objects as values
	 */
	public List<String> crawl(final String startURL) {

		final ExecutorService taskExecutor = Executors
				.newFixedThreadPool(POOL_SIZE);
		final CompletionService<List<String>> taskQueue = new ExecutorCompletionService<List<String>>(
				taskExecutor);
		

		/*
		 * Contains all discovered urls. Needs to be a set A collection that
		 * contains no duplicate elements.
		 */
		final Set<String> urlsCrawled = new HashSet<String>();

		
		/*
		 * Contains the urls to be visited. A collection designed for holding
		 * elements prior to processing.
		 */
		final Queue<String> urlsToVisit = new LinkedList<String>();
		urlsToVisit.add(startURL);
		System.out.println("urlsToVisit: " + urlsToVisit);

		// As long as the URL list with urls to visit contains elements
		// submit a new thread/task for every url in the list
		// while ((!urlsToVisit.isEmpty()) && urlsFound.size() < POOL_SIZE) {
		while (!urlsToVisit.isEmpty()) {
			for (String url : urlsToVisit) {
				taskQueue.submit(new CrawlCallable(url));
				urlsCrawled.add(url);
			}
			urlsToVisit.clear(); // all tasks started - task queue can be
									// cleared

			try {
				List<String> urlsList = taskQueue.take().get(); // wait for the
					System.out.println(urlsList);											// first result

				// add new url to url queue
				for (String url : urlsList) {
					if (url.startsWith(startURL) && !urlsCrawled.contains(url)
							&& !urlsToVisit.contains(url)) {
						urlsToVisit.add(url);
					}
				}

				
				Future<List<String>> myFuture;
				while ((myFuture = taskQueue.poll()) != null) { // just
																// poll,
																// don't
																// wait
					for (String url : myFuture.get()) {
						if (url.startsWith(startURL)
								&& !urlsCrawled.contains(url)
								&& !urlsToVisit.contains(url)) {
							urlsToVisit.add(url);
						}
					}
				} 

			} catch (Exception e) {
				e.printStackTrace();
			}


		}
		List<String> result = new ArrayList<String>(urlsCrawled.size());
		
		Iterator<String> it = urlsCrawled.iterator();
		
		while(it.hasNext()) {
			result.add(it.next());
			
		}
		
//		taskExecutor.shutdownNow();
		return result;
	}
}
