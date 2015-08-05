package tagcloud.server.controller;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.ExecutionException;

import tagcloud.core.Adapter;
import tagcloud.core.Functions;
import tagcloud.core.Tagprocessing;
import tagcloud.retriever.IRetriever;
import tagcloud.retriever.RetrieverImpl;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

public class RetrieveController {

	protected IRetriever retriever;
	Functions helperfunc;
	
	public RetrieveController() {
		this.retriever = new RetrieverImpl("elasticsearch", "127.0.0.1");
//		this.retriever = new Adapter("elasticsearch", "127.0.0.1");
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
	 * Retrieve documents which match by given hostname and keyword (by clicking on a link in tagcloud)
	 * @param hostname
	 * @param keyword
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Hashtable<String, String>> get(String hostname, String keyword) throws Exception {
		String indexName = "tagcloud";
		String host = hostname.replace("http://", "").replace("/", "");
		// SearchResponse as String (json)
		String result = retriever.retrieveByKeyword(indexName, hostname, keyword).toString();
		
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
		}
		return result;
	}
	
	/**
	 * Get all hostnames distinct from te index
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> getHostnames() throws Exception {
		ArrayList<String> hostnames = new ArrayList<String>();
		SearchResponse sr = retriever.retrieveHostnamesDistinct();
		Terms agg = sr.getAggregations().get("host_names_distinct");
		for (Terms.Bucket entry : agg.getBuckets()){
			hostnames.add(entry.getKey());
			//not used anymore because mysql impl
			//helperfunc.createFile("_blacklist", entry.getKey().toString() + ".txt");
		}
		
		return hostnames;
	}
	
	/**
	 * 
	 * @param hostname
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public boolean hostnameExists(String hostname) throws InterruptedException, ExecutionException {
		return retriever.hostnameExists(hostname);
	}
	
	/**
	 * Calls adapter class and get significant terms based on all documents by an given index
	 * @param hostname same as index name
	 * @throws Exception
	 */
	public ArrayList<Hashtable<String, String>> getSigTerms(String hostname, int size) throws Exception {
		
		String indexName = hostname.replace("http://", "").replace("/", "");
		
		SearchResponse result = retriever.retrieveSignificantTerms(indexName, size);	
		
		return new Tagprocessing().getSignificantTags(result.toString());
	}
}
