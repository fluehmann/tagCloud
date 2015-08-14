package tagcloud.webcrawler;


/**
 * @deprecated - use @CrawlerShell instead
 * Interface for crawlers. All crawler types need to implement this interface.
 */
public interface Crawler {
			
	/**
     * Crawls any source, starting at {@code src} 
     * @param src the URI to start crawling any resource
     * @return a list of uris that are reachable from {@code src}
     */
    public void crawl(String src);

}
