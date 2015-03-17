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
							//Erster Fall: 4 schwarze Nachbarn
							if(left == 'B' && right == 'B' && top == 'B' && down == 'B'){
							//(nicht a oder nicht b) und (nicht b oder nicht c) und (nicht a oder nicht c) und (nicht d oder nicht b) und (nicht d oder nicht a) und (nicht d oder nicht c)
								//(a impliziert nicht b)
								adjMtrx[a][nb] = true;
								//(b impliziert nicht a)
								adjMtrx[b][na] = true;
								//(b impliziert nicht c)
								adjMtrx[b][nc] = true;
								//(c impliziert nicht b)
								adjMtrx[c][nb] = true;
								//(a impliziert nicht c)
								adjMtrx[a][nc] = true;
								//(c impliziert nicht a)
								adjMtrx[c][na] = true;
								//(d impliziert nicht a)
								adjMtrx[d][na] = true;
								//(a impliziert nicht d)
								adjMtrx[a][nd] = true;
								//(d impliziert nicht b)
								adjMtrx[d][nb] = true;
								//(b impliziert nicht d)
								adjMtrx[b][nd] = true;
								//(d impliziert nicht c)
								adjMtrx[d][nc] = true;
								//(c impliziert nicht d)
								adjMtrx[c][nd] = true;
							}
							//Zweiter Fall: 3 schwarze Nachbarn
							else if((left == 'B' && top == 'B' && right == 'B') ||
									(down == 'B' && top == 'B' && right == 'B') ||
									(down == 'B' && left == 'B' && right == 'B') ||
									(down == 'B' && left == 'B' && top == 'B')){
								if(left == 'W' || left == '.'){
								//(nicht a oder nicht b) und (nicht b oder nicht c) und (nicht c oder nicht a) [und (nicht d oder nicht d)]
									//(a impliziert nicht b)
									adjMtrx[a][nb] = true;
									//(b impliziert nicht a)
									adjMtrx[b][na] = true;
									//(b impliziert nicht c)
									adjMtrx[b][nc] = true;
									//(c impliziert nicht b)
									adjMtrx[c][nb] = true;
									//(a impliziert nicht c)
									adjMtrx[a][nc] = true;
									//(c impliziert nicht a)
									adjMtrx[c][na] = true;
									if(left == 'W'){
										//(d impliziert nicht d)
										adjMtrx[d][nd] = true;
									}
								}else if(right == 'W' || right == '.'){
									//(nicht a oder nicht b) und (nicht b oder nicht d) und (nicht d oder nicht a) [und (nicht c oder nicht c)]
									//(a impliziert nicht b)
									adjMtrx[a][nb] = true;
									//(b impliziert nicht a)
									adjMtrx[b][na] = true;
									//(b impliziert nicht d)
									adjMtrx[b][nd] = true;
									//(d impliziert nicht b)
									adjMtrx[d][nb] = true;
									//(a impliziert nicht d)
									adjMtrx[a][nd] = true;
									//(d impliziert nicht a)
									adjMtrx[d][na] = true;
									if(right == 'W'){
										//(c impliziert nicht c)
										adjMtrx[c][nc] = true;
									}
								}else if(top == 'W' || top == '.'){
									//(nicht a oder nicht d) und (nicht d oder nicht c) und (nicht c oder nicht a) [und (nicht b oder nicht b)]
									//(a impliziert nicht d)
									adjMtrx[a][nd] = true;
									//(d impliziert nicht a)
									adjMtrx[d][na] = true;
									//(d impliziert nicht c)
									adjMtrx[d][nc] = true;
									//(c impliziert nicht d)
									adjMtrx[c][nd] = true;
									//(a impliziert nicht c)
									adjMtrx[a][nc] = true;
									//(c impliziert nicht a)
									adjMtrx[c][na] = true;
									if(top == 'W'){
										//(b impliziert nicht b)
										adjMtrx[b][nb] = true;
									}
								}else if(down == 'W' || down == '.'){
									//(nicht d oder nicht b) und (nicht b oder nicht c) und (nicht c oder nicht d) [und (nicht a oder nicht a)]
									//(d impliziert nicht b)
									adjMtrx[d][nb] = true;
									//(b impliziert nicht d)
									adjMtrx[b][nd] = true;
									//(b impliziert nicht c)
									adjMtrx[b][nc] = true;
									//(c impliziert nicht b)
									adjMtrx[c][nb] = true;
									//(d impliziert nicht c)
									adjMtrx[d][nc] = true;
									//(c impliziert nicht d)
									adjMtrx[c][nd] = true;
									if(top == 'W'){
										//(a impliziert nicht a)
										adjMtrx[a][na] = true;
									}
								}
							}
							//Dritter Fall: 2 schwarze Nachbarn
							else if(right == 'B' && down == 'B'){
							//(c oder d) und (nicht c oder nicht d) [und (nicht a oder nicht a)] [und (nicht b oder nicht b)]
								//(nicht c impliziert d)
								adjMtrx[nc][d] = true;
								//(nicht d impliziert c)
								adjMtrx[nd][c] = true;
								//(c impliziert nicht d)
								adjMtrx[c][nd] = true;
								//(d impliziert nicht c)
								adjMtrx[d][nc] = true;
								if(left == 'W'){
									//(a impliziert nicht a)
									adjMtrx[a][na] = true;
								}
								if(top == 'W'){
									//(b impliziert nicht b)
									adjMtrx[b][nb] = true;
								}
							}
							else if(top == 'B' && down == 'B'){
							//(b oder d) und (nicht b oder nicht d) [und (nicht a oder nicht a)] [und (nicht c oder nicht c)]
								//(nicht b impliziert d)
								adjMtrx[nb][d] = true;
								//(nicht d impliziert b)
								adjMtrx[nd][b] = true;
								//(b impliziert nicht d)
								adjMtrx[b][nd] = true;
								//(d impliziert nicht b)
								if(left == 'W'){
									//(a impliziert nicht a)
									adjMtrx[a][na] = true;
								}
								if(top == 'W'){
									//(c impliziert nicht c)
									adjMtrx[c][nc] = true;
								}	
							}
							else if(top == 'B' && right == 'B'){
							//(c oder b) und (nicht c oder nicht b) [und (nicht a oder nicht a)] [und (nicht d oder nicht d)]
								//(nicht c impliziert b)
								adjMtrx[nc][b] = true;
								//(nicht b impliziert c)
								adjMtrx[nb][c] = true;
								//(c impliziert nicht b)
								adjMtrx[c][nb] = true;
								//(b impliziert nicht c)
								adjMtrx[b][nc] = true;
								if(left == 'W'){
									//(a impliziert nicht a)
									adjMtrx[a][na] = true;
								}
								if(top == 'W'){
									//(d impliziert nicht d)
									adjMtrx[d][nd] = true;
								}	
							}
							else if(left == 'B' &&  down == 'B'){
							//(a oder d) und (nicht a oder nicht d) [und (nicht b oder nicht b)] [und (nicht c oder nicht c)]
								//(nicht a impliziert d)
								adjMtrx[na][d] = true;
								//(nicht d impliziert a)
								adjMtrx[nd][a] = true;
								//(a impliziert nicht d)
								adjMtrx[a][nd] = true;
								//(d impliziert nicht a)
								adjMtrx[d][na] = true;
								if(left == 'W'){
									//(b impliziert nicht b)
									adjMtrx[b][nb] = true;
								}
								if(top == 'W'){
									//(c impliziert nicht c)
									adjMtrx[c][nc] = true;
								}
							}
							else if(left == 'B' && right == 'B'){
							//(a oder c) und (nicht a oder nicht c) [und (nicht b oder nicht b)] [und (nicht d oder nicht d)]
								//(nicht a impliziert c)
								adjMtrx[na][c] = true;
								//(nicht c impliziert a)
								adjMtrx[nc][a] = true;
								//(a impliziert nicht c)
								adjMtrx[a][nc] = true;
								//(c impliziert nicht a)
								adjMtrx[c][na] = true;
								if(left == 'W'){
									//(b impliziert nicht b)
									adjMtrx[b][nb] = true;
								}
								if(top == 'W'){
									//(d impliziert nicht d)
									adjMtrx[d][nd] = true;
								}
							}
							else if(left == 'B' && top == 'B'){
							//(a oder b) und (nicht a oder nicht b) [und (nicht c oder nicht c)] [und (nicht d oder nicht d)]
								//(nicht a impliziert b)
								adjMtrx[na][b] = true;
								//(nicht b impliziert a)
								adjMtrx[nb][a] = true;
								//(a impliziert nicht b)
								adjMtrx[a][nb] = true;
								//(b impliziert nicht a)
								adjMtrx[b][na] = true;
								if(left == 'W'){
									//(c impliziert nicht c)
									adjMtrx[c][nc] = true;
								}
								if(top == 'W'){
									//(d impliziert nicht d)
									adjMtrx[d][nd] = true;
								}
							}
							//Vierter Fall: 1 schwarzer Nachbar
							else if(left == 'B'){
							//(a oder a) [und (nicht b oder nicht b)] [und (nicht c oder nicht c)] [und (nicht d oder nicht d)]
								//(nicht a impliziert a)
								adjMtrx[na][a] = true;
								if(top == 'W'){
									//(b impliziert nicht b)
									adjMtrx[b][nb] = true;
								}
								if(right == 'W'){
									//(c impliziert nicht c)
									adjMtrx[c][nc] = true;
								}
								if(down == 'W'){
									//(d impliziert nicht d)
									adjMtrx[d][nd] = true;
								}
							}
							else if(right == 'B'){
							//(c oder c) [und (nicht b oder nicht b)] [und (nicht a oder nicht a)] [und (nicht d oder nicht d)]
								//(nicht c impliziert c)
								adjMtrx[nc][c] = true;
								if(top == 'W'){
									//(b impliziert nicht b)
									adjMtrx[b][nb] = true;
								}
								if(right == 'W'){
									//(a impliziert nicht a)
									adjMtrx[a][na] = true;
								}
								if(down == 'W'){
									//(d impliziert nicht d)
									adjMtrx[d][nd] = true;
								}	
							}
							else if(top == 'B'){
							//(b oder b) [und (nicht a oder nicht a)] [und (nicht c oder nicht c)] [und (nicht d oder nicht d)]
								//(nicht b impliziert b)
								adjMtrx[nb][b] = true;
								if(top == 'W'){
									//(a impliziert nicht a)
									adjMtrx[a][na] = true;
								}
								if(right == 'W'){
									//(c impliziert nicht c)
									adjMtrx[c][nc] = true;
								}
								if(down == 'W'){
									//(d impliziert nicht d)
									adjMtrx[d][nd] = true;
								}
							}
							else if(down == 'B'){
							//(d oder d) [und (nicht b oder nicht b)] [und (nicht c oder nicht c)] [und (nicht a oder nicht a)]
								//(nicht d impliziert d)
								adjMtrx[nd][d] = true;
								if(top == 'W'){
									//(b impliziert nicht b)
									adjMtrx[b][nb] = true;
								}
								if(right == 'W'){
									//(c impliziert nicht c)
									adjMtrx[c][nc] = true;
								}
								if(down == 'W'){
									//(a impliziert nicht a)
									adjMtrx[a][na] = true;
								}
							}
							//Fünfter Fall: 0 schwarze Nachbarn
							else{
								return falseSatInstance();
							}
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
