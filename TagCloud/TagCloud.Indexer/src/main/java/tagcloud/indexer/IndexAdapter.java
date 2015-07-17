package tagcloud.indexer;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import org.elasticsearch.ElasticsearchException;

public interface IndexAdapter {

	public void indexDocument(String indexName, String type, String id, HashMap<String, String> json) throws ElasticsearchException, IOException;
}
