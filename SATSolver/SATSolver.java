package PieceItTogether.SATSolver;

public class SATSolver {
	
	//Im bool-array sind a und nicht a immer nebeneinander angeordnet
	public boolean solve(Vertex[] adjList){
		adjList= transitiveClosure(adjList);
		for(int i=0;i<adjList.length;i=i+2) {			//gehe durch alle "nichtnegierten Listen"
			for(int j = 0; j < adjList[i].getOutgoingEdges().size(); j++) {	//i-te Liste
				if(adjList[i].getOutgoingEdges().get(j).equals(adjList[i+1])) {	//schaue, ob Implikation zu negierter Liste
					for(int k = 0; k < adjList[i+1].getOutgoingEdges().size(); k++) {			//falls ja , gehe durch negierte Liste
						if(adjList[i+1].getOutgoingEdges().get(k).equals(adjList[i])) {
							return false;				//falls dort Implikation zu nichtnegierter dann Cycle also false
						}
					}
					break;							//falls in erster while-Schleife bereits negierte Liste gefunden wurde,
													//jedoch nicht umgekehrt, ist jede weitere Untersuchung dieses Listenpaars unnoetig
				}
			}
		}
		return true;							//sonst true
	}
	
	public boolean solve(Graph g){
		Graph tcg = g.getTransitiveClosure();
		for(int i = 0; i < tcg.getEdges().size(); i=i+2){
			Vertex a = tcg.getVertex(i);
			Vertex na = tcg.getVertex(i+1);
			if(a.getOutgoingEdges().contains(na) && na.getOutgoingEdges().contains(a)){
				return false;
			}
		}
		return true;
	}
	
	public Vertex[] transitiveClosure(Vertex[] graph){
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
	
	/*
	public Vertex[] transitiveClosure(Vertex[] graph) {
		boolean changed = false;
		boolean changedTemp = false;
		do	{
			changed = false;
			for(int j = 0; j < graph.length; j++) {		//gehe durch graphen
				for(int k = 0; k < graph[j].getOutgoingEdges().size(); k++) {	//für alle nachfolgeknoten
					for(int l = 0; l < graph[j].getOutgoingEdges().get(k).getOutgoingEdges().size(); l++) {	//fuege dessen nachfolgeknoten ein
						for(int m = 0; m < graph[j].getOutgoingEdges().size(); m++) {	//TODO: bessere loesung fuer die Ueberpruefung,
							//ob sich etwas geaendert hat
							if(graph[j].getOutgoingEdges().get(m).equals(graph[j].getOutgoingEdges().get(k).getOutgoingEdges().get(l))) {	//falls edge bereits vorhanden itemp=0
								changedTemp = false;
								break;
							} else {			//sonst iTemp=1 (es hat sich etwas verändert)
								changedTemp = true;
							}
						}
						graph[j].addOutgoingEdge(graph[j].getOutgoingEdges().get(k).getOutgoingEdges().get(l));	//setze neue Kante
						if  (changedTemp) {
							changed = true;
						}
					}
				}
			}
		} while(changed);				//Iterationen solange sich etwas veraendert
		return graph;
	}
	*/
}
