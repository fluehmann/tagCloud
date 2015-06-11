package tagcloud.core;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class Tagprocessing {

	ArrayList<String> indecies;
	
	public Tagprocessing() {
		indecies = new ArrayList<String>();
	}
	
	public ArrayList<String> getKeywords(String jsonResult) {
		
		// loop through hits and get the body of each found indecies
		
		// todo TF/IDF compare with germanKorpus
		
		JSONObject obj = new JSONObject(jsonResult);
		JSONArray pageName = obj.getJSONObject("hits").getJSONArray("hits");
//System.out.println(pageName);

		JSONArray arr = obj.getJSONObject("hits").getJSONArray("hits");
		for (int i = 0; i < arr.length(); i++)
		{
		    String indexUrl = arr.getJSONObject(i).getString("_id");
			indecies.add(indexUrl);
		    String indexTitle = arr.getJSONObject(i).getJSONObject("_source").getString("Title");
		    //indecies.add(indexTitle);
		}
		
		return indecies;
		
	}
	
	
}
