package Main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import Type.TypeCoordinates;
import Type.TypeAddress;
import Type.TypeDirections;

import Communication.Geofeed;
import Communication.MobileListener;
import Communication.WebsiteListener;
import Data.HandlerLocationDepot;
import Data.HandlerLocationDropoff;
import Data.HandlerLocationTransport;

public class Application {
	private static HandlerLocationDepot depots = null;
	private static HandlerLocationDropoff dropoffs = null;
	private static HandlerLocationTransport transports = null;
	
	private static WebsiteListener lstnWebsite = null;
	private static MobileListener lstnMobile = null;
	
	public static void main(String args[]){
		// Data initialization
		// ************************************
		
		System.out.print("DATA INITIALIZATION\n");
		
		// Load depots data
		System.out.print("  Loading depots data... ");
		Application.depots = HandlerLocationDepot.getInstance();
		System.out.print("DONE (" + String.valueOf(Application.depots.getNumDepots()) + " instances)\n");
		
		// Load dropoffs data
		System.out.print("  Loading dropoffs data... ");
		Application.dropoffs = HandlerLocationDropoff.getInstance();
		System.out.print("DONE (" + String.valueOf(Application.dropoffs.getNumDropoffs()) + " instances)\n");
		
		// Load transports data
		System.out.print("  Loading transports data... ");
		Application.transports = HandlerLocationTransport.getInstance();
		System.out.print("DONE (" + String.valueOf(Application.transports.getNumTransports()) + " instances)\n");
		
		System.out.print("\n");
		
		// Listeners initialization
		// ************************************
		
		System.out.print("LISTENERS INITIALIZATION\n");
		
		// Listen to website connections
		System.out.print("  Listening to website connections... ");
		Application.lstnWebsite = WebsiteListener.getInstance();
		if(Application.lstnWebsite != null){
			Application.lstnWebsite.startService();
			System.out.print("DONE\n");
		}else{
			System.out.print("FAIL\n");
		}
		
		System.out.print("\n");
		
		// WARNING! GROUND-0 TEST AREA
		// ************************************
		
		/*Geofeed geofeed = Geofeed.getInstance();
		
		TypeAddress direction = new TypeAddress("Spain", "Barcelona", 8011, "Comte d'Urgell", 109, 4, 2);
		TypeAddress direction2 = new TypeAddress("Spain", "Palafrugell", 17200, "Carrer del Terme", 40);
		
		TypeCoordinates coordinates = geofeed.getAddressCoordinates(direction);
		TypeCoordinates coordinates2 = geofeed.getAddressCoordinates(direction2);
		
		TypeDirections route = geofeed.getRoute(coordinates, coordinates2);
		System.out.println(route.toString());
		System.out.println(direction.toString());*/
		
		// Start console
		// ************************************
		
		try {
			Application.console();
		} catch (Exception e) {
			System.out.println("FATAL ERROR! APPLICATION TERMINATED!\n");
		}
		
		// Listeners detention
		// ************************************
		
		if(Application.lstnWebsite != null) Application.lstnWebsite.stopService();
	}
	
	public static void showMessage(String message){
		System.out.println();
		System.out.println(message);
		System.out.print("$shell: ");
	}
	
	private static void console() throws Exception{
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		boolean exit = false;
		
		System.out.println("Welcome to the TransportLogics shell!");
		System.out.println("Write 'help' to see a list of commands.");
		
		while(exit == false){
			// Read console line
			System.out.print("$shell: ");
			String cmd = in.readLine();
			if(cmd!=""){
				// Split command into words
				String pars[] = cmd.split("\\s+");
				// Switch depending on command
				if(pars[0].equals("help")){
					Application.commandHelp();
				}else if(pars[0].equals("exit")){
					exit = true;
				}else if(pars[0].equals("websvc")){
					Application.commandWebService(Application.getParams(pars));
				}else if(pars[0].equals("mobsvc")){
					Application.commandMobileService(Application.getParams(pars));
				}
			}
		}
	}
	
	private static void commandHelp(){
		System.out.println("");
		System.out.println("Shell allowed commands:");
		System.out.println("  exit :: Stop the application");
		System.out.println("  websvc [start|stop] :: Start/stop the web service");
		System.out.println("  mobsvc [start|stop] :: Start/stop the mobile service");
		System.out.println("");
	}
	
	private static void commandWebService(String params[]){
		if(params.length == 0){
			System.out.print("The WEB service is ");
			if(Application.lstnWebsite == null){
				System.out.print("OFFLINE\n");
			}else{
				System.out.print("ONLINE\n");
			}
		}else if(params.length > 1){
			System.out.print("Malformed command!");
		}else if(params[0].equals("start")){
			if(Application.lstnWebsite == null){
				System.out.print("Listening to website connections... ");
				Application.lstnWebsite = WebsiteListener.getInstance();
				if(Application.lstnWebsite == null){
					System.out.print("FAIL\n");
				}else{
					Application.lstnWebsite.startService();
					System.out.print("DONE\n");
				}
			}else{
				System.out.print("The WEB service is already running\n");
			}
		}else if(params[0].equals("stop")){
			if(Application.lstnWebsite != null){
				System.out.print("Stopping website service... ");
				Application.lstnWebsite.stopService();
				System.out.print("DONE\n");
			}else{
				System.out.print("The WEB service is already stopped\n");
			}
		}else{
			System.out.print("Malformed command!\n");
		}
	}
	
	private static void commandMobileService(String params[]){
		
	}
	
	private static String[] getParams(String words[]){
		String ret[] = new String[words.length-1];
		
		// Add parameters to string
		for(int i=1;i<words.length;i++){
			ret[i-1] = words[i];
		}
		
		return ret;
	}
}