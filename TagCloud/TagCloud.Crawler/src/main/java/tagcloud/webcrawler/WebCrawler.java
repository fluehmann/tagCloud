
package tagcloud.webcrawler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import tagcloud.core.Adapter;
import tagcloud.indexer.IndexAdapter;
import tagcloud.webcrawler.Crawler;

/**
 * Implementation of a Webcrawler
 * 
 */
//public class WebCrawler implements Crawler {
public class WebCrawler {

	//	private final int POOL_SIZE = 100;
	//	private final int POOL_SIZE = Runtime.getRuntime().availableProcessors()*10;
	private final int POOL_SIZE = Runtime.getRuntime().availableProcessors();
	private final int MAX_NR_OF_URLS = 1000;
	private final IndexAdapter idxAdptr;
	private final String hostname;

	public WebCrawler(String hostname){
		IndexAdapter idxAdptr = new Adapter("elasticsearch", "127.0.0.1");
		this.idxAdptr = idxAdptr;
		this.hostname = hostname;
	}

	/**
	 * Crawls the www starting at {@code startURL}
	 * 
	 * @param startURL
	 *            the URI to start crawling any resource
	 * @return 
	 * @return a hashmap with URLs as Key and webPage Objects as values
	 */
	public void crawl(final String startURL) {


		// three times faster than normal loop
		final ExecutorService taskExecutor = Executors.newFixedThreadPool(POOL_SIZE);
		// final ExecutorService taskExecutor = new ScheduledThreadPoolExecutor(300);
		// final ExecutorService taskExecutor = Executors.newCachedThreadPool();
		final CompletionService<List<String>> taskQueue = new ExecutorCompletionService<List<String>>(taskExecutor);


		/*
		 * Contains all discovered urls. Needs to be a set - a collection that
		 * contains no duplicate elements.
		 */
		final Set<String> urlsCrawled = new HashSet<String>();


		/*
		 * Contains the urls to be visited. 
		 */
		final Set<String> urlsToVisit = new HashSet<String>();
		urlsToVisit.add(startURL);
		System.out.println("urlsToVisit: " + urlsToVisit);


		// As long as the URL list with urls to visit contains elements
		// submit a new thread/task for every url in the list
		// start all the tasks as new Callables - clear list when tasks were submitted

		// while ((!urlsToVisit.isEmpty()) && urlsFound.size() < POOL_SIZE) {
		// while (urlsCrawled.size() < MAX_NR_OF_URLS) {
		// while (!urlsToVisit.isEmpty() && urlsCrawled.size() < MAX_NR_OF_URLS) {
		// while (urlsCrawled.size() < MAX_NR_OF_URLS) {

		// while (urlsCrawled.size() < MAX_NR_OF_URLS || !urlsToVisit.isEmpty()) {
		// while (!urlsToVisit.isEmpty()) {
		// BIG QUESTIONMARK - could even be a while(true) - once is done - executor service will terminate by itself. Would'nt it?
		while (!Thread.currentThread().isInterrupted()){

			// TaskQueue füllen mit Arbeit
			for (String url : urlsToVisit) {
				taskQueue.submit(new CrawlCallable(hostname, url, idxAdptr));
				urlsCrawled.add(url);
			} 
			urlsToVisit.clear(); 


			try {
				List<String> urlsList = taskQueue.take().get(); // waits for the first result and removes it
				if (urlsList != null){
					for (String url : urlsList) {
						// add new url to url queue
						// if (url.startsWith(startURL) && !urlsCrawled.contains(url) && !urlsToVisit.contains(url)) {
						if (url.startsWith(startURL) && !urlsCrawled.contains(url)) {
							urlsToVisit.add(url);
						}
					}

					Future<List<String>> myFuture;


					while ((myFuture = taskQueue.poll()) != null) { 

						for (String url : myFuture.get()) {
							// add new url to url queue
							// if (url.startsWith(startURL) && !urlsCrawled.contains(url) && !urlsToVisit.contains(url)) {
							if (url.startsWith(startURL) && !urlsCrawled.contains(url)) {
								urlsToVisit.add(url);
							}
						}
					}
				} else {
					// Result is null if there was a Server Error while trying to crawl a URL
				}

			} catch (NullPointerException e) {
				// Ignore Exception - go on crawling
			} catch (Exception e) {
				e.printStackTrace();
			}

		}



		taskExecutor.shutdown();
		try {
			System.out.println("Termination started");
			taskExecutor.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			System.out.println("Thread was interrupted - termination");
		}
		ThreadGroup currentGroup = 
				Thread.currentThread().getThreadGroup();
		int noThreads = currentGroup.activeCount();
		Thread[] lstThreads = new Thread[noThreads];
		currentGroup.enumerate(lstThreads);
		for (int i = 0; i < noThreads; i++)
			System.out.println("Thread No:" + i + " = "
					+ lstThreads[i].getName());
		System.out.println("Threads are shutting down");

	}
}
