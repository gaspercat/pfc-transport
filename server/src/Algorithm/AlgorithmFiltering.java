package Algorithm;

import java.util.ArrayList;

import Type.TypeDate;
import Type.TypeScheduledTask;

import Data.DataLocationDepot;
import Data.DataLocationDropoff;
import Data.DataLocationTransport;
import Data.HandlerLocationTransport;

/**
 * Filter the transportations between two dates in long distance transportations and
 * short distance transportations, creating a cross table for long distance transportations
 * and as many cross tables as single days for short distance transportations
 * @author Marc Oliu Sim—n
 *
 */
public class AlgorithmFiltering {
	private ArrayList<DataLocationDepot>    depots;
	private ArrayList<DataLocationDropoff>  clients;
	
    private PlanningLong                    longTable;
    private ArrayList<PlanningShort>        shortTables;
    
    public AlgorithmFiltering(ArrayList<DataLocationDepot> depots, ArrayList<DataLocationDropoff> clients){
    	this.depots = depots;
    	this.clients = clients;
    }
    
	// ** *********************************************************** ** //
	// ** ** PUBLIC FILTERING METHODS                              ** ** //
	// ** *********************************************************** ** //
    
    public void filterInterval(TypeDate start, TypeDate end){
    	// Initialize long planning
        this.longTable = this.initializeLongPlanning();
    	
        // Filter by date and insert to the corresponding planning class
    	TypeDate cur = new TypeDate(start);
    	while(cur.compareWith(end) <= 0){
    		PlanningShort planning = this.initializeShortPlanning(cur);
    		
    		// Get tasks of the clients for that date
    		for(int i=0;i<clients.size();i++){
    			// Get client taks for that date and merge overlaping tasks
    			ArrayList<TypeScheduledTask> scheduled = clients.get(i).getTasksOnDate(cur);
    			scheduled = this.overlapTasks(scheduled);
    			
    			// Add client tasks to the current date planning
    			for(int j=0;j<scheduled.size();j++){
    				Waypoint wayp = new Waypoint(clients.get(i), scheduled.get(j));
    				if(!planning.addClient(wayp)){
    					// If can't be covered on a single day, add to long-range planning
    					this.longTable.addClient(wayp);
    				}
    			}
    		}
    		shortTables.add(planning);
    		
    		// Get next date
    		cur = cur.getNext();
    	}
    }
    
	// ** *********************************************************** ** //
	// ** ** PUBLIC GETTERS & SETTERS                              ** ** //
	// ** *********************************************************** ** //
    
    public PlanningLong getLongPlanning(){
    	return this.longTable;
    }
    
    public int getNumShortPlannings(){
    	return this.shortTables.size();
    }
    
    public PlanningShort getShortPlanning(int idx){
    	if(idx < 0 || idx >= this.shortTables.size()) return null;
    	return this.shortTables.get(idx);
    }
    
	// ** *********************************************************** ** //
	// ** ** PRIVATE PLANNING INITIALIZERS                         ** ** //
	// ** *********************************************************** ** //
    
    private PlanningShort initializeShortPlanning(TypeDate date){
    	HandlerLocationTransport hdlrTransport = HandlerLocationTransport.getInstance();
    	ArrayList<DataLocationDepot> sd_depots = new ArrayList<DataLocationDepot>();
    	
    	// Filter depots which have short-distance transports at the given date
    	for(int i=0;i<this.depots.size();i++){
    		DataLocationDepot depot = this.depots.get(i);
    		for(int j=0;j<depot.getNumTransports();j++){
    			DataLocationTransport transport = hdlrTransport.getTransportByID(depot.getTransportID(j));
    			if(transport.getShortRange() && transport.getScheduleOnDate(date) != null){
    				sd_depots.add(depot);
    				break;
    			}
    		}
    	}
    	
    	return new PlanningShort(date, sd_depots);
    }
    
    private PlanningLong initializeLongPlanning(){
    	HandlerLocationTransport hdlrTransport = HandlerLocationTransport.getInstance();
    	ArrayList<DataLocationDepot> ld_depots = new ArrayList<DataLocationDepot>();
    	
    	// Filter depots which have long-distance transports
    	for(int i=0;i<this.depots.size();i++){
    		DataLocationDepot depot = this.depots.get(i);
    		for(int j=0;j<depot.getNumTransports();j++){
    			DataLocationTransport transport = hdlrTransport.getTransportByID(depot.getTransportID(j));
    			if(transport.getLongRange()){
    				ld_depots.add(depot);
    				break;
    			}
    		}
    	}
    	
    	// Create PlanningLong object
    	return new PlanningLong(ld_depots);
    }
    
	// ** *********************************************************** ** //
	// ** ** PRIVATE AUXILIARY FUNCTIONS                           ** ** //
	// ** *********************************************************** ** //
    
	/**
	 * Check for overlaping tasks and merge them on the overlaping lapse of time
	 * @param tasks tasks to overlap
	 * @return a list of the tasks after merging overlaping ones
	 */
    private ArrayList<TypeScheduledTask> overlapTasks(ArrayList<TypeScheduledTask> tasks){
    	ArrayList<TypeScheduledTask> ret = new ArrayList<TypeScheduledTask>(tasks);
    	for(int i=0;i<ret.size();i++){
    		int j = i + 1;
    		while(j<ret.size()){
    			TypeScheduledTask overlap = ret.get(i).overlap(ret.get(j));
    			if(overlap != null){
    				ret.remove(j);
    				ret.remove(i);
    				ret.add(i, overlap);
    			}else{
    				j++;
    			}
    		}
    	}
    	return ret;
    }
}
