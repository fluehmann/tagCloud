package tagcloud.server.controller;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.ExecutionException;

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
		this.retriever = new RetrieverImpl(Functions.CLUSTER_NAME, Functions.IP_ELASTICSEARCH);
		helperfunc = new Functions();
	}
	
	/**
	 * Return documents by a given index name. the amount of results depends on the settings
	 * @param hostname Is equal to index name
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Hashtable<String, String>> get(String hostname) throws Exception {
		// SearchResponse as String (json)
		String result = retriever.retrieveByIndexname(hostname).toString();
		
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
		// SearchResponse as String (json)
		String result = retriever.retrieveByKeyword(Functions.INDEX_NAME, hostname, keyword).toString();
		
		// parse json to ArrayList with key-value entries
		return new Tagprocessing().getTags(result.toString());
	}
	
	/**
	 * Retrieve all existing index names on this node in an arraylist
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
			if ( !entry.getKey().equals("localhost") ){
				hostnames.add(entry.getKey());
			}
		}
		
		return hostnames;
	}
	
	/**
	 * Check if given hostname exists in the index
	 * @param hostname
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public boolean hostnameExists(String hostname) throws InterruptedException, ExecutionException {
		return retriever.hostnameExists(hostname);
	}
	
	/**
	 * Get significant terms based on all documents by an given index
	 * @param hostname same as index name
	 * @throws Exception
	 */
	public ArrayList<Hashtable<String, String>> getSigTerms(String hostname, int size) throws Exception {
		
		SearchResponse result = retriever.retrieveSignificantTerms(hostname, size);	
		
		return new Tagprocessing().getSignificantTags(result.toString());
	}
}
