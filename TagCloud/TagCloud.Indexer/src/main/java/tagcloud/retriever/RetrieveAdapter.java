package tagcloud.retriever;

import org.elasticsearch.action.search.SearchResponse;

public interface RetrieveAdapter {

	public SearchResponse retriveDocument(String indexName, String tag);
	public SearchResponse retrieveByIndexname(String indexName) throws Exception;
	public SearchResponse retrieveSignificantTerms(String indexName) throws Exception;
}
