package tagcloud.retriever;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.hppc.ObjectLookupContainer;

public interface RetrieveAdapter {

	public SearchResponse retrieveByKeyword(String indexName, String tag);
	public SearchResponse retrieveByIndexname(String indexName) throws Exception;
	public SearchResponse retrieveSignificantTerms(String indexName) throws Exception;
	public ObjectLookupContainer<String> retrieveIndeces() throws Exception;
}
