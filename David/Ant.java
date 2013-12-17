import java.util.Random;
import java.util.*;

class Ant{
	private Vector<Node> path;
	private Vector<Float> costs;
	private boolean[] visited;
	private int id;
	private boolean debug = false;
	private float alpha;
	private float beta;


	public Ant(int id, Node start, int number_of_nodes, float a, float b){
		this.id = id;
		path = new Vector<Node>();
		costs = new Vector<Float>();		
		visited = new boolean[number_of_nodes];
		path.add(start);
		costs.add(0.0f);
		visited[start.id()] = true;
		alpha = a;
		beta = b;
	}

	//Reset parameters and prepare for a new tour
	public void newRound(){
		Node start = path.firstElement();
		path.clear();
		costs.clear();
		visited = new boolean[visited.length];
		path.add(start);
		costs.add(0.0f);
		visited[start.id()] = true;
	}

	//Check if the ant has found a complete path
	public boolean done(){
		for (int i=0; i<visited.length; i++)
			if(!visited[i]) return false;

		if(path.firstElement() == path.lastElement() && path.size() == visited.length+1)
			return true;
		return false;
	}

	//Print information about the ant and what he has visited
	public void printInfo(){
		debugPrint("Ant " + id + " is at " + path.lastElement().id() + " and has visited: ");
		for (int i=0; i<path.size(); i++) {
			if(i==path.size())
				debugPrint(path.elementAt(i).id() + "\n");
			else
				debugPrint(path.elementAt(i).id() + ", ");
		}
	}

	//Print the solution found
	public void printSolution(){
		int cost = 0;
		System.out.print("Ant " + id + " found a solution:");
		for (int i=0; i<path.size(); i++) {
			System.out.print(" " + path.elementAt(i).id());
			cost += costs.elementAt(i);
		}
		System.out.println(", Cost: " + cost);
	}

	//Return the solution found
	public Vector<Node> getPath(){
		return path;
	}

	//Return the cost for current solution
	public float getCost(){
		float cost = 0;
		for(float c : costs)
			cost += c;
		return cost;
	}

	//Update the ant with provided strategy
	public void takeTour(int strategy){
		printInfo();
		if(strategy == 0)
			strategy_randomStrategy();
		if(strategy == 1)
			strategy_firstNotVisited();
		if(strategy == 2)
			strategy_antSystem();
		
		visited[path.lastElement().id()] = true;

		for (boolean b : visited)
			if(!b) takeTour(strategy);
		if(path.lastElement().id() != path.firstElement().id()){
			printInfo();
			addStartVertice();
		}
	}	

	//Go to the city with best transition probability
	private void strategy_antSystem(){
		Vector<Vertice> vertices = path.lastElement().vertices();
		Vertice chosenVertice = vertices.elementAt(0);
		float[] probabilities = calculateProbabilites();

		Random r = new Random();
		float p = r.nextFloat();
		float start=0, end=0;
		for (int i=0; i<vertices.size(); i++) {
			end += probabilities[i];
			if(p >= start && p <= end){
				chosenVertice = vertices.elementAt(i);
				break;
			}
			start += probabilities[i];
		}

		// float bestProb = 0;
		// for (int i=0; i<vertices.size(); i++){
		// 	Vertice current = vertices.elementAt(0);
		// 	if(probabilities[i] > bestProb){
		// 		bestProb = probabilities[i];
		// 		bestVertice = vertices.elementAt(i);
		// 	}
		// }

		debugPrintln("  Chosen destination to take is " + chosenVertice.destination().id() + " with a cost " + chosenVertice.cost());
		path.add(chosenVertice.destination());
		costs.add(chosenVertice.cost());
	}

	//Go to the first not already visited node
	private void strategy_firstNotVisited(){
		for (Vertice v : path.lastElement().vertices()){
			if(!visited[v.destination().id()] || (v.destination().id() == path.firstElement().id() && path.size() == visited.length)) {
				path.add(v.destination());
				costs.add(v.cost());
				return;
			}
		}
	}

	//Pick a random edge and go to. Dont care if visited or not.
	private void strategy_randomStrategy(){
		Vector<Vertice> vertices = path.lastElement().vertices();
		Random r = new Random();
		//Take out a random edge and go to its destination
		int next = r.nextInt(vertices.size());
		path.add(vertices.elementAt(next).destination());
		costs.add(vertices.elementAt(next).cost());
	}

	//Add the vertice that takes you back to the start. If not available add none
	private void addStartVertice(){
		debugPrintln(" Adding vertice to go back to start");
		Vector<Vertice> vertices = path.lastElement().vertices();
		Node start = path.elementAt(0);
		for (Vertice v : vertices) {
			if(v.destination().id() == start.id()){
				debugPrintln("  Found start with destination " + v.destination().id() + " with cost " + v.cost());
				path.add(v.destination());
				costs.add(v.cost());
				return;
			}
		}
		debugPrintln("  Did not found a vertice to start");
	}

	//Calculate the probability to go to a each vertice
	private float[] calculateProbabilites(){
		Vector<Vertice> vertices = path.lastElement().vertices();
		Vector<Node> destinations = new Vector<Node>();

		float denominator = 0;
		//Take out all the cities not visited and calculate the denominator
		for (Vertice v : vertices) {
			if(!visited[v.destination().id()]){
				destinations.add(v.destination());
				denominator += Math.pow(v.pheromone(), alpha) * Math.pow(1.0f/v.cost(), beta);
			}
		}

		//Calculate the probability for each vertice
		float[] probabilities = new float[vertices.size()];
		for (int i=0; i<vertices.size(); i++){
			Vertice current = vertices.elementAt(i);
			if(visited[current.destination().id()]){
				probabilities[i] = 0;
				continue;
			}
			float pheromone = current.pheromone();
			float visibility = 1.0f/current.cost();
			float nominator = (float)Math.pow(pheromone, alpha) * (float)Math.pow(visibility, beta);
			probabilities[i] = nominator/denominator;
			debugPrintln(" probability for vertice " + i + " with destination " + vertices.elementAt(i).destination().id() + " is " + probabilities[i]);
		}
		return probabilities;
	}

	private void debugPrint(String s){
		if(debug) System.out.print(s);
	}
	private void debugPrintln(String s){
		if(debug) System.out.println(s);
	}
}