package tagcloud.indexer;

import java.io.IOException;
import java.util.HashMap;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;

import static org.elasticsearch.common.xcontent.XContentFactory.*;

public class Indexer {

	Client client;
	
	public Indexer(String clustername, String ip) {
		client = new Connection().connect(clustername, ip);
		
	}
	
	public boolean index(String index, String type, String id, HashMap<String, String> fields) throws ElasticsearchException, IOException {
		XContentBuilder builder = jsonBuilder();
		builder.startObject();
		for( String key : fields.keySet() ) {
			builder.field( key, fields.get(key) );
		}		
		builder.endObject();
			    
		IndexResponse response = client .prepareIndex(index, type, id)
									.setSource(builder).execute().actionGet();
		return true;
	}
}
