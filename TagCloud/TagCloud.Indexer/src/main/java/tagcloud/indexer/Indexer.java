package tagcloud.indexer;

import java.io.IOException;
import java.util.HashMap;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;

import tagcloud.connection.ESConnection;
import static org.elasticsearch.common.xcontent.XContentFactory.*;

public class Indexer {

	Client client;
	
	public Indexer(String clustername, String ip) {
		client = new ESConnection().connect(clustername, ip);
	}
	
	/**
	 * Save a new document based on the given parameters. The HashMap will be used to create a json object
	 * If the index (hostname) not exists on the node, it will be created
	 * 
	 * @param index Name of the index which is usually the hostname in our case
	 * @param type Define which type of source we want to index. We differ between "page" and "document"
	 * @param id An unique identifier of each indexed source. An URL would be a good choice for web usage
	 * @param fields A Collection with key and value fields to generate a json file within this method. Fields structure can be changes anytime
	 * @return
	 * @throws ElasticsearchException
	 * @throws IOException
	 */
	public boolean index(String index, String type, String id, HashMap<String, String> fields) throws ElasticsearchException, IOException {
		//System.out.println("indexName before saving: " + index);
		// check if indexName already exists
		if (!checkIfIndexExists(index)){
			Settings indexSettings = ImmutableSettings.settingsBuilder()
					.put("number_of_shards", 5)
					.put("number_of_replicas", 0)
					.build();
			//create index if not exists
			CreateIndexRequest indexRequest = new CreateIndexRequest(index, indexSettings);
			client.admin().indices().create(indexRequest).actionGet();
			System.out.println("index '" + index + "' created");
		}
		
		XContentBuilder builder = jsonBuilder();
		builder.startObject();
		for( String key : fields.keySet() ) {
			builder.field( key, fields.get(key) );
		}		
		builder.endObject();
   
		IndexResponse response = client.prepareIndex(index, type, id).setSource(builder).execute().actionGet();
		//System.out.println("Document stored. " + response.toString());
		System.out.println("Stored: " + id);
		//processKeywords(index, id, fields);
		return true;
	}
	
	/**
	 * Check if an given index name already exists on the node
	 * @param indexName
	 * @return
	 */
	public boolean checkIfIndexExists(String indexName) {

	    IndexMetaData indexMetaData = client.admin().cluster()
	            .state(Requests.clusterStateRequest())
	            .actionGet()
	            .getState()
	            .getMetaData()
	            .index(indexName);

	    return (indexMetaData != null);
	}
	
	/**
	 * Search for the "content" field as key in the given HashMap and split each word in an array
	 * @param fields
	 * @return
	 */
	private String[] getTags(HashMap<String, String> fields){
		//ArrayList<String> tags;
		String body = null;
		for( String key : fields.keySet() ) {
			if ( key == "content" ){
				body = fields.get(key);
				break;
			}
		}
		String[] tags = body.split(" ");
		
		return tags;		
	}
}
