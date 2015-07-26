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

public class Functions {

	/**
	 * Create a file if not exists in the data folder of the tomcat installation
	 * @param folderName
	 * @param indexName
	 * @return
	 */
	public File createFile(String folderName, String indexName) {
		File f = null;
		File catalinaBase = new File(System.getProperty("catalina.home")).getAbsoluteFile();
		File folder = new File(catalinaBase, "data/" + folderName);

		//System.out.println("home: " + System.getProperty("catalina.home"));
		//System.out.println("base: " + System.getProperty("catalina.base"));
		//System.out.println(folder);
		try {
			f = new File(folder + "/" + indexName + ".txt");

			if (!f.exists()) {
				folder.mkdirs();
				f.createNewFile();
				System.out.println("File '" + indexName + "' created");
			} else {
				System.out.println("File '" + indexName + "' alredy exists");
			}
		} catch (IOException e) {
			System.out.println("Pfad falsch");
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

//	public String getMappingsJsonString() {
//		String JsonMappings = null;
//		try {
//
//			File f = new File("mappings.json");
//			String path = f.getAbsolutePath();
//			InputStream fis = new FileInputStream(path);
//
//			StringBuilder sb = new StringBuilder();
//			Reader r = new InputStreamReader(fis);
//			int ch = r.read();
//			while (ch >= 0) {
//				sb.append((char) ch);
//				ch = r.read();
//			}
//			r.close();
//			JsonMappings = sb.toString();
//			System.out.println(JsonMappings);
//
//		} catch (FileNotFoundException e) {
//			System.err.println("Mapping-File not found: " + e.getMessage());
//		} catch (UnsupportedEncodingException e) {
//			System.err.println("Mapping-File Encoding Exception: "
//					+ e.getMessage());
//		} catch (IOException e) {
//			System.err
//					.println("Failed to load Mapping-File: " + e.getMessage());
//		}
//		return JsonMappings;
//	}
//
//	// retrieve JSON Settings file
//	public String getSettingsJsonString() {
//		String JsonSettings = null;
//		try {
//			// InputStream fis;
//			// fis = new FileInputStream("settings.json");
//
//			File f = new File("settings.json");
//			String path = f.getAbsolutePath();
//			InputStream fis = new FileInputStream(path);
//
//			StringBuilder sb = new StringBuilder();
//			Reader r = new InputStreamReader(fis);
//			int ch = r.read();
//			while (ch >= 0) {
//				sb.append((char) ch);
//				ch = r.read();
//			}
//			r.close();
//			JsonSettings = sb.toString();
//			System.out.println(JsonSettings);
//
//		} catch (FileNotFoundException e) {
//			System.err.println("Settings-File not found: " + e.getMessage());
//		} catch (UnsupportedEncodingException e) {
//			System.err.println("Settings-File Encoding Exception: "
//					+ e.getMessage());
//		} catch (IOException e) {
//			System.err.println("Failed to load Settings-File: "
//					+ e.getMessage());
//		}
//		return JsonSettings;
//	}

	/**
	 * Read the content from a file and give it back as a string
	 * @param folderName
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public String getJsonFile(String folderName, String fileName) throws IOException {
		
//		File catalinaBase = new File(System.getProperty("catalina.home")).getAbsoluteFile();
//		File folder = new File(catalinaBase, "data/" + folderName);
//		
//		//String path = System.getProperty("user.dir") + "/jsonfiles/";
//		//File directory = new File(folder + "/" + fileName);
//		
//		File file = new File(folder + "/" + fileName);
//		
//		if (!file.exists()){
//			folder.mkdirs();
//			file.createNewFile();
//		}
		InputStream fis = new FileInputStream(createFile(folderName, fileName));

		StringBuilder sb = new StringBuilder();
		Reader r = new InputStreamReader(fis);
		int ch = r.read();
		while (ch >= 0) {
			sb.append((char) ch);
			ch = r.read();
		}
		r.close();
//		String base = "/var/data";
//		String relative = new File(base).toURI().relativize(new File(path).toURI()).getPath();
//System.out.println(file);
System.out.println(sb.toString());
		return sb.toString();
	}
}
