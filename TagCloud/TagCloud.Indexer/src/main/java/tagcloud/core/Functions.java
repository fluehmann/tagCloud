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

public class Functions {

	public File createFile(String folderName, String indexName) {
		File f = null;
		File catalinaBase = new File(System.getProperty("catalina.base")).getAbsoluteFile();
		File folder = new File(catalinaBase, "wtpwebapps/TagCloud.Server/" + folderName);

		System.out.println(folder);

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

	public String getMappingsJsonString() {
		String JsonMappings = null;
		try {

			File f = new File("mappings.json");
			String path = f.getAbsolutePath();
			InputStream fis = new FileInputStream(path);

			StringBuilder sb = new StringBuilder();
			Reader r = new InputStreamReader(fis);
			int ch = r.read();
			while (ch >= 0) {
				sb.append((char) ch);
				ch = r.read();
			}
			r.close();
			JsonMappings = sb.toString();
			System.out.println(JsonMappings);

		} catch (FileNotFoundException e) {
			System.err.println("Mapping-File not found: " + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			System.err.println("Mapping-File Encoding Exception: "
					+ e.getMessage());
		} catch (IOException e) {
			System.err
					.println("Failed to load Mapping-File: " + e.getMessage());
		}
		return JsonMappings;
	}

	// retrieve JSON Settings file
	public String getSettingsJsonString() {
		String JsonSettings = null;
		try {
			// InputStream fis;
			// fis = new FileInputStream("settings.json");

			File f = new File("settings.json");
			String path = f.getAbsolutePath();
			InputStream fis = new FileInputStream(path);

			StringBuilder sb = new StringBuilder();
			Reader r = new InputStreamReader(fis);
			int ch = r.read();
			while (ch >= 0) {
				sb.append((char) ch);
				ch = r.read();
			}
			r.close();
			JsonSettings = sb.toString();
			System.out.println(JsonSettings);

		} catch (FileNotFoundException e) {
			System.err.println("Settings-File not found: " + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			System.err.println("Settings-File Encoding Exception: "
					+ e.getMessage());
		} catch (IOException e) {
			System.err.println("Failed to load Settings-File: "
					+ e.getMessage());
		}
		return JsonSettings;
	}

	public String getJsonFile(String folderName, String fileName) throws IOException {
		
		File catalinaBase = new File(System.getProperty("catalina.base")).getAbsoluteFile();
		File folder = new File(catalinaBase, "wtpwebapps/TagCloud.Server/" + folderName);
		
		//String path = System.getProperty("user.dir") + "/jsonfiles/";
		//File directory = new File(folder + "/" + fileName);
		
		File file = new File(folder + "/" + fileName);
		
		if (!file.exists()){
			folder.mkdirs();
			file.createNewFile();
		}
		InputStream fis = new FileInputStream(file);

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
System.out.println(file);
System.out.println(sb.toString());
		return sb.toString();
	}
}
