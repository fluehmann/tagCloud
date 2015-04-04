package TagCloud.test;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;

import static org.elasticsearch.node.NodeBuilder.*;
import static org.elasticsearch.common.xcontent.XContentFactory.*;
/**
 * Hello world!
 *
 */
public class App 
{
	public static void main( String[] args ) throws ElasticsearchException, IOException
    {

     // Create a node
     Node node = nodeBuilder().clusterName("elasticsearch").node();
     Client client = node.client();

     //prepare a JSON File
     Map<String, Object> json = new HashMap<String, Object>();
     json.put("user","kimchy");
     json.put("postDate",new Date());
     json.put("message","trying out Elasticsearch");
     
     //Using elasticsearch helper to create a json file
		// XContentBuilder builder = jsonBuilder()
		// .startObject()
		// .field("user", "kimchy")
		// .field("postDate", new Date())
		// .field("message", "trying out Elasticsearch")
		// .endObject();
     
     //index a document
		IndexResponse response = client
				.prepareIndex("twitter", "tweet", "1")
				.setSource(
						jsonBuilder().startObject().field("user", "kimchy")
								.field("postDate", new Date())
								.field("message", "trying out Elasticsearch")
								.endObject()).execute().actionGet();
		
		IndexResponse response2 = client
				.prepareIndex("twitter", "tweet", "2")
				.setSource(
						jsonBuilder().startObject().field("user", "bethesda")
								.field("postDate", new Date())
								.field("message", "what the hell is Elasticsearch?")
								.endObject()).execute().actionGet();
		
		IndexResponse response3 = client
				.prepareIndex("twitter", "tweet", "3")
				.setSource(
						jsonBuilder().startObject().field("user", "ubisoft")
								.field("postDate", new Date())
								.field("message", "how smart is Elasticsearch?")
								.endObject()).execute().actionGet();

     // on shutdown
     //node.close();
    }   
}
