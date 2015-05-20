package tagcloud.retriever;

import java.util.Map;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.search.MultiMatchQuery;
import org.elasticsearch.search.SearchHit;

import tagcloud.connection.ESConnection;

public class Retriever {
	Client client;

	public Retriever(String clustername, String ip) {
		client = new ESConnection().connect(clustername, ip);
	}

	/**
	 * 
	 * @param indexName Hostname
	 * @param indexType specify the index type ("page" or "document")
	 * @param indexId (oprional) if needed or known, an unique identifier such as URL can be given
	 * @return response from Elasticseach in json
	 */
	public GetResponse retrieve(String indexName, String indexType, @Nullable String indexId) {

		// 1.Param = index; 2.Param = Type; 3.Param = id(optional)
		GetResponse response = client.prepareGet(indexName, indexType, indexId)
				.execute().actionGet();
		return response;
	}

	/**
	 * 
	 * @param indexName Hostname
	 * @param tag An Keyword which is part of the search term 
	 * @return Response from Elasticsearch in json
	 */
	public SearchResponse retrieve(String indexName, String tag) {

		QueryBuilder multiMatch = QueryBuilders.multiMatchQuery(tag, "body");
		SearchResponse response = client.prepareSearch(indexName)
		        .setQuery(multiMatch)
		        .execute().actionGet();
		
		return response;
	}	
	
	/**
	 * Retrieve all indexed document by hostname
	 * @param indexName Hostname
	 * @return Response from Elasticseach in json
	 * @throws Exception
	 */
	public SearchResponse retrieveByIndexname(String indexName) throws Exception {
        //QueryBuilder qb = null;
        // create the query
        //qb = QueryBuilders.idsQuery().ids("beer_1", "beer_2");

        // Execute the query
        SearchResponse sr = null;
        sr = client.prepareSearch(indexName).execute().actionGet();

        System.out.println( "Total hits: " + sr.getHits().getTotalHits() );
		return sr;
    }

//		SearchResponse response = client.prepareSearch("mongoindex")
//				.setSearchType(SearchType.QUERY_AND_FETCH)
//				.setQuery(fieldQuery("body", tag)).setFrom(0).setSize(60)
//				.setExplain(true).execute().actionGet();
//		SearchHit[] results = response.getHits().getHits();
//
//		for (SearchHit hit : results) {
//			System.out.println(hit.getId()); // prints out the id of the
//												// document
//			Map<String, Object> result = hit.getSource(); // the retrieved
//															// document
//		}
	
}
