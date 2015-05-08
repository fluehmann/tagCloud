package tagcloud.core;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import org.elasticsearch.ElasticsearchException;

import tagcloud.indexer.IndexAdapter;
import tagcloud.retriever.RetrieveAdapter;

public class TestMain {

	public static void main(String[] args) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
//		HashMap<String, String> fields = new HashMap<String, String>();
//		fields.put("date", "2015-04-08 14:00:00");
//		fields.put("Title", "A World Beyond: neuer Titel, neuer Starttermin, neuer Trailer");
//		fields.put("body", "Mit überwältigenden Bildern und brillantem CGI erzählen der zweifach Oscar-prämierte Regisseur und Drehbuchautor Brad Bird (MISSION IMPOSSIBLE: PHANTOM PROTOKOLL, RATATOUILLE) und Drehbuchautor Damon Lindelof („Lost“) die unglaubliche, fesselnde Story über ein visionäres Land, in dem nichts unmöglich erscheint. Ebenso spektakulär wie das Team hinter der Kamera ist der Hauptcast: Oscar-Preisträger George Clooney trifft in einem visionären Gegenentwurf zu unserer irdischen Welt auf den ebenso genialen wie undurchsichtigen Wissenschaftler David Nix, gespielt vom zweifachen Golden Globe-Gewinner Hugh Laurie. An Clooneys Seite spielt Shootingstar Britt Robertson („Under the Dome“, KEIN ORT OHNE DICH) als die junge, aufgeweckte Casey, die eine aufregende und gefährliche Mission antritt, um dem Rätsel von Raum und Zeit und der Zukunft der Menschheit auf die Spur zu kommen. ");
//		fields.put("color", "black");
//		
////		Indexer index = new Indexer("elasticsearch", "127.0.0.1");
////		index.index("device", "laptop", "1", fields);
//		
//		IndexAdapter idx = new Adapter("elasticsearch", "127.0.0.1");
//		idx.indexDocument("gamesource.ch", "page", "http://www.gamesource.ch/a-world-beyond-neuer-titel-neuer-starttermin-neuer-trailer/", fields);
		
		RetrieveAdapter retriver = new Adapter("elasticsearch", "127.0.0.1");
		String result = retriver.retriveDocument("gamesource.ch", "Star").toString();
		
		System.out.println(result);
	}

}
