package Algorithm;

import java.util.ArrayList;

import Data.DataLocation;
import Data.DataLocationDepot;
import Data.DataLocationTransport;
import Type.TypeDate;
import Type.TypeSchedule;
import Type.TypeTime;

public class PlanningShort extends Planning {
	protected TypeDate                      date;
	
	PlanningShort(TypeDate date, ArrayList<DataLocationDepot> depots) {
		super(depots);
		this.date = date;
		
		// Set available depot transports
		for(int i=0;i<depots.size();i++){
			DataLocationDepot depot = depots.get(i);
			for(int j=0;j<depot.getNumTransports();j++){
				DataLocationTransport transport = this.hdlrTransport.getTransportByID(depot.getTransportID(j));
				if(transport.getShortRange() && transport.getScheduleOnDate(date) != null){
					this.depotTransports.get(i).add(transport);
				}
			}
		}
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC GETTERS & SETTERS                              ** ** //
	// ** *********************************************************** ** //
	
    public boolean addClient(Waypoint waypoint){
    	this.generalTable.addDropoff(waypoint.getClient());
    	this.waypoints.add(waypoint);
    	
    	// Check if any of the depots can serve the client in a single day
    	for(int i=0;i<this.depots.size();i++){
    		DataLocationDepot depot = this.depots.get(i);
    		// Calculate needed time
    		ArrayList<DataLocationTransport> transports = this.depotTransports.get(i);
    		for(int j=0;j<transports.size();j++){
    			if(checkTransport(transports.get(j), depot, waypoint)) return true;
    		}
    	}
    	
    	this.waypoints.remove(waypoint);
    	this.generalTable.delDropoff(waypoint.getClient());
    	return false;
    }
    
	// ** *********************************************************** ** //
	// ** ** PRIVATE ABSTRACT CLASS IMPLEMENTATIONS                ** ** //
	// ** *********************************************************** ** //
    
    protected int countRoundTrips(DataLocationTransport tpt, DataLocationDepot dpt, Waypoint wpt){
    	TypeSchedule  tsch = tpt.getScheduleOnDate(this.date);
    	int           ret = 0;
    	
    	// Get transport start, end & max time
    	int           tstt = tsch.getInterval().getStart().toMinutes();
    	int           tend = tsch.getInterval().getEnd().toMinutes();
    	int           tmax = tsch.getMinutes();
    	
    	// Get waypoint window start & end
    	int           wstt = wpt.getWindowStart().toMinutes();
    	int           wend = wpt.getWindowEnd().toMinutes();
    	
    	// Get travel time for depot->waypoint & waypoint->depot
    	int           gtime = this.generalTable.getDirections(dpt, wpt.getClient()).getTotalTime();
    	int           rtime = this.generalTable.getDirections(wpt.getClient(), dpt).getTotalTime();
    	
    	// Initialize variables
    	int           stime = tstt;
    	int           ctime = stime;
    	
    	// Adjust travel start time
    	if(stime + gtime < wstt){
    		stime = wstt - gtime;
    		ctime = stime;
    	}
    	
    	// Count number of possible round trips
    	while(true){
    		ctime = ctime + gtime + rtime;
    		if(ctime > tend || ctime - rtime > wend || ctime - stime > tmax) return ret;
    		ret++;
    	}
    }
    
	// ** *********************************************************** ** //
	// ** ** PRIVATE AUXILIARY FUNCTIONS                           ** ** //
	// ** *********************************************************** ** //
    
    private boolean checkTransport(DataLocationTransport transport, DataLocation depot, Waypoint waypoint){
		ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();
		waypoints.add(waypoint);
		return this.checkTransport(transport, depot, waypoints);
    }
    
    private boolean checkTransport(DataLocationTransport transport, DataLocation depot, ArrayList<Waypoint> waypoints){
    	if(waypoints.size() == 0) return true;
    	
    	// Get transport schedule
    	TypeSchedule tsch = transport.getScheduleOnDate(this.date);
    	if(tsch == null) return false;
    	
    	// Set transport times
    	int tpt_start = tsch.getInterval().getStart().toMinutes();
    	int tpt_end = tsch.getInterval().getEnd().toMinutes();
    	int tpt_act = tpt_start;
    	
    	// Adjust transport start time
    	Waypoint fwpt = waypoints.get(0);
    	int ttime = this.generalTable.getDirections(depot, fwpt.getClient()).getTotalTime();
    	int tstart = fwpt.getWindowStart().toMinutes() - ttime;
    	if(tstart < tpt_start) tstart = tpt_start;
    	if(tstart + ttime >= fwpt.getWindowEnd().toMinutes()) return false;
    	tpt_start = tstart;
    	
    	// Check if waypoints fit on transport's schedule
    	DataLocation pos = depot;
    	for(int i=0;i<waypoints.size();i++){
    		Waypoint waypoint = waypoints.get(i);
    		
	    	// Get destination schedule
	    	DataLocation dest = waypoint.getClient();
	    	int wstart = waypoint.getWindowStart().toMinutes();
	    	int wend = waypoint.getWindowEnd().toMinutes();
	    	
	    	// Get transport minimal arrival time
	    	int gtime = this.generalTable.getDirections(pos, dest).getTotalTime();
	    	tpt_act = tpt_act + gtime;
	    	if(tpt_act >= wend) return false;
	    	if(tpt_act < wstart) tpt_act = wstart;
	    	
	    	// Check transport still has time
	    	if(tpt_act > tpt_end) return false;
	    	if(tpt_act - tpt_start > tsch.getMaxTime()) return false;
	    	pos = dest;
    	}
    	
    	// Check transport returns on time
    	int rtime = this.generalTable.getDirections(pos, depot).getTotalTime();
    	tpt_act = tpt_act + rtime;
    	if(tpt_act > tpt_end) return false;
    	if(tpt_act - tpt_start > tsch.getMaxTime()) return false;
    	return true;
    }
}
