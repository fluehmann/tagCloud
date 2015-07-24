package tagcloud.server.controller;

import java.util.ArrayList;
import java.util.Hashtable;

import tagcloud.core.Tagprocessing;
import tagcloud.retriever.Retriever;
import tagcloud.server.controller.RetrieveController;
import tagcloud.connection.ESConnection;
import tagcloud.core.Tagprocessing;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;


public class testclass {

	public static void main(String[] args) throws Exception {


		
		SearchResponse x = new Retriever("elasticsearch", "127.0.0.1").retrieveByIndexname("www.20min.ch");
		
		ArrayList<Hashtable<String, String>> y = new Tagprocessing().getTags(x.toString());
		System.out.println(y);
		
		for (Hashtable<String,String> holla : y){
			
			System.out.println(holla.get("content"));
		}
	}

}
