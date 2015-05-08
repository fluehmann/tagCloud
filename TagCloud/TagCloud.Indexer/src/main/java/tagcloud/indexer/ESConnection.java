package tagcloud.indexer;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class ESConnection implements IConnection {

	private static final int PORT = 9300;
	
	public Client connect(String clustername, String ip) {
		
		Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", clustername).build();
		Client client =    new TransportClient(settings)
							.addTransportAddress(new InetSocketTransportAddress(ip, PORT))
							.addTransportAddress(new InetSocketTransportAddress(ip, PORT));
		return client;
	}

	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

}
