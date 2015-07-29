package tagcloud.crawler;

import tagcloud.core.Adapter;
import tagcloud.indexer.IndexAdapter;

public abstract class CrawlerShell {
		
	protected final IndexAdapter idxAdptr;
	protected final String hostname;

	public CrawlerShell(String hostname){
		IndexAdapter idxAdptr = new Adapter("elasticsearch", "127.0.0.1");
		this.idxAdptr = idxAdptr;
		this.hostname = hostname;
	}
	public abstract void crawl(String src);
}
