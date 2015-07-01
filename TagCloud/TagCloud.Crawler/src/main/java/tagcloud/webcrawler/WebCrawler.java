
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
	private final int POOL_SIZE = Runtime.getRuntime().availableProcessors()*10;
	private final int MAX_NR_OF_URLS = 1000;
	private final IndexAdapter x;
	private final String hostname;
	
	public WebCrawler(String hostname){
		IndexAdapter x = new Adapter("elasticsearch", "127.0.0.1");
		this.x = x;
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

		 final ExecutorService taskExecutor = Executors.newFixedThreadPool(POOL_SIZE);
//		 final ExecutorService taskExecutor = new ScheduledThreadPoolExecutor(300);
//		final ExecutorService taskExecutor = Executors.newCachedThreadPool();
		final CompletionService<List<String>> taskQueue = new ExecutorCompletionService<List<String>>(taskExecutor);


		/*
		 * Contains all discovered urls. Needs to be a set - a collection that
		 * contains no duplicate elements.
		 */
		final Set<String> urlsCrawled = new HashSet<String>();


		/*
		 * Contains the urls to be visited. 
		 */
//		final Queue<String> urlsToVisit = new LinkedList<String>();
		final Set<String> urlsToVisit = new HashSet<String>();
		urlsToVisit.add(startURL);
		System.out.println("urlsToVisit: " + urlsToVisit);


		// As long as the URL list with urls to visit contains elements
		// submit a new thread/task for every url in the list
		// start all the tasks as new Callables - clear list when tasks were submitted

		// while ((!urlsToVisit.isEmpty()) && urlsFound.size() < POOL_SIZE) {
		// while (urlsCrawled.size() < MAX_NR_OF_URLS) {
//		while (!urlsToVisit.isEmpty()) {
//		while (!urlsToVisit.isEmpty() && urlsCrawled.size() < MAX_NR_OF_URLS) {
//		while (urlsCrawled.size() < MAX_NR_OF_URLS) {

		while (urlsCrawled.size() < MAX_NR_OF_URLS || !urlsToVisit.isEmpty()) {
			
			// TaskQueue füllen mit Arbeit
			for (String url : urlsToVisit) {
				taskQueue.submit(new CrawlCallable(hostname, url, x));
				urlsCrawled.add(url);
			} 


			try {
				urlsToVisit.clear(); 
				List<String> urlsList = taskQueue.take().get(); // waits for the first result and removes it
				if (urlsList != null){
				// add new url to url queue
				for (String url : urlsList) {
//					if (url.startsWith(startURL) && !urlsCrawled.contains(url) && !urlsToVisit.contains(url)) {
					if (url.startsWith(startURL)) {
						urlsToVisit.add(url);
						System.out.println(url + " was added to queue - 1");
					}
				}

				Future<List<String>> myFuture;

				// Already new Results in the future available? 
				// Blockierend warten?
				while ((myFuture = taskQueue.poll()) != null) { 
					System.err.println("--------------------CONFIRMED--------------------");

					for (String url : myFuture.get()) {
//						if (url.startsWith(startURL) && !urlsCrawled.contains(url) && !urlsToVisit.contains(url)) {
						if (url.startsWith(startURL)) {
							urlsToVisit.add(url);
							System.out.println(url + " was added to queue - 2");
						}
					}
				}
				} else {
					
					// if null
					System.out.println("was null");
				}

			} catch (NullPointerException e) {
//				 e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}


		// should await termination...
//		taskExecutor.shutdownNow();
//		System.out.println("...Shut down...");
		
		
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
		

//		List<String> result = new ArrayList<String>(urlsCrawled.size());
//
//		int i = 0;
//		System.out.println(i);
//		Iterator<String> it = urlsCrawled.iterator();
//		while (it.hasNext()) {
//			result.add(it.next());
//		}


//		System.out.println(result);
//		return result;
	}
}
