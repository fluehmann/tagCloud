package tagcloud.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Functions {

	public File createStopwordFile(String indexName) {
		File f = null;
		File catalinaBase = new File( System.getProperty( "catalina.base" ) ).getAbsoluteFile();
		File folder = new File( catalinaBase, "wtpwebapps/TagCloud.Server/_blacklist" );

		System.out.println(folder);
		
		try {
			f = new File(folder + "/" + indexName + ".txt");
			
			if ( !f.exists() ){
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
	
	public String getExcludedTerms(String indexName) {
		String line;
		StringBuilder sb;
		String excludedKeywords = "";
		
		try {

			BufferedReader br = new BufferedReader(new FileReader(createStopwordFile(indexName)));
			sb = new StringBuilder();
			line = br.readLine();
			
			while (line != null) {
				System.out.println("gelesen: " +line);
				sb.append(line + "|");
				line = br.readLine();
			}
			br.close();
			
			// remove last separator and convert to String
			if (sb.length() > 1){
				excludedKeywords = sb.substring(0, sb.length()-1).toString();
			}
		
		} catch (FileNotFoundException e1) {
			System.out.println("Die Datei wurde nicht gefunden");
		} catch (IOException e) {
			System.out.println("Fehler beim Lesen der Datei");
		}
		
		return excludedKeywords;
	}
}
