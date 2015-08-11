package tagcloud.retriever;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.hppc.ObjectLookupContainer;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.significant.SignificantTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import tagcloud.connection.ESConnection;
import tagcloud.core.Functions;
import tagcloud.database.Database;

public class RetrieverImpl implements IRetriever {
	Client client;
	Functions helperfunc;
	Database db;

	public RetrieverImpl(String clustername, String ip) {
		client 		= new ESConnection().connect(clustername, ip);
		helperfunc  =  new Functions();
		db 			= Database.getDbCon();
	}

	/**
	 * 
	 * @param indexName
	 *            Hostname
	 * @param indexType
	 *            specify the index type ("page" or "document")
	 * @param indexId
	 *            (oprional) if needed or known, an unique identifier such as
	 *            URL can be given
	 * @return response from Elasticseach in json
	 */
	public GetResponse retrieve(String indexName, String indexType, @Nullable String indexId) {

		// 1.Param = index; 2.Param = Type; 3.Param = id(optional)
		GetResponse response = client.prepareGet(indexName, indexType, indexId).execute().actionGet();
		return response;
	}
	
	/**
	 * Get documents which match by a specific tag and show the results as a link to the source website
	 * in addition show a snipped of the text which contains this keyword
	 * 
	 * @param indexName
	 *            Hostname
	 * @param tag
	 *            An Keyword which is part of the search term
	 * @return Response from Elasticsearch in json
	 */
	public SearchResponse retrieveByKeyword(String indexName, String hostname, String keyword) {

//		QueryBuilder multiMatch = QueryBuilders.multiMatchQuery(keyword, "content");
//		SearchResponse response = client.prepareSearch(indexName)
//				.setQuery(multiMatch).execute().actionGet();
		
		QueryBuilder qb = QueryBuilders.boolQuery()
				.must(QueryBuilders.matchPhraseQuery("hostname", helperfunc.removeProtocollFromHost(hostname)))
				.must(QueryBuilders.matchQuery("content", keyword));
		
		SearchResponse response =  client.prepareSearch(helperfunc.removeProtocollFromHost(indexName))
				.setQuery(qb)
				.addHighlightedField("content")
				.execute().actionGet();
		
		System.out.println(response.toString());
		return response;
	}

	/**
	 * Retrieve all indexed document by hostname
	 * 
	 * @param indexName
	 *            Hostname
	 * @return Response from Elasticseach in json
	 * @throws Exception
	 */
	public SearchResponse retrieveByIndexname(String indexName)	throws Exception {

		// Execute the query
		SearchResponse sr = null;
		sr = client.prepareSearch(helperfunc.removeProtocollFromHost(indexName))
				.setQuery(QueryBuilders.matchAllQuery()).setSize(50).execute()
				.actionGet();

		System.out.println("Total hits: " + sr.getHits().getTotalHits());
		return sr;
	}

	/**
	 * Get all available index names on this node
	 * 
	 * @return Container<String> which stores all indeces on the node
	 * @throws Exception
	 */
	public ObjectLookupContainer<String> retrieveIndeces() throws Exception {

		ObjectLookupContainer<String> olc = client.admin().cluster()
				.prepareState().execute().actionGet().getState().getMetaData()
				.indices().keys();
		return olc;
	}
	
	/**
	 * Select distinct hostnames from a specific index
	 * Actually the index name is hardcoded as "tagcloud" -> could be passed by parameters
	 * @return
	 * @throws IOException 
	 */
	public SearchResponse retrieveHostnamesDistinct() throws IOException {
		// fix variables -> can use as parameters if needed
		String indexName = "tagcloud";
		String indexType = "website";
		String fieldName = "hostname";
		
		// check if indexName already exists -> otherwise create new one
		helperfunc.createMissingIndex(indexName, client);
		
		AggregationBuilder<?> aggregation = AggregationBuilders
				.terms("host_names_distinct")
				.field(fieldName)
				.size(0);
		
		SearchResponse sr = client.prepareSearch(indexName)
				.setQuery(QueryBuilders.matchAllQuery())
				.setSearchType(SearchType.COUNT)
				.addAggregation(aggregation)
				.execute()
				.actionGet();
		
		Terms agg = sr.getAggregations().get("host_names_distinct");
		if ( !agg.getBuckets().isEmpty() ){
			for ( Terms.Bucket entry : agg.getBuckets() ){
				System.out.println("DocCount: " + entry.getDocCount());
				System.out.println("website: " + entry.getKey());
				try {
					db.createTable(entry.getKey());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return sr;
	}

	/**
	 * 
	 * @param indexName
	 * @return
	 * @throws Exception
	 */
	public SearchResponse retrieveSignificantTerms(String indexName, int size) throws Exception {
		String tblName = helperfunc.removeProtocollFromHost(indexName);
		
		String hostname = tblName;
		indexName = "tagcloud";
		
		String excludes = helperfunc.getExcludedKeywords(tblName, db);
System.out.println(excludes);		
		AggregationBuilder<?> aggregation = AggregationBuilders
				.significantTerms("tagcloud_keywords")
				.field("content")
				.exclude(excludes)
				//.exclude(helperfunc.getExcludedTerms("_blacklist", hostname + ".txt"))
				.size(size);

		SearchResponse sr = client.prepareSearch(indexName)
				//.setQuery(QueryBuilders.matchAllQuery())
				.setQuery(QueryBuilders.termQuery("_type", "website"))
				.setQuery(QueryBuilders.matchQuery("hostname", hostname))
				.setSearchType(SearchType.COUNT)
				.addAggregation(aggregation)
				// .get();
				.execute().actionGet();

//		SignificantTerms agg = sr.getAggregations().get("significant_keywords");

		// For each entry
//		for (SignificantTerms.Bucket entry : agg.getBuckets()) {
//			entry.getKey(); // Term
//			entry.getDocCount(); // Doc count
//			System.out.println(entry.getKey() + "  " + entry.getDocCount());
//		}

		return sr;
	}
	
	public boolean hostnameExists(String hostname) throws InterruptedException, ExecutionException {
		String indexName = "tagcloud";
		SearchResponse sr = client.prepareSearch(indexName)
				.setQuery(QueryBuilders.matchQuery("hostname", hostname))
				.execute().get();
		
		if (sr.getHits().totalHits() >= 1) {
			return true;
		}
		return false;
	}

	public List<Map<String, Object>> getAllDocs(String indexName, String indexType) {
		int scrollSize = 500;
		List<Map<String, Object>> esData = new ArrayList<Map<String, Object>>();
		SearchResponse response = null;
		int i = 0;
		while (response == null || response.getHits().hits().length != 0) {
			response = client.prepareSearch(indexName).setTypes(indexType)
					.setQuery(QueryBuilders.matchAllQuery())
					.setSize(scrollSize).setFrom(i * scrollSize).execute()
					.actionGet();
			for (SearchHit hit : response.getHits()) {
				esData.add(hit.getSource());
			}
			i++;
		}
		return esData;
	}
}
