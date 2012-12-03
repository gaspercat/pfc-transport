package Type;

import java.util.ArrayList;


public class TypeScheduledTask {
	private TypeSchedule        timeInterval;
	
	private ArrayList<Integer>  idDropoff;
	private ArrayList<Integer>  qtDropoff;
	
	private ArrayList<Integer>  idPickup;
	private ArrayList<Integer>  qtPickup;
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC CONSTRUCTORS                                   ** ** //
	// ** *********************************************************** ** //
	
	public TypeScheduledTask(TypeScheduledTask task){
		this.timeInterval = new TypeSchedule(task.timeInterval);
		
		this.idDropoff = new ArrayList<Integer>();
		this.qtDropoff = new ArrayList<Integer>();
		for(int i=0;i<task.idDropoff.size();i++){
			this.idDropoff.add(new Integer(task.idDropoff.get(i)));
			this.qtDropoff.add(new Integer(task.qtDropoff.get(i)));
		}

		this.idPickup = new ArrayList<Integer>();
		this.qtPickup = new ArrayList<Integer>();
		for(int i=0;i<task.idPickup.size();i++){
			this.idPickup.add(new Integer(task.idPickup.get(i)));
			this.qtPickup.add(new Integer(task.qtPickup.get(i)));
		}
	}
	
	public TypeScheduledTask(TypeSchedule timeInterval){
		this.timeInterval = timeInterval;
		
		this.idDropoff = new ArrayList<Integer>();
		this.qtDropoff = new ArrayList<Integer>();
		this.idPickup = new ArrayList<Integer>();
		this.qtPickup = new ArrayList<Integer>();
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC GETTERS & SETTERS                              ** ** //
	// ** *********************************************************** ** //
	
	public TypeSchedule getTimeInterval(){
		return this.timeInterval;
	}
	
	public void setTimeInteval(TypeSchedule timeInterval){
		this.timeInterval = timeInterval;
	}
	
	public boolean checkDate(TypeDate date){
		return this.timeInterval.checkDate(date);
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC GETTERS & SETTERS FOR DROP-OFFS                ** ** //
	// ** *********************************************************** ** //
	
	public int getNumDropoffs(){
		return this.idDropoff.size();
	}
	
	public int getDropoffID(int idx){
		if(idx < 0 || idx >= this.idDropoff.size()) return -1;
		return idDropoff.get(idx).intValue();
	}
	
	public int getDropoffQuantity(int idx){
		if(idx < 0 || idx >= this.qtDropoff.size()) return -1;
		return qtDropoff.get(idx).intValue();
	}
	
	public void setDropoffID(int idx, int id){
		if(idx < 0 || idx >= this.idDropoff.size()) return;
		idDropoff.remove(idx);
		idDropoff.add(idx, new Integer(id));
	}
	
	public void setDropoffQuantity(int idx, int quantity){
		if(idx < 0 || idx >= this.qtDropoff.size()) return;
		qtDropoff.remove(idx);
		qtDropoff.add(idx, new Integer(quantity));
	}
	
	public void addDropoff(int id, int quantity){
		// If product present, increase quantity
		for(int i=0;i<this.idDropoff.size();i++){
			if(this.idDropoff.get(i).intValue() == id){
				int val = this.qtDropoff.remove(i).intValue();
				this.qtDropoff.add(i, new Integer(val + quantity));
				return;
			}
		}
		
		// Add product to schedule
		this.idDropoff.add(new Integer(id));
		this.qtDropoff.add(new Integer(quantity));
	}
	
	public boolean delDropoff(int id){
		for(int i=0;i<this.idDropoff.size();i++){
			if(this.idDropoff.get(i).intValue() == id){
				this.idDropoff.remove(i);
				this.qtDropoff.remove(i);
				return true;
			}
		}
		
		return false;
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC GETTERS & SETTERS FOR PICKUPS                  ** ** //
	// ** *********************************************************** ** //
	
	public int getNumPickups(){
		return this.idPickup.size();
	}
	
	public int getPickupID(int idx){
		if(idx < 0 || idx >= this.idPickup.size()) return -1;
		return idPickup.get(idx).intValue();
	}
	
	public int getPickupQuantity(int idx){
		if(idx < 0 || idx >= this.qtPickup.size()) return -1;
		return qtPickup.get(idx).intValue();
	}
	
	public void setPickupID(int idx, int id){
		if(idx < 0 || idx >= this.idPickup.size()) return;
		idPickup.remove(idx);
		idPickup.add(idx, new Integer(id));
	}
	
	public void setPickupQuantity(int idx, int quantity){
		if(idx < 0 || idx >= this.qtPickup.size()) return;
		qtPickup.remove(idx);
		qtPickup.add(idx, new Integer(quantity));
	}
	
	public void addPickup(int id, int quantity){
		// If product present, increase quantity
		for(int i=0;i<this.idPickup.size();i++){
			if(this.idPickup.get(i).intValue() == id){
				int val = this.qtPickup.remove(i).intValue();
				this.qtPickup.add(i, new Integer(val + quantity));
				return;
			}
		}
		
		// Add product to schedule
		this.idPickup.add(new Integer(id));
		this.qtPickup.add(new Integer(quantity));
	}
	
	public boolean delPickup(int id){
		for(int i=0;i<this.idPickup.size();i++){
			if(this.idPickup.get(i).intValue() == id){
				this.idPickup.remove(i);
				this.qtPickup.remove(i);
				return true;
			}
		}
		
		return false;
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC OPERATORS                                      ** ** //
	// ** *********************************************************** ** //
	
	public TypeScheduledTask overlap(TypeScheduledTask task){
		TypeSchedule interval = this.timeInterval.overlap(task.timeInterval);
		if(interval == null) return null;
		
		TypeScheduledTask ret = new TypeScheduledTask(this.timeInterval);
		
		for(int i=0;i<task.idDropoff.size();i++){
			this.addDropoffQuantity(task.idDropoff.get(i), task.qtDropoff.get(i));
		}
		
		for(int i=0;i<task.idPickup.size();i++){
			this.addDropoffQuantity(task.idPickup.get(i), task.qtPickup.get(i));
		}
		
		for(int i=0;i<this.idDropoff.size();i++){
			this.addDropoffQuantity(this.idDropoff.get(i), this.qtDropoff.get(i));
		}
		
		for(int i=0;i<this.idPickup.size();i++){
			this.addDropoffQuantity(this.idPickup.get(i), this.qtPickup.get(i));
		}
		
		return ret;
	}
	
	// ** *********************************************************** ** //
	// ** ** PRIVATE AUXILIAR FUNCTIONS                            ** ** //
	// ** *********************************************************** ** //
	
	private void addDropoffQuantity(int id, int quantity){
		for(int i=0;i<this.idDropoff.size();i++){
			if(this.idDropoff.get(i).intValue() == id){
				int val = this.qtDropoff.remove(i).intValue();
				this.qtDropoff.add(i, new Integer(val + quantity));
				return;
			}
		}
		
		this.idDropoff.add(new Integer(id));
		this.qtDropoff.add(new Integer(quantity));
	}
}
