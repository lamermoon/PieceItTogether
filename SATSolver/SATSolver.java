package PieceItTogether.SATSolver;

import PieceItTogether.Node;

public class SATSolver {
	
	//Im bool-array sind a und nicht a immer nebeneinander angeordnet
	public boolean solve(Node[] adjList){
		adjList= transitiveClosure(adjList);
		for(int i=0;i<adjList.length;i=i+2) {			//gehe durch alle "nichtnegierten Listen"
			int j=0;
			while(adjList[i].getEdge(j)!=null) {	//erste Liste
				if(adjList[i].getEdge(j).equals(adjList[i+1])) {	//schaue, ob Implikation zu negierter Liste
					int k=0;
					while(adjList[i+1].getEdge(k)!=null) {			//falls ja , gehe durch negierte Liste
						if(adjList[i+1].getEdge(k).equals(adjList[i])) {
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
		
	public Node[] transitiveClosure(Node[] graph) {

		return graph;
	}
}
