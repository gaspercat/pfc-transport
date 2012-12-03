package Type;

import java.io.Serializable;
import java.util.Calendar;

import Main.Constants;

public class TypeTime implements Serializable{
	private static final long serialVersionUID = 1L;
	
	int hour;
	int minute;
	
	TypeTime(){
		Calendar curr = Calendar.getInstance(Constants.timeZone);
		this.hour = curr.get(Calendar.HOUR_OF_DAY);
		this.minute = curr.get(Calendar.MINUTE);
	}
	
	TypeTime(TypeTime time){
		this.hour = time.hour;
		this.minute = time.minute;
	}
	
	TypeTime(int hour, int minute){
		hour = this.formatHour(hour);
		minute = this.formatMinute(minute);
		
		this.hour = hour;
		this.minute = minute;
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC GETTERS & SETTERS                              ** ** //
	// ** *********************************************************** ** //
	
	public int getHour(){
		return this.hour;
	}
	
	public int getMinute(){
		return this.minute;
	}
	
	public void setHour(int hour){
		this.hour = hour;
	}
	
	public void setMinute(int minute){
		this.minute = minute;
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC OPERATORS                                      ** ** //
	// ** *********************************************************** ** //
	
	/**
	 * Check if the passed time is equal to this one
	 * @param time time to compare with
	 * @return true if the times are equal, false otherwise
	 */
	public boolean equals(TypeTime time){
		if(this.minute != time.minute) return false;
		return (this.hour == time.hour);
	}
	
	public TypeTime substract(TypeTime time){
		TypeTime ret = new TypeTime(this);
		
		ret.minute = ret.minute - time.minute;
		if(ret.minute < 0){
			ret.minute = 60 - ret.minute;
			ret.hour--;
		}
		
		ret.hour = ret.hour - time.hour;
		if(ret.hour < 0){
			ret.hour = 0;
			ret.minute = 0;
		}
		
		return ret;
	}
	
	public TypeTime substract(int minutes){
		TypeTime ret = new TypeTime(this);
		
		ret.minute = ret.minute - minutes;
		if(ret.minute < 0){
			ret.hour = ret.hour - (-ret.minute) / 60;
			if((-ret.minute) % 60 > 0){
				ret.hour--;
				ret.minute = 60 - ((-ret.minute) % 60);
			}else{
				ret.minute = 0;
			}
		}
		
		return ret;
	}
	
	/**
	 * Compares this time with the time passed as parameter
	 * @param time time to which compare
	 * @return 0 if equal, 1 if current time is greater, -1 if current time is lower
	 */
	public int compareWith(TypeTime time){
		if(this.hour < time.hour) return -1;
		if(this.hour > time.hour) return 1;
		return 0;
	}
	
	public int toMinutes(){
		return this.hour * 60 + this.minute;
	}
	
	/**
	 * Get the object encoded as a Json string
	 * @return the Json representation of the object
	 */
	public String toString(){
		return "{\"hour\":" + this.hour + ",\"minute\":" + this.minute + "}";
	}
	
	// ** *********************************************************** ** //
	// ** ** PRIVATE TIME FORMATTERS                               ** ** //
	// ** *********************************************************** ** //
	
	private int formatHour(int hour){
		while(hour<0) hour = hour + 24;
		if(hour>23) return hour % 24;
		return hour;
	}
	
	private int formatMinute(int minute){
		while(minute<0) minute = minute + 60;
		if(minute>59) return minute % 60;
		return minute;
	}
}
