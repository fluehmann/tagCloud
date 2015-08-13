package tagcloud.indexer;

import java.io.IOException;
import java.util.HashMap;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;

import tagcloud.connection.ESConnection;
import tagcloud.core.Functions;
import static org.elasticsearch.common.xcontent.XContentFactory.*;

public class IndexerImpl implements IIndexer {

	Client client;
	Functions helperfunc;

	public IndexerImpl(String clustername, String ip) {

		client = new ESConnection().connect(clustername, ip);
		helperfunc = new Functions();
	}

	/**
	 * Try to save a document in Elasticsearch
	 * @param index Name of the index which is usually the hostname in our case
	 * @param type Define which type of source we want to index. We differ between "page" and "document"
	 * @param id An unique identifier of each indexed source. An URL would be a good choice for web usage
	 * @param fields A Collection with key and value fields to generate a json file within this method
	 * @return
	 * @throws ElasticsearchException
	 * @throws IOException
	 */
	public boolean indexDocument(String index, String type, String id, HashMap<String, String> fields) throws ElasticsearchException, IOException {
		// solution with one index
		index = Functions.INDEX_NAME;

		XContentBuilder builder = jsonBuilder();
		builder.startObject();
		for( String key : fields.keySet() ) {
			builder.field( key, fields.get(key) );
		}		
		builder.endObject();
		// System.out.println("JSON: " + builder.string());   
		client.prepareIndex(index, type, id).setSource(builder).execute().actionGet();
		System.out.println("Document stored: " + id);
		return true;
	}

	
	/**
	 * Temporary solution to show tagcloud with only one host.
	 * Fake fore- and background set by adding this dummy document
	 */
	public void createDummy() throws IOException {
		
		// check if indexName already exists -> otherwise create new one
		helperfunc.createMissingIndex(Functions.INDEX_NAME, client);
		
		XContentBuilder builder = jsonBuilder();
		builder.startObject();
		builder.field("hostname", "localhost");
		builder.field("content", "this is a dummy entry on localhost");
		builder.endObject();
		client.prepareIndex(Functions.INDEX_NAME, "website", "localhost").setSource(builder).execute().actionGet();
	}

//	public boolean checkIfIndexExists(String indexName) {
//
//		IndexMetaData indexMetaData = client.admin().cluster()
//				.state(Requests.clusterStateRequest())
//				.actionGet()
//				.getState()
//				.getMetaData()
//				.index(indexName);
//
//		return (indexMetaData != null);
//	}


}
