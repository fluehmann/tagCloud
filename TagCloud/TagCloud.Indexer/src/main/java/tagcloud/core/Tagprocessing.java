package tagcloud.core;

import java.util.ArrayList;
import java.util.Hashtable;
import org.json.JSONArray;
import org.json.JSONObject;

public class Tagprocessing {

	ArrayList<Hashtable<String, String>> tags;
	
	public Tagprocessing() {
		tags = new ArrayList<Hashtable<String, String>>();
	}
	
	/**
	 * The given String (json format) in the parameter is the result of an SearchResponse object
	 * Based on that json, the required fields can be read by their names
	 * Extend the dictionary object to get additional results in the frontend
	 * @param jsonResult
	 * @return
	 */
	public ArrayList<Hashtable<String, String>> getTags(String jsonResult) {
		
		// loop through hits and get the body of each found indecies
		
		JSONObject obj = new JSONObject(jsonResult);
		JSONArray arr = obj.getJSONObject("hits").getJSONArray("hits");
		
		for (int i = 0; i < arr.length(); i++)
		{
		    String indexUrl = arr.getJSONObject(i).getString("_id");
		    // get nested json object
		    String content = arr.getJSONObject(i).getJSONObject("_source").getString("content");
		    Hashtable<String, String> dictionary = new Hashtable<String, String>(); 
		    
		    // values from json
		    dictionary.put("url", indexUrl);
		    dictionary.put("content", content);
		    
		    // values which are not stored in the documents on elasticsearch
		    dictionary.put("keyword", "sdsdsd");
			
		    tags.add(dictionary);
		}
		return tags;
	}
	
	public ArrayList<Hashtable<String, String>> getSignificantTags(String jsonResult) {
		
		JSONObject obj = new JSONObject(jsonResult);
		JSONArray arr = obj.getJSONObject("aggregations").getJSONObject("significant_keywords").getJSONArray("buckets");
		
		for (int i = 0; i < arr.length(); i++)
		{
			String keyword = arr.getJSONObject(i).getString("key");
			double score = arr.getJSONObject(i).getDouble("score")*1000;
		    Hashtable<String, String> dictionary = new Hashtable<String, String>(); 
		    
		    // values from json
		    dictionary.put("keyword", keyword);
		    dictionary.put("score", Double.toString(score));
//System.out.println(score);		    
		    // values which are not stored in the documents on elasticsearch
		    //dictionary.put("keyword", "sdsdsd");
			
		    tags.add(dictionary);
		}
		return tags;
	}
}
