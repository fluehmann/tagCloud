package tagcloud.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.hppc.ObjectLookupContainer;

import tagcloud.indexer.IIndexer;
import tagcloud.indexer.IndexerImpl;
import tagcloud.retriever.IRetriever;
import tagcloud.retriever.RetrieverImpl;

public class Adapter implements IIndexer, IRetriever{

	private IndexerImpl idx;
	private RetrieverImpl retrv;
	
	/**
	 * Establish connection to Elasticsearch node
	 * @param clustername Name of Elasticsearch cluster. Default name is "elasticsearch"
	 * @param ip IP-address of server where Elasticseach is runnung on
	 */
	public Adapter(String clustername, String ip) {
		idx = new IndexerImpl(clustername, ip);
		retrv = new RetrieverImpl(clustername, ip);
	}

	/**
	 * Save a new document in Elasticsearch by calling the index-method from another class (Indexer)
	 * @param indexName Name of index in which the document will be saved
	 * @param type Define a type of the document
	 * @param id A unique identifier for the document. The URI is a logical choice
	 * @param json Pre-created json as string with field-value entries
	 * @return 
	 */
	public boolean indexDocument(String indexName, String type, String id, HashMap<String, String> json) throws ElasticsearchException, IOException {
		
		Boolean status = idx.indexDocument(indexName, type, id, json);
		if (status){
			System.out.println("Document stored: " + id);
		}
		return status;
	}
	
	/**
	 * 
	 */
	public SearchResponse retrieveByKeyword(String indexName, String hostname, String keyword) {		
		return retrv.retrieveByKeyword(indexName, hostname, keyword);
	}

	/**
	 * Get indexed documents from Elasticsearch by a given index name
	 */
	public SearchResponse retrieveByIndexname(String indexName) throws Exception {		
		return retrv.retrieveByIndexname(indexName);
	}

	/**
	 * Get significant terms by a given index name
	 * @return SearchResponse object as json
	 */
	public SearchResponse retrieveSignificantTerms(String indexName, int size) throws Exception {
		return retrv.retrieveSignificantTerms(indexName, size);
	}

	/**
	 * Get all indeces from this node.
	 * @return ObjectLookupContainer with string elements within
	 */
	public ObjectLookupContainer<String> retrieveIndeces() throws Exception {
		return retrv.retrieveIndeces();
	}

	public SearchResponse retrieveHostnamesDistinct() throws Exception {
		return retrv.retrieveHostnamesDistinct();
	}

	public boolean hostnameExists(String hostname) throws InterruptedException, ExecutionException {
		return retrv.hostnameExists(hostname);
	}

}
