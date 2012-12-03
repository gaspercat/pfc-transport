package Data;

import java.util.ArrayList;

import Type.TypeCoordinates;
import Type.TypeAddress;

public class HandlerLocationDepot {
	private static HandlerLocationDepot instance = null;
	
	private ArrayList<DataLocationDepot> depots;
	
	// ** *********************************************************** ** //
	// ** ** PRIVATE CONSTRUCTORS                                  ** ** //
	// ** *********************************************************** ** //
	
	private HandlerLocationDepot(){
		// Initialize depots array
		this.depots = new ArrayList<DataLocationDepot>();
		
		// Get list of depots
		String className = DataLocationDepot.class.getName();
		ArrayList<Integer> depots = Persistence.listJavaObjects(className);
		if(depots == null) return;
		
		// Load listed depots
		for(int i=0;i<depots.size();i++){
			DataLocationDepot depot = DataLocationDepot.loadObject(depots.get(i));
			if(depot != null){
				this.depots.add(depot);
			}
		}
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC CONSTRUCTORS                                   ** ** //
	// ** *********************************************************** ** //
	
	public static HandlerLocationDepot getInstance(){
		if(HandlerLocationDepot.instance == null) HandlerLocationDepot.instance = new HandlerLocationDepot();
		return HandlerLocationDepot.instance;
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC GETTERS & SETTERS                              ** ** //
	// ** *********************************************************** ** //
	
	public ArrayList<DataLocationDepot> getDepots(){
		return this.depots;
	}
	
	public int getNumDepots(){
		return this.depots.size();
	}
	
	public DataLocationDepot getDepot(int idx){
		if(idx<0 || idx>=this.depots.size()) return null;
		return this.depots.get(idx);
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC DEPOT CONSTRUCTORS                             ** ** //
	// ** *********************************************************** ** //
	
	public DataLocationDepot newDepot(TypeCoordinates coordinates){
		DataLocationDepot ret = new DataLocationDepot(coordinates);
		this.depots.add(ret);
		return ret;
	}
	
	public DataLocationDepot newDepot(TypeAddress directions){
		DataLocationDepot ret = new DataLocationDepot(directions);
		this.depots.add(ret);
		return ret;
	}
	
	public DataLocationDepot newDepot(float latitude, float longitude){
		DataLocationDepot ret = new DataLocationDepot(latitude, longitude);
		this.depots.add(ret);
		return ret;
	}
	
	public DataLocationDepot newDepot(String state, String city, int postcode, String street, int number){
		DataLocationDepot ret = new DataLocationDepot(state, city, postcode, street, number);
		this.depots.add(ret);
		return ret;
	}
	
	public DataLocationDepot newDepot(String state, String city, int postcode, String street, int number, int floor, int door){
		DataLocationDepot ret = new DataLocationDepot(state, city, postcode, street, number, floor, door);
		this.depots.add(ret);
		return ret;
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC DEPOT DESTRUCTORS                              ** ** //
	// ** *********************************************************** ** //
	
	public boolean deleteDepot(int idx){
		if(idx<0 || idx>=this.depots.size()) return false;
		if(this.depots.get(idx).deleteObject()){
			this.depots.remove(idx);
			return true;
		}
		
		return false;
	}
	
	public boolean deleteDepot(DataLocationDepot depot){
		if(depot == null) return false;
		if(depot.deleteObject()){
			this.depots.remove(depot);
			return true;
		}
		
		return false;
	}
}
