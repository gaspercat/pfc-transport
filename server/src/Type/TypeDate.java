package Type;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import Main.Constants;

public class TypeDate implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private static int monthDays[] = {31,28,31,30,31,30,31,31,30,31,30,31};
	
	private int day;
	private int month;
	private int year;
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC CONSTRUCTORS                                   ** ** //
	// ** *********************************************************** ** //
	
	/**
	 * Get current date
	 */
	public TypeDate(){
		Calendar curr = Calendar.getInstance(Constants.timeZone);

		this.day = curr.get(Calendar.DAY_OF_MONTH);
		this.month = curr.get(Calendar.MONTH) + 1;
		this.year = curr.get(Calendar.YEAR);
	}
	
	public TypeDate(TypeDate date){
		this.day = date.day;
		this.month = date.month;
		this.year = date.year;
	}
	
	public TypeDate(int day, int month, int year){
		this.day = day;
		this.month = month;
		this.year = year;
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC GETTERS & SETTERS                              ** ** //
	// ** *********************************************************** ** //
	
	public int getDay(){
		return this.day;
	}
	
	public int getMonth(){
		return this.month;
	}
	
	public int getYear(){
		return this.year;
	}
	
	public TypeDate getNext(){
		int d = this.day + 1;
		int m = this.month;
		int y = this.year;

		if(d > TypeDate.monthDays[m-1] && (m != 2 || !this.isLeapYear() || d > 29)){
			d = 1;
		    m++;
		    if (m > 12){
		    	m = 1; 
				y++;
			}
		}
		
		return new TypeDate(d, m, y);
	}
	
	public TypeDate getPrev(){
		int d = this.day - 1;
		int m = this.month;
		int y = this.year;
		
		if(d < 1){
			m--;
			if(m < 1){
				m = 12;
				y--;
			}
			
			if(m == 2 &&  this.isLeapYear()) d = 29;
			else d = TypeDate.monthDays[m-1];
		}
		
		return new TypeDate(d, m, y);
	}
	
	public boolean isLeapYear(){
		return ((year % 400 == 0) || ((year % 100 != 0) && (year % 4 == 0)));
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC COMPARE METHODS                                ** ** //
	// ** *********************************************************** ** //
	
	/**
	 * Compares this date with the date passed as parameter
	 * @param date date to which compare
	 * @return 0 if equal, 1 if current date is greater, -1 if current date is lower
	 */
	public int compareWith(TypeDate date){
		if(this.year < date.year) return -1;
		if(this.year > date.year) return 1;
		if(this.month < date.month) return -1;
		if(this.month > date.month) return 1;
		if(this.day < date.day) return -1;
		if(this.day > date.day) return 1;
		return 0;
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC OPERATORS                                      ** ** //
	// ** *********************************************************** ** //
	
	/**
	 * Check if the passed date is equal to this one
	 * @param date date to compare with
	 * @return true if the dates are equal, false otherwise
	 */
	public boolean equals(TypeDate date){
		if(this.day != date.day) return false;
		if(this.month != date.month) return false;
		return (this.year == date.year);
	}
	
	/**
	 * Get the object encoded as a Json string
	 * @return the Json representation of the object
	 */
	public String toString(){
		return "{\"day\":" + this.day + ",\"month\":" + this.month + ",\"year\":" + this.year;
	}
}
