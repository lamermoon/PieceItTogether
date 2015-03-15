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
			/* alle Felder werden mit false initialisiert */
			for(int i = 0; i < adjMtrx.length; i++){
				for(int j = 0; j < adjMtrx.length; j++){	
					adjMtrx[i][j] = false;
				}
			}
	/* *************************************************************************************************************************************************
	 * ************************************************** REDUKTION BEGINN *****************************************************************************
	 * ************************************************************************************************************************************************* */
		/*gehe durch Pattern und stelle Adjmtrx auf*/
			for(int i = 0; i < p.getN(); i++) {
				for(int j = 0; j < p.getM(); j++) {
					if(pattern[i][j] == 'W' || pattern[i][j] == 'B') {
						if(i != 0) {					//Wenn pattern[i][j] nicht am linken Rand liegt,
							top = pattern[i-1][j];		// gibt es ein Feld links daneben.
						} else { top = '.'; }
						
						if (j != 0) {					//Wenn pattern[i][j] nicht am oberen rand liegt,
							left = pattern[i][j-1];		// gibt es ein Feld darueber.
						} else { left = '.'; }
						
						if (i != p.getN()-1) {			//Wenn pattern[i][j] nicht am rechten Rand liegt,
							down = pattern[i+1][j];	// gibt es ein Feld rechts daneben.
						} else{ down = '.'; }
						
						if (j != p.getM()-1) {			//Wenn pattern[i][j] nicht am unteren Rand liegt,
							right = pattern[i][j+1];		// gibt es ein Feld darunter.
						} else { right = '.'; }
					
					int a = 2*((p.getM()-1)*i + j - 1);
					int na = a + 1;
					int c = 2*((p.getM()-1)*i + j);
					int nc = c + 1;
					int b = 2*((p.getM()-1)*p.getN() + (p.getN()-1)*j + i - 1);
					int nb = b + 1;
					int d = 2*((p.getM()-1)*p.getN() + (p.getN()-1)*j + i);
					int nd = d + 1;
					/* Wenn das aktuelle Feld ein schwarzes Feld ist: */
						if(pattern[i][j] == 'B') {
							//Erster Fall - Left und Right weiss
							if(left == 'W' && right == 'W') {
							//(a oder c) und (nicht a oder nicht c)
								//nicht a impliziert c
								adjMtrx[na][c] = true;
								//nicht c impliziert a
								adjMtrx[nc][a] = true;
								//a impliziert nicht c
								adjMtrx[a][nc] = true;
								//c impliziert nicht a
								adjMtrx[c][na] = true;
							} //Zweiter Fall - Left und Right nicht weiss 
							else if(left != 'W' && right != 'W') {
								//Pattern nicht erfuellbar
								return falseSatInstance();
							} //Dritter Fall - Left weiss, right nicht
							else if(left == 'W' && right != 'W') {
							//(a oder a)
								//nicht a impliziert a
								adjMtrx[na][a] = true;
							} //Vierter Fall - Left nicht weiss, right weiss
							else if(left != 'W' && right == 'W') {
							//(c oder c)
								//nicht c impliziert c
								adjMtrx[nc][c] = true;
							} 
							//Fuenfter Fall - Top und down weiss
							//Neue Betrachtung! Kein else if!
							if(top == 'W' && down == 'W') {
							//(b oder d) und (nicht b oder nicht d)
								//nicht b impliziert d
								adjMtrx[nb][d] = true;
								//nicht d impliziert b
								adjMtrx[nd][b] = true;
								//b impliziert nicht d
								adjMtrx[b][nd] = true;
								//d impliziert nicht b
								adjMtrx[d][nb] = true;
							} //Sechster Fall - Top und Down nicht weiss
							else if(top != 'W' && down != 'W') {
								//Pattern nicht erfuellbar
								return falseSatInstance();
							} //Siebter Fall - Top weiss und down nicht
							else if(top == 'W' && down != 'W') {
							//(b oder b)
								//nicht b impliziert b
								adjMtrx[nb][b] = true;
							} //Achter Fall - Top nicht weiss, down weiss
							else if(top != 'W' && down == 'W') {
							//(d oder d)
								//nicht d impliziert d
								adjMtrx[nd][d] = true;
							}
							
					/* Wenn das aktuelle Feld ein weisses Feld ist: */
						} else {
							//TODO: Faelle abarbeiten
						}
					}
					
				}
			}
	/* *************************************************************************************************************************************************
	 * ************************************************** REDUKTION ENDE *******************************************************************************
	 * ************************************************************************************************************************************************* */

/* Sonst nicht */	
		} else {
			return falseSatInstance();
		}
		return adjMtrx;
	}
	
	private boolean[][] falseSatInstance(){
		boolean[] a = {true, true};					//Es wird einfach eine AdjMtrx
		boolean[] b = {true, true};					// aufgestellt, die definitiv
		boolean[][] adjMtrx = new boolean[2][2];	// einen Cycle enthaelt
		adjMtrx[0] = a;								// und somit auf jeden Fall
		adjMtrx[1] = b;								// eine Nein-Instanz
		return adjMtrx;								// des SATSolvers ist.
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
