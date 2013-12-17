import java.util.ArrayList;
import java.util.Random;


/**
 * An ant object is the smallest agent used in the TSP problem. This agent will
 * travel from city to city until it has completed a tour.
 *
 * @author Kegan Kaiser
 *         Created December 10, 2013.
 */
public class Ant {

	/**
	 * The list of cities around which this ant must travel.
	 */
	private ArrayList<City> cities = new ArrayList<City>();
	/**
	 * The city this ant is currently at.
	 */
	private City currentCity;
	/**
	 * This list contains the numbers of the cities this ant has visited, in
	 * the order that they were visited.
	 */
	private ArrayList<Integer> visited = new ArrayList<Integer>();
	
	/**
	 * The index of the currentCity in cities. Currently not in use.
	 */
	private int currentIndex;
	
	/**
	 * The anthill all ants must return to after completing a tour.
	 */
	private Anthill hill = new Anthill();
	
	/**
	 * Default constructor initializes an ant with currentCity = -2. This 
	 * tells other functions that the ant is currently at the anthill.
	 * This constructor is only used in dummy ants in TSP_Instance.
	 */
	public Ant(){
		this.setCurrentCity(new City(-2));
		this.visited.add(-2);
	}
	
	/**
	 * Main constructor takes a list of cities that this ant must travel, a
	 * city to start at, and an anthill. After setting these values the ants
	 * tour begins.
	 * @param cityList
	 * @param start
	 * @param hill 
	 */
	public Ant(ArrayList<City> cityList, City start, Anthill hill){
		this.setCurrentCity(start);
		this.setCities(cityList);
		//this.setCurrentIndex(findCurrentIndex());
		this.setHill(hill);
		this.visited.add(start.getName());
	}
	
	/**
	 * Tells this ant to complete an entire tour around the cities by invoking
	 * travel() until the ant returns to the anthill.
	 */
	public void tour(){
		int city = -1;
		while(city != -2){
			city = this.travel();
		}
	}
	
	/**
	 * Tells the ant to travel from the current city another. The city to
	 * travel to is chosen using the DiPhTable for that city. This function
	 * also updates pheromone trails.
	 * 
	 * Adding pheromone
	 * One layer of pheromone is always added. Additional layers are added	TODO break into three parts and update both ways
	 * under certain conditions. If a path ends in a tour that can be
	 * continued, one extra layer of pheromone is added. If a path leads to
	 * the completion of a tour, two extra layers are added. One layer of
	 * pheromone is removed from each path in TSP_Instance periodically.
	 *
	 * @return The city traveled to, -2 if traveled to anthill.
	 */
	public int travel(){
		//Build a list of cities this ant can legally travel to
		City next = findBestPath();
		//If there were no options (next is -2), go to anthill
		if (next.getName() == -2){
			//If we can return to the start city from here, return to anthill
			int start = this.getVisited().get(0);
			if(this.getCurrentCity().findTableIndex(start) != -1){
				this.visited.add(start);
				this.getHill().receiveAnt(this.getVisited());
				//Add 2 pheromone between the last city found and the prior city
				//TODO breaks if there are less than 3 cities, < 3 should not be possible though
				int city1 = this.getVisited().get(this.getVisited().size() - 2);
				int city2 = this.getVisited().get(this.getVisited().size() - 3);
				this.getCities().get(findCityIndex(city1)).addPheromone(city2, 2);
				this.getCities().get(findCityIndex(city2)).addPheromone(city1, 2);
			}
			//else, failed tour
		}
		//Otherwise travel to a new city, update city list and return
		else{
			this.visited.add(next.getName());
			this.setCurrentCity(next);
			//this.setCurrentIndex(findCurrentIndex());
			//Add 1 pheromone between the current and last cities
			int city1 = this.getVisited().get(this.getVisited().size() - 1);
			int city2 = this.getVisited().get(this.getVisited().size() - 2);
			this.getCities().get(findCityIndex(city1)).addPheromone(city2, 1);
			this.getCities().get(findCityIndex(city2)).addPheromone(city1, 1);
		}
		
		//Return the number of the city traveled to
		return next.getName();
	}

	/**
	 * @return 	The best city for this ant to travel to, determined by distance
	 * 			and pheromone.
	 */
	public City findBestPath(){
		/**
		 * A selection of the following variables is used by each heuristic.
		 * They are safe to leave uncommented regardless of which is being used.
		 */
		//Best path found so far, initialized to City(-2), which is returned if
		//no valid path is found.
		City bestPath = new City(-2);
		//Copy the DiPhTable of the current city
		ArrayList<ArrayList<Integer>> table = this.getCurrentCity().getDiPhTable();
		//List of possible paths
		ArrayList<Integer> legalPaths = new ArrayList<Integer>();
		//List containing the probability of picking each path (book algorithm)
		ArrayList<Double> prob = new ArrayList<Double>();
		int bestDist = -1;//Distance between this city and the bestPath city
		int nearCity = -1;	//Index of nearest legal city in cities
		int nearDist = -1;	//Distance from current city to nearest legal city
		Random rn = new Random();//Used to randomly select a path
		
//		/**
//		 * Weighted Probability Heuristic. Each legal option is added to a list.
//		 * One is picked at random from this list. Each option is added once,
//		 * and once more for each layer of pheromone connecting it to the
//		 * current city.
//		 * 
//		 * This can be run with or without distance calculations. The cities are
//		 * sorted by their distances. The shortest paths get additional copies
//		 * added to the list. See the loop below.
//		 */
//		for (int i = 0; i < table.size(); i++){
//			//Check to see if the list of visited nodes contains this city
//			if(!(this.getVisited().contains(table.get(i).get(0)))){	//Does not contain
//				for (int j = 0; j <= table.get(i).get(2); j++){//Add once per pheromone level
//					int index = findCityIndex(table.get(i).get(0));
//					legalPaths.add(this.getCities().get(index).getName());
//				}
//				if (nearCity == -1){
//					nearCity = findCityIndex(table.get(i).get(0));
//					nearDist = table.get(i).get(1);
//				}
//				else{
//					if(table.get(i).get(1) < nearDist){
//						nearCity = findCityIndex(table.get(i).get(0));
//						nearDist = table.get(i).get(1);
//					}
//				}
//			}
//		}
//		
//		//If there are legal paths, pick one at random. If there are not, the
//		//initialized -2 city will be returned.
//		if(legalPaths.size() > 0){
//			/**
//			 * Now take distances into account. (Optional)
//			 */
//			//Find the second nearest city
//			int nearCity2 = findNextNearest(nearDist, legalPaths);
//			//Finding the name of the second city
//			int near2Name = this.getCities().get(nearCity2).getName();
//			//Find the third nearest city
//			int nearCity3 = findNextNearest(this.lookupDist(near2Name), legalPaths);
//			
//			//Add additional copies of the nearest cities.
//			for (int i = 0; i < 3; i++){
//				legalPaths.add(this.getCities().get(nearCity).getName());
//				if (i > 0) legalPaths.add(this.getCities().get(nearCity2).getName());
//				if (i > 1) legalPaths.add(this.getCities().get(nearCity3).getName());
//			}
//			/**
//			 * End of distance calculations. Comment the block out between these
//			 * points to ignore distances.
//			 */
//			
//			
//			/**
//			 * Pick an option at random from the built list of options. This
//			 * list is used in the weighted probability heuristic described
//			 * above and may include distance calculations (also above).
//			 */
//			int randIndex = rn.nextInt(legalPaths.size());
//			bestPath = this.getCities().get(findCityIndex(legalPaths.get(randIndex)));
//		}
		
		
		
		/**
		 * Book algorithm. This is the Random Transition Rule.
		 * Make sure the above weighted probability heuristic is commented out
		 * before using this section and vice versa.
		 * Algorithm:
		 * P = [(t)^A * (n)^B] / E[(t)^A * (n)^B]
		 * Where P is the probability a path will be chosen, t is the
		 * pheromone level present, n is the visibility (inverse of distance),
		 * the denominator is the sum of all t^A * n^B around this city, and
		 * both A and B are tuning parameters.
		 */
		//Set the tuning parameters
		int A = 1;
		int B = 1;
		//Create the E variable
		double E = 0;
		for (int i = 0; i < table.size(); i++){
			if(!(this.getVisited().contains(table.get(i).get(0)))){	//Does not contain
				E += (Math.pow(table.get(i).get(2), A) *				//t^A
						Math.pow(Math.pow(table.get(i).get(1), -1), B));//n^B
			}
		}
		for (int i = 0; i < table.size(); i++){
			//Check to see if the list of visited nodes contains this city
			if(!(this.getVisited().contains(table.get(i).get(0)))){	//Does not contain
				//Find the index of this city in the table
				int index = findCityIndex(table.get(i).get(0));
				//Create t and n for the currently selected city and compute the
				//probability, p, of it being selected
				double t = table.get(i).get(2);
				double n = Math.pow(table.get(i).get(1), -1);
				double p = ((Math.pow(t, A)) * (Math.pow(n, B))) / E;
				//Add the city to the list of legal paths
				legalPaths.add(this.getCities().get(index).getName());
				//Add the corresponding probability
				prob.add(p);
			}
		}
		//Transition Rule complete. Either use the highest probability or
		//randomly select a path based on the calculated probabilities.
		//If there are legal paths, pick one according to the heuristic.
		//If there are not, the initialized -2 city will be returned.
		if(legalPaths.size() > 0){
			//Use highest prob
//			int bestProbIndex = this.findMaxIndex(prob);
//			int bestPathName = legalPaths.get(bestProbIndex);
//			bestPath = this.getCities().get(findCityIndex(bestPathName));
			//Random selection TODO broken formula
			int randomIndex = this.pickWithProb(prob);
			bestPath = this.getCities().get(randomIndex);
		}

		
		
		/**
		 * Closest city heuristic. Always picks the nearest city.
		 */
//		for (int i = 0; i < table.size(); i++){
//			//Check to see if the list of visited nodes contains this city
//			if(!(this.getVisited().contains(table.get(i).get(0)))){	//Does not contain
//				//Replace the best path with this path if it scores higher
//				if(bestDist == -1){//First loop through, just replace whatever you find
//					bestDist = table.get(i).get(1);
//					bestPath = this.getCities().get(findCityIndex(table.get(i).get(0)));
//				}
//				else{//Otherwise check and see if the path is better
//					if(table.get(i).get(1) < bestDist){
//						bestDist = table.get(i).get(1);
//						bestPath = this.getCities().get(findCityIndex(table.get(i).get(0)));
//					}
//				}
//			}
//		}	
		return bestPath;
	}
	
	/**
	 * @param prob - A list containing probabilities.
	 * @return 	The index of a randomly selected element from prob, where each
	 * 			entry in prob determines its own chance at being selected.
	 */
	private int pickWithProb(ArrayList<Double> prob) {
		//This list will have multiple copies of each index, note that the index
		//is stored, not the object in prob
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		//Temporary variable storing the number of entries of each element to add
		double numCopies = 0;
		
		//Used to generate a random index
		Random rn = new Random();
		
		for (int i = 0; i < prob.size(); i++){
			numCopies = Math.floor(100 * prob.get(i));
			for (double j = 0; j <= numCopies; j++){
				list.add(i);
			}
		}
		System.out.println(prob.size() + " " + list.size());
		//Now return one of the indices at random. Note that each index in list
		//now corresponds to a valid index in prob and list has additional
		//copies of each index proportional to that indices probability
		return list.get(rn.nextInt(list.size()));
	}

	/**
	 * @param list
	 * @return The index of the largest element in the given list
	 */
	public int findMaxIndex(ArrayList<Double> list){
		int maxIndex = -1;
		double maxValue = -1.0;
		
		for (int i =0; i < list.size(); i++){
			if (maxValue == -1){//Take the first object you see
				maxIndex = i;
				maxValue = list.get(i);
			}
			else{	//Something has been selected, check for larger value
				if(list.get(i) > maxValue){
					maxIndex = i;
				}
			}
		}
		
		return maxIndex; //TODO error checking
	}
	
	/**
	 * @param dist
	 * @param list
	 * @return 	The nearest city in list that is farther away than the given
	 * 			distance. The index of this city in this.cities is returned,
	 * 			rather than the name of the city or the object itself.
	 */
	public int findNextNearest(int dist, ArrayList<Integer> list){
		int nextNearest = -1;	//The name of the next nearest city to return
		int nextDist = -1;		//The distance to the next nearest city
		int tempDist = -1;		//Temporary distance used in the loop
		for (int i = 0; i < list.size(); i++){
			//Lookup distance from current city to listed city
			tempDist = this.lookupDist(list.get(i));
			if(nextDist == -1){//No city found yet, take the first one
				nextDist = tempDist;
				nextNearest = list.get(i);
			}
			else{//A city has been found, check to see if it is the next nearest
				if((tempDist < nextDist) && (tempDist > dist)){
					nextDist = tempDist;
					nextNearest = list.get(i);
				}
			}
		}
		
		return nextNearest;
	}
	
	/**
	 * @param target - The city we want to find in this.cities
	 * @return The index in cities corresponding to the city with the target name
	 */
	public int findCityIndex(int target){
		for (int i = 0; i < this.getCities().size(); i++){
			if (this.getCities().get(i).getName() == target){
				return i;
			}
		}
		return -1;//TODO error checking
	}
	
	/**
	 * @param target - The city we want the distance of
	 * @return 	The distance between the current city and the target, -1 if the
	 * 			current city and the target are not connected
	 */
	public int lookupDist(int target){
		ArrayList<ArrayList<Integer>> table = this.getCurrentCity().getDiPhTable();
		for (int i = 0; i < table.size(); i++){
			if (table.get(i).get(0) == target){
				return table.get(i).get(1);
			}
		}
		return -1;//TODO error checking
	}
	
	/**
	 * @return The index of the currentCity in cities.
	 */
	public int findCurrentIndex(){
		int desiredCity = this.getCurrentCity().getName();
		for (int i = 0; i < this.getCities().size(); i++){
			if (desiredCity == this.getCities().get(i).getName()){
				return i;
			}
		}
		
		return -1; //TODO error checking
	}
	
	/**
	 * Sets the list of cities to the given list.
	 * @param cities
	 */
	public void setCities(ArrayList<City> cities) {
		this.cities = cities;
	}

	/**
	 * @return Returns the list of cities this ant must visit.
	 */
	public ArrayList<City> getCities() {
		return this.cities;
	}

	/**
	 * Sets the current city to the given city.
	 * @param currentCity
	 */
	public void setCurrentCity(City currentCity) {
		this.currentCity = currentCity;
	}

	/**
	 * @return Returns the current city this ant is at.
	 */
	public City getCurrentCity() {
		return this.currentCity;
	}

	/**
	 * Sets the list of cities this ant has visited to the given list.
	 * @param visited
	 */
	public void setVisited(ArrayList<Integer> visited) {
		this.visited = visited;
	}

	/**
	 * @return 	Returns the list of cities this ant has visited, in the order
	 * 			in which they were visited.
	 */
	public ArrayList<Integer> getVisited() {
		return this.visited;
	}

	/**
	 * Sets the index of the currentCity to the given index.
	 * @param currentIndex
	 */
	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	/**
	 * @return Returns the index in cities of the currentCity.
	 */
	public int getCurrentIndex() {
		return this.currentIndex;
	}

	/**
	 * Sets the anthill to the given anthill. This method will not see general
	 * use outside of testing.
	 * @param hill
	 */
	public void setHill(Anthill hill) {
		this.hill = hill;
	}

	/**
	 * @return Returns the anthill that this ant will return to.
	 */
	public Anthill getHill() {
		return this.hill;
	}
}
