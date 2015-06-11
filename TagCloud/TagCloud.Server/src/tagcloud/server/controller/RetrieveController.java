package tagcloud.server.controller;

import java.util.ArrayList;

import tagcloud.core.Adapter;
import tagcloud.core.Tagprocessing;
import tagcloud.retriever.RetrieveAdapter;

public class RetrieveController {

	RetrieveAdapter retriver;
	
	public RetrieveController() {
		retriver = new Adapter("elasticsearch", "127.0.0.1");
	}
	
	public ArrayList<String> get(String indexName) throws Exception {
		String result = retriver.retrieveByIndexname(indexName).toString();

		// System.out.println(result);
		return new Tagprocessing().getKeywords(result);
	}
}
