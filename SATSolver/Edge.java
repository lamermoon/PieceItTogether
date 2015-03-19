package PieceItTogether.SATSolver;

public class Edge {
	private final int id;
	private Vertex tail;
	private Vertex head;
	
	/* Konstruktor */
	public Edge(int id, Vertex tail, Vertex head){
		this.id = id;
		this.tail = tail;
		this.head = head;
		this.tail.addOutgoingEdge(this.head);
		this.head.addIncomingEdge(this.tail);
	}
	
	/* Overrides */
	@Override
	public boolean equals(Object o){
		if(o instanceof Edge){
			if(this.id == ((Edge) o).getID() &&
					this.tail.equals(((Edge) o).getTail()) &&
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
	public int getID(){
		return this.id;
	}
	
	public Vertex getHead(){
		return this.head;
	}
	
	public Vertex getTail(){
		return this.tail;
	}

}
