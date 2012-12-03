package Data;

import java.util.ArrayList;

import Communication.Database;
import Main.Constants;

public class Persistence {
	static final String FIELD_ID = "id";
	static final String FIELD_OBJECT = "serialized";
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC JAVA OBJECTS PERSISTENCE METHODS               ** ** //
	// ** *********************************************************** ** //
	
	public static ArrayList<Integer> listJavaObjects(String className){
		// Get DB connection
		Database connection = Database.getConnection(Constants.DB_HOST, Constants.DB_DATABASE, Constants.DB_USERNAME, Constants.DB_PASSWORD);
		
		// Get class objects IDs
		String table = Persistence.getObjectTable(className);
		return connection.obtainIDs(table);
	}
	
	public static int saveJavaObject(int id, Object object){
		// Get DB connection
		Database connection = Database.getConnection(Constants.DB_HOST, Constants.DB_DATABASE, Constants.DB_USERNAME, Constants.DB_PASSWORD);
		
		// Get table name
		String table = Persistence.getObjectTable(object);
		
		// Prepare field names, types and values
		ArrayList<String> fields = new ArrayList<String>();
		ArrayList<Integer> types = new ArrayList<Integer>();
		ArrayList<Object> values = new ArrayList<Object>();
		fields.add(Persistence.FIELD_OBJECT);
		types.add(Database.TYPE_BLOB);
		values.add(object);
		
		// If object not saved, insert into DB
		if(id == -1){
			Persistence.createObjectTable(connection, object);
			return connection.insertRow(table, fields, values);
		// If object saved, update into DB
		}else{
			boolean ret = connection.updateRow(table, id, fields, values);
			if(ret == true) return id;
			return -1;
		}
	}
	
	public static boolean removeJavaObject(String className, int id){
		if(id == -1) return false;
		
		// Get DB connection
		Database connection = Database.getConnection(Constants.DB_HOST, Constants.DB_DATABASE, Constants.DB_USERNAME, Constants.DB_PASSWORD);
		
		// Remove java object
		String table = Persistence.getObjectTable(className);
		return connection.removeRow(table, id);
	}
	
	public static Object loadJavaObject(String className, int id){
		// Get DB connection
		Database connection = Database.getConnection(Constants.DB_HOST, Constants.DB_DATABASE, Constants.DB_USERNAME, Constants.DB_PASSWORD);
	
		// Prepare parameters
		String table = Persistence.getObjectTable(className);
		ArrayList<String> fields = new ArrayList<String>();
		fields.add(Persistence.FIELD_OBJECT);
		
		// Request object
		ArrayList<Object> ret = connection.obtainRow(table, id, fields);
		
		// Return the object if success, null otherwise
		if(ret != null) return ret.get(0);
		return null;
	}
	
	// ** *********************************************************** ** //
	// ** ** PRIVATE TABLE CREATION METHODS                        ** ** //
	// ** *********************************************************** ** //
	
	private static void createObjectTable(Database db, Object obj){
		// Prepare table name
		String table = getObjectTable(obj);
		
		// Prepare field names
		ArrayList<String> fields = new ArrayList<String>();
		fields.add(Persistence.FIELD_ID);
		fields.add(Persistence.FIELD_OBJECT);
		
		// Prepare field types
		ArrayList<Integer> types = new ArrayList<Integer>();
		types.add(Database.TYPE_PKEY);
		types.add(Database.TYPE_BLOB);
		
		// Create table
		db.createTable(table, fields, types);
	}
	
	// ** *********************************************************** ** //
	// ** ** PRIVATE NAME ENCODING METHODS                         ** ** //
	// ** *********************************************************** ** //
	
	private static String getObjectTable(Object obj){
		return Persistence.getObjectTable(obj.getClass().getName());
	}
	
	private static String getObjectTable(String className){
		String parts[] = className.split("\\.");
		return "jobj_" + parts[parts.length-1];
	}
}