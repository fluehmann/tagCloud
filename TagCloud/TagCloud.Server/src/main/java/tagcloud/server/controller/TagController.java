package tagcloud.server.controller;

import java.sql.SQLException;
import java.util.LinkedHashMap;

import tagcloud.database.Database;

public class TagController {
	Database db;
	
	public TagController() {
		db = Database.getDbCon();
	}
	
	public void addKeywordToBlacklist(String hostname, String keyword) throws SQLException {
		String table = hostname.replace(".", "_");
		db.addKeyword(table, keyword);
	}
	
	public LinkedHashMap<Integer, String> getKeywordFromBlacklist(String hostname) throws SQLException {
		String table = hostname.replace(".", "_");
		return db.getKeywords(table);
	}
	
	public void delKeywordFromBlacklist(String hostname, String id) throws SQLException {
		String table = hostname.replace(".", "_");
		db.delKeyword(table, id);
	}
}
