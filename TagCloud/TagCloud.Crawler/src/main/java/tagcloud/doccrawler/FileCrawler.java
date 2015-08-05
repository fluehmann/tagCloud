package tagcloud.doccrawler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;

import org.apache.commons.io.FilenameUtils;
import org.elasticsearch.ElasticsearchException;

import tagcloud.crawler.CrawlerShell;
import tagcloud.indexer.IIndexer;

// implementation of a file crawler
public class FileCrawler extends CrawlerShell {

	private String fileName;

	public FileCrawler(String hostname) {
		super(hostname);
	}

	public void crawl(String filePath) {

		File f = new File(filePath);
		if ((fileName = f.getName()) == null) {
			fileName = "ES_File_" + (1 + (int) (Math.random() * (99)));
		}

		String suffix = FilenameUtils.getExtension(filePath);
		// build json document from filepath and filecontent
		HashMap<String, String> json = new HashMap<String, String>();

		// determine which import-method to use
		// can easily be enhanced with other file-types
		switch (suffix.toLowerCase()) {
		case "txt":
			json.put(fileName, importTextFile(filePath));
			break;
		case "docx":
			System.err.println("." + suffix + " Files cannot be imported yet");
			break;
		case "pdf":
			System.err.println("." + suffix + " Files cannot be imported yet");
			break;
		default:
			System.err.println("." + suffix + " Files cannot be imported yet");
		}
		sendToIndex(fileName, json);
	}

	// method to get the content of a file
	public String importTextFile(String filePath) {

		String content = "empty file";

		InputStream fis;
		try {
			fis = new FileInputStream(filePath);
			StringBuilder sb = new StringBuilder();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}

	public void sendToIndex(String filePath, HashMap<String, String> json) {
		// index document
		try {
			indexer.indexDocument(fileName, "file", filePath, json);
		} catch (ElasticsearchException e) {
			e.printStackTrace();
			System.out.println("Problem 1");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Problem 2");
		}
	}
}
