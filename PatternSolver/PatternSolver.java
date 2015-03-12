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
		char left;
		char right;
		char top;
		char down;
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
		/*gehe durch Pattern und stelle Adjmtrx auf*/
		for(int i=0;i<p.getM();i++) {
			for(int j=0;j<p.getN();j++) {
				if(pattern[i][j]=='.') {
					//tue nichts
				} else {
					if(i!=0) {
						left=pattern[i-1][j];
					} else {
						left= '.';
					}
					if (j!=0) {
						top=pattern[i][j-1];
					} else {
						top='.';
					}
					if (i!=p.getM()-1) {
						right=pattern[i+1][j];
					} else{
						right='.';
					}
					if (j!=p.getN()-1) {
						down=pattern[i][j+1];
					} else {
						down='.';
					}
					if(pattern[i][j]=='B') {
						//Erster Fall
						if(left=='W'&&right=='W') {
							//nicht a impliziert c
							adjMtrx[(p.getM()-1)*2*(j)+(i-1)*2+1][(p.getM()-1)*2*(j)+(i)*2]=true;
							//a impliziert nicht c
							adjMtrx[(p.getM()-1)*2*(j)+(i-1)*2][(p.getM()-1)*2*(j)+(i)*2+1]=true;
						}
					}
				}
			}
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
