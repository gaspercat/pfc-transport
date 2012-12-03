package Communication;

import java.util.ArrayList;

public abstract class Database {
	// Field types definition
	static public final int TYPE_PKEY = 1;
	static public final int TYPE_BIGINT = 2;
	static public final int TYPE_INTEGER = 3;
	static public final int TYPE_SMALLINT = 4;
	static public final int TYPE_TINYINT = 5;
	static public final int TYPE_VARCHAR2 = 6;
	static public final int TYPE_VARCHAR4 = 7;
	static public final int TYPE_VARCHAR8 = 8;
	static public final int TYPE_VARCHAR16 = 9;
	static public final int TYPE_VARCHAR32 = 10;
	static public final int TYPE_VARCHAR64 = 11;
	static public final int TYPE_VARCHAR128 = 12;
	static public final int TYPE_VARCHAR256 = 13;
	static public final int TYPE_VARCHAR512 = 14;
	static public final int TYPE_VARCHAR1024 = 15;
	static public final int TYPE_VARCHAR2048 = 16;
	static public final int TYPE_VARCHAR4096 = 17;
	static public final int TYPE_VARCHAR8192 = 18;
	static public final int TYPE_VARCHAR16384 = 19;
	static public final int TYPE_FLOAT = 20;
	static public final int TYPE_DOUBLE = 21;
	static public final int TYPE_BLOB = 22;
	
	// Constructor
	static public Database getConnection(String host, String database, String username, String password){
		return new DatabaseMysql(host, database, username, password);
	}
	
	public abstract boolean checkConnected();
	public abstract boolean createTable(String table, ArrayList<String> field, ArrayList<Integer> type);
	public abstract ArrayList<Integer> obtainIDs(String table);
	public abstract ArrayList<Object> obtainRow(String table, int id, ArrayList<String> field);
	public abstract ArrayList<ArrayList<Object>> obtainRows(String table, ArrayList<String> fields, ArrayList<String> constraints);
	public abstract int insertRow(String table, ArrayList<String> field, ArrayList<Object> value);
	public abstract boolean removeRow(String table, int id);
	public abstract boolean updateRow(String table, int id, ArrayList<String> field, ArrayList<Object> value);
}
