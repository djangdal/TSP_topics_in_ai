import java.util.*;


/**
 * Begins the TSP problem by building the data structure for this problem
 * and passing it to TSP_Instance to solve.
 *
 * @author Kegan Kaiser
 *         Created December 10, 2013.
 */
public class Main {

	/**
	 * ASSUMPTIONS
	 * FALSE - 	Each city must be connected to at least one other city. If this
	 * 			is not true, this problem is unsolvable unless there is only one
	 * 			city, in which case it is trivial.
	 * Why - 	It appears some cities have connections such as [1,2,x], where
	 * 			[2,1,x] does not appear as it is redundant. If 2 is only
	 * 			connected to 1 it will not appear in the list.
	 */
	
	/**
	 * Starts the program.
	 * 
	 * @param args 
	 */
	public static void main (String[] args){
		System.out.println("Generating TSP instance");
		ArrayList<ArrayList<Integer>> cityList = generateFullyConnected();
		TSP_Instance TSP = new TSP_Instance(cityList);
		System.out.println("Successfully generated TSP instance");
		System.out.println("Solving TSP instance");
		//TSP.printCities();
		//TSP.printConnections();
		TSP.solve();
		System.out.println("Task completed");
	}
	
/**
 * The function given to generate fully connected graphs for testing purposes.
 * Slightly modified, the function now returns its result as well as printing it.
 * @return The generated list of cities.
 */
public static ArrayList<ArrayList<Integer>> generateFullyConnected() {
		
		int numberOfCities = 100;
		
		// Ok, I am fixing the seed, so that instead of reading in the
		// data from the text files, you can use this function to
		// generate the data I posted.
		Random r = new Random(0);
		ArrayList<ArrayList<Integer>> cityList = new ArrayList<ArrayList<Integer>>();
		//System.out.println("This instance contains the following connections: ");
		for (int i = 0; i < numberOfCities; i++) {
			for (int j = i + 1; j < numberOfCities; j++) {
				ArrayList<Integer> temp = new ArrayList<Integer>();
				temp.add(i);
				temp.add(j);
				temp.add((r.nextInt(100) + 1));
				//System.out.println(temp);
				cityList.add(temp);
			}
		}
		
		return cityList;
	}

/**
 * The function given to generate fully sparsely graphs for testing purposes.
 * Slightly modified, the function now returns its result as well as printing it.
 * @return The generated list of cities.
 */
//public static void generateSparselyConnected() { TODO sparse
//
//	int numberOfCities = 100;
//	Node [] city = new Node[numberOfCities];
//
//	for (int i = 0; i < numberOfCities; i++) {
//	    city[i] = new Node(i, numberOfCities);
//	}
//
//	Random r = new Random(0);
//	int distance;
//	int dice;
//
//	// To generate large, sparse random graphs we use the Erdos-Renyi model of random graphs.
//	for (int i = 0; i < numberOfCities; i++) {
//	    for (int j = i + 1; j < numberOfCities; j++) {
//		distance = r.nextInt(100) + 1;
//		dice = r.nextInt(numberOfCities);
//		if (dice <= (numberOfCities/10)) {
//		    city[i].setNeighbor(city[j], distance);
//		    city[j].setNeighbor(city[i], distance);
//		}
//
//	    }
//	    if (city[i].getNeighbors().length == 0) {
//		distance = r.nextInt(100) + 1;
//		dice = r.nextInt(numberOfCities);
//		city[i].setNeighbor(city[dice], distance);
//		city[dice].setNeighbor(city[i], distance);
//	    }
//	    while (city[i].getNeighbors().length == 1) {
//		dice = r.nextInt(numberOfCities);
//		if (dice != city[i].getNeighbors()[0].getID()) {
//		    distance = r.nextInt(100) + 1;
//		    city[i].setNeighbor(city[dice], distance);
//		    city[dice].setNeighbor(city[i], distance);
//		}
//	    }
//	}
//
//	for (int i = 0; i < numberOfCities; i++){
//	    Node[] temp = city[i].getNeighbors();
//	    int j = 0;
//	    while (temp[j] != null){
//		System.out.println(i + " " + city[i].getNeighborID(j) + " " + city[i].getDistance(j));
//		j++;
//	    }
//	}
//    }
}
