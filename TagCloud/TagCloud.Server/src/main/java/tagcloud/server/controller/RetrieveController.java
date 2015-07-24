package tagcloud.server.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import tagcloud.core.Adapter;
import tagcloud.core.Functions;
import tagcloud.core.Tagprocessing;
import tagcloud.retriever.RetrieveAdapter;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.bucket.significant.SignificantTerms;

public class RetrieveController {

	RetrieveAdapter retriever;
	Functions helperfunc;
	
	public RetrieveController() {
		retriever = new Adapter("elasticsearch", "127.0.0.1");
		helperfunc = new Functions();
	}
	
	/**
	 * Calls adapter class and return documents by a given index name. the amount of results depends on the settings
	 * @param hostname Is equal to index name
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Hashtable<String, String>> get(String hostname) throws Exception {
		String indexName = hostname.replace("http://", "").replace("/", "");
		// SearchResponse as String (json)
		String result = retriever.retrieveByIndexname(indexName).toString();
		
		// parse json to ArrayList with key-value entries
		return new Tagprocessing().getTags(result.toString());
	}
	
	/**
	 * 
	 * @param hostname
	 * @param keyword
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Hashtable<String, String>> get(String hostname, String keyword) throws Exception {
		String indexName = hostname.replace("http://", "").replace("/", "");
		// SearchResponse as String (json)
		String result = retriever.retrieveByKeyword(indexName, keyword).toString();
		
		// parse json to ArrayList with key-value entries
		return new Tagprocessing().getTags(result.toString());
	}
	
	/**
	 * Calls adapter class und retrieve all existing index names on this node in an arraylist
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> getIndeces() throws Exception {
		//ObjectLookupContainer<String> mapAl = client.admin().cluster() .prepareState().execute() .actionGet().getState()
		Object[] indeces = retriever.retrieveIndeces().toArray();
		
		ArrayList<String> result = new ArrayList<String>();
		for(int i=0; i < indeces.length; i++){
			result.add(indeces[i].toString());
			
			// create stopword file
			helperfunc.createFile("_blacklist", indeces[i].toString());
		}
		return result;
	}
	
	/**
	 * THIS METHOD IS NOT YET READY
	 * Calls adapter class and get significant terms based on all documents by an given index
	 * @param hostname same as index name
	 * @throws Exception
	 */
	public ArrayList<Hashtable<String, String>> getSigTerms(String hostname) throws Exception {
		//ArrayList<Hashtable<String, String>> tags = new ArrayList<Hashtable<String, String>>();
		//Hashtable<String, String> dictionary = new Hashtable<String, String>();
		
		String indexName = hostname.replace("http://", "").replace("/", "");
		
		SearchResponse result = retriever.retrieveSignificantTerms(indexName);
		System.out.println("SignificantTerms: " + result.toString());

		// sr is here your SearchResponse object
		//SignificantTerms agg = result.getAggregations().get("tagcloud_keywords");		
		
		return new Tagprocessing().getSignificantTags(result.toString());
	}
}
