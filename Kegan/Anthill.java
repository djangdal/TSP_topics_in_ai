import java.util.ArrayList;


/**
 * After their tours, each ant returns to the anthill. The anthill records the
 * best tour.
 *
 * @author Kegan Kaiser
 *         Created December 12, 2013.
 */
public class Anthill {

	private int bestDistance;
	private ArrayList<City> cities = new ArrayList<City>();
	private ArrayList<Integer> bestPath = new ArrayList<Integer>();
	
	/**
	 * Default constructor initializes bestDistance to -1. If the best distance
	 * is -1 when an ant arrives, that ant will immediately have its data 
	 * recorded.
	 */
	public Anthill(){
		this.setBestDistance(-1);
	}
	
	/**
	 * Constructor given a list of cities. Best distance is set to -1.If the
	 * best distance is -1 when an ant arrives, that ant will immediately have
	 * its data recorded.
	 * @param cityList
	 */
	public Anthill(ArrayList<City> cityList){
		this.setBestDistance(-1);
		this.setCities(cityList);
	}
	
	/**
	 * Ants that have finished their tour will call this function just before
	 * deleting themselves to score their tour. Instead of receiving an entire
	 * ant as a parameter the ants tour is taken instead, as this is all that
	 * is needed for the function.
	 * @param tour
	 */
	public void receiveAnt(ArrayList<Integer> tour){
		//Check to see if this tour went through each city
		if (tour.size() != this.getCities().size() + 1){//TODO ASSUMPTION no city will appear twice
			System.out.println("Visited had improper size");
			return;	//If not, ignore it.
		}
		//Otherwise, determine the total distance of this tour
		int dist = determineDistance(tour);
		
		//If it is less than the previous best tour, replace it
		//First check to see if a path has been found. If not, take the first
		//one that comes along.
		if (this.getBestDistance() == -1){
			this.setBestDistance(dist);
			this.setBestPath(tour);
		}
		//Otherwise perform a check to see if this distance is shorter
		if(dist < this.getBestDistance()){
			this.setBestDistance(dist);
			this.setBestPath(tour);
		}
	}

	/**
	 * @param tour - 	An arraylist representing a tour around the cities in
	 * 					this TSP instance
	 * @return The distance required to follow the given tour from start to end
	 */
	private int determineDistance(ArrayList<Integer> tour) {
		int totalDist = 0;
		for (int i = 0; i < tour.size() - 1; i++){
			//Find the distance from the current point to the next point
			int currentIndex = this.findCityIndex(tour.get(i));
			
			//The city we are currently at
			City currentCity = this.getCities().get(currentIndex);
			
			//Entry in the DiPhTable for the next city
			int tableIndex = currentCity.findTableIndex(tour.get(i + 1));
			
			totalDist += currentCity.getDiPhTable().get(tableIndex).get(1);
		}
		return totalDist;	//TODO error checking
	}
	
	/**
	 * @param city
	 * @return The index of city in this.cities, if it exists. -1 otherwise.
	 */
	private int findCityIndex(int city) {
		for(int i=0; i < this.cities.size(); i++){
			if(this.cities.get(i).getName() == city){
				return i;
			}
		}
		return -1;//TODO error checking with this?
	}

	/**
	 * Sets the bestDistance to the given distance
	 * @param distance
	 */
	public void setBestDistance(int distance) {
		this.bestDistance = distance;
	}

	/**
	 * @return Returns the bestDistance found so far.
	 */
	public int getBestDistance() {
		return this.bestDistance;
	}

	/**
	 * Sets the list of cities to a given list.
	 * @param cities
	 */
	public void setCities(ArrayList<City> cities) {
		this.cities = cities;
	}

	/**
	 * @return Returns the list of cities associated with this TSP instance.
	 */
	public ArrayList<City> getCities() {
		return this.cities;
	}

	/**
	 * Sets the bestPath found so far by this anthill to the given path.
	 * @param bestPath
	 */
	public void setBestPath(ArrayList<Integer> bestPath) {
		this.bestPath = bestPath;
	}

	/**
	 * @return Returns the best path found so far.
	 */
	public ArrayList<Integer> getBestPath() {
		return this.bestPath;
	}

	/**
	 * This function is called at the end of all tours to print out
	 * the best path found for this TSP instance.
	 */
	public void printResults() {
		System.out.println("Best path found was: " + this.getBestPath() + 
							" for a total distance of " + this.getBestDistance());
	}
	
}
