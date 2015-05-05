package tagcloud.indexer;

import java.io.IOException;
import java.util.HashMap;

import org.elasticsearch.ElasticsearchException;

public class TestMain {

	public static void main(String[] args) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub
		HashMap<String, String> fields = new HashMap<String, String>();
		fields.put("date", "2015-05-05 23:00:00");
		fields.put("Title", "STAR WARS: Die digitale Filmkollektion ab heute als Video on Demand verfügbar");
		fields.put("body", "Während die Spannung steigt, bis im Dezember mit STAR WARS: DAS ERWACHEN DER MACHT das erste STAR WARS Abenteuer seit zehn Jahren die Kinos erobert, sind ab heute – Freitag, 10. April 2015 – erstmals alle sechs bahnbrechenden Filme der Saga, von DIE DUNKLE BEDROHUNG bis zu DIE RÜCKKEHR DER JEDI-RITTER, in der gesamten Galaxie – oder zumindest hier auf Erden – auch als Video on Demand erhältlich.");
		fields.put("color", "black");
		
//		Indexer index = new Indexer("elasticsearch", "127.0.0.1");
//		index.index("device", "laptop", "1", fields);
		
		IndexAdapter idx = new Adapter("elasticsearch", "127.0.0.1");
		idx.indexDocument("gamesource.ch", "page", "http://www.gamesource.ch/star-wars-die-digitale-filmkollektion-ab-heute-als-video-on-demand-verfugbar/", fields);
	}

}
