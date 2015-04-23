package TagCloud.test;

import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;

public class GetSearchResult {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Settings settings = ImmutableSettings.settingsBuilder()
		        .put("cluster.name", "elasticsearch").build();
		
		Client client =    new TransportClient(settings)
		.addTransportAddress(new InetSocketTransportAddress("localhost", 9300))
		.addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
		//Add transport addresses and do something with the client...
		
		//SearchResponse response = client.prepareSearch()
				//.setQuery(QueryBuilders.termQuery("title", "226"))  
		//		.execute().actionGet();
		//System.out.println( response );
		
		//==========================================
		
		SearchRequestBuilder srb1 = client
			    .prepareSearch().setQuery(QueryBuilders.queryString("ubisoft")).setSize(1);
			//SearchRequestBuilder srb2 = client
			    //.prepareSearch().setQuery(QueryBuilders.matchQuery("user", "ubi")).setSize(1);

			MultiSearchResponse sr = client.prepareMultiSearch()
			        .add(srb1)
			        //.add(srb2)
			        .execute().actionGet();

			// You will get all individual responses from MultiSearchResponse#getResponses()
			long nbHits = 0;
			for (MultiSearchResponse.Item item : sr.getResponses()) {
			    SearchResponse response2 = item.getResponse();
			    nbHits += response2.getHits().getTotalHits();
			    
			    System.out.println(response2);
			}
	}

}
