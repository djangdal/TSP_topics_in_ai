import java.util.ArrayList;
import java.util.Random;


/**
 * An instance of the Traveling Sales Person problem to be solved with swarm
 * intelligence. Receives a set of cities from main and builds a City instance
 * for each.
 *
 * @author Kegan Kaiser
 *         Created December 10, 2013.
 */
public class TSP_Instance {

	 /**
	  * This list represents all of the cities for this TSP problem.
	  */
	private ArrayList<City> cities = new ArrayList<City>();
	/**
	 * The final destination of each ant, the anthill will record the best
	 * tour found so far.
	 */
	private Anthill dest;
	
	/**
	 * Creates an instance of the TSP problem with the given graph.
	 * @param cityList - 	The cities for use in this TSP problem. The list is
	 * 					of the following form:
	 * 	[[City0 City2 Dist],[City0 City3 Dist],[City1 City4 Dist]...]
	 */
	public TSP_Instance(ArrayList<ArrayList<Integer>> cityList){
		/**
		 * TODO ASSUMPTION FOR TSP_INSTANCE
		 * We do not know if each city will be represented. i.e., if 4 is the 
		 * highest numbered city, we are not guaranteed to have cities numbered
		 * 0, 1, 2, and 3. If we are guaranteed this it would be faster to loop
		 * through cityList creating each city, sort them, then add connections.
		 * Since this cannot be done the cities are being created and the
		 * connections added at the same time below.
		 */
				
		//Iterate through cityList and create each city
		for (int i=0; i < cityList.size(); i++){
			//Each entry in cityList is of the form [CityA CityB Distance]
			int firstCity = cityList.get(i).get(0);
			int secondCity = cityList.get(i).get(1);
			int dist = cityList.get(i).get(2);
			
			//Check to see if the first city exists yet
			if (cityExists(firstCity)){
				//Make sure the second city exists
				if(cityExists(secondCity)){	//Both cities exist
					//Now add the connections
					int firstIndex = findCityIndex(firstCity);
					int secondIndex = findCityIndex(secondCity);
					this.cities.get(firstIndex).addConnection(secondCity, dist);
					this.cities.get(secondIndex).addConnection(firstCity, dist);
				}
				else{	//First exists, second doesn't
					this.cities.add(new City(secondCity));
					int firstIndex = findCityIndex(firstCity);
					int secondIndex = this.cities.size()-1;
					this.cities.get(firstIndex).addConnection(secondCity, dist);
					this.cities.get(secondIndex).addConnection(firstCity, dist);
				}
			}
			else{
				//Create the first city
				this.cities.add(new City(firstCity));
				int firstIndex = this.cities.size()-1;
				//Now make sure the second city exists
				if(cityExists(secondCity)){	//First doesn't exist, second does
					//Now add the connections
					int secondIndex = findCityIndex(secondCity);
					this.cities.get(firstIndex).addConnection(secondCity, dist);
					this.cities.get(secondIndex).addConnection(firstCity, dist);
				}
				else{	//Neither City exists
					this.cities.add(new City(secondCity));
					int secondIndex = this.cities.size()-1;
					this.cities.get(firstIndex).addConnection(secondCity, dist);
					this.cities.get(secondIndex).addConnection(firstCity, dist);
				}
			}
		}
		this.setDest(new Anthill(this.getCities()));
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
		return -1;//TODO error checking
	}

	/**
	 * @param city
	 * @return - 	TRUE if the given city is in the private cities list,
	 * 				FALSE otherwise
	 */
	private boolean cityExists(int city) {
		for(int i=0; i < this.cities.size(); i++){
			if(this.cities.get(i).getName() == city){
				return true;
			}
		}
		return false;
	}

	/**
	 * Prints the list of cities.
	 */
	public void printCities(){
		for (int i=0; i < this.cities.size(); i++){
			this.cities.get(i).printCity();
		}
	}
	
	/**
	 * Begins solving this problem. This is initiated by main after the instance
	 * has been successfully set up.
	 */
	public void solve(){
		Random rn = new Random();	//Will be used to determine starting cities
		int start = 0;				//Stores starting cities
		int numCities = this.getCities().size();
		ArrayList<Ant> ants = new ArrayList<Ant>();	//Stores each ant
		
		//Create each ant with a random starting city and store them in ants
		for(int i = 0; i < 100; i++){
			start = rn.nextInt(numCities);
			Ant agent = new Ant(this.getCities(), this.getCities().get(start), this.getDest());
			ants.add(agent);
		}
		
		//Step through each ant one travel at a time
		int dest = -1;	//Temporary variable for the coming loop. Value of -1
						//can be use to identify errors
		//Keep looping while there are entries in ants
		while(ants.size() > 0){
			//Let each ant have a chance to move once
			for(int i = 0; i < ants.size(); i++){
				//Let this ant travel once. Store its destination.
				dest = ants.get(i).travel();
				//If the destination is -2, this ant is finished and can be deleted
				if (dest == -2){
					//This ant is done. Set its value in ants to a dummy value
					//that we will remove from the list before the next iteration
					ants.set(i, new Ant());
				}
			}
			//Remove all dummy ants
			for(int i = 0; i < ants.size(); i++){
				if(ants.get(i).getCurrentCity().getName() == -2){
					ants.remove(i);
				}
			}
			//Dissipate pheromone trails
			for (int i = 0; i < this.getCities().size(); i++){
				ArrayList<ArrayList<Integer>> table = this.getCities().get(i).getDiPhTable();
				for (int j = 0; j < table.size(); j++){
					ArrayList<Integer> temp = new ArrayList<Integer>();
					temp.add(table.get(j).get(0));//Add name
					temp.add(table.get(j).get(1));//Add distance
					//The following if/else adds the proper pheromone level.
					//Note that pheromone levels cannot be lower than 0.
					if(table.get(j).get(2) != 0) temp.add(table.get(j).get(2) - 1);
					else temp.add(0);
					table.set(j, temp);
				}
			}
		}
		
		//Display the results of this TSP instance
		this.getDest().printResults();
	}
	
	/**
	 * Sets the list of cities to the given list. This function will not see
	 * general use after testing.
	 * 
	 * @param cities - The list of cities to set.
	 */
	public void setCities(ArrayList<City> cities) {
		this.cities = cities;
	}

	/**
	 * @return Returns the list of cities in this TSP problem.
	 */
	public ArrayList<City> getCities() {
		return this.cities;
	}

	/**
	 * Tells each city to print out its connections.
	 */
	public void printConnections() {
		for(int i = 0; i < this.cities.size(); i++){
			this.cities.get(i).printConnections();
		}
		
	}

	/**
	 * Sets the anthill for this instance.
	 * @param dest
	 */
	public void setDest(Anthill dest) {
		this.dest = dest;
	}

	/**
	 * @return Returns the anthill for this instance.
	 */
	public Anthill getDest() {
		return this.dest;
	}
	
}
