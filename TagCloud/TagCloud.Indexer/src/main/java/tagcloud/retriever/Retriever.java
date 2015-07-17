package tagcloud.retriever;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.hppc.ObjectLookupContainer;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.significant.SignificantTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

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

        // Execute the query
        SearchResponse sr = null;
        sr = client.prepareSearch(indexName)
        		.setQuery(QueryBuilders.matchAllQuery())
                .setSize(50)
                .execute()
                .actionGet();

        System.out.println( "Total hits: " + sr.getHits().getTotalHits() );
		return sr;
	}
	
	/**
	 * Get all available index names on this node
	 * @return Container<String> which stores all indeces on the node
	 * @throws Exception
	 */
	public ObjectLookupContainer<String> retrieveIndeces() throws Exception {
		
		ObjectLookupContainer<String> olc = client.admin().cluster().prepareState().execute().actionGet().getState().getMetaData().indices().keys();
		return olc;
	}
	
	/**
	 * 
	 * @param indexName
	 * @return
	 * @throws Exception
	 */
	public SearchResponse retrieveSignificantTerms(String indexName) throws Exception {
		AggregationBuilder<?> aggregation = AggregationBuilders
		                					.significantTerms("significant_keywords")
		                					.field("content")
		                					.exclude("fhnw|kontakt|der|die|das|im|in|ins|an|am|nbsp|und|oder|für|of|impressum|login|startseite|dr|bis|prof")
		                					.size(50);

		// Let say you search for men only
		SearchResponse sr = client.prepareSearch(indexName)
		        .setQuery(QueryBuilders.termQuery("_type", "website"))
		        .setSearchType(SearchType.COUNT)
		        .addAggregation(aggregation)
		        //.get();
		        .execute()
                .actionGet();

//		// sr is here your SearchResponse object
//		SignificantTerms agg = sr.getAggregations().get("significant_keywords");
//
//		// For each entry
//		for (SignificantTerms.Bucket entry : agg.getBuckets()) {
//		    entry.getKey();      // Term
//		    entry.getDocCount(); // Doc count
//		    System.out.println( entry.getKey() + "  " + entry.getDocCount() );
//		}
		
		return sr;
	}
	
	
	public List<Map<String, Object>> getAllDocs(String indexName, String indexType){
        int scrollSize = 500;
        List<Map<String,Object>> esData = new ArrayList<Map<String,Object>>();
        SearchResponse response = null;
        int i = 0;
        while( response == null || response.getHits().hits().length != 0){
            response = client.prepareSearch(indexName)
                    .setTypes(indexType)
                       .setQuery(QueryBuilders.matchAllQuery())
                       .setSize(scrollSize)
                       .setFrom(i * scrollSize)
                    .execute()
                    .actionGet();
            for(SearchHit hit : response.getHits()){
                esData.add(hit.getSource());
            }
            i++;
        }
        return esData;
	}
	
}
