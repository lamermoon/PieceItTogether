package PieceItTogether.SATSolver;

public class SATSolver {
	
	//Im bool-array sind a und nicht a immer nebeneinander angeordnet
	public boolean solve(Vertex[] adjList){
		adjList= transitiveClosure(adjList);
		for(int i=0;i<adjList.length;i=i+2) {			//gehe durch alle "nichtnegierten Listen"
			int j=0;
			while(adjList[i].getOutgoingEdges().get(j)!=null) {	//i-te Liste
				if(adjList[i].getOutgoingEdges().get(j).equals(adjList[i+1])) {	//schaue, ob Implikation zu negierter Liste
					int k=0;
					while(adjList[i+1].getOutgoingEdges().get(k)!=null) {			//falls ja , gehe durch negierte Liste
						if(adjList[i+1].getOutgoingEdges().get(k).equals(adjList[i])) {
							return false;				//falls dort Implikation zu nichtnegierter dann Cycle also false
						}
						k++;
					}
					break;							//falls in erster while-Schleife bereits negierte Liste gefunden wurde,
													//jedoch nicht umgekehrt, ist jede weitere Untersuchung dieses Listenpaars unnoetig
				}
				j++;
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
	
	public Vertex[] transitiveClosure(Vertex[] graph) {
		boolean changed = false;
		boolean changedTemp = false;
		do	{
			changed=false;
			for(int j=0;j<graph.length;j++) {		//gehe durch graphen
				for(int k=0;graph[j].getOutgoingEdges().get(k)!=null;k++) {	//f�r alle nachfolgeknoten
					for(int l=0;graph[j].getOutgoingEdges().get(k).getOutgoingEdges().get(l)!=null;l++) {	//fuege dessen nachfolgeknoten ein
						for(int m=0;graph[j].getOutgoingEdges().get(m)!=null;m++) {	//TODO: bessere loesung fuer die Ueberpruefung,
							//ob sich etwas geaendert hat
							if(graph[j].getOutgoingEdges().get(m).equals(graph[j].getOutgoingEdges().get(k).getOutgoingEdges().get(l))) {	//falls edge bereits vorhanden itemp=0
								changedTemp=false;
								break;
							} else {			//sonst iTemp=1 (es hat sich etwas ver�ndert)
								changedTemp=true;
							}
						}
						graph[j].addOutgoingEdge(graph[l]);	//setze neue Kante
						if  (changedTemp) {
							changed=true;
						}
					}
				}
			}
		} while(changed);				//Iterationen solange sich etwas veraendert
		return graph;
	}
}
