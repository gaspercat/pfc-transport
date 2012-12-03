package Algorithm;

import java.util.ArrayList;

import Data.DataLocation;
import Data.DataLocationDepot;
import Data.DataLocationTransport;
import Type.TypeDate;
import Type.TypeInterval;
import Type.TypeSchedule;

public class PlanningLong extends Planning {
	PlanningLong(ArrayList<DataLocationDepot> depots) {
		super(depots);
		
		// Set available depot transports
		for(int i=0;i<depots.size();i++){
			DataLocationDepot depot = depots.get(i);
			for(int j=0;j<depot.getNumTransports();j++){
				DataLocationTransport transport = this.hdlrTransport.getTransportByID(depot.getTransportID(j));
				if(transport.getLongRange()){
					this.depotTransports.get(i).add(transport);
				}
			}
		}
	}
	
	public void addClient(Waypoint waypoint){
		this.generalTable.addDropoff(waypoint.getClient());
		this.waypoints.add(waypoint);
	}
	
	// ** *********************************************************** ** //
	// ** ** PRIVATE ABSTRACT CLASS IMPLEMENTATIONS                ** ** //
	// ** *********************************************************** ** //
    
    protected int countRoundTrips(DataLocationTransport tpt, DataLocationDepot dpt, Waypoint wpt){
    	
    	TypeSchedule dep = this.getDeparture(tpt, dpt, wpt, wpt.getSchedule());
    	if(dep == null) return 0;
    	
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
    
    private TypeSchedule getArrival(DataLocationTransport tpt, DataLocation orig, DataLocation dest, TypeSchedule departure){
    	TypeDate today = new TypeDate();
    	TypeDate cdate = today;
    	
    	// Get transport time
    	int gtime = this.generalTable.getDirections(orig, dest).getTotalTime();
    	int ctime = 0;
    	
    	// Check that the transport can be there on the departure window
    	TypeSchedule sch = tpt.getScheduleOnDate(today);
    	if(sch == null) return null;
    	if(sch.getInterval().getStart().compareWith(departure.getInterval().getEnd()) == 1) return null;
    	if(sch.getInterval().getEnd().compareWith(departure.getInterval().getStart()) == -1) return null;
    	
    	// Obtain the arrival date & time
    	do{
    		
    	}while();
    }
    
    private TypeSchedule getDeparture(DataLocationTransport tpt, DataLocation orig, DataLocation dest, TypeSchedule arrival){
    	// Get minimum and maximum dates for the transport
    	TypeDate today = new TypeDate();
    	TypeDate deliv = arrival.getDate();
    	TypeDate cdate = deliv;
    	
    	// Get transport time
    	int gtime = this.generalTable.getDirections(orig, dest).getTotalTime();
    	int ctime = 0;
    	
    	// Check that the transport can be there on the arrival window
    	TypeSchedule sch = tpt.getScheduleOnDate(deliv);
    	if(sch == null) return null;
    	if(sch.getInterval().getStart().compareWith(arrival.getInterval().getEnd()) == 1) return null;
    	if(sch.getInterval().getEnd().compareWith(arrival.getInterval().getStart()) == -1) return null;
    	
    	// Obtain the departure date & time
    	do{
    		// Add that day schedule to the travel time
    		if(sch != null){
    			if(ctime + sch.getDuration().toMinutes() >= gtime){
    				// If travel is covered, return departure interval
    				TypeInterval rival = new TypeInterval(sch.getInterval().getStart(), sch.getInterval().getEnd().substract(gtime - ctime));
    				return new TypeSchedule(rival, cdate);
    			}else{
    				ctime = ctime + sch.getDuration().toMinutes();
    			}
    		}
    		
    		cdate = cdate.getPrev();
    		sch = tpt.getScheduleOnDate(cdate);
    	}while(cdate.compareWith(today) >= 0);
    	
    	// If could not cover the travel, return null
    	return null;
    }
}
