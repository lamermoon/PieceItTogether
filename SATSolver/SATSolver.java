package PieceItTogether.SATSolver;

public class SATSolver {
	
	
	public boolean solve(){
		
		return true;
	}
	
	public boolean[][] transitiveClosure(boolean[][] graph) {
		for (int v=0; v<graph.length; v++)
			graph[v][v] = true;
		for (int v=0; v<graph.length; v++) {
			for (int i=0; i<graph.length; i++) {
				for (int j=0; j<graph.length; j++) {
					graph[i][j]= graph[i][j] || (graph[i][v]&&graph[v][i]);
				}
					
			}
		}
		return graph;
	}
}
