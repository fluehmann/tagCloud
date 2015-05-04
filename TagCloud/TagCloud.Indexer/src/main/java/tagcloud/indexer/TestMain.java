package tagcloud.indexer;

import java.io.IOException;
import java.util.HashMap;

import org.elasticsearch.ElasticsearchException;

public class TestMain {

	public static void main(String[] args) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		HashMap<String, String> fields = new HashMap<String, String>();
		fields.put("size", "14.3");
		fields.put("manufacter", "lenovo");
		fields.put("model", "T430s");
		fields.put("color", "black");
		
		Indexer index = new Indexer("elasticsearch", "127.0.0.1");
		
		index.index("device", "laptop", "1", fields);
	}

}
