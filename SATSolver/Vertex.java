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
	public void setIncomingEdges(ArrayList<Vertex> inE){
		this.incomingEdges = inE;
	}
	
	public void setOutgoingEdges(ArrayList<Vertex> outE){
		this.outgoingEdges = outE;
	}
	
	public void addOutgoingEdge(Vertex v){
		this.outgoingEdges.add(v);
	}
	
	public void addIncomingEdge(Vertex v){
		this.incomingEdges.add(v);
	}
	
	/* Getter */
	public int getID(){
		return this.id;
	}
	
	public ArrayList<Vertex> getEdgesFrom(){
		return this.incomingEdges;
	}
	
	public ArrayList<Vertex> getEdgesTo(){
		return this.outgoingEdges;
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