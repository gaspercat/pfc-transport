package Algorithm;

import Data.HandlerLocationDepot;
import Data.HandlerLocationDropoff;
import Type.TypeDate;

public class Algorithm {
	private HandlerLocationDepot    depots;
	private HandlerLocationDropoff  clients;
	
	public Algorithm(){
		// Initialize handlers
		this.depots = HandlerLocationDepot.getInstance();
		this.clients = HandlerLocationDropoff.getInstance();
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC ALGORITHM ENTRY POINT                          ** ** //
	// ** *********************************************************** ** //
	
	public void runAlgorithm(TypeDate start, TypeDate end, boolean assignDepots){		
		AlgorithmFiltering filter = new AlgorithmFiltering(this.depots.getDepots(), this.clients.getDropoffs());
		AlgorithmClustering cluster = new AlgorithmClustering();
		
		// Filter tasks into plannings according to date and distance
		filter.filterInterval(start, end);
		
		// Process each short-term planning
		for(int i=0;i<filter.getNumShortPlannings();i++){
			PlanningShort planning = filter.getShortPlanning(i);
			
			// Initialize available products
			planning.initializeAvailableProducts();
			
			// Cluster planning
			if(assignDepots) cluster.clusterShort(planning);
			else cluster.clusterPreset(planning);
			
			// Generate routes
			// TODO
			
			// Save planning
			// TODO
		}
	
		// Process the long-term planning
		PlanningLong planning = filter.getLongPlanning();
		if(planning.getNumWaypoints() > 0){
			// Initialize available products
			planning.initializeAvailableProducts();
			
			// Cluster planning
			if(assignDepots) cluster.clusterLong(planning);
			else cluster.clusterPreset(planning);
			
			// Generate routes
			// TODO
			
			// Save planning
			// TODO
		}
	}
	
	// ** *********************************************************** ** //
	// ** ** PRIVATE AUXILIAR METHODS                              ** ** //
	// ** *********************************************************** ** //

}
