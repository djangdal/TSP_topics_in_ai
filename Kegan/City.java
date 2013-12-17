import java.util.ArrayList;


/**
 * Records information about a city for use in the TSP problem.
 *
 * @author Kegan Kaiser
 *         Created December 10, 2013.
 */
public class City {

	/**
	 * The Distance and Pheremone table stores information about the cities connected
	 * to this city. It has the following form:
	 * 			City1	Distance 	Pheromone Level
	 * 			City2	Distance	Pheromone Level
	 */
	private ArrayList<ArrayList<Integer>> DiPhTable = new ArrayList<ArrayList<Integer>>();
	
	/**
	 * The integer that will identify this city. Names are >= 0. We can check
	 * for a -1 name to see if an error has occurred.
	 */
	private int name = -1;
	
	/**
	 * Creates a city with the given name, which is an integer.
	 * @param aName - The integer that will identify this city.
	 */
	public City(int aName) {
		this.setName(aName);
	}
	
	
	/**
	 * Adds a connection to this city. The DiPhTable will be updated with this
	 * distance information and a pheromone level of 0.
	 * 
	 * @param city - The name of the city being connected
	 * @param distance - The distance to the city being connected
	 *
	 */
	public void addConnection(int city, int distance){
		if(city == -1){
			System.out.println("Bad connection in city " + this.getName());
		}
		else{
			ArrayList<Integer> newCity = new ArrayList<Integer>();
			newCity.add(city);		//City name
			newCity.add(distance);	//Distance between these cities
			newCity.add(1);			//Initialize pheromone to 1
			this.DiPhTable.add(newCity);
		}
	}
	
	/**
	 * This function is used to add pheromone to the connections between cities.
	 * @param city - The city forming a connection with this city
	 * @param ph - The pheromone level to add to the connection
	 *
	 */
	public void addPheromone(int city, int ph){
		int target = findTableIndex(city);
		if(target == -1){
			System.out.println("Bad pheromone trail at " + this.getName());
		}
		else{
			ArrayList<Integer> newConnection = new ArrayList<Integer>();
			newConnection.add(this.DiPhTable.get(target).get(0));
			newConnection.add(this.DiPhTable.get(target).get(1));
			newConnection.add(this.DiPhTable.get(target).get(2) + ph);
			this.DiPhTable.set(target, newConnection);
		}
	}
	
	/**
	 * @param city
	 * @return 	The index of the given city in the DiPhTable, or -1 if the city
	 * 			is not in the DiPhTable.
	 */
	public int findTableIndex(int city){
		for (int i = 0; i < this.DiPhTable.size(); i++){
			if(this.DiPhTable.get(i).get(0) == city){
				return i;
			}
		}
		return -1;
	}

	/**
	 * Prints the information stored about this city.
	 */
	public void printCity(){
		System.out.println("City - " + this.getName());
	}
	
	/**
	 * Sets this city's name to the given name.
	 * @param name
	 */
	public void setName(int name) {
		this.name = name;
	}


	/**
	 * @return Returns this city's name.
	 */
	public int getName() {
		return this.name;
	}


	/**
	 * Sets the DiPhTable to the given table. This setter will not see general
	 * use outside testing.
	 * @param diPhTable The diPhTable to set.
	 */
	public void setDiPhTable(ArrayList<ArrayList<Integer>> diPhTable) {
		this.DiPhTable = diPhTable;
	}


	/**
	 * @return Returns the DiPhTable.
	 */
	public ArrayList<ArrayList<Integer>> getDiPhTable() {
		return this.DiPhTable;
	}


	/**
	 * Prints the connections of this city.
	 */
	public void printConnections() {
		String connections = "";
		for(int i=0; i < this.DiPhTable.size(); i++){
			connections += this.DiPhTable.get(i).get(0) + ", ";
		}
		System.out.println("Connections to city " + this.getName() + " are: " + connections);
	}
}
