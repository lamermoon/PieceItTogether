package PieceItTogether.SATSolver;

public class SATSolver {
	
	public boolean solve(boolean[][] adjMtrx){
		calcTransClosure(adjMtrx);
		for(int i = 0; i < adjMtrx.length; i=i+2){
			if(adjMtrx[i][i+1] && adjMtrx[i+1][i]){
				return false;
			}
		}
		return true;
		
	}
	
	private void calcTransClosure(boolean[][] adjMtrx){
		for(int i = 0; i < adjMtrx.length; i++){
			adjMtrx[i][i] = true;
		}
		for(int i = 0; i < adjMtrx.length; i++){
			for(int j = 0; j < adjMtrx.length; j++){
				for(int k = 0; k < adjMtrx.length; k++){
					adjMtrx[j][k] = adjMtrx[j][k] || (adjMtrx[j][i] && adjMtrx[i][k]);
				}
			}
		}
	}
}
