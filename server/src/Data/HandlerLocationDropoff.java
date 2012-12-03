package Data;

import java.util.ArrayList;

import Type.TypeCoordinates;
import Type.TypeAddress;

public class HandlerLocationDropoff {
	private static HandlerLocationDropoff instance = null;
	
	private ArrayList<DataLocationDropoff> dropoffs;
	
	// ** *********************************************************** ** //
	// ** ** PRIVATE CONSTRUCTORS                                  ** ** //
	// ** *********************************************************** ** //
	
	private HandlerLocationDropoff(){
		// Initialize dropoffs array
		this.dropoffs = new ArrayList<DataLocationDropoff>();
		
		// Get list of dropoffs
		String className = DataLocationDropoff.class.getName();
		ArrayList<Integer> dropoffs = Persistence.listJavaObjects(className);
		if(dropoffs == null) return;
		
		// Load listed dropoffs
		for(int i=0;i<dropoffs.size();i++){
			DataLocationDropoff dropoff = DataLocationDropoff.loadObject(dropoffs.get(i));
			if(dropoff != null){
				this.dropoffs.add(dropoff);
			}
		}
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC CONSTRUCTORS                                   ** ** //
	// ** *********************************************************** ** //
	
	public static HandlerLocationDropoff getInstance(){
		if(HandlerLocationDropoff.instance == null) HandlerLocationDropoff.instance = new HandlerLocationDropoff();
		return HandlerLocationDropoff.instance;
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC GETTERS & SETTERS                              ** ** //
	// ** *********************************************************** ** //
	
	public ArrayList<DataLocationDropoff> getDropoffs(){
		return this.dropoffs;
	}
	
	public int getNumDropoffs(){
		return this.dropoffs.size();
	}
	
	public DataLocationDropoff getDropoff(int idx){
		if(idx<0 || idx>=this.dropoffs.size()) return null;
		return this.dropoffs.get(idx);
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC DROPOFF CONSTRUCTORS                           ** ** //
	// ** *********************************************************** ** //
	
	public DataLocationDropoff newDropoff(TypeCoordinates coordinates){
		DataLocationDropoff ret = new DataLocationDropoff(coordinates);
		this.dropoffs.add(ret);
		return ret;
	}
	
	public DataLocationDropoff newDropoff(TypeAddress directions){
		DataLocationDropoff ret = new DataLocationDropoff(directions);
		this.dropoffs.add(ret);
		return ret;
	}
	
	public DataLocationDropoff newDropoff(float latitude, float longitude){
		DataLocationDropoff ret = new DataLocationDropoff(latitude, longitude);
		this.dropoffs.add(ret);
		return ret;
	}
	
	public DataLocationDropoff newDropoff(String state, String city, int postcode, String street, int number){
		DataLocationDropoff ret = new DataLocationDropoff(state, city, postcode, street, number);
		this.dropoffs.add(ret);
		return ret;
	}
	
	public DataLocationDropoff newDropoff(String state, String city, int postcode, String street, int number, int floor, int door){
		DataLocationDropoff ret = new DataLocationDropoff(state, city, postcode, street, number, floor, door);
		this.dropoffs.add(ret);
		return ret;
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC DROPOFF DESTRUCTORS                            ** ** //
	// ** *********************************************************** ** //
	
	public boolean deleteDropoff(int idx){
		if(idx<0 || idx>=this.dropoffs.size()) return false;
		if(this.dropoffs.get(idx).deleteObject()){
			this.dropoffs.remove(idx);
			return true;
		}
		
		return false;
	}
	
	public boolean deleteDropoff(DataLocationDropoff dropoff){
		if(dropoff == null) return false;
		if(dropoff.deleteObject()){
			this.dropoffs.remove(dropoff);
			return true;
		}
		
		return false;
	}
}
