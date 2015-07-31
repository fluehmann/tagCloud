package tagcloud.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;

public final class Database {
	public Connection conn;
	private Statement statement;
	public static Database db;

	private Database() {
		String url = "jdbc:mysql://localhost:3306/";
		String dbName = "estagcloud";
		String driver = "com.mysql.jdbc.Driver";
		String userName = "root";
		String password = "";
		try {
			Class.forName(driver).newInstance();
			this.conn = (Connection) DriverManager.getConnection(url + dbName, userName, password);
		} catch (Exception sqle) {
			sqle.printStackTrace();
		}
	}

	/**
	 *
	 * @return MysqlConnect Database connection object
	 */
	public static synchronized Database getDbCon() {
		if (db == null) {
			db = new Database();
		}
		return db;
	}

	/**
	 *
	 * @param query
	 *            String The query to be executed
	 * @return a ResultSet object containing the results or null if not
	 *         available
	 * @throws SQLException
	 */
	public ResultSet query(String query) throws SQLException {
		statement = db.conn.createStatement();
		ResultSet res = statement.executeQuery(query);
		return res;
	}

	/**
	 * @desc Method to insert data to a table
	 * @param insertQuery
	 *            String The Insert query
	 * @return boolean
	 * @throws SQLException
	 */
	public int insert(String insertQuery) throws SQLException {
		statement = db.conn.createStatement();
		int result = statement.executeUpdate(insertQuery);
		return result;
	}
	
	/**
	 * Delete a record from a table
	 * @param deleteQuery
	 * @return
	 * @throws SQLException
	 */
	public int delete(String deleteQuery) throws SQLException {
		statement = db.conn.createStatement();
		int result = statement.executeUpdate(deleteQuery);
		return result;
	}
	
	public int createTable(String tableName) throws SQLException {
		String table = tableName.replace(".", "_");
	System.out.println(table);
		statement = db.conn.createStatement();
		int result = statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+ table +" ("
						 + "id INT NOT NULL AUTO_INCREMENT, "
						 + "keyword VARCHAR(50) NOT NULL, "
						 + "PRIMARY KEY (id)"
						 + ")");
		return result;
	}
	
	public int addKeyword(String table, String keyword) throws SQLException {
		statement = db.conn.createStatement();
		int result = statement.executeUpdate("INSERT INTO "+ table +" ("
						 + "keyword)"
						 + " VALUES('" + keyword + "')"
						 );
		return result;
	}
	
	public Hashtable<Integer, String> getKeywords(String table) throws SQLException {
		Hashtable<Integer, String> keywords = new Hashtable<Integer, String>();
		
		statement = db.conn.createStatement();
		ResultSet rs = statement.executeQuery("SELECT id, keyword FROM "+ table
						 + " ORDER BY keyword ASC"
						 );
		
		while ( rs.next() ){
			keywords.put(rs.getInt(1), rs.getString(2));
		}
		return keywords;
	}
	
	public int delKeyword(String table, String id) throws SQLException {
		statement = db.conn.createStatement();
		int result = statement.executeUpdate("DELETE FROM "+ table + " WHERE id = " + id);
		return result;
	}
}
