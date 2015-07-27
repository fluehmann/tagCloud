package tagcloud.preprocessing;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import org.elasticsearch.ElasticsearchException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import tagcloud.indexer.IndexAdapter;

public class Cleaner {
	
	public Cleaner(IndexAdapter idxAdpt, Document doc, String url, String hostname) throws ElasticsearchException, IOException {
//		IndexAdapter x = new Adapter("elasticsearch", "127.0.0.1");
//		System.out.println(url + ".. is indexed!");
		
//		Send data over to to ElasticSearch
		String host = hostname.replace("http://", "").replace("/", "");
		idxAdpt.indexDocument("tagcloud", "website", url, extractJson(host, url,doc));

	}
	
	public void sendToIndex(){
		
	}
	
	public HashMap<String, String> extractJson(String hostname, String url, Document doc){
		
		
		String raw = doc.html(); // convert doc to String
		raw = raw.replaceAll("&nbsp;","").trim(); // replace nonbreakingspace
		raw = raw.replaceAll("&amp;","und").trim(); // replace &
				
		// Whitelist will strip html from the string & prevent from XSS attacks
		// Choose a different Whitelist to keep more tags & information:
		// Whitelist whitelist = Whitelist.none();
		Whitelist whitelist = Whitelist.none();
		String plain = Jsoup.clean(raw, url, whitelist);
				
		// Build up the JSON File to transmit to ES - here the magic happens
		HashMap<String, String> json = new HashMap<String, String>();
		json.put("hostname", hostname);
		json.put("url", url);
		json.put("content", plain);

		// put date
		String timeStamp = new SimpleDateFormat("HH:mm:ss, dd.MM.yyyy").format(Calendar.getInstance().getTime());
		json.put("timestamp", timeStamp);
		
		return json;
	}

}
