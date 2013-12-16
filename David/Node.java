import java.util.*;

//Class to store a vertice
class Vertice{
	Node dest;
	float cost;
	float pheromone;

	//Create a vertice to dest with cost
	Vertice(Node dest, float cost){
		this.dest = dest;
		this.cost = cost;
		pheromone = 0.000001f;
	}

	//Return this vertice destination
	public Node destination(){
		return dest;
	}

	//Return the amount of pheromone for this city
	public float pheromone(){
		return pheromone;
	}

	public void pheromone(float pheromone){
		this.pheromone = pheromone;
	}

	//Return the cost for this vertice
	public float cost(){
		return cost;
	}
}

//Class to keep information about a Node
public class Node {
	private int id;
	private Vector<Vertice> vertices;
	int e = 5; //Elitist ants
	float p = 0.5f; //Evaporation
	float q = 100; //Q parameter, optimal tour length

	//Create a Node with an ID
	Node(int id){
		this.id = id;
		vertices = new Vector<Vertice>();
	}

	public void updatePheromone(Node destination, Ant[] ants, Vector<Node> path, float cost){
		for (Vertice v : vertices) {
			if(v.destination().id() == destination.id()){
				float a = (1-p)*v.pheromone();
				float b = 0;
				for(Ant ant : ants){
					b += antPheromone(id(), destination.id(), ant);
				}
				float c = partOfTour(id(), destination.id(), path)? q/(float)cost : 0;
				float tao = a + b + e*c;
				v.pheromone(tao);
			}
		}
	}

	//Calculate the pheromone to add with tour from ant 
	private float antPheromone(int i, int j, Ant a){
		if(partOfTour(i, j, a.getPath()))
			return (q/a.getCost());
		return 0;
	}

	//See if edge (i, j) is part of tour path
	private boolean partOfTour(int i, int j, Vector<Node> path){
		for(int a=0; a<path.size()-1; a++) {
			if(i==path.elementAt(a).id() && j==path.elementAt(a+1).id() ||
			   j==path.elementAt(a).id() && i==path.elementAt(a+1).id())
				return true;
		}
		return false;
	}


	//Return Nodes ID
	public int id(){
		return id;
	}

	//Return all vertices for this noce
	public Vector<Vertice> vertices(){
		return vertices;
	}

	//Connect this node to another node with a cost
	public void connect(Node dest, int cost){
		vertices.add(new Vertice(dest, cost));
	}

	//Print all the connections associated with this Node
	public void print(){
		System.out.println("Node " + id + " is connected to:");
		for (Vertice v : vertices) {
			String s = v.cost()<10?" ":"";
			System.out.println("  " + v.destination().id() + " with cost " + v.cost() + s + " and pheromone " + v.pheromone());
		}
	}
}