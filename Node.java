package PieceItTogether;

import java.util.ArrayList;

public class Node {
	private final int id;
	private ArrayList<Node> edges = new ArrayList<Node>();

	public Node(int id){
		this.id = id;
	}

	public void setEdge(Node k){
		this.edges.add(k);
	}

	public int getID(){
		return this.id;
	}
	
	public ArrayList<Node> getEdges(){
		return this.edges;
	}
	
	public Node getEdge(int i){
		return edges.get(i);
	}

	public String toString(){
		String s = "Knoten "+id+" hat Kanten zu den Knoten:\n";
		for(int i = 0; i < edges.size(); i++){
			s += edges.get(i).getID() + "\n";
		}
		return s;
	}
}
