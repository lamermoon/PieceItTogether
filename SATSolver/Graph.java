package PieceItTogether.SATSolver;

public class Graph {
	private Vertex[] v;
	private boolean empty;
	
	/* Konstruktor */
	//neuer leerer Graph
	public Graph(int n){
		v = new Vertex[n];
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
			for(int j=0;y[i].getOutgoingEdge(j)!=null;j++) {
				if(!a.directConnection(x[y[i].getID()], x[y[i].getOutgoingEdge(j).getID()])) {
					x[y[i].getID()].addOutgoingEdge(x[y[i].getOutgoingEdge(j).getID()]);
				}
			}
		}
		Graph c = new Graph(a);
		return c;
	}
}
