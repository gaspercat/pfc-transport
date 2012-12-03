package Main;

import java.util.TimeZone;

public class Constants {
	// Database connection
	static public String DB_HOST = "localhost";
	static public String DB_DATABASE = "transportlogics";
	static public String DB_USERNAME = "root";
	static public String DB_PASSWORD = "";
	
	// Services ports
	static public int PORT_WEBSITE = 3002;
	static public int PORT_MOBILE = 3001;
	
	// Time values
	static public TimeZone timeZone = TimeZone.getTimeZone("America/Los_Angeles");
}
