package Algorithm;

public class AlgorithmClustering {
	private Planning  planning;
	
	public AlgorithmClustering(){}
	
	/**
	 * Cluster a short-distance planning's waypoints to the different
	 * depots by proximity to the cluster
	 * @param planning The planning to cluster
	 * @return The same planning, clustered
	 */
	public PlanningShort clusterShort(PlanningShort planning){
		this.planning = planning;
		
		// Cluster all waypoints 
	}
	
	/**
	 * Cluster a long-distance planning's waypoints to the different
	 * depots by poximity to the cluster
	 * @param planning The planning to cluster
	 * @return The same planning, clustered
	 */
	public PlanningLong clusterLong(PlanningLong planning){
		this.planning = planning;
	}
	
	/**
	 * Cluster a planning's waypoints to the different depots by using
	 * the preset assignation of client-depot
	 * @param planning The planning to cluster
	 * @return The same planning, clustered
	 */
	public Planning clusterPreset(Planning planning){
		this.planning = planning;
	}
}
