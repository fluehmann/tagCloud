package tagcloud.retriever;

import org.elasticsearch.action.search.SearchResponse;

public interface RetrieveAdapter {

	public SearchResponse retriveDocument(String indexName, String tag);
}
