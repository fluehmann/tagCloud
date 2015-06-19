package tagcloud.preprocessing;

import java.io.IOException;
import java.util.HashMap;

import org.elasticsearch.ElasticsearchException;
import org.jsoup.Jsoup;
import org.jsoup.examples.HtmlToPlainText;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import tagcloud.core.Adapter;
import tagcloud.indexer.IndexAdapter;

public class Cleaner {
	
	public Cleaner(IndexAdapter x, Document doc, String url, String hostname) throws ElasticsearchException, IOException {
//		System.out.println(url + ".. is indexed!");
//		System.out.println(doc);
		
//		Send it to ElasticSearch
//		IndexAdapter x = new Adapter("elasticsearch", "127.0.0.1");
		String indexName = hostname.replace("http://", "").replace("/", "");
		

		x.indexDocument(indexName, "website", url, extractJson(url,doc));
		
		
		

	}
	
	public void sendToIndex(){
		
	}
	
	public HashMap<String, String> extractJson(String url, Document doc){
		
		// chose different Whitelist for more tags & information:
		// public static Whitelist basic()
		Whitelist whitelist = Whitelist.simpleText();
		whitelist.addAttributes("strong", "em", "b", "i");
		String plain = Jsoup.clean(doc.html(), url, whitelist);
		
		
//		String plain = new HtmlToPlainText().getPlainText(Jsoup.parse(raw));
		
		HashMap<String, String> json = new HashMap<String, String>();
		json.put(url, plain);
		return json;
	}

}
