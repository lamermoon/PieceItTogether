package PieceItTogether.SATSolver;

public class SATSolver {
	
	//Im bool-array sind a und nicht a immer nebeneinander angeordnet
	public boolean solve(Graph graph){
		graph = transitiveClosureNew(graph);
		for(int i = 0; i < graph.getVertices().length; i = i+2) {									//gehe durch alle "nichtnegierten Listen"
			for(int j = 0; j < graph.getVertices()[i].getOutgoingEdges().size(); j++) {
				
				if(graph.getVertices()[i].getOutgoingEdges().get(j).equals(graph.getVertices()[i+1])) {			//schaue, ob Implikation zu negierter Liste
					for(int k = 0; k < graph.getVertices()[i+1].getOutgoingEdges().size(); k++) {	//falls ja , gehe durch negierte Liste
						if(graph.getVertices()[i+1].getOutgoingEdges().get(k).equals(graph.getVertices()[i])) {
							return false;												//falls dort Implikation zu nichtnegierter dann Cycle also false
						}
					}																	//falls in erster while-Schleife bereits negierte Liste gefunden wurde,
					break;																//jedoch nicht umgekehrt, ist jede weitere Untersuchung dieses Listenpaars unnoetig
				}
			}
		}
		return true; 																	//sonst true
	}
	
	private Vertex[] transitiveClosure(Vertex[] graph){
		//Naiver Algo
		boolean changed;
		do{
			changed = false;
			for(int v = 0; v < graph.length; v++){
				for(int i = 0; i < graph[v].getIncomingEdges().size(); i++){
					for(int j = 0; j < graph[v].getOutgoingEdges().size(); j++){
						if(graph[v].getIncomingEdge(i).getOutgoingEdges().contains(graph[v].getOutgoingEdge(j))){
							break;
						}else{
							graph[v].getIncomingEdge(i).addOutgoingEdge(graph[v].getOutgoingEdge(j));
							changed = true;
						}
					}
				}
			}
		}while(changed);
		return graph;
	}
	private Graph transitiveClosureNew(Graph e){
		//Logarithmischer Algo
		Graph deltaE = new Graph(e);
		Graph eX = new Graph(e);
		while(!eX.isEmpty()) {
			eX= new Graph(e.getVertices().length);
			Graph deltaENew= new Graph(e.getVertices().length);
			for(int i=0; i<deltaE.getVertices().length;i++) {
				for(int v=0; deltaE.getVertices()[i].getOutgoingEdge(v)!=null;v++) { 
					for(int j=0; deltaE.getVertices()[i].getOutgoingEdge(v).getOutgoingEdge(j)!=null;j++) {
						deltaENew.addOutEdge(deltaENew.getVertices()[i], deltaENew.getVertices()[deltaE.getVertices()[i].getOutgoingEdge(v).getOutgoingEdge(j).getID()]);
						if(!e.directConnection(e.getVertices()[i], e.getVertices()[j])) {
							eX.addOutEdge(eX.getVertices()[i], eX.getVertices()[j]);
						}
					}
				}
			}
			deltaE = deltaENew;
			Graph eNew= new Graph(e);
			for(int i=0;i<e.getVertices().length;i++) {
				for(int v=0;e.getVertices()[i].getOutgoingEdge(v)!=null;v++) {
					for(int j=0;deltaE.getVertices()[e.getVertices()[i].getOutgoingEdge(v).getID()].getOutgoingEdge(j)!=null;j++) {
						if(!eNew.directConnection(eNew.getVertices()[i], eNew.getVertices()[j])) {
							eNew.addOutEdge(eNew.getVertices()[i], eNew.getVertices()[j]);							
						}
					}
				}
			}
			e.uniteGraphs(eNew, deltaE);
		}
		return e;
	}
}
