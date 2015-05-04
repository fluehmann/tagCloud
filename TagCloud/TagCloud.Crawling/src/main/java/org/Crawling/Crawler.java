/**
 * 
 */
package org.Crawling;

import java.util.List;

/**
 * Interface for crawler
 * @author simonfluhmann
 */
public interface Crawler {
	
	/**
     * Crawls any source, starting at {@code src} 
     * @param src the URI to start crawling any resource
     * @return a list of uris that are reachable from {@code src}
     */
    List<String> crawl(String src);
    
    // indexDocument();
    // interface indexable implementiert alle indexierbaren dokumente


}
