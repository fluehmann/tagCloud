
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
 * Implementation of a concurrent webcrawler.
 * Uses producer/consumer structure to accomplish the task of crawling a website.
 * While urls are crawled concurrently, new urls are added to a queue.
 * The work of crawling a url is encapsulated in a Callable @CrawlCallable.
 * The number of threads is limited to the number of available processors.
 * Field tests have shown that using too many threads at once will result in complaints
 * from the IT-department of the crawled website.
 * Crawling huge websites like http://www.fhnw.ch may take a while...
 * 
 * @param hostname: name of a website
 */
public class WebCrawler extends CrawlerShell {

	// call super constructor in abstract class CrawlerShell
	public WebCrawler(String hostname) {
		super(hostname);
	}

	// unleash ultimate power - hold your horses!
//	private final int POOL_SIZE = Runtime.getRuntime().availableProcessors()*10;
	private final int POOL_SIZE = Runtime.getRuntime().availableProcessors();
	private final int MAX_NR_OF_URLS = 500;

	// counter variable to control the progression of the ExecutorService.
	// each submitted task increments @completedTasks
	// executorService terminates as soon as @completedTasks == @submittedTasks
	private final AtomicInteger completedTasks = new AtomicInteger(0);

	/**
	 * Method to crawl the www starting at @startURL.
	 * For each url a @CrawlCallable is submitted to the que @taskQueue.
	 * Threads will then execute the work encapsulated in the Callable concurrently.
	 * 
	 * @param startURL: the URI to start crawling any resource
	 */
	public void crawl(final String startURL) {
		
		// start an ExecutionCompletionService with an thread pool implementation
		final ExecutorService taskExecutor = Executors.newFixedThreadPool(POOL_SIZE);
		final CompletionService<List<String>> taskQueue = new ExecutorCompletionService<List<String>>(taskExecutor);

		// contains all urls that have been submitted to the @taskQueue
		final Set<String> urlsSubmitted = new HashSet<String>();

		// contains all urls still to be visited 
		final Set<String> urlsToVisit = new HashSet<String>();
		urlsToVisit.add(startURL);

		// as long as the URL list with urls to visit contains elements
		// submit a new thread/task for every url in the list
		// threads will execute the call() in CrawlCallable concurrently
		// clear list when tasks were submitted
		while (urlsToVisit.size() < MAX_NR_OF_URLS || completedTasks.get() != urlsSubmitted.size()-1) {

			// submit new tasks to the queue
			for (String url : urlsToVisit) {
				taskQueue.submit(new CrawlCallable(hostname, url, indexer));
				urlsSubmitted.add(url);
			} 
			// clear list - all tasks started
			urlsToVisit.clear(); 


			try {
				List<String> urlsList;
				// take first result from the future - wait for it
				if ((urlsList = taskQueue.take().get()) != null){
					completedTasks.incrementAndGet();
				}
				if (urlsList != null) {
					for (String url : urlsList) {
						// add new url to the url todo-list
						if (url.startsWith(startURL) && !urlsSubmitted.contains(url)) {
							urlsToVisit.add(url);
						}
					}

					Future<List<String>> myFuture;
					// take result from the future object - don't wait just try
					while ((myFuture = taskQueue.poll()) != null) {
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
			// shutdown the ExecutorCompletionService as soon as it has crawled the complete website
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
