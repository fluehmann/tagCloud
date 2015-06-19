
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

import tagcloud.core.Adapter;
import tagcloud.indexer.IndexAdapter;
import tagcloud.webcrawler.Crawler;

/**
 * Implementation of a Webcrawler
 * 
 */
public class WebCrawler implements Crawler {

	private final int POOL_SIZE = 100;
	private final int MAX_NR_OF_URLS = 5000;
	private final IndexAdapter idxAdapter;
	private final String hostname;
	
	public WebCrawler(String hostname, IndexAdapter adapter){
		//IndexAdapter x = new Adapter("elasticsearch", "127.0.0.1");
		this.idxAdapter = adapter;
		this.hostname = hostname;
	}

	/**
	 * Crawls the www starting at {@code startURL}
	 * 
	 * @param startURL
	 *            the URI to start crawling any resource
	 * @return a hashmap with URLs as Key and webPage Objects as values
	 */
	public List<String> crawl(final String startURL) {

		// final ExecutorService taskExecutor = Executors.newFixedThreadPool(POOL_SIZE);
		final ExecutorService taskExecutor = Executors.newCachedThreadPool();
		final CompletionService<List<String>> taskQueue = new ExecutorCompletionService<List<String>>(taskExecutor);


		/*
		 * Contains all discovered urls. Needs to be a set - a collection that
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
		// start all the tasks as new Callables - clear list when tasks were submitted

		// while ((!urlsToVisit.isEmpty()) && urlsFound.size() < POOL_SIZE) {
		// while (urlsCrawled.size() < MAX_NR_OF_URLS) {
		while (!urlsToVisit.isEmpty()) {

			// TaskQueue füllen mit Arbeit
			for (String url : urlsToVisit) {
				taskQueue.submit(new CrawlCallable(hostname, url, idxAdapter));
				urlsCrawled.add(url);
			} urlsToVisit.clear(); 


			try {
				List<String> urlsList = taskQueue.take().get(); // waits for the first result and removes it
				// add new url to url queue
				for (String url : urlsList) {
					if (url.startsWith(startURL) && !urlsCrawled.contains(url) && !urlsToVisit.contains(url)) {
						urlsToVisit.add(url);
						System.out.println(url + " was added to queue - 1");
					}
				}

				Future<List<String>> myFuture;

				// Already new Results in the future available? 
				// Blockierend warten?
				while ((myFuture = taskQueue.poll()) != null) { 
					System.err.println("--------------------DRINDRINDRIN--------------------");

					for (String url : myFuture.get()) {
						if (url.startsWith(startURL) && !urlsCrawled.contains(url) && !urlsToVisit.contains(url)) {
							urlsToVisit.add(url);
							System.out.println(url + " was added to queue - 2");
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}


		// should await termination...
		taskExecutor.shutdownNow();
		System.out.println("...Shut down...");

		List<String> result = new ArrayList<String>(urlsCrawled.size());

		int i = 0;
		System.out.println(i);
		Iterator<String> it = urlsCrawled.iterator();
		while (i < MAX_NR_OF_URLS && it.hasNext()) {
			result.add(it.next());
		}



		return result;
	}
}
