
package tagcloud.webcrawler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import tagcloud.crawler.CrawlerShell;


/**
 * Implementation of a Webcrawler
 * 
 */
//public class WebCrawler implements Crawler {
public class WebCrawler extends CrawlerShell {

	// call super constructor
	public WebCrawler(String hostname) {
		super(hostname);
	}



	// Unleash ultimate power - hold your horses!
//	private final int POOL_SIZE = Runtime.getRuntime().availableProcessors()*10;
	private final int POOL_SIZE = Runtime.getRuntime().availableProcessors();
	
	// old fields - now superFields
//	private final IndexAdapter idxAdptr;
//	private final String hostname;

	private final int MAX_NR_OF_URLS = 500;

	// Counters to control the progression of the ExecutorService.
	// Each entry in the queue increments @submittedTasks
	// Each submitted task increments @completedTasks
	// ExecutorService terminates as soon as @completedTasks = @submittedTasks
//	private final AtomicInteger openTasks = new AtomicInteger(0);
	private final AtomicInteger completedTasks = new AtomicInteger(0);


	// old constuctor - now superConstructor
//	public WebCrawler(String hostname){
//		IndexAdapter idxAdptr = new Adapter("elasticsearch", "127.0.0.1");
//		this.idxAdptr = idxAdptr;
//		this.hostname = hostname;
//	}

	/**
	 * Crawls the www starting at {@code startURL}
	 * 
	 * @param startURL the URI to start crawling any resource
	 * @return a hashmap with URLs as Key and webPage Objects as values
	 */
	public void crawl(final String startURL) {
		
		final ExecutorService taskExecutor = Executors.newFixedThreadPool(POOL_SIZE);
		final CompletionService<List<String>> taskQueue = new ExecutorCompletionService<List<String>>(taskExecutor);


		// contains all urls that have been submitted to the @taskQueue
		final Set<String> urlsSubmitted = new HashSet<String>();

		// contains all urls still to be visited 
		final Set<String> urlsToVisit = new HashSet<String>();
		urlsToVisit.add(startURL);

		// As long as the URL list with urls to visit contains elements
		// submit a new thread/task for every url in the list
		// start all the tasks as new Callables - clear list when tasks were submitted
		// System.out.println("open: "+ openTasks.get() + " vs completed " + completedTasks.get());
//		while (true) {
//		while (completedTasks.get() != openTasks.get()-1) {
		while (urlsToVisit.size() < MAX_NR_OF_URLS || completedTasks.get() != urlsSubmitted.size()-1) {

			// TaskQueue füllen mit Arbeit
			for (String url : urlsToVisit) {
				taskQueue.submit(new CrawlCallable(hostname, url, indexer)); // füllen der Queue
				urlsSubmitted.add(url); // rename to submittedTasks
//				openTasks.incrementAndGet();
			} 
			// clear todo list - all tasks started
			urlsToVisit.clear(); 


			try {
				List<String> urlsList;
				if ((urlsList = taskQueue.take().get()) != null){ //start work
					// take first result 
					completedTasks.incrementAndGet();
				}
				if (urlsList != null) {
					for (String url : urlsList) {
						// add new url to url task queue
						if (url.startsWith(startURL) && !urlsSubmitted.contains(url)) {
							urlsToVisit.add(url);
						}
					}

					Future<List<String>> myFuture;
					while ((myFuture = taskQueue.poll()) != null) { // start work 
						completedTasks.incrementAndGet();

						for (String url : myFuture.get()) {
							// add url to urlsToVisit tasklist
							if (url.startsWith(startURL) && !urlsSubmitted.contains(url)) {
								urlsToVisit.add(url);
							}
						}
					}
				} else {
					// Result is null if there was a Server Error while trying to crawl a URL
					System.err.println("Bad server response (403, 404, 406, 500, 503, etc.");
					completedTasks.incrementAndGet();
				}

			
			} catch (Exception e) {
				// Ignore Exception - continue crawling
				System.err.println("Error ocurred while crawling in Crawler-Module: " + e.getMessage());
				completedTasks.incrementAndGet();
			}
		}

		try {
			taskExecutor.shutdown();
			System.out.println("Starting termination of Crawler...");
			System.out.println("Finishing up running Threads");
			
			// wait 100 seconds to finish possible pending futures
			taskExecutor.awaitTermination(100, TimeUnit.SECONDS);
			java.awt.Toolkit.getDefaultToolkit().beep(); // beep
		} catch (InterruptedException e) {
			System.err.println("Termination was interrupted");
		}
	}
}
