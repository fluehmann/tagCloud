package tagcloud.indexer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import org.elasticsearch.ElasticsearchException;

public class Adapter implements IndexAdapter {

	private Indexer idx;
	
	public Adapter(String clustername, String ip) {
		idx = new Indexer(clustername, ip);
	}

	public void indexDocument(String indexName, String type, String id, HashMap<String, String> json) throws ElasticsearchException, IOException, SQLException {

		idx.index(indexName, type, id, json);
		
	}
}
