package Type;

import java.io.Serializable;
import java.util.ArrayList;

public class TypeDirections implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private ArrayList<TypeCoordinates> waypoints;  
	private ArrayList<Integer> distance;
	private ArrayList<Integer> time;
	private Integer totalDistance;                 // Meters
	private Integer totalTime;                     // Seconds
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC CONSTRUCTORS                                   ** ** //
	// ** *********************************************************** ** //
	
	public TypeDirections(){
		this.waypoints = new ArrayList<TypeCoordinates>();
		this.distance = new ArrayList<Integer>();
		this.time = new ArrayList<Integer>();
		this.totalDistance = 0;
		this.totalTime = 0;
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC GETTERS & SETTERS                              ** ** //
	// ** *********************************************************** ** //
	
	/**
	 * Get the total distance that takes to realize the route
	 * @return distance of the route in meters
	 */
	public int getTotalDistance(){
		return this.totalDistance.intValue();
	}
	
	/**
	 * Get the total time that takes to realize the route
	 * @return time of the route in minutes
	 */
	public int getTotalTime(){
		return this.totalTime.intValue() / 60;
	}
	
	/**
	 * Get the number of waypoints of the route, including origin and destination
	 * @return number of waypoints
	 */
	public int getNumWaypoints(){
		return this.waypoints.size();
	}
	
	/**
	 * Get the coordinates of a waypoint
	 * @param idx index of the waypoint
	 * @return coordinates of the requested waypoint
	 */
	public TypeCoordinates getWaypointCoordinates(int idx){
		if(idx<0 || idx>=this.waypoints.size()) return null;
		return this.waypoints.get(idx);
	}
	
	/**
	 * Get the distance from previous waypoint to this one
	 * @param idx index of the waypoint
	 * @return time in seconds
	 */
	public Integer getWaypointDistance(int idx){
		if(idx<0 || idx>=this.waypoints.size()) return null;
		return this.distance.get(idx);
	}
	
	/**
	 * Get the time from previous waypoint to this one
	 * @param idx index of the waypoint
	 * @return time in seconds
	 */
	public Integer getWaypointTime(int idx){
		if(idx<0 || idx>=this.waypoints.size()) return null;
		return this.time.get(idx);
	}
	
	/**
	 * Add a new waypoint to the route
	 * @param coords coordinates of the new waypoint
	 * @param distance distance from the previous waypoint
	 * @param time time from the previous waypoint
	 */
	public void addWaypoint(TypeCoordinates coords, int distance, int time){
		// Add new waypoint
		this.waypoints.add(coords);
		this.distance.add(new Integer(distance));
		this.time.add(new Integer(time));
		// Sum distance and time to totals
		this.totalDistance += distance;
		this.totalTime += time;
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC OPERATORS                                      ** ** //
	// ** *********************************************************** ** //
	
	/**
	 * Check if the passed directions are equal to this ones
	 * @param directions directions to compare with
	 * @return true if the directions are equal, false otherwise
	 */
	public boolean equals(TypeDirections directions){
		if(this.waypoints.size() != directions.waypoints.size()) return false;
		
		if(this.totalTime.intValue() != directions.totalTime.intValue()) return false;
		if(this.totalDistance.intValue() != directions.totalDistance.intValue()) return false;
		
		for(int i=0;i<this.waypoints.size();i++){
			if(!this.waypoints.get(i).equals(directions.waypoints.get(i))) return false;
		}
		return true;
	}
	
	/**
	 * Get the object encoded as a Json string
	 * @return the Json representation of the object
	 */
	public String toString(){
		String ret = "{\"waypoints\":[";
		for(int i=0;i<this.waypoints.size();i++){
			TypeCoordinates coords = this.waypoints.get(i);
			int distance = this.distance.get(i);
			int time = this.time.get(i);
			ret = ret + "{\"coordinates\":" + coords.toString() + ",\"distance\":" + distance + ",\"time\":" + time + "}";
			if(i<this.waypoints.size()-1) ret = ret + ",";
		}
		ret = ret + "]}";
		return ret;
	}
}