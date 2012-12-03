package Data;

import java.io.Serializable;
import java.util.ArrayList;

import Type.TypeCoordinates;
import Type.TypeAddress;
import Type.TypeDate;
import Type.TypeSchedule;
import Type.TypeScheduledTask;

public class DataLocationTransport extends DataLocation implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private boolean                       shortRange;   // Indicates if performs long-range transports
	private boolean                       longRange;    // Indicates if performs short-range transports
	private ArrayList<TypeSchedule>       schedules;    // Schedule of when the transport works
	private ArrayList<TypeScheduledTask>  tasks;        // Tasks for the transport to perform 
	float                                 volume;       // Maximum transport volume
	float                                 weight;       // Maximum transport weight
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC CONSTRUCTORS                                   ** ** //
	// ** *********************************************************** ** //
	
	public DataLocationTransport(TypeCoordinates coordinates, float mvolume, float mweight, boolean shortRange, boolean longRange){
		super(coordinates);
		this.shortRange = shortRange;
		this.longRange = longRange;
		this.schedules = new ArrayList<TypeSchedule>();
		this.tasks = new ArrayList<TypeScheduledTask>();
		this.volume = mvolume;
		this.weight = mweight;
		this.setPersistenceData();
	}
	
	public DataLocationTransport(TypeAddress directions, float mvolume, float mweight, boolean shortRange, boolean longRange){
		super(directions);
		this.shortRange = shortRange;
		this.longRange = longRange;
		this.schedules = new ArrayList<TypeSchedule>();
		this.tasks = new ArrayList<TypeScheduledTask>();
		this.volume = mvolume;
		this.weight = mweight;
		this.setPersistenceData();
	}
	
	public DataLocationTransport(float latitude, float longitude, float mvolume, float mweight, boolean shortRange, boolean longRange){
		super(latitude, longitude);
		this.shortRange = shortRange;
		this.longRange = longRange;
		this.schedules = new ArrayList<TypeSchedule>();
		this.tasks = new ArrayList<TypeScheduledTask>();
		this.volume = mvolume;
		this.weight = mweight;
		this.setPersistenceData();
	}
	
	public DataLocationTransport(String state, String city, int postcode, String street, int number, float mvolume, float mweight, boolean shortRange, boolean longRange){
		super(state, city, postcode, street, number);
		this.shortRange = shortRange;
		this.longRange = longRange;
		this.schedules = new ArrayList<TypeSchedule>();
		this.tasks = new ArrayList<TypeScheduledTask>();
		this.volume = mvolume;
		this.weight = mweight;
		this.setPersistenceData();
	}
	
	public DataLocationTransport(String state, String city, int postcode, String street, int number, int floor, int door, float mvolume, float mweight, boolean shortRange, boolean longRange){
		super(state, city, postcode, street, number, floor, door);
		this.shortRange = shortRange;
		this.longRange = longRange;
		this.schedules = new ArrayList<TypeSchedule>();
		this.tasks = new ArrayList<TypeScheduledTask>();
		this.volume = mvolume;
		this.weight = mweight;
		this.setPersistenceData();
	}
	
	// ** *********************************************************** ** //
	// ** ** GENERAL GETTERS & SETTERS                             ** ** //
	// ** *********************************************************** ** //
	
	public float getMaxVolume(){
		return this.volume;
	}
	
	public void setMaxVolume(float volume){
		this.volume = volume;
	}
	
	public float getMaxWeight(){
		return this.weight;
	}
	
	public void setMaxWeight(float weight){
		this.weight = weight;
	}
	
	public boolean getShortRange(){
		return this.shortRange;
	}
	
	public void setShortRange(boolean shortRange){
		this.shortRange = shortRange;
	}
	
	public boolean getLongRange(){
		return this.longRange;
	}
	
	public void setLongRange(boolean longRange){
		this.longRange = longRange;
	}
	
	// ** *********************************************************** ** //
	// ** ** GETTERS & SETTERS FOR SCHEDULES                       ** ** //
	// ** *********************************************************** ** //
	
	public TypeSchedule getScheduleOnDate(TypeDate date){
		for(int i=0;i<this.schedules.size();i++){
			TypeSchedule tret = this.schedules.get(i);
			if(tret.checkDate(date)) return tret;
		}
		
		return null;
	}
	
	public void addSchedule(TypeSchedule schedule){
		TypeDate date = schedule.getDate();
		
		// Remove schedule for that date if present
		for(int i=0;i<this.schedules.size();i++){
			TypeSchedule sch = this.schedules.get(i);
			if(sch.checkDate(date)){
				this.schedules.remove(i);
				break;
			}
		}
		
		// Add schedule
		this.schedules.add(schedule);
	}
	
	public boolean delSchedule(TypeSchedule schedule){
		return this.schedules.remove(schedule);
	}
	
	// ** *********************************************************** ** //
	// ** ** GETTERS & SETTERS FOR TASKS                           ** ** //
	// ** *********************************************************** ** //
	
	public ArrayList<TypeScheduledTask> getTasksOnDate(TypeDate date){
		ArrayList<TypeScheduledTask> tasks = new ArrayList<TypeScheduledTask>();
		
		for(int i=0;i<this.tasks.size();i++){
			TypeScheduledTask task = this.tasks.get(i);
			if(task.checkDate(date)) tasks.add(task);
		}
		
		return tasks;
	}
	
	public void addTask(TypeScheduledTask task){
		this.tasks.add(task);
	}
	
	public boolean delTask(TypeScheduledTask task){
		return this.tasks.remove(task);
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC PERSISTENCE METHODS                            ** ** //
	// ** *********************************************************** ** //
	
	protected static DataLocationTransport loadObject(int uid){
		Object obj = Persistence.loadJavaObject(DataLocationTransport.class.getName(), uid);
		if(obj != null){
			DataLocationTransport ret = (DataLocationTransport)obj;
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
