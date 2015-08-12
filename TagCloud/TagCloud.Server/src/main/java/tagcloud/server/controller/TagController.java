package tagcloud.server.controller;

import java.sql.SQLException;
import java.util.LinkedHashMap;

import tagcloud.database.Database;

public class TagController {
	Database db;
	
	public TagController() {
		db = Database.getDbCon();
	}
	
	/**
	 * Add keyword into blacklist table to ignore those in tagcloud
	 * @param hostname
	 * @param keyword
	 * @throws SQLException
	 */
	public void addKeywordToBlacklist(String hostname, String keyword) throws SQLException {
		String table = hostname.replace(".", "_");
		db.addKeyword(table, keyword);
	}
	
	/**
	 * Return all blacklisted keywords from a specific host
	 * @param hostname
	 * @return
	 * @throws SQLException
	 */
	public LinkedHashMap<Integer, String> getKeywordFromBlacklist(String hostname) throws SQLException {
		String table = hostname.replace(".", "_");
		return db.getKeywords(table);
	}
	
	/**
	 * Delete keyword from a table
	 * @param hostname
	 * @param id
	 * @throws SQLException
	 */
	public void delKeywordFromBlacklist(String hostname, String id) throws SQLException {
		String table = hostname.replace(".", "_");
		db.delKeyword(table, id);
	}
}
