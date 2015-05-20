package tagcloud.indexer;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
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
		
		XContentBuilder builder = jsonBuilder();
		builder.startObject();
		for( String key : fields.keySet() ) {
			builder.field( key, fields.get(key) );
		}		
		builder.endObject();
		//System.out.println(builder.string());   
		IndexResponse response = client.prepareIndex(index, type, id).setSource(builder).execute().actionGet();
		//System.out.println(response.toString());
		//processKeywords(index, id, fields);
		return true;
	}
	
//	private void processKeywords(String hostname, String id, HashMap<String, String> fields) throws SQLException{
//		String statement;
//		PreparedStatement ps = db.conn.prepareStatement ("INSERT INTO tc_index (hostname, urlidx) VALUES(?, ?)");
//		ps.setString(1, hostname);
//		ps.setString(2, id);
//		statement = ps.toString();
//		System.out.println(statement);
//		//db.insert(statement);
//		statement = "INSERT INTO tc_index (hostname, urlidx) VALUES('" + hostname + "', '" + id + "')";
//		db.insert(statement);
//		
//		ResultSet rs = db.query("SELECT id FROM tc_index WHERE hostname = '" + hostname + "'");
//		int fk = 0;
//		if ( rs.next() ){
//			fk = rs.getInt(1);
//			
//			//delete existing tag for reindexing
//			db.insert("DELETE FROM tc_keywords WHERE indexid = " + fk);
//		}
//		
//		
//		String tags[] = getTags(fields);
//		for(String tag : tags) {
//			db.insert("INSERT INTO tc_keywords (indexid, word) VALUES(" + Integer.toString(fk) + ", '" + tag + "')");
//		}
//	}
	
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
