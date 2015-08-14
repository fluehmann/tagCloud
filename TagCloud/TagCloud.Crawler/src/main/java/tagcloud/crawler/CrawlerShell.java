package tagcloud.crawler;

import tagcloud.core.Functions;
import tagcloud.indexer.IIndexer;
import tagcloud.indexer.IndexerImpl;

public abstract class CrawlerShell {
		
	protected final IIndexer indexer;
	protected final String hostname;

	public CrawlerShell(String hostname){
		this.indexer = new IndexerImpl(Functions.CLUSTER_NAME, Functions.IP_ELASTICSEARCH);
		this.hostname = hostname;
	}
	public abstract void crawl(String src);
}
