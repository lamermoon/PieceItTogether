package PieceItTogether.SATSolver;

public class Graph {
	private Vertex[] v;
	private boolean empty;
	
	/* Konstruktor */
	//neuer leerer Graph
	public Graph(int n){
		v = new Vertex[n];
		for(int i = 0; i < v.length; i++){
			v[i] = new Vertex(i);
		}
		empty = true;
	}
	//Kopie
	public Graph(Graph g) {
		this.v = g.getVertices();
		empty = g.isEmpty();
	}
	
	//Getter
	public boolean isEmpty() {
		return this.empty;
	}
	
	public Vertex[] getVertices() {
		return this.v;
	}
	
	//Setter/Adder
	public void notEmpty() {
		empty=false;
	}
	public void addOutEdge(Vertex a, Vertex b) {
		if(isEmpty()) {
			notEmpty();
		}
		a.addOutgoingEdge(b);
	}
	public void addInEdge(Vertex a, Vertex b) {
		if(isEmpty()) {
			notEmpty();
		}
		a.addIncomingEdge(b);
	}
	//Achtung: Nur zum Testen!
	public void setVertices(Vertex[] v) {
		this.v=v;
		notEmpty();
	}
	
	//Ueberpruefe ob direkte Verbindung zwischen zwei Knoten besteht
	public boolean directConnection(Vertex a, Vertex b) {
		return a.getOutgoingEdges().contains(b);
	}
	
	//Vereinige zwei Graphen
	public Graph uniteGraphs(Graph a, Graph b) {
		if(a.getVertices().length!=b.getVertices().length) {
			System.out.println("Fehler, die uebergebenen Graphen muessen gleich gross sein.");
			return a;
		}
		Vertex[] x= a.getVertices();
		Vertex[] y= b.getVertices();
		for(int i=0;i<x.length;i++) {
			for(int j=0;j<y[i].getOutgoingEdges().size();j++) {
				if(!a.directConnection(x[y[i].getID()], x[y[i].getOutgoingEdge(j).getID()])) {
					a.addOutEdge(x[y[i].getID()], x[y[i].getOutgoingEdge(j).getID()]);
				}
			}
		}
		Graph c = new Graph(a);
		return c;
	}
}
