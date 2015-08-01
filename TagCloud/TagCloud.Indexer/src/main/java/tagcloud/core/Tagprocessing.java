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
			StringBuilder highlights = new StringBuilder();
		    String indexUrl = arr.getJSONObject(i).getString("_id");
		    // get nested json object
		    //String content = arr.getJSONObject(i).getJSONObject("_source").getString("content");
		    JSONArray highlightArr = arr.getJSONObject(i).getJSONObject("highlight").getJSONArray("content");
		   
		    for ( int j = 0; j < highlightArr.length(); j++ ){
		    	if ( j > 0 ){
		    		highlights.append(" .....");
		    	}
		    	highlights.append(highlightArr.getString(j));
		    }

		    Hashtable<String, String> dictionary = new Hashtable<String, String>(); 
		    
		    // values from json
		    dictionary.put("url", indexUrl);
		    dictionary.put("highlight", highlights.toString());
			
		    tags.add(dictionary);
		}
		return tags;
	}
	
	public ArrayList<Hashtable<String, String>> getSignificantTags(String jsonResult) {
		
		JSONObject obj = new JSONObject(jsonResult);
		JSONArray arr = obj.getJSONObject("aggregations").getJSONObject("tagcloud_keywords").getJSONArray("buckets");
		
		// normalize score
		// get top entry
		//double upper = arr.getJSONObject(0).getDouble("score");
		// get last entry
		//double lower = arr.getJSONObject(arr.length()-1).getDouble("score");		
		
		for (int i = 0; i < arr.length(); i++)
		{
			String keyword = arr.getJSONObject(i).getString("key");
			double score = arr.getJSONObject(i).getDouble("score");
		    Hashtable<String, String> dictionary = new Hashtable<String, String>(); 
		    
		    // values from json
		    dictionary.put("keyword", keyword);
		    dictionary.put("score", normalizeScore(arr.length()-1, i));
		    dictionary.put("scoreOrig", Double.toString(score));
			
		    tags.add(dictionary);
		}
		return tags;
	}
	
	public String normalizeScore(int size, int pointer) {
		final int GROUPS = 5;	// there are 5 groups of font sizes. Each tag will be assigned to one group
		int units;		  		// how many entries a group should have
		int score = 4;		
		
		if ( size <= GROUPS ){
			return Integer.toString(score);
		}
		
		units = size/GROUPS;
		
		// 5th group least relevance
		if (pointer <= units*5){
			score = 4;
		}
		
		// 4th group
		if (pointer <= units*4){
			score = 6;
		}
		
		// 3rd group
		if (pointer <= units*3){
			score = 8;
		}
		
		// 2nd group
		if (pointer <= units*2){
			score = 10;
		}
		
		// 1st group. Most relevance
		if (pointer <= units){
			score = 12;
		}
		
		// 1st group. Most relevance
		if (pointer <= units-(units*0.6)){
			score = 15;
		}
		
		return Integer.toString(score);
	}
}
