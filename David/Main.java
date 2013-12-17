import java.util.*;	
import java.io.*;

public class Main{
	
	public static void main(String[] args) {
		int number_of_nodes = 10;
		int number_of_ants = 10;
		int number_of_episodes = 30;
		int strategy = 2;
		float alpha = 5f;
		float beta = 5.0f;

		float bestCost = -1;
		Vector<Node> bestPath = new Vector<Node>();

		Node[] nodes = new Node[number_of_nodes];
		Ant[] ants = new Ant[number_of_ants];
		String file = "tests/ten.txt";	

		//Create all the nodes needed
		for(int i=0; i<nodes.length; i++){
			nodes[i] = new Node(i);
		}

		//Read connections from file
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			for(String line; (line = br.readLine()) != null; ) {
				String[] s = line.split("\\s+");
				nodes[Integer.parseInt(s[0])].connect(nodes[Integer.parseInt(s[1])], Integer.parseInt(s[2]));
				nodes[Integer.parseInt(s[1])].connect(nodes[Integer.parseInt(s[0])], Integer.parseInt(s[2]));
			}
		}
		catch(Exception e){
			System.out.println("Something went wrong reading file");
			System.exit(1);
		}

		//Print all the nodes and connections
		for (Node n : nodes) {
			// n.print();
		}

		//Create the ants with ID and random starting node
		Random r = new Random();
		for (int i=0; i<number_of_ants; i++) {
			ants[i] = new Ant(i, nodes[r.nextInt(number_of_nodes)], number_of_nodes, alpha, beta);
			// ants[i].printInfo();
		}

		//Run the loop number_of_nodes times, each city and back to start
		for (int t=0; t<number_of_episodes; t++) {
			System.out.println("\n------- new episode " + t + " -------");
			// for (Node n : nodes) {
			// 	n.print();
			// }
			//Compute a new tour for each ant and print the solution
			for (Ant a : ants) {
				a.newRound();
				a.takeTour(strategy);
				if(a.done())
					a.printSolution();
				else
					System.out.println("Ant didnt find a solution");
			}

			//Check if any ant has found a better solution and store it
			for (Ant a : ants) {
				if(a.done()){
					if(a.getCost() < bestCost || bestCost == -1){
						bestCost = a.getCost();
						bestPath = a.getPath();
					}
				}
			}
			// printSolution(bestPath, bestCost);


			//Update the pheromone for all edges (i, j)
			for(int i=0; i<nodes.length; i++){
				for (int j=0; j<nodes.length; j++) {
					Node n = nodes[i];
					n.updatePheromone(nodes[j], ants, bestPath, bestCost);
				}
			}
		}
		printSolution(bestPath, bestCost);
	}

	private static void printSolution(Vector<Node> path, float cost){
		System.out.print("Best solution found so far: ");
		for (Node n : path) {
			System.out.print(" " + n.id());
		}
		System.out.println(", Cost: " + cost);
	}
}