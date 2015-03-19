package PieceItTogether.SATSolver;


public class SATSolver {
	
	//Im bool-array sind a und nicht a immer nebeneinander angeordnet
	public boolean solve(Vertex[] adjList){
		adjList= transitiveClosure(adjList);
		for(int i=0;i<adjList.length;i=i+2) {			//gehe durch alle "nichtnegierten Listen"
			int j=0;
			while(adjList[i].getEdgesTo().get(j)!=null) {	//i-te Liste
				if(adjList[i].getEdgesTo().get(j).equals(adjList[i+1])) {	//schaue, ob Implikation zu negierter Liste
					int k=0;
					while(adjList[i+1].getEdgesTo().get(k)!=null) {			//falls ja , gehe durch negierte Liste
						if(adjList[i+1].getEdgesTo().get(k).equals(adjList[i])) {
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
	
	
	public Vertex[] transitiveClosure(Vertex[] graph) {
		int i=0;
		int iTemp=0;
		do	{
			i=0;
			for(int j=0;j<graph.length;j++) {		//gehe durch graphen
				int k=0;
				while(graph[j].getEdgesTo().get(k)!=null) {	//für alle nachfolgeknoten
					int l=0;
					while(graph[j].getEdgesTo().get(k).getEdgesTo().get(l)!=null) {	//fuege dessen nachfolgeknoten ein
						int m=0;
						while(graph[j].getEdgesTo().get(m)!=null) {	//TODO: bessere loesung fuer die Ueberpruefung,
							//ob sich etwas geaendert hat
							if(graph[j].getEdgesTo().get(m).equals(graph[j].getEdgesTo().get(k).getEdgesTo().get(l))) {	//falls edge bereits vorhanden itemp=0
								iTemp=0;
								break;
							} else {			//sonst iTemp=1 (es hat sich etwas verändert)
								iTemp=1;
							}
						}
						graph[j].addOutgoingEdge(graph[l]);	//setze neue Kante
					}
					if(iTemp!=0) {			//Ist das noetig? Wollte vermeiden, dass i oben ueberschrieben wird
						i=1;
					}
				}
			}
		} while(i!=0);				//Iterationen solange sich etwas veraendert
		return graph;
	}
}
