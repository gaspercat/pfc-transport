package Communication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import Main.Application;
import Main.Constants;
import Synchronization.SyncProducts;

public class WebsiteListener implements Runnable{
	private static WebsiteListener instance = null;
	private static Thread thrd = null;
	private static boolean alive = false;
	
	private ServerSocket listener;
	
	private WebsiteListener(){
		// Create socket
		try {
			this.listener = new ServerSocket(Constants.PORT_WEBSITE, 15);
		} catch (IOException e) {
			this.listener = null;
		}
	};
	
	// ** *********************************************************** ** //
	// ** ** PRIVATE CONNECTION HANDLER CLASS                      ** ** //
	// ** *********************************************************** ** //
	
	private class ConnectionHandler extends Thread{
		BufferedReader in;
		BufferedWriter out;
		Socket sck;
		
		public void setConnection(Socket sck){
			this.sck = sck;
			
			try {
	            this.in = new BufferedReader(new InputStreamReader(sck.getInputStream()));
	            this.out = new BufferedWriter(new OutputStreamWriter(sck.getOutputStream()));
			} catch (IOException e) {
				Application.showMessage("Failed to bind Website connection!");
				this.sck = null;
		    }
		}
		
		public void run(){
			if(this.sck == null) return;
			
			JsonParser parser = new JsonParser();
			String buffer = "";
			
			// Read request
			try {
				int tlen;
				char tbuff[] = new char[1024];
				while(!parser.checkFormat(buffer)){
					tlen = this.in.read(tbuff, 0, 1024);
					buffer = buffer + String.valueOf(tbuff, 0, tlen);
				}
			} catch (IOException e) {
				Application.showMessage("Failed to read Website request!");
			}
				
			// Process received JSON request
			JsonParser.JsonNode request = parser.parseString(buffer);
			JsonParser.JsonNode response = this.processSentence(request);
			String outdata = response.getStringEncoded();
				
			// Send response
			try{
				this.out.write(outdata);
				this.out.flush();
				this.sck.close();
			} catch (IOException e) {
				Application.showMessage("Failed to respond Website request!");
			}
		}
		
		private JsonParser.JsonNode processSentence(JsonParser.JsonNode sentence){
			String query = sentence.getStringAttribute("act", 0);
			JsonParser.JsonNode ret = null;
			
			// If asking to sync down changes on server
			if(query.compareTo("synd") == 0){
				String repo = sentence.getStringAttribute("repo", 0);
				
				// If sync down products
				if(repo.compareTo("products") == 0){
					SyncProducts sync = SyncProducts.getInstance();
					ret = sync.syncDown(sentence.getIntegerAttribute("ver", 0).intValue());
				}
				
	        // If asking to sync up changes on client
			}else if(query.compareTo("synu") == 0){
				String repo = sentence.getStringAttribute("repo", 0);
				
				// If sync up products
				if(repo.compareTo("products") == 0){
					SyncProducts sync = SyncProducts.getInstance();
					ret = sync.syncUp(sentence);
				}
			}
			
			return ret;
		}
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC CONSTRUCTORS                                   ** ** //
	// ** *********************************************************** ** //
	
	public static WebsiteListener getInstance(){
		// If non-existent, create singleton instance
		if(WebsiteListener.instance == null){
			WebsiteListener.instance = new WebsiteListener();
			// If could not listen to the port, remove instance
			if(WebsiteListener.instance.listener == null){
				WebsiteListener.instance = null;
			}
		}
		// Return instance
		return WebsiteListener.instance;
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC SERVICE CONTROL METHODS                        ** ** //
	// ** *********************************************************** ** //
	
	public void startService(){
		if(WebsiteListener.thrd == null){
			WebsiteListener.alive = true;
			WebsiteListener.thrd = new Thread(this);
			thrd.start();
		}
	}
	
	public void stopService(){
		if(WebsiteListener.thrd != null){
			// Stop thread
			WebsiteListener.alive = false;
			// Stop listener
			try {
				this.listener.close();
			} catch (IOException e) {}
		}
	}
	
	public void run(){
		while(WebsiteListener.alive){
			try {
				// Listen for a connection
				Socket sck = this.listener.accept();
				// Assign a thread to the new connection
				ConnectionHandler hdlr = new ConnectionHandler();
				hdlr.setConnection(sck);
				hdlr.start();
			} catch (IOException e) {}
		}
	}
}
