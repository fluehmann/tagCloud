package tagcloud.crawler;

import tagcloud.indexer.IIndexer;
import tagcloud.indexer.IndexerImpl;

public abstract class CrawlerShell {
		
	protected final IIndexer indexer;
	protected final String hostname;

	public CrawlerShell(String hostname){
//		IndexAdapter idxAdptr = new Adapter("elasticsearch", "127.0.0.1");
		this.indexer = new IndexerImpl("elasticsearch", "127.0.0.1");
		this.hostname = hostname;
	}
	public abstract void crawl(String src);
}
