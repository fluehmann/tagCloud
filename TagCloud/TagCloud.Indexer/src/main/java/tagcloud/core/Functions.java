package tagcloud.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;

import tagcloud.database.Database;

public class Functions {
	
	public static String INDEX_NAME = "tagcloud";
	public static String DATABASE_NAME = "tagcloud";
	public static String CLUSTER_NAME = "elasticsearch";
	public static String IP_ELASTICSEARCH = "127.0.0.1";

	/**
	 * Create a file if not exists in the data folder of the tomcat installation
	 * @param folderName
	 * @param indexName
	 * @return
	 */
	public File createFile(String folderName, String fileName) {
		File f = null;
		File catalinaBase = new File(System.getProperty("catalina.home")).getAbsoluteFile();
		File folder = new File(catalinaBase, "data/" + folderName);

		try {
			f = new File(folder + "/" + fileName);

			if (!f.exists()) {
				folder.mkdirs();
				f.createNewFile();
				System.out.println("File '" + fileName + "' created");
			} else {
				System.out.println("File '" + fileName + "' alredy exists");
			}
		} catch (FileNotFoundException e) {
			System.err.println(fileName + " File not found: " + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			System.err.println(fileName + " File Encoding Exception: " + e.getMessage());
		} catch (IOException e) {
			System.err.println(fileName + " Failed to load File: " + e.getMessage());
		}
		return f;
	}

	/**
	 * Get all lines from the text file which named same as the index as a string
	 * @param folderName
	 * @param indexName
	 * @return
	 */
	public String getExcludedTerms(String folderName, String indexName) {
		String line;
		StringBuilder sb;
		String excludedKeywords = "";

		try {

			BufferedReader br = new BufferedReader(new FileReader(createFile(folderName, indexName)));
			sb = new StringBuilder();
			line = br.readLine();

			while (line != null) {
				System.out.println("gelesen: " + line);
				sb.append(line + "|");
				line = br.readLine();
			}
			br.close();

			// remove last separator and convert to String
			if (sb.length() > 1) {
				excludedKeywords = sb.substring(0, sb.length() - 1).toString();
			}

		} catch (FileNotFoundException e1) {
			System.out.println("Die Datei wurde nicht gefunden");
		} catch (IOException e) {
			System.out.println("Fehler beim Lesen der Datei");
		}

		return excludedKeywords;
	}

	/**
	 * Read the content from a file and give it back as a string
	 * @param folderName
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public String getJsonFile(String folderName, String fileName) throws IOException {
		
		File catalinaBase = new File(System.getProperty("catalina.base")).getAbsoluteFile();
		File path = new File(catalinaBase, "/wtpwebapps/TagCloud.Server/WEB-INF/lib/" + fileName);		
		InputStream fis = new FileInputStream(path);

		StringBuilder sb = new StringBuilder();
		Reader r = new InputStreamReader(fis);
		int ch = r.read();
		while (ch >= 0) {
			sb.append((char) ch);
			ch = r.read();
		}
		r.close();

		return sb.toString();
	}
	
	/**
	 * Check if a specific given index exists
	 * @param indexName
	 * @param client
	 * @return
	 */
	public boolean checkIfIndexExists(String indexName, Client client) {

		IndexMetaData indexMetaData = client.admin().cluster()
				.state(Requests.clusterStateRequest())
				.actionGet()
				.getState()
				.getMetaData()
				.index(indexName);

		if (indexMetaData != null){
			System.out.println("index exists");
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Create new index given by name if not already exists
	 * import prepared json settings and mappings files
	 * @param indexName
	 * @param client
	 * @throws IOException
	 */
	public void createMissingIndex(String indexName, Client client) throws IOException{
		System.out.println(checkIfIndexExists(indexName, client));
		boolean result = checkIfIndexExists(indexName, client);
		
		if (!result){
			Settings indexSettings = ImmutableSettings.settingsBuilder()
					.put("number_of_shards", 3)
					.put("number_of_replicas", 1)
					.build();			
			
			//create index if not exists
			CreateIndexRequest indexRequest = new CreateIndexRequest(indexName, indexSettings);
			indexRequest.settings(getJsonFile("/", "settings.json"));
			indexRequest.mapping("website", getJsonFile("/", "mappings.json"));

			client.admin().indices().create(indexRequest).actionGet();
						
			System.out.println("index '" + indexName + "' created");
		}
	}
	
	/**
	 * Return the hostname without protocoll
	 * @param hostname
	 * @return
	 */
	public String removeProtocollFromHost(String hostname){
		String newHost = "";
		newHost = hostname.replace("http://", "")
						  .replace("https://", "")
						  .replace("/", "");
		return newHost;
	}
	
	/**
	 * Get all excluded Terms as string, each term separated by "|" (RegEx notation)
	 * @param tableName
	 * @param db
	 * @return
	 * @throws SQLException
	 */
	public String getExcludedKeywords(String tableName, Database db) throws SQLException {

		String table = tableName.replace(".", "_");
		ResultSet rs = db.query("SELECT id, keyword FROM " + table + " ORDER BY keyword ASC;");
		StringBuilder sb = new StringBuilder();

		while ( rs.next() ){
			//1=id, 2=keyword
			sb.append(rs.getString(2) + "|");
		}
		
		return sb.toString();
	}
}
