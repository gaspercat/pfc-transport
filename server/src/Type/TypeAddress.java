package Type;

import java.io.Serializable;

public class TypeAddress implements Serializable{
	private static final long serialVersionUID = 1L;
	
	String state;
	String city;
	int postcode;
	String street;
	int number;
	int floor;
	int door;
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC CONSTRUCTORS                                   ** ** //
	// ** *********************************************************** ** //
	
	public TypeAddress(){
		this.state = "";
		this.city = "";
		this.postcode = 0;
		this.street = "";
		this.number = 0;
		this.floor = 0;
		this.door = 0;
	}
	
	public TypeAddress(TypeAddress directions){
		this.state = directions.state;
		this.city = directions.city;
		this.postcode = directions.postcode;
		this.street = directions.street;
		this.number = directions.number;
		this.floor = directions.floor;
		this.door = directions.door;
	}
	
	public TypeAddress(String state, String city, int postcode, String street, int number){
		this.state = state;
		this.city = city;
		this.postcode = postcode;
		this.street = street;
		this.number = number;
		this.floor = -1;
		this.door = -1;
	}
	
	public TypeAddress(String state, String city, int postcode, String street, int number, int floor, int door){
		this.state = state;
		this.city = city;
		this.postcode = postcode;
		this.street = street;
		this.number = number;
		this.floor = floor;
		this.door = door;
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC GETTERS & SETTERS                              ** ** //
	// ** *********************************************************** ** //
	
	public String getState(){
		return this.state;
	}
	
	public String getCity(){
		return this.city;
	}
	
	public int getPostcode(){
		return this.postcode;
	}
	
	public String getStreet(){
		return this.street;
	}
	
	public int getNumber(){
		return this.number;
	}
	
	public int getFloor(){
		return this.floor;
	}
	
	public int getDoor(){
		return this.door;
	}
	
	public void setState(String state){
		this.state = state;
	}
	
	public void setCity(String city){
		this.city = city;
	}
	
	public void setPostcode(int postcode){
		this.postcode = postcode;
	}
	
	public void setStreet(String street){
		this.street = street;
	}
	
	public void setNumber(int number){
		this.number = number;
	}
	
	public void setFloor(int floor){
		this.floor = floor;
	}
	
	public void setDoor(int door){
		this.door = door;
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC OPERATORS                                      ** ** //
	// ** *********************************************************** ** //
	
	/**
	 * Check if the passed address is equal to this one
	 * @param address address to compare with
	 * @return true if the addresses are equal, false otherwise
	 */
	public boolean equals(TypeAddress address){
		if(this.door != address.door) return false;
		if(this.floor != address.floor) return false;
		if(this.number != address.number) return false;
		if(!this.street.equalsIgnoreCase(address.street)) return false;
		if(this.postcode != address.postcode) return false;
		if(!this.city.equalsIgnoreCase(address.city)) return false;
		return this.state.equalsIgnoreCase(address.state);
	}
	
	/**
	 * Get the object encoded as a Json string
	 * @return the Json representation of the object
	 */
	public String toString(){
		String ret = "{";
		
		if(this.door != -1 && this.floor != -1){
			ret += "\"door\":" + this.door + ",\"floor\":" + this.floor + ",";
		}
		ret += "\"number\":" + this.number + ",\"street\":\"" + this.street + "\",\"postcode\":" + this.postcode + ",\"city\":\""  + this.city + "\",\"state\":\"" + this.state + "\"}";
		return ret;
	}
}
