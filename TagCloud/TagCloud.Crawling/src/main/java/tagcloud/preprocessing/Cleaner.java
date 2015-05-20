package tagcloud.preprocessing;

import java.io.IOException;
import java.util.HashMap;

import org.elasticsearch.ElasticsearchException;
import org.jsoup.nodes.Document;

import tagcloud.indexer.Adapter;
import tagcloud.indexer.IndexAdapter;

public class Cleaner {
	
	public Cleaner(Document doc, String url) throws ElasticsearchException, IOException {
//		System.out.println(url + ".. is indexed!");
//		System.out.println(doc);
		
//		Send it to ElasticSearch
		IndexAdapter x = new Adapter("elasticsearch", "127.0.0.1");
		x.indexDocument("testindexsimon", "website", url, new HashMap<String, String>());
		
		
		
		// 1st extract json parts
		// 2nd get rid of html
	}
	
	public void sendToIndex(){
		
	}
	
	public void extractJson(){
		
	}

}
