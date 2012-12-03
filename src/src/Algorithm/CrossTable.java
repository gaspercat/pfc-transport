package Algorithm;

import java.util.ArrayList;

import Communication.Geofeed;
import Data.DataLocation;
import Data.DataLocationDepot;
import Data.DataLocationDropoff;
import Type.TypeDirections;

public class CrossTable {
	private ArrayList<ArrayList<TypeDirections>> crossTable;
	private ArrayList<DataLocation> locations;
	private int numDepots;
	private int numDropoffs;
	
	public CrossTable(ArrayList<DataLocationDepot> depots, ArrayList<DataLocationDropoff> dropoffs){
		this.crossTable = new ArrayList<ArrayList<TypeDirections>>();
		
		// Set attributes
		this.numDepots = depots.size();
		this.numDropoffs = dropoffs.size();
		
		// Create locations list
		this.locations = new ArrayList<DataLocation>(depots);
		this.locations.addAll(dropoffs);
		
		for(int i=0;i<locations.size();i++){
			ArrayList<TypeDirections> routesList = new ArrayList<TypeDirections>();
			DataLocation origin = locations.get(i);
			for(int j=0;j<locations.size();j++){
				if(i == j){
					routesList.add(new TypeDirections());
				}else{
					TypeDirections route = this.generateRoute(origin, locations.get(j));
					routesList.add(route);
				}
			}
			this.crossTable.add(routesList);
		}
	}
	
	public int getNumDepots(){
		return this.numDepots;
	}
	
	public DataLocationDepot getDepot(int idx){
		if(idx < 0 || idx >= this.numDepots) return null;
		return (DataLocationDepot)this.locations.get(idx);
	}
	
	public int getNumDropoffs(){
		return this.numDropoffs;
	}
	
	public DataLocationDropoff getDropoff(int idx){
		if(idx < 0 || idx >= this.numDropoffs) return null;
		return (DataLocationDropoff)this.locations.get(this.numDepots + idx);
	}
	
	public TypeDirections getDirections(DataLocation origin, DataLocation destination){
		int orig = this.locations.indexOf(origin);
		int dest = this.locations.indexOf(destination);
		
		if(orig == -1 || dest == -1) return null;
		return this.crossTable.get(orig).get(dest);
	}
	
	public void addDropoff(DataLocationDropoff dropoff){
		// Add routes from all locations to the new one
		for(int i=0;i<locations.size();i++){
			TypeDirections nroute = this.generateRoute(locations.get(i), dropoff);
			this.crossTable.get(i).add(nroute);
		}
		
		// Add new location
		this.locations.add(dropoff);
		this.numDropoffs++;
		
		// Add routes from the new one to the others
		ArrayList<TypeDirections> routesList = new ArrayList<TypeDirections>();
		for(int i=0;i<this.locations.size();i++){
			if(i == this.locations.size() - 1){
				routesList.add(new TypeDirections());
			}else{
				TypeDirections route = this.generateRoute(dropoff, locations.get(i));
				routesList.add(route);
			}
		}
		this.crossTable.add(routesList);
	}
	
	public void delDropoff(DataLocationDropoff dropoff){
		int idx = this.locations.indexOf(dropoff);
		if(idx == -1) return;
		
		// Remove routes to the location
		for(int i=0;i<this.locations.size();i++){
			this.crossTable.get(i).remove(idx);
		}
		
		// Remove location
		this.crossTable.remove(idx);
		this.locations.remove(idx);
		this.numDropoffs--;
	}
	
	private TypeDirections generateRoute(DataLocation origin, DataLocation destination){
		Geofeed geofeed = Geofeed.getInstance();
		return geofeed.getRoute(origin.getCoordinates(), destination.getCoordinates());
	}
}
