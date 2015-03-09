package PieceItTogether.SATSolver;

import java.util.ArrayList;

class Node {
	private String name;
	private ArrayList<Node> edges = new ArrayList<Node>();
	
	public Node(String name){
		this.name = name;
	}
	
	public void setEdge(Node k){
		this.edges.add(k);
	}
	
	public String getName(){
		return this.name;
	}
	
	public ArrayList<Node> getEdges(){
		return this.edges;
	}
	
	public Node getEdge(int i){
		return edges.get(i);
	}
	
	public String toString(){
		String s = "Knoten "+name+" hat Kanten zu den Knoten:\n";
		for(int i = 0; i < edges.size(); i++){
			s += edges.get(i).getName() + "\n";
		}
		return s;
	}
}
