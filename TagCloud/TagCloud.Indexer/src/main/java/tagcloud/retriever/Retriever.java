package tagcloud.retriever;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
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
	
	public ObjectLookupContainer<String> retrieveIndeces() throws Exception {
		
		ObjectLookupContainer<String> olc = client.admin().cluster().prepareState().execute().actionGet().getState().getMetaData().indices().keys();
		return olc;
	}
	
	public SearchResponse retrieveSignificantTerms(String indexName) throws Exception {
		AggregationBuilder aggregation = AggregationBuilders
							                .significantTerms("significant_terms")
							                .field("content")
							                .size(3);

		// Let say you search for 'website' only
		SearchResponse sr = client.prepareSearch(indexName)
		        //.setQuery(QueryBuilders.termQuery("_type", "website"))
				.setQuery(QueryBuilders.matchAllQuery())
				.addAggregation(aggregation)
		        .execute().actionGet();
		
//		SearchResponse sr=
//                client.prepareSearch(indexName).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
//                FilterBuilders.andFilter(
//                        FilterBuilders.termFilter("_type","website")
//                ))).addAggregation(
//                AggregationBuilders.terms("dt_timeaggs").field("content").size(100)
//        ).setSize(0).get();
		
		return sr;
	}
	
//	SearchResponse response=
//            client.prepareSearch('yourindex').setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
//            FilterBuilders.andFilter(
//                    FilterBuilders.termFilter("server","x"),
//                    FilterBuilders.termFilter("dt_time","x")
//            ))).addAggregation(
//            AggregationBuilders.terms("dt_timeaggs").field("dt_time").size(100).subAggregation(
//                    AggregationBuilders.terms("cpu_aggs").field("cpu").size(100)
//            )
//    ).setSize(0).get();
	
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
