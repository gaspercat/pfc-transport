package Communication;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseMysql extends Database{
	Connection conn = null;
	String hostname = "";
	String database = "";
	String username = "";
	String password = "";
	
	public DatabaseMysql(String host, String database, String username, String password){
		// Save attributes
		this.hostname = host;
		this.database = database;
		this.username = username;
		this.password = password;
		
		// Configure DB connection
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://" + host + "/" + database;
		
		// Create database (if doesn't exist)
		try {
			this.createDatabase();
		} catch (Exception e2) {}
		
		// Establish connection
		try {
			Class.forName(driver);
			this.conn = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			this.conn = null;
		}
	}
	
	public boolean checkConnected(){
		if(conn == null) return false;
		return true;
	}
	
	// Check if database exists. If it doesn't, creates it
	private void createDatabase() throws Exception{
        ArrayList<String> list=new ArrayList<String>();
        
        // Connect to MySQL
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://" + this.hostname, this.username, this.password);
        
        // Get catalog of databases
        Statement st = con.createStatement();
        DatabaseMetaData meta = con.getMetaData();
        ResultSet rs = meta.getCatalogs();
        while (rs.next()) {
            String listofDatabases=rs.getString("TABLE_CAT");
            list.add(listofDatabases);
        }
        
        // If database doesn't exist, create it
        if(!list.contains(this.database)){
            st.executeUpdate("CREATE DATABASE " + this.database);
        }
        
        // Close resources
        rs.close();
        st.close();
        con.close();
	}
	
	public boolean createTable(String table, ArrayList<String> field, ArrayList<Integer> type){
		if(this.conn == null) return false;
		
		String statement = "CREATE TABLE " + table + " (";
	
		// Prepare statement
		for(int i=0;i<field.size();i++){
			if(i>0) statement = statement + ", ";
			statement = statement + field.get(i) + " ";
			switch(type.get(i)){
				case Database.TYPE_PKEY:			statement = statement + "INT NOT NULL AUTO_INCREMENT, PRIMARY KEY(" + field.get(i) + ")";	break;
				case Database.TYPE_BIGINT:			statement = statement + "BIGINT";															break;
				case Database.TYPE_INTEGER:			statement = statement + "INT";																break;
				case Database.TYPE_SMALLINT:		statement = statement + "SMALLINT";															break;
				case Database.TYPE_TINYINT:			statement = statement + "TINYINT";															break;				
				case Database.TYPE_VARCHAR2:		statement = statement + "VARCHAR(2)";														break;
				case Database.TYPE_VARCHAR4:		statement = statement + "VARCHAR(4)";														break;
				case Database.TYPE_VARCHAR8:		statement = statement + "VARCHAR(8)";														break;
				case Database.TYPE_VARCHAR16:		statement = statement + "VARCHAR(16)";														break;
				case Database.TYPE_VARCHAR32:		statement = statement + "VARCHAR(32)";														break;
				case Database.TYPE_VARCHAR64:		statement = statement + "VARCHAR(64)";														break;
				case Database.TYPE_VARCHAR128:		statement = statement + "VARCHAR(128)";														break;
				case Database.TYPE_VARCHAR256:		statement = statement + "VARCHAR(256)";														break;
				case Database.TYPE_VARCHAR512:		statement = statement + "VARCHAR(512)";														break;
				case Database.TYPE_VARCHAR1024:		statement = statement + "VARCHAR(1024)";													break;
				case Database.TYPE_VARCHAR2048:		statement = statement + "VARCHAR(2048)";													break;
				case Database.TYPE_VARCHAR4096:		statement = statement + "VARCHAR(4096)";													break;
				case Database.TYPE_VARCHAR8192:		statement = statement + "VARCHAR(8192)";													break;
				case Database.TYPE_VARCHAR16384:	statement = statement + "VARCHAR(16384)";													break;
				case Database.TYPE_FLOAT:			statement = statement + "FLOAT";															break;
				case Database.TYPE_DOUBLE:			statement = statement + "DOUBLE";															break;
				case Database.TYPE_BLOB:			statement = statement + "BLOB";																break;
			}
		}
		statement = statement + ")";
		
		try {
			// Make request
			PreparedStatement pstmt = conn.prepareStatement(statement);
			pstmt.executeUpdate();
			pstmt.close();
			conn.commit();
			// Return true on success
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public ArrayList<Integer> obtainIDs(String table){
		if(this.conn == null) return null;
		
		String request = "SELECT id FROM " + table;
		ArrayList<Integer> ret = new ArrayList<Integer>();
		
		try {
			// Make request
			PreparedStatement pstmt = conn.prepareStatement(request);
			ResultSet result = pstmt.executeQuery();
			while(result.next()){
				Integer val = new Integer(result.getInt("id"));
				ret.add(val);
			}
			result.close();
			pstmt.close();
			conn.commit();
			// Return ID list on success
			return ret;
		} catch (SQLException e) {
			return null;
		}
	}
	
	public ArrayList<Object> obtainRow(String table, int id, ArrayList<String> field){
		if(this.conn == null) return null;
		
		String request = "SELECT ";
		ArrayList<Object> ret = new ArrayList<Object>();
		
		// Prepare request statement
		for(int i=0;i<field.size();i++){
			request = request + field.get(i);
			if(i<field.size()-1) request = request + ", ";
		}
		request = request + " FROM " + table + " WHERE id=?";
		
		try {
			// Make request
			PreparedStatement pstmt = conn.prepareStatement(request);
			pstmt.setInt(1, id);
			ResultSet result = pstmt.executeQuery();
			ResultSetMetaData resultMeta = result.getMetaData();
			if(result.next()){
				for(int i=0;i<field.size();i++){
					switch(resultMeta.getColumnType(i)){
						case Types.BIGINT:
						case Types.INTEGER:
						case Types.SMALLINT:
						case Types.TINYINT:
							ret.add(new Integer(result.getInt(i)));
							break;
							
						case Types.VARCHAR:
						case Types.LONGNVARCHAR:
						case Types.LONGVARCHAR:
							ret.add(new String(result.getString(i)));
							break;
							
						case Types.FLOAT:
							ret.add(new Float(result.getFloat(i)));
							break;
							
						case Types.DOUBLE:
							ret.add(new Double(result.getDouble(i)));
							break;
							
						case Types.BLOB:
							ret.add(result.getObject(i));
							
					}
					ret.add(result.getObject(1+i));
				}
			}else{
				return null;
			}
			result.close();
			pstmt.close();
			conn.commit();
			// Return row on success
			return ret;
		} catch (SQLException e) {
			return null;
		}
	}
	
	public ArrayList<ArrayList<Object>> obtainRows(String table, ArrayList<String> fields, ArrayList<String> constraints){
		if(this.conn == null) return null;
		
		String request = "SELECT ";
		ArrayList<ArrayList<Object>> ret = new ArrayList<ArrayList<Object>>();
		ArrayList<Object> res = new ArrayList<Object>();
		
		// Prepare request statement
		for(int i=0;i<fields.size();i++){
			request = request + fields.get(i);
			if(i<fields.size()-1) request = request + ", ";
		}
		request = request + " FROM " + table;
		
		if(constraints != null && constraints.size()>0){
			 request = request + " WHERE ";
			 for(int i=0;i<constraints.size();i++){
				 request = request + constraints.get(i) + " ";
			 }
		}
		
		try {
			// Make request
			PreparedStatement pstmt = conn.prepareStatement(request);
			ResultSet result = pstmt.executeQuery();
			ResultSetMetaData resultMeta = result.getMetaData();
			while(result.next()){
				res = new ArrayList<Object>();
				for(int i=0;i<fields.size();i++){
					switch(resultMeta.getColumnType(i)){
						case Types.BIGINT:
						case Types.INTEGER:
						case Types.SMALLINT:
						case Types.TINYINT:
							res.add(new Integer(result.getInt(1+i)));
							break;
							
						case Types.VARCHAR:
						case Types.LONGNVARCHAR:
						case Types.LONGVARCHAR:
							res.add(new String(result.getString(1+i)));
							break;
							
						case Types.FLOAT:
							res.add(new Float(result.getFloat(1+i)));
							break;
							
						case Types.DOUBLE:
							res.add(new Double(result.getDouble(1+i)));
							break;
							
						case Types.BLOB:
							res.add(result.getObject(1+i));
							
					}
				}
				ret.add(res);
			}
			result.close();
			pstmt.close();
			conn.commit();
			// Return row on success
			return ret;
		} catch (SQLException e) {
			return null;
		}
	}
	
	public ArrayList<ArrayList<Object>> obtainRows(String request, int nfields){
		if(this.conn == null) return null;
		
		ArrayList<ArrayList<Object>> ret = new ArrayList<ArrayList<Object>>();
		ArrayList<Object> res = new ArrayList<Object>();
		
		try {
			// Make request
			PreparedStatement pstmt = conn.prepareStatement(request);
			ResultSet result = pstmt.executeQuery();
			ResultSetMetaData resultMeta = result.getMetaData();
			while(result.next()){
				res = new ArrayList<Object>();
				for(int i=0;i<nfields;i++){
					switch(resultMeta.getColumnType(1+i)){
						case Types.BIGINT:
						case Types.INTEGER:
						case Types.SMALLINT:
						case Types.TINYINT:
							res.add(new Integer(result.getInt(1+i)));
							break;
							
						case Types.VARCHAR:
						case Types.LONGNVARCHAR:
						case Types.LONGVARCHAR:
							res.add(new String(result.getString(1+i)));
							break;
							
						case Types.FLOAT:
							res.add(new Float(result.getFloat(1+i)));
							break;
							
						case Types.DOUBLE:
							res.add(new Double(result.getDouble(1+i)));
							break;
							
						case Types.BLOB:
							res.add(result.getObject(1+i));
							
						default:
							res.add(new Float(result.getFloat(1+i)));
							break;
					}
				}
				ret.add(res);
			}
			result.close();
			pstmt.close();
			// Return row on success
			return ret;
		} catch (SQLException e) {
			return null;
		}
	}
	
	public int insertRow(String table, ArrayList<String> field, ArrayList<Object> value){
		if(this.conn == null) return -1;
		
		String statement = "INSERT INTO " + table;
		String fields = "(";
		String values = "(";
		
		// Prepare parameters
		for(int i=0;i<field.size();i++){
			fields = fields + field.get(i);
			values = values + "?";
			if(i<field.size()-1){
				fields = fields + ", ";
				values = values + ", ";
			}
		}
		fields = fields + ")";
		values = values + ")";
		
		// Prepare request
		statement = statement + " " + fields + " VALUES " + values;
		
		try {
			// Make request
			PreparedStatement pstmt = conn.prepareStatement(statement);
			for(int i=0;i<value.size();i++){
				Object val = value.get(i);
				if(val.getClass().getName() == "String"){
					pstmt.setString(1+i, (String)val);
				}else if(val.getClass().getName() == "Integer"){
					pstmt.setInt(1+i, ((Integer)val).intValue());
				}else if(val.getClass().getName() == "Float"){
					pstmt.setFloat(1+i, ((Float)val).floatValue());
				}else if(val.getClass().getName() == "Double"){
					pstmt.setDouble(1+i, ((Double)val).doubleValue());
				}else{
					pstmt.setObject(1+i, val);
				}
			}
			pstmt.executeUpdate();
			// Return ID on success
			ResultSet rs = pstmt.getGeneratedKeys();
		    int id = -1;
		    if (rs.next()) {
		      id = rs.getInt(1);
		    }
		    rs.close();
		    pstmt.close();
		    conn.commit();
			return id;
		} catch (SQLException e) {
			return -1;
		}
	}
	
	public boolean removeRow(String table, int id){
		if(this.conn == null) return false;
		
		String statement = "DELETE FROM " + table + " WHERE id=?";
		
		try {
			// Make request
			PreparedStatement pstmt = conn.prepareStatement(statement);
			pstmt.setInt(1, id);
			int num = pstmt.executeUpdate();
			pstmt.close();
			conn.commit();
			// Return true on success
			if(num == 0) return false;
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public boolean updateRow(String table, int id, ArrayList<String> field, ArrayList<Object> value){
		if(this.conn == null) return false;
		
		String statement = "UPDATE " + table + " SET ";
		
		// Prepare statement
		for(int i=0;i<field.size();i++){
			statement = statement + field.get(i) + "=?";
			if(i<field.size()-1) statement = statement + ", ";
		}
		statement = statement + " WHERE id=" + id;
		
		try {
			// Make request
			PreparedStatement pstmt = conn.prepareStatement(statement);
			for(int i=0;i<field.size();i++){
				Object val = value.get(i);
				if(val.getClass().getName() == "String"){
					pstmt.setString(1+i, (String)val);
				}else if(val.getClass().getName() == "Integer"){
					pstmt.setInt(1+i, ((Integer)val).intValue());
				}else if(val.getClass().getName() == "Float"){
					pstmt.setFloat(1+i, ((Float)val).floatValue());
				}else if(val.getClass().getName() == "Double"){
					pstmt.setDouble(1+i, ((Double)val).doubleValue());
				}else{
					pstmt.setObject(1+i, val);
				}
			}
			pstmt.executeUpdate();
		    pstmt.close();
		    conn.commit();
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
}
