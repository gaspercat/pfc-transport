package Type;

import java.io.Serializable;

public class TypeSchedule implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private TypeDate      date;       // Date of the schedule
	private TypeInterval  interval;   // Time interval of the schedule
	private int           maxTime;    // Maximum time to use from the interval
	
	public TypeSchedule(TypeSchedule element){
		this.interval = new TypeInterval(element.interval);
		this.maxTime = element.maxTime;
		this.date = new TypeDate(element.date);
	}
	
	public TypeSchedule(TypeInterval interval, TypeDate date){
		this.interval = new TypeInterval(interval);
		this.maxTime = interval.getMinutes();
		this.date = new TypeDate(date);
	}
	
	public TypeSchedule(TypeInterval interval, int maxTime, TypeDate date){
		this.interval = new TypeInterval(interval);
		this.date = new TypeDate(date);
		
		if(maxTime > interval.getMinutes()){
			this.maxTime = interval.getMinutes();
			return;
		}
		
		this.maxTime = maxTime;
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC GETTERS & SETTERS                              ** ** //
	// ** *********************************************************** ** //
	
	public TypeDate getDate(){
		return new TypeDate(this.date);
	}
	
	public TypeInterval getInterval(){
		return new TypeInterval(this.interval);
	}
	
	public TypeTime getDuration(){
		return this.interval.getDuration();
	}
	
	public int getMinutes(){
		return this.interval.getMinutes();
	}
	
	public int getMaxTime(){
		return this.maxTime;
	}
	
	public void setMaxTime(int maxTime){
		if(maxTime > this.interval.getMinutes()){
			this.maxTime = this.interval.getMinutes();
			return;
		}
		
		this.maxTime = maxTime;
	}
	
	public boolean checkDate(TypeDate date){
		return this.date.equals(date);
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC OPERATORS                                      ** ** //
	// ** *********************************************************** ** //
	
	/**
	 * Check if the passed schedule element is equal to this one
	 * @param element schedule element to compare with
	 * @return true if the schedule elements are equal, false otherwise
	 */
	public boolean equals(TypeSchedule element){
		if(!this.date.equals(element.date)) return false;
		return this.interval.equals(element.interval);
	}
	
	public TypeSchedule overlap(TypeSchedule schedule){
		if(!this.date.equals(schedule.date)) return null;
		
		// Calculate overlaping interval
		TypeInterval interval = this.interval.conjunction(schedule.interval);
		if(interval.isNull()) return null;
		
		// Calculate maximum time
		int maxTime = (this.maxTime < schedule.maxTime) ? this.maxTime : schedule.maxTime;
		if(maxTime > interval.getMinutes()) maxTime = interval.getMinutes();
		
		return new TypeSchedule(interval, maxTime, new TypeDate(this.date));
	}
	
	/**
	 * Compares this element with the element passed as parameter
	 * @param element element with which compare
	 * @return 0 if equal, 1 if current element is greater, -1 if current element is lower
	 */
	public int compareWith(TypeSchedule element){
		int comp = this.date.compareWith(element.date);
		if(comp != 0) return comp;
		return this.interval.compareWith(element.interval);
	}
	
	/**
	 * Get the object encoded as a Json string
	 * @return the Json representation of the object
	 */
	public String toString(){
		return "{\"date\":" + this.date.toString() + ",\"interval\":" + this.interval.toString() + "}";
	}
}