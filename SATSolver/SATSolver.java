package PieceItTogether.SATSolver;

public class SATSolver {
	
	//Im bool-array sind a und nicht a immer nebeneinander angeordnet
	public boolean solve(Vertex[] adjList){
		adjList = transitiveClosure(adjList);
		for(int i = 0; i < adjList.length; i = i+2) {									//gehe durch alle "nichtnegierten Listen"
			for(int j = 0; j < adjList[i].getOutgoingEdges().size(); j++) {
				
				if(adjList[i].getOutgoingEdges().get(j).equals(adjList[i+1])) {			//schaue, ob Implikation zu negierter Liste
					for(int k = 0; k < adjList[i+1].getOutgoingEdges().size(); k++) {	//falls ja , gehe durch negierte Liste
						if(adjList[i+1].getOutgoingEdges().get(k).equals(adjList[i])) {
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
}
