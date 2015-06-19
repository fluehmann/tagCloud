package tagcloud.preprocessing;

import java.io.IOException;
import java.util.HashMap;

import org.elasticsearch.ElasticsearchException;
import org.jsoup.nodes.Document;

import tagcloud.core.Adapter;
import tagcloud.indexer.IndexAdapter;

public class Cleaner {
	
	public Cleaner(IndexAdapter idxAdapter, Document doc, String url, String hostname) throws ElasticsearchException, IOException {
//		System.out.println(url + ".. is indexed!");
//		System.out.println(doc);
		
//		Send it to ElasticSearch
//		IndexAdapter x = new Adapter("elasticsearch", "127.0.0.1");
		String indexName = hostname.replace("http://", "").replace("/", "");
		
		idxAdapter.indexDocument(indexName, "website", url, extractJson(url, doc));
		
		
		
		// 1st extract json parts
		// 2nd get rid of html
	}
	
	public void sendToIndex(){
		
	}
	
	public HashMap<String, String> extractJson(String url, Document doc){
		
		// remove html tags and stuff
		String parsedBody = doc.toString();
			
		HashMap<String, String> json = new HashMap<String, String>();
		json.put(url, parsedBody);
		return json;
	}

}
