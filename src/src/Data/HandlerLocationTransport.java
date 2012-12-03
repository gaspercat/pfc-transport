package Data;

import java.util.ArrayList;

import Type.TypeCoordinates;
import Type.TypeAddress;

public class HandlerLocationTransport {
	private static HandlerLocationTransport instance = null;
	
	private ArrayList<DataLocationTransport> transports;
	
	// ** *********************************************************** ** //
	// ** ** PRIVATE CONSTRUCTORS                                  ** ** //
	// ** *********************************************************** ** //
	
	private HandlerLocationTransport(){
		// Initialize transports array
		this.transports = new ArrayList<DataLocationTransport>();
		
		// Get list of transports
		String className = DataLocationTransport.class.getName();
		ArrayList<Integer> transports = Persistence.listJavaObjects(className);
		if(transports == null) return;
		
		// Load listed transports
		for(int i=0;i<transports.size();i++){
			DataLocationTransport transport = DataLocationTransport.loadObject(transports.get(i));
			if(transport != null){
				this.transports.add(transport);
			}
		}
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC CONSTRUCTORS                                   ** ** //
	// ** *********************************************************** ** //
	
	public static HandlerLocationTransport getInstance(){
		if(HandlerLocationTransport.instance == null) HandlerLocationTransport.instance = new HandlerLocationTransport();
		return HandlerLocationTransport.instance;
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC GETTERS & SETTERS                              ** ** //
	// ** *********************************************************** ** //
	
	public int getNumTransports(){
		return this.transports.size();
	}
	
	public DataLocationTransport getTransport(int idx){
		if(idx<0 || idx>=this.transports.size()) return null;
		return this.transports.get(idx);
	}
	
	public DataLocationTransport getTransportByID(int id){
		for(int i=0;i<this.transports.size();i++){
			for(int j=0;j<this.transports.size();j++){
				if(this.transports.get(i).getID() == id) return this.transports.get(i);
			}
		}
		return null;
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC TRANSPORT CONSTRUCTORS                         ** ** //
	// ** *********************************************************** ** //
	
	public DataLocationTransport newTransport(TypeCoordinates coordinates, float mvolume, float mweight, boolean shortRange, boolean longRange){
		DataLocationTransport ret = new DataLocationTransport(coordinates, mvolume, mweight, shortRange, longRange);
		this.transports.add(ret);
		return ret;
	}
	
	public DataLocationTransport newTransport(TypeAddress directions, float mvolume, float mweight, boolean shortRange, boolean longRange){
		DataLocationTransport ret = new DataLocationTransport(directions, mvolume, mweight, shortRange, longRange);
		this.transports.add(ret);
		return ret;
	}
	
	public DataLocationTransport newTransport(float latitude, float longitude, float mvolume, float mweight, boolean shortRange, boolean longRange){
		DataLocationTransport ret = new DataLocationTransport(latitude, longitude, mvolume, mweight, shortRange, longRange);
		this.transports.add(ret);
		return ret;
	}
	
	public DataLocationTransport newTransport(String state, String city, int postcode, String street, int number, float mvolume, float mweight, boolean shortRange, boolean longRange){
		DataLocationTransport ret = new DataLocationTransport(state, city, postcode, street, number, mvolume, mweight, shortRange, longRange);
		this.transports.add(ret);
		return ret;
	}
	
	public DataLocationTransport newTransport(String state, String city, int postcode, String street, int number, int floor, int door, boolean shortRange, boolean longRange){
		DataLocationTransport ret = new DataLocationTransport(state, city, postcode, street, number, floor, door, shortRange, longRange);
		this.transports.add(ret);
		return ret;
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC TRANSPORT DESTRUCTORS                          ** ** //
	// ** *********************************************************** ** //
	
	public boolean deleteTransport(int idx){
		if(idx<0 || idx>=this.transports.size()) return false;
		if(this.transports.get(idx).deleteObject()){
			this.transports.remove(idx);
			return true;
		}
		
		return false;
	}
	
	public boolean deleteTransport(DataLocationTransport transport){
		if(transport == null) return false;
		if(transport.deleteObject()){
			this.transports.remove(transport);
			return true;
		}
		
		return false;
	}
}
