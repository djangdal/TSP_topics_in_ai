import java.util.*;

//Class to store a vertice
class Vertice{
	Node dest;
	int cost;
	float pheromone;

	//Create a vertice to dest with cost
	Vertice(Node dest, int cost){
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

	//Return the cost for this vertice
	public int cost(){
		return cost;
	}
}

//Class to keep information about a Node
public class Node  {
	private int id;
	private Vector<Vertice> vertices;

	//Create a Node with an ID
	Node(int id){
		this.id = id;
		vertices = new Vector<Vertice>();
	}

	//Return Nodes ID
	public int id(){
		return id;
	}

	//Return all vertices for this noce
	Vector<Vertice> vertices(){
		return vertices;
	}

	//Connect this node to another node with a cost
	void connect(Node dest, int cost){
		vertices.add(new Vertice(dest, cost));
	}

	//Print all the connections associated with this Node
	void print(){
		System.out.println("Node " + id + " is connected to:");
		for (Vertice v : vertices) {
			System.out.println("  " + v.destination().id() + " with cost " + v.cost());
		}
	}
}