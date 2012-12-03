package Data;

import java.io.Serializable;

import Communication.Geofeed;
import Type.TypeCoordinates;
import Type.TypeAddress;

public class DataLocation implements Serializable{
	private static final long serialVersionUID = 1L;
	
	int uid;
	TypeCoordinates coordinates;
	TypeAddress address;
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC CONSTRUCTORS                                   ** ** //
	// ** *********************************************************** ** //
	
	public DataLocation(TypeCoordinates coordinates){
		this.uid = -1;
		this.coordinates = coordinates;
		this.extrapolateDirections();
		this.setPersistenceData();
	}
	
	public DataLocation(TypeAddress address){
		this.uid = -1;
		this.address = address;
		this.extraploateCoordinates();
		this.setPersistenceData();
	}
	
	public DataLocation(float latitude, float longitude){
		this.uid = -1;
		this.coordinates = new TypeCoordinates(latitude, longitude);
		this.extrapolateDirections();
		this.setPersistenceData();
	}
	
	public DataLocation(String state, String city, int postcode, String street, int number){
		this.uid = -1;
		this.address = new TypeAddress(state, city, postcode, street, number);
		this.extraploateCoordinates();
		this.setPersistenceData();
	}
	
	public DataLocation(String state, String city, int postcode, String street, int number, int floor, int door){
		this.uid = -1;
		this.address = new TypeAddress(state, city, postcode, street, number, floor, door);
		this.extraploateCoordinates();
		this.setPersistenceData();
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC GETTERS & SETTERS                              ** ** //
	// ** *********************************************************** ** //
	
	public int getID(){
		return this.uid;
	}
	
	public TypeCoordinates getCoordinates(){
		return this.coordinates;
	}
	
	public TypeAddress getDirections(){
		return this.address;
	}
	
	public void setCoordinates(TypeCoordinates coordinates){
		this.coordinates = coordinates;
		this.extrapolateDirections();
		this.setPersistenceData();
	}
	
	public void setAddress(TypeAddress address){
		this.address = address;
		this.extraploateCoordinates();
		this.setPersistenceData();
	}

	// ** *********************************************************** ** //
	// ** ** PRIVATE PERSISTENCE METHODS                           ** ** //
	// ** *********************************************************** ** //
	
	private boolean setPersistenceData(){
		int uid = Persistence.saveJavaObject(this.uid, this);
		if(uid != -1){
			this.uid = uid;
			return true;
		}
		
		return false;
	}
	
	// ** *********************************************************** ** //
	// ** ** PRIVATE EXTRAPOLATION METHODS                         ** ** //
	// ** *********************************************************** ** //
	
	private boolean extraploateCoordinates(){
		Geofeed geofeed = Geofeed.getInstance();
		TypeCoordinates coordinates = geofeed.getAddressCoordinates(this.address);
		if(coordinates == null){
			this.coordinates = null;
			return false;
		}
		this.coordinates = coordinates;
		return true;
	}
	
	private boolean extrapolateDirections(){
		Geofeed geofeed = Geofeed.getInstance();
		TypeAddress directions = geofeed.getCoordinatesAddress(this.coordinates);
		if(directions == null){
			this.address = null;
			return false;
		}
		this.address = directions;
		return true;
	}
}
