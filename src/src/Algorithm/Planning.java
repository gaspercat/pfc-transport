package Algorithm;

import java.util.ArrayList;

import Data.DataLocationDepot;
import Data.DataLocationDropoff;
import Data.DataLocationTransport;
import Data.DataProduct;
import Data.HandlerLocationTransport;
import Data.HandlerProduct;
import Type.TypeDirections;

public abstract class Planning {
	protected HandlerLocationTransport                     hdlrTransport;     // Handler for the transports
	protected HandlerProduct                               hdlrProduct;       // Handler for the products
	
	// ** Data for the assignation of resources to the planning
	// **************************************************************
	protected CrossTable                                   generalTable;      // Driving routes between locations
	protected ArrayList<DataLocationDepot>                 depots;            // Used depots
	protected ArrayList<Waypoint>                          waypoints;         // Waypoints to cover
	protected ArrayList<ArrayList<DataLocationTransport>>  depotTransports;   // Transports for each waypoint
	
	// ** Data for the assignation of waypoints to the depots
	// **************************************************************
	protected ArrayList<ArrayList<Waypoint>>               depotWaypoints;    // Waypoints assigned to each depot
	protected ArrayList<ArrayList<DataProduct>>            depotProductsID;   // Products available at each depot
	protected ArrayList<ArrayList<Integer>>                depotProductsQT;   // Product quantities available at each depot
	
	// ** *********************************************************** ** //
	// ** ** PRIVATE CONSTRUCTORS                                  ** ** //
	// ** *********************************************************** ** //
	
    protected Planning(ArrayList<DataLocationDepot> depots){
    	this.hdlrTransport = HandlerLocationTransport.getInstance();
    	this.hdlrProduct = HandlerProduct.getInstance();
    	
    	this.depots = depots;
    	this.waypoints = new ArrayList<Waypoint>();
    	
    	// Generate initial cross table
    	ArrayList<DataLocationDropoff> lclients = new ArrayList<DataLocationDropoff>();
    	this.generalTable = new CrossTable(depots, lclients);
    	
    	// Generate empty transports & waypoints assignment to depots
    	this.depotTransports = new ArrayList<ArrayList<DataLocationTransport>>();
    	this.depotWaypoints = new ArrayList<ArrayList<Waypoint>>();
    	for(int i=0;i<depots.size();i++){
    		this.depotTransports.add(new ArrayList<DataLocationTransport>());
    		this.depotWaypoints.add(new ArrayList<Waypoint>());
    	}
    }
    
	// ** *********************************************************** ** //
	// ** ** GENERAL GETTERS                                       ** ** //
	// ** *********************************************************** ** //
    
    public int getNumDepots(){
    	return this.depots.size();
    }
    
    public int getNumWaypoints(){
    	return this.waypoints.size();
    }
    
	// ** *********************************************************** ** //
	// ** ** GENERAL SETTERS                                       ** ** //
	// ** *********************************************************** ** //
    
    public void addWaypoint(Waypoint wpt){
    	this.waypoints.add(wpt);
    }
    
    public void delWaypoint(int idx){
    	this.waypoints.remove(idx);
    }
    
    public void delWaypoint(Waypoint wpt){
    	this.waypoints.remove(wpt);
    }
    
	// ** *********************************************************** ** //
	// ** ** GETTERS FOR TRAVEL DISTANCE                           ** ** //
	// ** *********************************************************** ** //
    
    public int getTravelDistance(Waypoint origin, Waypoint destination){
    	TypeDirections dirs = this.generalTable.getDirections(origin.getClient(), destination.getClient());
        return dirs.getTotalDistance();
    }
    
    public int getTravelDistance(DataLocationDepot origin, Waypoint destination){
    	TypeDirections dirs = this.generalTable.getDirections(origin, destination.getClient());
    	return dirs.getTotalDistance();
    }
    
    public int getTravelDistance(Waypoint origin, DataLocationDepot destination){
    	TypeDirections dirs = this.generalTable.getDirections(origin.getClient(), destination);
    	return dirs.getTotalDistance();
    }
    
	// ** *********************************************************** ** //
	// ** ** GETTERS FOR TRAVEL TIME                               ** ** //
	// ** *********************************************************** ** //
    
    public int getTravelTime(Waypoint origin, Waypoint destination){
    	TypeDirections dirs = generalTable.getDirections(origin.getClient(), destination.getClient());
        return dirs.getTotalTime();
    }
    
    public int getTravelTime(DataLocationDepot origin, Waypoint destination){
    	TypeDirections dirs = this.generalTable.getDirections(origin, destination.getClient());
    	return dirs.getTotalTime();
    }
    
    public int getTravelTime(Waypoint origin, DataLocationDepot destination){
    	TypeDirections dirs = this.generalTable.getDirections(origin.getClient(), destination);
    	return dirs.getTotalTime();
    }
    
	// ** *********************************************************** ** //
	// ** ** GETTERS FOR DEPOTS CLUSTERS DATA                      ** ** //
	// ** *********************************************************** ** //
    
    /**
     * Get the medium travel time from a point to the rest of points of a cluster
     * @param idxWaypoint The waypoint for which we want to check the travel time
     * @param idxDepot The depot of the cluster to which we want to check the travel time
     * @return The medium travel time (in minutes) to the cluster
     */
    public int getDistanceToCluster(int idxWaypoint, int idxDepot){
    	Waypoint waypoint = this.waypoints.get(idxWaypoint);
    	DataLocationDepot depot = this.depots.get(idxDepot);
    	
    	// Add times for waypoint-depot travel
    	int dist = this.getTravelTime(depot, waypoint);
    	dist = dist + this.getTravelTime(waypoint, depot);
    	
    	// Add times for waypoint-waypoint travel
    	ArrayList<Waypoint> waypoints = this.depotWaypoints.get(idxDepot);
    	for(int i=0;i<waypoints.size();i++){
    		Waypoint twpt = waypoints.get(i);
    		dist = dist + this.getTravelTime(twpt, waypoint);
    		dist = dist + this.getTravelTime(waypoint, twpt);
    	}
    	
    	// Divide total distance by number of travels used
    	return dist / ((2 + waypoints.size()) * 2);
    }
    
    /**
     * Check if the waypoint can be covered by the depot, taking into account the max.
     * travel time of the transports and the depot's resources.
     * @param idxWaypoint Waypoint that is checked
     * @param idxDepot Depot with which we want to check
     * @return true if it can be covered, false otherwise
     */
    public boolean checkAssignationViability(int idxWaypoint, int idxDepot){
    	ArrayList<DataLocationTransport> depTrans = this.depotTransports.get(idxDepot); 
    	ArrayList<DataProduct> depProds = this.depotProductsID.get(idxDepot);
    	ArrayList<Integer> depQttys = this.depotProductsQT.get(idxDepot);
    	Waypoint wpt = this.waypoints.get(idxWaypoint);
    	
    	// Check products are available at this depot
    	for(int i=0;i<wpt.getNumDropoffs();i++){
    		for(int j=0;j<depProds.size();j++){
    			if(depProds.get(j).getID() != wpt.getDropoffID(i)) continue;
    			if(depQttys.get(j) < wpt.getDropoffQuantity(i)) return false;
    		}
    	}
    	
    	// Get pickup & dropoff volumes & weights
    	float puVolume = wpt.getPickupsVolume();
    	float puWeight = wpt.getPickupsWeight();
    	float doVolume = wpt.getDropoffsVolume();
    	float doWeight = wpt.getDropoffsWeight();
    	
    	// Get total volume & weight capacities for transports which can make it in time
    	float tweight = 0;
    	float tvolume = 0;
    	for(int i=0;i<depTrans.size();i++){
    		DataLocationTransport ttrans = depTrans.get(i);
    		int ntimes = this.countRoundTrips(ttrans, depots.get(idxDepot), wpt);
    		if(ntimes > 0){
    			tweight = tweight + ttrans.getMaxWeight() * ntimes;
    			tvolume = tvolume + ttrans.getMaxVolume() * ntimes;
    		}
    	}
    	
    	// Check if the depot can potentially serve the command
    	if(tweight < puWeight || tweight < doWeight) return false;
    	if(tvolume < puVolume || tvolume < doVolume) return false;
    	return true;
    }
    
	// ** *********************************************************** ** //
	// ** ** AVAILABLE PRODUCTS INITIALIZATION                     ** ** //
	// ** *********************************************************** ** //
    
    /**
     * Initialize the amount of each product available at the depots
     */
    public void initializeAvailableProducts(){
    	this.depotProductsID = new ArrayList<ArrayList<DataProduct>>();
    	this.depotProductsQT = new ArrayList<ArrayList<Integer>>();
    	for(int i=0;i<this.depots.size();i++){
    		DataLocationDepot depot = this.depots.get(i);
			ArrayList<DataProduct> pRFs = new ArrayList<DataProduct>();
			ArrayList<Integer> pQTs = new ArrayList<Integer>();
    		for(int j=0;j<depot.getNumProducts();j++){
    			pRFs.add(hdlrProduct.getProductByID(depot.getProductID(j)));
    			pQTs.add(new Integer(depot.getProductQuantity(j)));
    		}
    		this.depotProductsID.add(pRFs);
    		this.depotProductsQT.add(pQTs);
    	}
    }
    
	// ** *********************************************************** ** //
	// ** ** PRIVATE ABSTRACT METHODS                              ** ** //
	// ** *********************************************************** ** //
    
    /**
     * Return the number of times the passed transport can do the round trip from the
     * depot to the waypoint and back to the depot within schedule and delivery window
     * @param tpt Transport that makes the trip
     * @param wpt Waypoint to visit
     * @return Number of times that the round trip can be done
     */
    protected abstract int countRoundTrips(DataLocationTransport tpt, DataLocationDepot dpt, Waypoint wpt);
}
