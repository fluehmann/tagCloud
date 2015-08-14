package tagcloud.doccrawler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

import org.apache.commons.io.FilenameUtils;
import org.elasticsearch.ElasticsearchException;

import tagcloud.core.Functions;
import tagcloud.crawler.CrawlerShell;

// implementation of a file crawler
public class FileCrawler extends CrawlerShell {

	private String fileName;

	public FileCrawler(String hostname) {
		super(hostname);
	}


	/**
	 * 
	 */
	public void crawl(String filePath) {

		File f = new File(filePath);
		if ((fileName = f.getName()) == null) {
			fileName = "ES_File_" + (1 + (int) (Math.random() * (999)));
		}

		String suffix = FilenameUtils.getExtension(filePath);
		// build json document from filepath and filecontent
		HashMap<String, String> json = new HashMap<String, String>();

		// determine which import-method to use
		// can easily be enhanced with other file-types
		// Due to incopatibility in older Java-Versions switch-case is replaced by if/else if statements
		
		String sfx = suffix.toLowerCase();
		if (sfx.equals("txt")){
			
			try {
				json.put("hostname", Functions.getDomainName(filePath));
				json.put("content", importTextFile(filePath));
				
				if ( new URL(filePath).openConnection().getContentLength() != -1 ){
					sendToIndex(filePath, json);
				}
			} catch (URISyntaxException | IOException e) {
				e.printStackTrace();
			}

		} else if(sfx.equals("docx")) {
			System.err.println("." + suffix + " Files cannot be imported yet");
		} else if(sfx.equals("pdf")) {
			System.err.println("." + suffix + " Files cannot be imported yet");
		} else {
			System.err.println("File cannot be imported");
		}
		
	}

	/**
	 * method to get the content of a file
	 * @param filePath
	 * @return
	 */
	public String importTextFile(String filePath) {
		
		StringBuilder sb = new StringBuilder();
		String content = "empty file";
		InputStream fis;
		
		try {
			fis = new URL(filePath).openStream();
			
			Reader r = new InputStreamReader(fis);
			int ch = r.read();
			while (ch >= 0) {
				sb.append((char) ch);
				ch = r.read();
			}
			r.close();
			content = sb.toString();

		} catch (FileNotFoundException e) {
			System.err.println("The file '" + fileName + "' was not found. "
					+ e.getMessage());
		} catch (IOException e) {
			System.err.println("Error: " 
					+ e.getMessage());
		}
		return content;
	}

	/**
	 * 
	 * @param filePath
	 * @param json
	 */
	public void sendToIndex(String filePath, HashMap<String, String> json) {
		// index document
		try {
			indexer.indexDocument(Functions.INDEX_NAME, "file", filePath, json);
		} catch (ElasticsearchException e) {
			e.printStackTrace();
			System.out.println("Problem 1");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Problem 2");
		}
	}
}
