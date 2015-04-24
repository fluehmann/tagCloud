package TagCloud.test;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.node.Node;

import static org.elasticsearch.node.NodeBuilder.*;
import static org.elasticsearch.common.xcontent.XContentFactory.*;
/**
 * Hello world!
 *
 */
public class App_RemoteServer 
{
	public static void main( String[] args ) throws ElasticsearchException, IOException
    {

		Settings settings = ImmutableSettings.settingsBuilder()
		        .put("cluster.name", "elasticsearch").build();
		Client client =    new TransportClient(settings)
							.addTransportAddress(new InetSocketTransportAddress("192.168.56.101", 9300))
							.addTransportAddress(new InetSocketTransportAddress("192.168.56.101", 9300));
		//Add transport addresses and do something with the client...
		
		IndexResponse response2 = client
				.prepareIndex("auto", "audi", "3")
				.setSource(
						jsonBuilder().startObject().field("title", "audi mustang")
								.field("postDate", new Date())
								.field("message", "brumm... audi")
								.endObject()).execute().actionGet();
    }   
}
