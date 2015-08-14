package tagcloud.crawler;

import tagcloud.core.Functions;
import tagcloud.indexer.IIndexer;
import tagcloud.indexer.IndexerImpl;

/**
 * Abstract class CrawlerShell.
 * Every Crawler-Type inherits all Fields from this class.
 * This class will keep the connection to the Elasticsearch Index.
 * 
 * @param hostname: the name of the website
 * 
 * */
public abstract class CrawlerShell {
		
	protected final IIndexer indexer;
	protected final String hostname;

	public CrawlerShell(String hostname){
		this.indexer = new IndexerImpl(Functions.CLUSTER_NAME, Functions.IP_ELASTICSEARCH);
		this.hostname = hostname;
	}
	public abstract void crawl(String src);
}
