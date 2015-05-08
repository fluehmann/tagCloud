/**
 * 
 */
package tagcloud.backup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import tagcloud.webcrawler.CrawlCallable;
import tagcloud.webcrawler.Crawler;
import tagcloud.webcrawler.webPage;


/**
 * Implementation of a Webcrawler
 * 
 * @author simonfluhmann
 *
 */
public class WebCrawler_old implements Crawler {

	private final int POOL_SIZE = 50;

	/**
	 * Crawls the www starting at {@code startURL}
	 * 
	 * @param startURL the URI to start crawling any resource
	 * @return a hashmap with URLs as Key and webPage Objects as values
	 */
	public List<String> crawl(final String startURL) {

		final ExecutorService taskExecutor = Executors.newFixedThreadPool(POOL_SIZE);
		final CompletionService<HashMap<String, webPage>> taskQueue
			= new ExecutorCompletionService<HashMap<String, webPage>>(taskExecutor);
		

		/* Contains all discovered urls. Needs to be a set A collection that contains no duplicate elements.  */
		final Set<String> urlsCrawled = new HashSet<String>();

		/* Contains the urls to be visited. A collection designed for holding elements prior to processing. */
		final Queue<String> urlsToVisit = new LinkedList<String>();
		urlsToVisit.add(startURL);

		urlsToVisit.add(startURL);

		// As long as the URL list with urls to visit contains elements
		// submit a new thread/task for every url in the list 
		// while ((!urlsToVisit.isEmpty()) && urlsFound.size() < POOL_SIZE) {
		while (!urlsToVisit.isEmpty()) {
			for (String url : urlsToVisit) {
//				taskQueue.submit(new CrawlCallable(url));
				urlsCrawled.add(url);
			} urlsToVisit.clear(); // all tasks started - task queue can be cleared

			try {
				HashMap<String, webPage> result = taskQueue.take().get();  // wait for the first result 
				
				// iterate through res and add new urls to queue
				Iterator<Entry<String, webPage>> entries = result.entrySet().iterator();
				while (entries.hasNext()) {
				  Entry thisEntry = (Entry) entries.next();
				  String key = (String) thisEntry.getKey();
				  webPage value = (webPage) thisEntry.getValue(); // webPage Object
				  List<String> links = value.extractLinks();
				  
				  for(String url: links) {
					  if(!urlsCrawled.contains(url) && !urlsToVisit.contains(url)) {
						  urlsToVisit.add(url);
					  }
				  }
				  Future<HashMap<String, webPage>> fut;
				  while((fut = taskQueue.poll()) != null) { // just poll, don't wait
					  
					  for (Entry<String, webPage> entry2 : ((HashMap<String, webPage>) fut).entrySet())
					  {
						  List<String> links2 = entry2.getValue().extractLinks();
					      for(String url: links2) {
							  if(!urlsCrawled.contains(url) && !urlsToVisit.contains(url)) {
								  urlsToVisit.add(url);
							  }
						  }
 
					  }
				  }
				  }
				  } catch (Exception e) { e.printStackTrace(); }
			
				  
//			List<String> result = new ArrayList<String>(MAX_VISITS); int i = 0;
//			Iterator<String> it = urlsFound.iterator();
//			while(i++ < MAX_VISITS && it.hasNext()) { result.add(it.next()); } ex.shutdownNow(); // ignore result
//			return result; }
//			
			
			return new ArrayList<String>();

		}
		return null;
	}
}
