package Type;

import java.io.Serializable;

public class TypeInterval implements Serializable{
	private static final long serialVersionUID = 1L;
	
	TypeTime start;
	TypeTime end;
	TypeTime duration;
	
	public TypeInterval(TypeInterval interval){
		this.start = new TypeTime(interval.start);
		this.end = new TypeTime(interval.end);
		this.duration = new TypeTime(interval.duration);
	}
	
	public TypeInterval(TypeTime start, TypeTime end){
		this.start = start;
		this.end = end;
		this.calculateDuration();
	}
	
	public TypeInterval(int shour, int sminute, int ehour, int eminute){
		this.start = new TypeTime(shour, sminute);
		this.end = new TypeTime(ehour, eminute);
		this.calculateDuration();
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC GETTERS & SETTERS                              ** ** //
	// ** *********************************************************** ** //
	
	public TypeTime getStart(){
		return this.start;
	}
	
	public TypeTime getEnd(){
		return this.end;
	}
	
	public TypeTime getDuration(){
		return this.duration;
	}
	
	public int getMinutes(){
		return duration.getHour() * 60 + duration.getMinute();
	}
	
	public void setStart(TypeTime start){
		this.start = start;
	}
	
	public void setEnd(TypeTime end){
		this.end = end;
	}
	
	public void setStart(int hour, int minute){
		this.start = new TypeTime(hour, minute);
	}
	
	public void setEnd(int hour, int minute){
		this.end = new TypeTime(hour, minute);
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC OPERATORS                                      ** ** //
	// ** *********************************************************** ** //
	
	/**
	 * Check if the passed interval is equal to this one
	 * @param interval interval to compare with
	 * @return true if the intervals are equal, false otherwise
	 */
	public boolean equals(TypeInterval interval){
		if(!this.start.equals(interval.start)) return false;
		return (this.end.equals(interval.end));
	}
	
	public TypeInterval conjunction(TypeInterval interval){
		TypeTime start;
		TypeTime end;
		
		// If there's no overlap, return 00:00 - 00:00 interval
		if(this.end.compareWith(interval.start) <= 0) return new TypeInterval(new TypeTime(0, 0), new TypeTime(0, 0));
		if(interval.end.compareWith(this.start) <= 0) return new TypeInterval(new TypeTime(0, 0), new TypeTime(0, 0));
		
		// Set start & end times
		start = new TypeTime((this.start.compareWith(interval.start) == 1) ? this.start : interval.start);
		end = new TypeTime((this.end.compareWith(interval.end) == 1) ? interval.end : this.end );
	    
		return new TypeInterval(start, end);
	}
	
	public int compareWith(TypeInterval interval){
		return this.start.compareWith(interval.start);
	}
	
	public boolean isNull(){
		if(this.start.getHour() != this.end.getHour()) return false;
		if(this.start.getMinute() != this.end.getMinute()) return false;
		return true;
	}
	
	/**
	 * Get the object encoded as a Json string
	 * @return the Json representation of the object
	 */
	public String toString(){
		return "{\"start\":" + this.start.toString() + ",\"end\":" + this.start.toString() + ",\"duration\":" + this.duration.toString() + "}";
	}
	
	// ** *********************************************************** ** //
	// ** ** PRIVATE METHODS                                       ** ** //
	// ** *********************************************************** ** //
	
	private void calculateDuration(){
		int min = start.getHour() * 60 + start.getMinute();
		int max = end.getHour() * 60 + end.getMinute();
		
		// Calculate duration
		int duration = 0;
		if(min>max) duration = (24 * 60) - (min - max);
		else duration = max - min;
		
		// Factor duration into hours and minutes
		this.duration = new TypeTime(duration / 60, duration % 60);
	}
}
