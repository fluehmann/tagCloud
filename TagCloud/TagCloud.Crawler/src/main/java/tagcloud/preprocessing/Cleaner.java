package tagcloud.preprocessing;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import org.elasticsearch.ElasticsearchException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import tagcloud.core.Functions;
import tagcloud.indexer.IIndexer;

public class Cleaner {
	
	/**
	 * Cleaner class to preprocess contents of a website.
	 * Will remove all HTML tags and unnecessary content.
	 * 
	 * @param indexer: reference to the indexer interface
	 * @param doc: the content of a website stored as a document
	 * @param url: the exact url of the website
	 * @param hostname: the name of the website
	 */
	public Cleaner(IIndexer indexer, Document doc, String url, String hostname) throws ElasticsearchException, IOException {		

		// get hostname
		String host = hostname.replace("http://", "").replace("/", "");
		// send content over to Elasticsearch
		indexer.indexDocument(Functions.INDEX_NAME, "website", url, extractJson(host, url,doc));
	}
	
	/**
	 * Method to extract contents from a document object and put it
	 * into a json-alike format
	 * @param hostname
	 * @param url
	 * @param doc
	 * @return json
	 */
	public HashMap<String, String> extractJson(String hostname, String url, Document doc){
		
		String raw = doc.html(); // convert doc to String
		raw = raw.replaceAll("&nbsp;"," ").trim(); // replace nonbreakingspace
		raw = raw.replaceAll("&amp;","und").trim(); // replace &
				
		// Whitelist will strip all html-tags from the string
		// Whitelist.none() does not allow any html-tag.
		// Choose a different Whitelist to keep more tags & information.
		Whitelist whitelist = Whitelist.none();
		String plain = Jsoup.clean(raw, url, whitelist);
				
		// Build up the JSON File to transmit to Elasticsearch - here the magic happens
		HashMap<String, String> json = new HashMap<String, String>();
		json.put("hostname", hostname);
		json.put("url", url);
		json.put("content", plain);

		// set the date
		String timeStamp = new SimpleDateFormat("HH:mm:ss, dd.MM.yyyy").format(Calendar.getInstance().getTime());
		json.put("timestamp", timeStamp);
		
		return json;
	}

}
