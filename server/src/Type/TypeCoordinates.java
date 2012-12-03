package Type;

import java.io.Serializable;

public class TypeCoordinates implements Serializable{
	private static final long serialVersionUID = 1L;
	
	float latitude;
	float longitude;
	
	public TypeCoordinates(TypeCoordinates location){
		this.latitude = location.latitude;
		this.longitude = location.longitude;
	}
	
	public TypeCoordinates(float latitude, float longitude){
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC GETTERS & SETTERS                              ** ** //
	// ** *********************************************************** ** //
	
	public float getLatitude(){
		return this.latitude;
	}
	
	public float getLongitude(){
		return this.longitude;
	}
	
	public void setLatitude(float latitude){
		this.latitude = latitude;
	}
	
	public void setLongitude(float longitude){
		this.longitude = longitude;
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC OPERATORS                                      ** ** //
	// ** *********************************************************** ** //
	
	/**
	 * Check if the passed coordinates are equal to this one
	 * @param coordinates coordinates to compare with
	 * @return true if the coordinates are equal, false otherwise
	 */
	public boolean equals(TypeCoordinates coordinates){
		if(this.latitude != coordinates.latitude) return false;
		return (this.longitude == coordinates.longitude);
	}
	
	/**
	 * Get the object encoded as a Json string
	 * @return the Json representation of the object
	 */
	public String toString(){
		return "{\"lat\":" + this.latitude + ",\"lng\":" + this.longitude + "}";
	}
}
