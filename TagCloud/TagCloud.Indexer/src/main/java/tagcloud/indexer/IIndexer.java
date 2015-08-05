package tagcloud.indexer;

import java.io.IOException;
import java.util.HashMap;

import org.elasticsearch.ElasticsearchException;

public interface IIndexer {

	public boolean indexDocument(String indexName, String type, String id, HashMap<String, String> json) throws ElasticsearchException, IOException;
}
