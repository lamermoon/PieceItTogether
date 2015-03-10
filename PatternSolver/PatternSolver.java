package PieceItTogether.PatternSolver;

import java.util.ArrayList;
import PieceItTogether.SATSolver.SATSolver;

public class PatternSolver {
	final ArrayList<Pattern> cases = new ArrayList<Pattern>(); 
	
	public boolean[] solve(){
		boolean[] output = new boolean[cases.size()];
		SATSolver sat = new SATSolver();
		for(int i = 0; i < cases.size(); i++){
			boolean[][] adjMtrx = reduceTo2Sat(cases.get(i));
			output[i] = sat.solve(adjMtrx);
		}
		return output;
	}
	
	private boolean[][] reduceTo2Sat(Pattern p){
		int black = 0;
		int white = 0;
		char[][] pattern = p.getPattern();
	/* Zaehle Anzahl der schwarzen und weissen Felder */
		for(int i = 0; i < p.getN(); i++){
			for(int j = 0; j < p.getM(); j++){
				if(pattern[i][j] == 'W'){
					white++;
				}else if(pattern[i][j] == 'B'){
					black++;
				}
			}
		}
		boolean[][] adjMtrx;
	/* Wenn doppelt so viele weisse wie schwarze Felder da sind, ist eine Loesung moeglich */
		if(2*black == white){
			
			adjMtrx = new boolean[2*(p.getN()*(p.getM()-1)+(p.getN()-1)*p.getM())][2*(p.getN()*(p.getM()-1)+(p.getN()-1)*p.getM())]; //2*(p.getN()*(p.getM()-1)+(p.getN()-1)*p.getM()) ergibt die Anzahl der Kanten im Pattern
			
			for(int i = 0; i < adjMtrx.length; i++){
				for(int j = 0; j < adjMtrx.length; j++){
					if(adjMtrx[i][j]){
						//hier passiert nichts
					}else{
						adjMtrx[i][j] = false;
					}
				}
			}
	/* Sonst nicht */	
		} else {
			boolean[] a = {true, true};
			boolean[] b = {true, true};
			adjMtrx = new boolean[2][2];
			adjMtrx[0] = a;
			adjMtrx[1] = b;
		}
		return adjMtrx;
	}
	
	public void addPattern(Pattern p){
		this.cases.add(p);
	}
	
	public String toString(){
		String s = "";
		for(int i = 0; i < cases.size(); i++){
			s += "Pattern "+(i+1)+":\n"+cases.get(i).toString();
		}
		return s;
	}
}
