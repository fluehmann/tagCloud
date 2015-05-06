/**
 * 
 */
package tagcloud.webcrawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Implementation of a Webcrawler
 * 
 * @author simonfluhmann
 *
 */
public class WebCrawler implements Crawler {
//	public class WebCrawler implements Crawler, indexAdapter { => how to access indexAdapter?

	private final int POOL_SIZE = 50;

	/**
	 * Crawls the www starting at {@code src}
	 * 
	 * @param src the URI to start crawling any resource
	 * @return a hashmap with URLs as Key and webPage Objects as values
	 */
	public List<String> crawl(final String startURL) {
		
		final ExecutorService taskExecutor = Executors.newFixedThreadPool(POOL_SIZE);
		final CompletionService<List<String>> taskQueue = new ExecutorCompletionService<List<String>>(taskExecutor);

		/* Contains all discovered urls. */
		final Set<String> urlsFound = new HashSet<String>();
		
		/* Contains the urls to be visited. */
		final Queue<String> urlsToVisit = new LinkedList<String>(); urlsToVisit.add(startURL);


		
		
		
		

		return new ArrayList<String>();

	}
}
