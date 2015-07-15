package tagcloud.core;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchResponse;

import tagcloud.indexer.IndexAdapter;
import tagcloud.indexer.Indexer;
import tagcloud.retriever.RetrieveAdapter;
import tagcloud.retriever.Retriever;

public class Adapter implements IndexAdapter, RetrieveAdapter{

	private Indexer idx;
	private Retriever retrv;

	/**
	 * Establish connection to Elasticsearch node
	 * @param clustername Name of Elasticsearch cluster. Default name is "elasticsearch"
	 * @param ip IP-address of server where Elasticseach is runnung on
	 */
	public Adapter(String clustername, String ip) {
		idx = new Indexer(clustername, ip);
		retrv = new Retriever(clustername, ip);
	}

	public void indexDocument(String indexName, String type, String id, HashMap<String, String> json)
			throws ElasticsearchException, IOException {

		Boolean status = idx.index(indexName, type, id, json);
		if (status){
			System.out.println("Document stored: " + id);
		}
	}

	public SearchResponse retriveDocument(String indexName, String tag) {

		return retrv.retrieve(indexName, tag);
	}

	public SearchResponse retrieveByIndexname(String indexName) throws Exception {

		return retrv.retrieveByIndexname(indexName);
	}

	public SearchResponse retrieveSignificantTerms(String indexName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
