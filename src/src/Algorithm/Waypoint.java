package Algorithm;

import java.util.ArrayList;

import Data.DataLocationDropoff;
import Data.HandlerProduct;
import Type.TypeDate;
import Type.TypeInterval;
import Type.TypeSchedule;
import Type.TypeScheduledTask;
import Type.TypeTime;

public class Waypoint {
    private DataLocationDropoff  client;      // Client to serve
    private TypeDate             date;        // Date of delivery
    private TypeTime             start;       // Start of the time window
    private TypeTime             end;         // End of the time window
    
	ArrayList<Integer>           idDropoff;   // ID of the products to dropoff
	ArrayList<Integer>           qtDropoff;   // Quantity of the products to dropoff
	
	ArrayList<Integer>           idPickup;    // ID of the products to pick-up
	ArrayList<Integer>           qtPickup;    // Quantity of the products to pick-up
	
	public Waypoint(DataLocationDropoff client, TypeScheduledTask task){
    	this.client = client;
    	
    	// Set window start and end
    	TypeInterval ival = task.getTimeInterval().getInterval();
    	this.date = task.getTimeInterval().getDate();
    	this.start = ival.getStart();
    	this.end = ival.getEnd();
    	
    	// Set drop-offs
    	this.idDropoff = new ArrayList<Integer>();
    	this.qtDropoff = new ArrayList<Integer>();
    	for(int i=0;i<task.getNumDropoffs();i++){
    		this.idDropoff.add(new Integer(task.getDropoffID(i)));
    		this.qtDropoff.add(new Integer(task.getDropoffQuantity(i)));
    	}
    	
    	// Set pick-ups
    	this.idPickup = new ArrayList<Integer>();
    	this.qtPickup = new ArrayList<Integer>();
    	for(int i=0;i<task.getNumPickups();i++){
    		this.idPickup.add(new Integer(task.getPickupID(i)));
    		this.qtPickup.add(new Integer(task.getPickupQuantity(i)));
    	}
    }
	
	public DataLocationDropoff getClient(){
		return this.client;
	}
	
	public TypeSchedule getSchedule(){
		return new TypeSchedule(new TypeInterval(this.start, this.end), this.date);
	}
	
	public TypeDate getDate(){
		return this.date;
	}
	
	public TypeTime getWindowStart(){
		return this.start;
	}
	
	public TypeTime getWindowEnd(){
		return this.end;
	}
	
	public int getNumDropoffs(){
		return this.idDropoff.size();
	}
	
	public int getDropoffID(int idx){
		if(idx<0 || idx>=this.idDropoff.size()) return -1;
		return this.idDropoff.get(idx);
	}
	
	public int getDropoffQuantity(int idx){
		if(idx<0 || idx>=this.idDropoff.size()) return -1;
		return this.qtDropoff.get(idx);
	}
	
	public float getDropoffVolume(int idx){
		if(idx<0 || idx>=this.idDropoff.size()) return -1;
		
		HandlerProduct hdlr = HandlerProduct.getInstance();
		float ret = hdlr.getProductByID(this.idDropoff.get(idx)).getVolume();
		return ret * this.qtDropoff.get(idx);
	}
	
	public float getDropoffWeight(int idx){
		if(idx<0 || idx>=this.idDropoff.size()) return -1;
		
		HandlerProduct hdlr = HandlerProduct.getInstance();
		float ret = hdlr.getProductByID(this.idDropoff.get(idx)).getWeight();
		return ret * this.qtDropoff.get(idx);
	}
	
	public float getDropoffsVolume(){
		HandlerProduct hdlr = HandlerProduct.getInstance();
		float ret = 0;
		
		for(int i=0;i<this.idDropoff.size();i++){
			float tret = hdlr.getProductByID(this.idDropoff.get(i)).getVolume();
			ret = ret + tret * this.qtDropoff.get(i);
		}
		
		return ret;
	}
	
	public float getDropoffsWeight(){
		HandlerProduct hdlr = HandlerProduct.getInstance();
		float ret = 0;
		
		for(int i=0;i<this.idDropoff.size();i++){
			float tret = hdlr.getProductByID(this.idDropoff.get(i)).getWeight();
			ret = ret + tret * this.qtDropoff.get(i);
		}
		
		return ret;
	}
	
	public int getPickupID(int idx){
		if(idx<0 || idx>=this.idPickup.size()) return -1;
		return this.idPickup.get(idx);
	}
	
	public int getPickupQuantity(int idx){
		if(idx<0 || idx>=this.idPickup.size()) return -1;
		return this.qtPickup.get(idx);
	}
	
	public float getPickupVolume(int idx){
		if(idx<0 || idx>=this.idPickup.size()) return -1;
		
		HandlerProduct hdlr = HandlerProduct.getInstance();
		float ret = hdlr.getProductByID(this.idPickup.get(idx)).getVolume();
		return ret * this.qtPickup.get(idx);
	}
	
	public float getPickupWeight(int idx){
		if(idx<0 || idx>=this.idPickup.size()) return -1;
		
		HandlerProduct hdlr = HandlerProduct.getInstance();
		float ret = hdlr.getProductByID(this.idPickup.get(idx)).getWeight();
		return ret * this.qtPickup.get(idx);
	}
	
	public float getPickupsVolume(){
		HandlerProduct hdlr = HandlerProduct.getInstance();
		float ret = 0;
		
		for(int i=0;i<this.idPickup.size();i++){
			float tret = hdlr.getProductByID(this.idPickup.get(i)).getVolume();
			ret = ret + tret * this.qtPickup.get(i);
		}
		
		return ret;
	}
	
	public float getPickupsWeight(){
		HandlerProduct hdlr = HandlerProduct.getInstance();
		float ret = 0;
		
		for(int i=0;i<this.idPickup.size();i++){
			float tret = hdlr.getProductByID(this.idPickup.get(i)).getWeight();
			ret = ret + tret * this.qtPickup.get(i);
		}
		
		return ret;
	}
}
