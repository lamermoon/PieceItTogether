package PieceItTogether.SATSolver;

import java.util.ArrayList;

public class Graph {
	private ArrayList<Edge> e;
	private ArrayList<Vertex> v;
	
	/* Konstruktor */
	public Graph(){
		e = new ArrayList<Edge>();
		v = new ArrayList<Vertex>();
	}
	
	public Graph(ArrayList<Edge> e, ArrayList<Vertex> v){
		this.e = e;
		this.v = v;
	}

	/* Methoden */
	public Graph getTransitiveClosure(){
		//TODO: implementieren
		for(int v = 0; v < this.v.size(); v++){
			for(int i = 0; i < this.v.size(); i++){
				for(int j = 0; j < this.v.size(); j++){
					Vertex vtx = this.v.get(v);
					this.e.add(new Edge(vtx.getIncomingEdges().get(i), vtx.getOutgoingEdges().get(j)));//Transitive Huelle
					this.e.add(new Edge(vtx, vtx));//Reflexsive Huelle
				}
			}
		}
		Graph g = new Graph();
		
		return g;
	}
	
	public boolean containsEdge(Edge e){
		return this.e.contains(e);
	}
	
	public boolean containsEdge(Vertex tail, Vertex head){
		if(this.v.contains(tail) && this.v.contains(head)){
			for(int i = 0; i < this.e.size(); i++){
				if(this.e.get(i).getTail().equals(tail) &&
						this.e.get(i).getHead().equals(head)){
					return true;
				}
			}
		}
		return false;
	}
	
	/* Overrides */
	@Override
	public boolean equals(Object o){
		if(o instanceof Graph){
			if(this.e.equals(((Graph) o).getEdges()) && this.v.equals(((Graph) o).getVertices())){
				return true;
			}
		}
		return false;
	}
	
	/* Setter/Adder */
	public void setE(ArrayList<Edge> e){
		this.e = new ArrayList<Edge>();
		for(int i = 0; i < e.size(); i++){
			addEdge(e.get(i));
		}
	}
	
	public void setV(ArrayList<Vertex> v){
		this.v = new ArrayList<Vertex>();
		for(int i = 0; i < v.size(); i++){
			addVertex(v.get(i));
		}
	}
	
	public void addEdge(Edge e){
		if(!this.e.contains(e)){
			this.e.add(e);
			if(!this.v.contains(e.getHead())){
				this.v.add(e.getHead());
			}
			if(!this.v.contains(e.getTail())){
				this.v.add(e.getTail());
			}
		} else {
			return;
		}
	}
	
	public void addVertex(Vertex v){
		if(!this.v.contains(v)){
			this.v.add(v);
		}
	}
	
	/* Getter */
	public ArrayList<Edge> getEdges(){
		return this.e;
	}
	
	public Edge getEdge(int i){
		return this.e.get(i);
	}
	
	public ArrayList<Vertex> getVertices(){
		return this.v;
	}
	
	public Vertex getVertex(int i){
		return this.v.get(i);
	}
}
