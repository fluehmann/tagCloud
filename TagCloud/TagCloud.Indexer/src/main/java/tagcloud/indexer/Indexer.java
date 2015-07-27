package tagcloud.indexer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;

import tagcloud.connection.ESConnection;
import static org.elasticsearch.common.xcontent.XContentFactory.*;

public class Indexer {

	Client client;
	//Database db;

	public Indexer(String clustername, String ip) {

		client = new ESConnection().connect(clustername, ip);
		//db = Database.getDbCon();
	}

	/**
	 * 
	 * @param index Name of the index which is usually the hostname in our case
	 * @param type Define which type of source we want to index. We differ between "page" and "document"
	 * @param id An unique identifier of each indexed source. An URL would be a good choice for web usage
	 * @param fields A Collection with key and value fields to generate a json file within this method
	 * @return
	 * @throws ElasticsearchException
	 * @throws IOException
	 */
	public boolean index(String index, String type, String id, HashMap<String, String> fields) throws ElasticsearchException, IOException {
		//System.out.println("indexName before saving: " + index);
		// check if indexName already exists
		if (!checkIfIndexExists(index)){
			Settings indexSettings = ImmutableSettings.settingsBuilder()
					.put("number_of_shards", 5)
					.put("number_of_replicas", 1)
					.build();
			
			
			//create index if not exists
			CreateIndexRequest indexRequest = new CreateIndexRequest(index, indexSettings);
			indexRequest.settings(getSettingsJsonString());
			indexRequest.mapping("website", getMappingsJsonString());

			client.admin().indices().create(indexRequest).actionGet();

			System.out.println("index '" + index + "' created");
		}

		XContentBuilder builder = jsonBuilder();
		builder.startObject();
		for( String key : fields.keySet() ) {
			builder.field( key, fields.get(key) );
		}		
		builder.endObject();
		// System.out.println("JSON: " + builder.string());   
		IndexResponse response = client.prepareIndex(index, type, id).setSource(builder)
				.execute().actionGet();

		return true;
	}

	private String getMappingsJsonString() {
		String JsonMappings = null;
		try {
			
//			InputStream fis;
//			fis = new FileInputStream("/Users/fluehmann_mbp/Desktop/tagCloud/TagCloud/TagCloud.Indexer/mappings.json");
//			fis = new FileInputStream("mappings.json");
			
//			File catalinaBase = new File( System.getProperty( "catalina.base" ) ).getAbsoluteFile();
//			File folder = new File( catalinaBase, "wtpwebapps/TagCloud.Server/mappings.json" );
//			
//			InputStream fis = new FileInputStream(folder);
			
			File f = new File("mappings.json");
			String path = f.getAbsolutePath();
			InputStream fis = new FileInputStream(path);
			
			StringBuilder sb = new StringBuilder();
		    Reader r = new InputStreamReader(fis);
		    int ch = r.read();
		    while(ch >= 0) {
		        sb.append((char)ch);
		        ch = r.read();
		    }
		    r.close();
		    JsonMappings = sb.toString();
		    System.out.println(JsonMappings);
		    
		} catch (FileNotFoundException e) {
			System.err.println("Mapping-File not found: " + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			System.err.println("Mapping-File Encoding Exception: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Failed to load Mapping-File: " + e.getMessage());
		}
	return JsonMappings;
	}

	// retrieve JSON Settings file
	public String getSettingsJsonString() {
		String JsonSettings = null;
			try {
//				InputStream fis;
//				fis = new FileInputStream("settings.json");
				
				File f = new File("settings.json");
				String path = f.getAbsolutePath();
				InputStream fis = new FileInputStream(path);
				
				StringBuilder sb = new StringBuilder();
			    Reader r = new InputStreamReader(fis);
			    int ch = r.read();
			    while(ch >= 0) {
			        sb.append((char)ch);
			        ch = r.read();
			    }
			    r.close();
			    JsonSettings = sb.toString();
			    
			} catch (FileNotFoundException e) {
				System.err.println("Settings-File not found: " + e.getMessage());
			} catch (UnsupportedEncodingException e) {
				System.err.println("Settings-File Encoding Exception: " + e.getMessage());
			} catch (IOException e) {
				System.err.println("Failed to load Settings-File: " + e.getMessage());
			}
		return JsonSettings;
	}


	public boolean checkIfIndexExists(String indexName) {

		IndexMetaData indexMetaData = client.admin().cluster()
				.state(Requests.clusterStateRequest())
				.actionGet()
				.getState()
				.getMetaData()
				.index(indexName);

		return (indexMetaData != null);
	}

	/**
	 * 
	 * @param fields
	 * @return
	 */
	private String[] getTags(HashMap<String, String> fields){
		//ArrayList<String> tags;
		String body = null;
		for( String key : fields.keySet() ) {
			if ( key == "body" ){
				body = fields.get(key);
				break;
			}
		}

		String[] tags = body.split(" ");

		return tags;		
	}
}
