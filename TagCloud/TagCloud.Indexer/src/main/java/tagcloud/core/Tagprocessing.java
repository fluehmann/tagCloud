package tagcloud.core;

import java.util.ArrayList;
import java.util.Hashtable;
import org.json.JSONArray;
import org.json.JSONObject;

public class Tagprocessing {

	ArrayList<Hashtable<String, String>> indecies;
	
	public Tagprocessing() {
		indecies = new ArrayList<Hashtable<String, String>>();
	}
	
	public ArrayList<Hashtable<String, String>> getKeywords(String jsonResult) {
		
		// loop through hits and get the body of each found indecies
		
		// todo TF/IDF compare with germanKorpus
		
		JSONObject obj = new JSONObject(jsonResult);
		JSONArray pageName = obj.getJSONObject("hits").getJSONArray("hits");
//System.out.println(pageName);

		JSONArray arr = obj.getJSONObject("hits").getJSONArray("hits");
		for (int i = 0; i < arr.length(); i++)
		{
		    String indexUrl = arr.getJSONObject(i).getString("_id");
		    // get nested json object
		    String content = arr.getJSONObject(i).getJSONObject("_source").getString("content");
		    Hashtable<String, String> dictionary = new Hashtable<String, String>(); 
		    
		    dictionary.put("url", indexUrl);
		    dictionary.put("keyword", "sdsdsd");
		    dictionary.put("content", content);
			
		    indecies.add(dictionary);
		    //String indexTitle = arr.getJSONObject(i).getJSONObject("_source").getString("Title");
		    //indecies.add(indexTitle);
		}
		
		return indecies;
		
	}
	
	
}
