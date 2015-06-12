package tagcloud.server.controller;

import java.util.ArrayList;

import tagcloud.core.Adapter;
import tagcloud.core.Tagprocessing;
import tagcloud.retriever.RetrieveAdapter;

public class RetrieveController {

	RetrieveAdapter retriever;
	
	public RetrieveController() {
		retriever = new Adapter("elasticsearch", "127.0.0.1");
	}
	
	public ArrayList<String> get(String hostname) throws Exception {
		String indexName = hostname.replace("http://", "").replace("/", "");
		String result = retriever.retrieveByIndexname(indexName).toString();

		// System.out.println(result);
		return new Tagprocessing().getKeywords(result);
	}
}
