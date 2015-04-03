package PieceItTogether.SATSolver;

public class Edge {
	private Vertex tail;
	private Vertex head;
	
	/* Konstruktor */
	public Edge(Vertex tail, Vertex head){
		this.tail = tail;
		this.head = head;
		this.tail.addOutgoingEdge(this.head);
		this.head.addIncomingEdge(this.tail);
	}
	
	/* Overrides */
	@Override
	public boolean equals(Object o){
		if(o instanceof Edge){
			if(this.tail.equals(((Edge) o).getTail()) &&
					this.head.equals(((Edge) o).getHead())){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString(){
		String s = "("+this.tail.toString()+","+this.head.toString()+")\n";
		return s;
	}
	
	/* Getter */	
	public Vertex getHead(){
		return this.head;
	}
	
	public Vertex getTail(){
		return this.tail;
	}

}
