package PieceItTogether.SATSolver;

import java.util.ArrayList;

public class Vertex {
	private final int id;
	private ArrayList<Vertex> outgoingEdges = new ArrayList<Vertex>(); 
	private ArrayList<Vertex> incomingEdges = new ArrayList<Vertex>(); 
	
	/* Konstruktor */
	public Vertex(int id){
		this.id = id;
	}
	
	/* Setter/Adder */
	public void addOutgoingEdge(Vertex v){
		this.outgoingEdges.add(v);
		v.incomingEdges.add(this);
	}
	
	public void addIncomingEdge(Vertex v){
		this.incomingEdges.add(v);
		v.outgoingEdges.add(this);
	}
	
	/* Getter */
	public int getID(){
		return this.id;
	}
	
	public ArrayList<Vertex> getIncomingEdges(){
		return this.incomingEdges;
	}
	
	public ArrayList<Vertex> getOutgoingEdges(){
		return this.outgoingEdges;
	}
	
	public Vertex getIncomingEdge(int i){
		return this.incomingEdges.get(i);
	}
	
	public Vertex getOutgoingEdge(int i){
		return this.outgoingEdges.get(i);
	}
	
	/* Overrides */
	@Override
	public boolean equals(Object o){
		if(o instanceof Vertex){
			if(this.id == ((Vertex) o).getID()){
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString(){
		String s = ((Integer) id).toString();
		return s;
	}
}
