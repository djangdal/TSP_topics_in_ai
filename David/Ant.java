import java.util.Random;
import java.util.*;

class Ant{
	private Vector<Node> path;
	private Vector<Integer> costs;
	private boolean[] visited;
	private int id;


	public Ant(int id, Node start, int number_of_nodes){
		this.id = id;
		path = new Vector<Node>();
		costs = new Vector<Integer>();		
		visited = new boolean[number_of_nodes];
		path.add(start);
		costs.add(0);
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
		System.out.print("Ant " + id + " is at " + path.lastElement().id() + " and has visited: ");
		for (int i=0; i<path.size(); i++) {
				System.out.print(path.elementAt(i).id() + ", ");
		}
		System.out.println();
	}

	public void printSolution(){
		int cost = 0;
		System.out.print("Ant " + id + " found a solution:");
		for (int i=0; i<path.size(); i++) {
			System.out.print(" " + path.elementAt(i).id());
			cost += costs.elementAt(i);
		}
		System.out.println(", Cost: " + cost);
	}

	//Update the ant with provided strategy
	public void update(int strategy){
		if(strategy == 0)
			randomStrategy();
		if(strategy == 1)
			firstNotVisited();
		if(strategy == 2)
			antSystem();
		
		visited[path.lastElement().id()] = true;

		for (boolean b : visited)
			if(!b) update(strategy);
	}

	private void antSystem(){
		Vector<Vertice> vertices = path.lastElement().vertices();
		float[] probabilities = calculateProbabilites();

		Vertice bestVertice = vertices.elementAt(0);
		float bestProb = 0;
		for (int i=0; i<vertices.size(); i++){
			Vertice current = vertices.elementAt(0);
			if(probabilities[i] > bestProb){
				bestProb = probabilities[i];
				bestVertice = vertices.elementAt(i);
			}
		}

		System.out.println("Best destination to take is " + bestVertice.destination().id() + " with a cost " + bestVertice.cost());
		path.add(bestVertice.destination());
	}


	private float[] calculateProbabilites(){
		Vector<Vertice> vertices = path.lastElement().vertices();
		Vector<Node> destinations = new Vector<Node>();

		float alpha = 1;
		float beta = 5;
		float denominator = 0;
		//Take out all the cities not visited and calculate the denominator
		for (Vertice v : vertices) {
			if(!visited[v.destination().id()]){
				destinations.add(v.destination());
				denominator += Math.pow(v.pheromone(), alpha) * Math.pow(1.0f/v.cost(), beta);
				// System.out.println("Calculating denominator " + denominator + " with pheromone " + v.pheromone() + " and " + (float)1/v.cost());
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
			System.out.println("probability for vertice " + i + " with destination " + vertices.elementAt(i).destination().id() + " is " + probabilities[i]);
		}
		return probabilities;
	}


	//Go to the first no already visited node
	private void firstNotVisited(){
		for (Vertice v : path.lastElement().vertices()){
			if(!visited[v.destination().id()] || (v.destination().id() == path.firstElement().id() && path.size() == visited.length)) {
				path.add(v.destination());
				costs.add(v.cost());
				return;
			}
		}
	}

	//Pick a random edge and go to. Dont care if visited or not.
	private void randomStrategy(){
		Vector<Vertice> vertices = path.lastElement().vertices();
		Random r = new Random();
		//Take out a random edge and go to its destination
		int next = r.nextInt(vertices.size());
		path.add(vertices.elementAt(next).destination());
		costs.add(vertices.elementAt(next).cost());
	}
}