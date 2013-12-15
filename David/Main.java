import java.util.*;	
import java.io.*;

public class Main{
	
	public static void main(String[] args) {
		int number_of_nodes = 6;
		int number_of_ants = 1;
		int number_of_episodes = 1;
		int strategy = 2;
		int delay = 100;

		Node[] nodes = new Node[number_of_nodes];
		Ant[] ants = new Ant[number_of_ants];
		String file = "tests/six.txt";	

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
			n.print();
		}

		//Create the ants with ID and random starting node
		Random r = new Random();
		for (int i=0; i<number_of_ants; i++) {
			ants[i] = new Ant(i, nodes[r.nextInt(number_of_nodes)], number_of_nodes);
			ants[i].printInfo();
		}

		//Run the loop number_of_nodes times, each city and back to start
		for (int i=0; i<number_of_episodes; i++) {
			// System.out.println("\n------- new round " + i + " -------");
			for (Ant a : ants) {
				a.update(strategy);
				a.printInfo();
			}
		}

		//Check if anyone has a solution
		boolean solution = false;
		for (Ant a : ants) {
			if(a.done()){
				a.printSolution();
				return;
			}
		}

		if(!solution)
			System.out.println("\nNo solution was found...\n");
	}
}