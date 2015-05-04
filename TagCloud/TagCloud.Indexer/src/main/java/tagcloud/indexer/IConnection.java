package tagcloud.indexer;

import org.elasticsearch.client.Client;

public interface IConnection {

	public Client connect(String clustername, String ip);
	public void disconnect();
}
