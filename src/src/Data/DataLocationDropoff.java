package Data;

import java.io.Serializable;
import java.util.ArrayList;

import Type.TypeCoordinates;
import Type.TypeAddress;
import Type.TypeDate;
import Type.TypeScheduledTask;

public class DataLocationDropoff extends DataLocation implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private ArrayList<TypeScheduledTask>  tasks;
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC CONSTRUCTORS                                   ** ** //
	// ** *********************************************************** ** //
	
	public DataLocationDropoff(TypeCoordinates coordinates){
		super(coordinates);
		this.setPersistenceData();
	}
	
	public DataLocationDropoff(TypeAddress directions){
		super(directions);
		this.setPersistenceData();
	}
	
	public DataLocationDropoff(float latitude, float longitude){
		super(latitude, longitude);
		this.setPersistenceData();
	}
	
	public DataLocationDropoff(String state, String city, int postcode, String street, int number){
		super(state, city, postcode, street, number);
		this.setPersistenceData();
	}
	
	public DataLocationDropoff(String state, String city, int postcode, String street, int number, int floor, int door){
		super(state, city, postcode, street, number, floor, door);
		this.setPersistenceData();
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC GETTETS & SETTERS                              ** ** //
	// ** *********************************************************** ** //
	
	public int getNumTasks(){
		return this.tasks.size();
	}
	
	public TypeScheduledTask getTask(int idx){
		if(idx < 0 || idx >= this.tasks.size()) return null;
		return this.tasks.get(idx);
	}
	
	public ArrayList<TypeScheduledTask> getTasksOnDate(TypeDate date){
		ArrayList<TypeScheduledTask> ret = new ArrayList<TypeScheduledTask>();
		
		for(int i=0;i<this.tasks.size();i++){
			if(this.tasks.get(i).checkDate(date)){
				ret.add(this.tasks.get(i));
			}
		}
		return ret;
	}
	
	public void addTask(TypeScheduledTask task){
		this.tasks.add(task);
	}
	
	public boolean delTask(int idx){
		if(idx < 0 || idx >= this.tasks.size()) return false;
		this.tasks.remove(idx);
		return true;
	}
	
	public boolean delTask(TypeScheduledTask task){
		return this.tasks.remove(task);
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC PERSISTENCE METHODS                            ** ** //
	// ** *********************************************************** ** //
	
	protected static DataLocationDropoff loadObject(int uid){
		Object obj = Persistence.loadJavaObject(DataLocationDropoff.class.getName(), uid);
		if(obj != null){
			DataLocationDropoff ret = (DataLocationDropoff)obj;
			ret.uid = uid;
			return ret;
		}
		
		return null;
	}
	
	protected boolean deleteObject(){
		if(this.uid == -1) return true;
		return Persistence.removeJavaObject(this.getClass().getName(), this.uid);
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
}
