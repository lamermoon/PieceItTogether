package PieceItTogether.SATSolver;

public class SATSolver {
	
	//Im bool-array sind a und nicht a immer nebeneinander angeordnet
	public boolean solve(boolean[][] graph){
		graph= transitiveClosure(graph);
		for(int i=0; i<graph.length;i=i+2) {	//gehe durch den graph in zweierschritten
			if(graph[i][i+1]&&graph[i+1][i]) {	//ueberpruefe, ob verbindung von a zu nicht a und vice versa
				return false;					//dann false
			}
		}
		return true;							//sonst true
	}
	
	public boolean[][] transitiveClosure(boolean[][] graph) {
		for (int v=0; v<graph.length; v++)
			graph[v][v] = true;
		for (int v=0; v<graph.length; v++) {
			for (int i=0; i<graph.length; i++) {
				for (int j=0; j<graph.length; j++) {
					graph[i][j]= graph[i][j] || (graph[i][v]&&graph[v][j]);
				}
					
			}
		}
		return graph;
	}
}
