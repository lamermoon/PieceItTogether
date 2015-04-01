package PieceItTogether.PatternSolver;

import java.util.ArrayList;

import PieceItTogether.SATSolver.SATSolver;
import PieceItTogether.SATSolver.Vertex;

public class PatternSolver {
	private ArrayList<Pattern> cases = new ArrayList<Pattern>(); 
	
	public String[] solve(){
		String[] output = new String[cases.size()];
		SATSolver sat = new SATSolver();
		for(int i = 0; i < cases.size(); i++){
			if(cases.get(i).getN() == -1){
				output[i] = "**ignored**\n";
			} else {
				Vertex[] adjList = reduceTo2Sat(cases.get(i));
				boolean answer = sat.solve(adjList);
				if(answer){
					output[i] = "Yes\n";
				} else {
					output[i] = "No\n";
				}
			}
			
		}
		return output;
	}
	
	private Vertex[] reduceTo2Sat(Pattern p){
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
		
		Vertex[] adjList;
/* Wenn doppelt so viele weisse wie schwarze Felder da sind, ist eine Loesung moeglich */
		if(2*black == white){
			adjList = new Vertex[2*(p.getN()*(p.getM()-1)+(p.getN()-1)*p.getM())]; //2*(p.getN()*(p.getM()-1)+(p.getN()-1)*p.getM()) ergibt die Anzahl der Kanten im Pattern
			/* adjList initialisieren */
			for(int i = 0; i < adjList.length; i++){
				adjList[i] = new Vertex(i);
			}
	/* *************************************************************************************************************************************************
	 * ************************************************** REDUKTION BEGINN *****************************************************************************
	 * ************************************************************************************************************************************************* */
		/*gehe durch Pattern und stelle AdjList auf*/
			for(int i = 0; i < p.getN(); i++) {
				for(int j = 0; j < p.getM(); j++) {
					if(pattern[i][j] == 'W' || pattern[i][j] == 'B') {
						if(i != 0) {					//Wenn pattern[i][j] nicht am oberen Rand liegt,
							top = pattern[i-1][j];		// gibt es ein Feld darueber.
						} else { top = '.'; }
						
						if (j != 0) {					//Wenn pattern[i][j] nicht am linken rand liegt,
							left = pattern[i][j-1];		// gibt es ein Feld links daneben.
						} else { left = '.'; }
						
						if (i != p.getN()-1) {			//Wenn pattern[i][j] nicht am unteren Rand liegt,
							down = pattern[i+1][j];	// gibt es ein Feld darunter.
						} else{ down = '.'; }
						
						if (j != p.getM()-1) {			//Wenn pattern[i][j] nicht am rechten Rand liegt,
							right = pattern[i][j+1];		// gibt es ein Feld rechts daneben.
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
								adjList[na].addOutgoingEdge(adjList[c]);
								//nicht c impliziert a
								adjList[nc].addOutgoingEdge(adjList[a]);
								//a impliziert nicht c
								adjList[a].addOutgoingEdge(adjList[a]);
								//c impliziert nicht a
								adjList[c].addOutgoingEdge(adjList[na]);
							} //Zweiter Fall - Left und Right nicht weiss 
							else if(left != 'W' && right != 'W') {
								//Pattern nicht erfuellbar
								return falseSatInstance();
							} //Dritter Fall - Left weiss, right nicht
							else if(left == 'W' && right != 'W') {
							//(a oder a)
								//nicht a impliziert a
								adjList[na].addOutgoingEdge(adjList[a]);
							} //Vierter Fall - Left nicht weiss, right weiss
							else if(left != 'W' && right == 'W') {
							//(c oder c)
								//nicht c impliziert c
								adjList[nc].addOutgoingEdge(adjList[c]);
							} 
							//Fuenfter Fall - Top und down weiss
							//Neue Betrachtung! Kein else if!
							if(top == 'W' && down == 'W') {
							//(b oder d) und (nicht b oder nicht d)
								//nicht b impliziert d
								adjList[nb].addOutgoingEdge(adjList[d]);
								//nicht d impliziert b
								adjList[nd].addOutgoingEdge(adjList[b]);
								//b impliziert nicht d
								adjList[b].addOutgoingEdge(adjList[nd]);
								//d impliziert nicht b
								adjList[d].addOutgoingEdge(adjList[nb]);
							} //Sechster Fall - Top und Down nicht weiss
							else if(top != 'W' && down != 'W') {
								//Pattern nicht erfuellbar
								return falseSatInstance();
							} //Siebter Fall - Top weiss und down nicht
							else if(top == 'W' && down != 'W') {
							//(b oder b)
								//nicht b impliziert b
								adjList[nb].addOutgoingEdge(adjList[b]);
							} //Achter Fall - Top nicht weiss, down weiss
							else if(top != 'W' && down == 'W') {
							//(d oder d)
								//nicht d impliziert d
								adjList[nd].addOutgoingEdge(adjList[d]);
							}
							
					/* Wenn das aktuelle Feld ein weisses Feld ist: */
						} else {
							//Erster Fall: 4 schwarze Nachbarn
							if(left == 'B' && right == 'B' && top == 'B' && down == 'B'){
							//(nicht a oder nicht b) und (nicht b oder nicht c) und (nicht a oder nicht c) und (nicht d oder nicht b) und (nicht d oder nicht a) und (nicht d oder nicht c)
								//(a impliziert nicht b)
								adjList[a].addOutgoingEdge(adjList[nb]);
								//(b impliziert nicht a)
								adjList[b].addOutgoingEdge(adjList[na]);
								//(b impliziert nicht c)
								adjList[b].addOutgoingEdge(adjList[nc]);
								//(c impliziert nicht b)
								adjList[c].addOutgoingEdge(adjList[nb]);
								//(a impliziert nicht c)
								adjList[a].addOutgoingEdge(adjList[nc]);
								//(c impliziert nicht a)
								adjList[c].addOutgoingEdge(adjList[na]);
								//(d impliziert nicht a)
								adjList[d].addOutgoingEdge(adjList[na]);
								//(a impliziert nicht d)
								adjList[a].addOutgoingEdge(adjList[nd]);
								//(d impliziert nicht b)
								adjList[d].addOutgoingEdge(adjList[nb]);
								//(b impliziert nicht d)
								adjList[b].addOutgoingEdge(adjList[nd]);
								//(d impliziert nicht c)
								adjList[d].addOutgoingEdge(adjList[nc]);
								//(c impliziert nicht d)
								adjList[c].addOutgoingEdge(adjList[nd]);
							}
							//Zweiter Fall: 3 schwarze Nachbarn
							else if((left == 'B' && top == 'B' && right == 'B') ||
									(down == 'B' && top == 'B' && right == 'B') ||
									(down == 'B' && left == 'B' && right == 'B') ||
									(down == 'B' && left == 'B' && top == 'B')){
								if(left == 'W' || left == '.'){
								//(nicht d oder nicht b) und (nicht b oder nicht c) und (nicht c oder nicht d) [und (nicht a oder nicht a)]
									//(d impliziert nicht b)
									adjList[d].addOutgoingEdge(adjList[nb]);
									//(b impliziert nicht d)
									adjList[b].addOutgoingEdge(adjList[nd]);
									//(b impliziert nicht c)
									adjList[b].addOutgoingEdge(adjList[nc]);
									//(c impliziert nicht b)
									adjList[c].addOutgoingEdge(adjList[nb]);
									//(d impliziert nicht c)
									adjList[d].addOutgoingEdge(adjList[nc]);
									//(c impliziert nicht d)
									adjList[c].addOutgoingEdge(adjList[nd]);
									if(left == 'W'){
										//(a impliziert nicht a)
										adjList[a].addOutgoingEdge(adjList[na]);
									}
								}else if(right == 'W' || right == '.'){
								//(nicht a oder nicht b) und (nicht b oder nicht d) und (nicht d oder nicht a) [und (nicht c oder nicht c)]
									//(a impliziert nicht b)
									adjList[a].addOutgoingEdge(adjList[nb]);
									//(b impliziert nicht a)
									adjList[b].addOutgoingEdge(adjList[na]);
									//(b impliziert nicht d)
									adjList[b].addOutgoingEdge(adjList[nd]);
									//(d impliziert nicht b)
									adjList[d].addOutgoingEdge(adjList[nb]);
									//(a impliziert nicht d)
									adjList[a].addOutgoingEdge(adjList[nd]);
									//(d impliziert nicht a)
									adjList[d].addOutgoingEdge(adjList[na]);
									if(right == 'W'){
										//(c impliziert nicht c)
										adjList[c].addOutgoingEdge(adjList[nc]);
									}
								}else if(top == 'W' || top == '.'){
								//(nicht a oder nicht d) und (nicht d oder nicht c) und (nicht c oder nicht a) [und (nicht b oder nicht b)]
									//(a impliziert nicht d)
									adjList[a].addOutgoingEdge(adjList[nd]);
									//(d impliziert nicht a)
									adjList[d].addOutgoingEdge(adjList[na]);
									//(d impliziert nicht c)
									adjList[d].addOutgoingEdge(adjList[nc]);
									//(c impliziert nicht d)
									adjList[c].addOutgoingEdge(adjList[nd]);
									//(a impliziert nicht c)
									adjList[a].addOutgoingEdge(adjList[nc]);
									//(c impliziert nicht a)
									adjList[c].addOutgoingEdge(adjList[na]);
									if(top == 'W'){
										//(b impliziert nicht b)
										adjList[b].addOutgoingEdge(adjList[nb]);
									}
								}else if(down == 'W' || down == '.'){
								//(nicht a oder nicht b) und (nicht b oder nicht c) und (nicht c oder nicht a) [und (nicht d oder nicht d)]
									//(a impliziert nicht b)
									adjList[a].addOutgoingEdge(adjList[nb]);
									//(b impliziert nicht a)
									adjList[b].addOutgoingEdge(adjList[na]);
									//(b impliziert nicht c)
									adjList[b].addOutgoingEdge(adjList[nc]);
									//(c impliziert nicht b)
									adjList[c].addOutgoingEdge(adjList[nb]);
									//(a impliziert nicht c)
									adjList[a].addOutgoingEdge(adjList[nc]);
									//(c impliziert nicht a)
									adjList[c].addOutgoingEdge(adjList[na]);
									if(down == 'W'){
										//(d impliziert nicht d)
										adjList[d].addOutgoingEdge(adjList[nd]);
									}
								}
							}
							//Dritter Fall: 2 schwarze Nachbarn
							else if(right == 'B' && down == 'B'){
							//(c oder d) und (nicht c oder nicht d) [und (nicht a oder nicht a)] [und (nicht b oder nicht b)]
								//(nicht c impliziert d)
								adjList[nc].addOutgoingEdge(adjList[d]);
								//(nicht d impliziert c)
								adjList[nd].addOutgoingEdge(adjList[c]);
								//(c impliziert nicht d)
								adjList[c].addOutgoingEdge(adjList[nd]);
								//(d impliziert nicht c)
								adjList[d].addOutgoingEdge(adjList[nc]);
								if(left == 'W'){
									//(a impliziert nicht a)
									adjList[a].addOutgoingEdge(adjList[na]);
								}
								if(top == 'W'){
									//(b impliziert nicht b)
									adjList[b].addOutgoingEdge(adjList[nb]);
								}
							}
							else if(top == 'B' && down == 'B'){
							//(b oder d) und (nicht b oder nicht d) [und (nicht a oder nicht a)] [und (nicht c oder nicht c)]
								//(nicht b impliziert d)
								adjList[nb].addOutgoingEdge(adjList[d]);
								//(nicht d impliziert b)
								adjList[nd].addOutgoingEdge(adjList[b]);
								//(b impliziert nicht d)
								adjList[nb].addOutgoingEdge(adjList[d]);
								//(d impliziert nicht b)
								adjList[d].addOutgoingEdge(adjList[nb]);
								if(left == 'W'){
									//(a impliziert nicht a)
									adjList[a].addOutgoingEdge(adjList[na]);
								}
								if(top == 'W'){
									//(c impliziert nicht c)
									adjList[c].addOutgoingEdge(adjList[nc]);
								}	
							}
							else if(top == 'B' && right == 'B'){
							//(c oder b) und (nicht c oder nicht b) [und (nicht a oder nicht a)] [und (nicht d oder nicht d)]
								//(nicht c impliziert b)
								adjList[nc].addOutgoingEdge(adjList[b]);
								//(nicht b impliziert c)
								adjList[nb].addOutgoingEdge(adjList[c]);
								//(c impliziert nicht b)
								adjList[c].addOutgoingEdge(adjList[nb]);
								//(b impliziert nicht c)
								adjList[b].addOutgoingEdge(adjList[nc]);
								if(left == 'W'){
									//(a impliziert nicht a)
									adjList[a].addOutgoingEdge(adjList[na]);
								}
								if(top == 'W'){
									//(d impliziert nicht d)
									adjList[d].addOutgoingEdge(adjList[nd]);
								}	
							}
							else if(left == 'B' &&  down == 'B'){
							//(a oder d) und (nicht a oder nicht d) [und (nicht b oder nicht b)] [und (nicht c oder nicht c)]
								//(nicht a impliziert d)
								adjList[na].addOutgoingEdge(adjList[d]);
								//(nicht d impliziert a)
								adjList[nd].addOutgoingEdge(adjList[a]);
								//(a impliziert nicht d)
								adjList[a].addOutgoingEdge(adjList[nd]);
								//(d impliziert nicht a)
								adjList[d].addOutgoingEdge(adjList[na]);
								if(left == 'W'){
									//(b impliziert nicht b)
									adjList[b].addOutgoingEdge(adjList[nb]);
								}
								if(top == 'W'){
									//(c impliziert nicht c)
									adjList[c].addOutgoingEdge(adjList[nc]);
								}
							}
							else if(left == 'B' && right == 'B'){
							//(a oder c) und (nicht a oder nicht c) [und (nicht b oder nicht b)] [und (nicht d oder nicht d)]
								//(nicht a impliziert c)
								adjList[na].addOutgoingEdge(adjList[c]);
								//(nicht c impliziert a)
								adjList[nc].addOutgoingEdge(adjList[a]);
								//(a impliziert nicht c)
								adjList[a].addOutgoingEdge(adjList[nc]);
								//(c impliziert nicht a)
								adjList[c].addOutgoingEdge(adjList[na]);
								if(left == 'W'){
									//(b impliziert nicht b)
									adjList[b].addOutgoingEdge(adjList[nb]);
								}
								if(top == 'W'){
									//(d impliziert nicht d)
									adjList[d].addOutgoingEdge(adjList[nd]);
								}
							}
							else if(left == 'B' && top == 'B'){
							//(a oder b) und (nicht a oder nicht b) [und (nicht c oder nicht c)] [und (nicht d oder nicht d)]
								//(nicht a impliziert b)
								adjList[na].addOutgoingEdge(adjList[b]);
								//(nicht b impliziert a)
								adjList[nb].addOutgoingEdge(adjList[a]);
								//(a impliziert nicht b)
								adjList[a].addOutgoingEdge(adjList[nb]);
								//(b impliziert nicht a)
								adjList[b].addOutgoingEdge(adjList[na]);
								if(left == 'W'){
									//(c impliziert nicht c)
									adjList[c].addOutgoingEdge(adjList[nc]);
								}
								if(top == 'W'){
									//(d impliziert nicht d)
									adjList[d].addOutgoingEdge(adjList[nd]);
								}
							}
							//Vierter Fall: 1 schwarzer Nachbar
							else if(left == 'B'){
							//(a oder a) [und (nicht b oder nicht b)] [und (nicht c oder nicht c)] [und (nicht d oder nicht d)]
								//(nicht a impliziert a)
								adjList[na].addOutgoingEdge(adjList[a]);
								if(top == 'W'){
									//(b impliziert nicht b)
									adjList[b].addOutgoingEdge(adjList[nb]);
								}
								if(right == 'W'){
									//(c impliziert nicht c)
									adjList[c].addOutgoingEdge(adjList[nc]);
								}
								if(down == 'W'){
									//(d impliziert nicht d)
									adjList[d].addOutgoingEdge(adjList[nd]);
								}
							}
							else if(right == 'B'){
							//(c oder c) [und (nicht b oder nicht b)] [und (nicht a oder nicht a)] [und (nicht d oder nicht d)]
								//(nicht c impliziert c)
								adjList[nc].addOutgoingEdge(adjList[c]);
								if(top == 'W'){
									//(b impliziert nicht b)
									adjList[b].addOutgoingEdge(adjList[nb]);
								}
								if(right == 'W'){
									//(a impliziert nicht a)
									adjList[a].addOutgoingEdge(adjList[na]);
								}
								if(down == 'W'){
									//(d impliziert nicht d)
									adjList[d].addOutgoingEdge(adjList[nd]);
								}	
							}
							else if(top == 'B'){
							//(b oder b) [und (nicht a oder nicht a)] [und (nicht c oder nicht c)] [und (nicht d oder nicht d)]
								//(nicht b impliziert b)
								adjList[nb].addOutgoingEdge(adjList[b]);
								if(top == 'W'){
									//(a impliziert nicht a)
									adjList[a].addOutgoingEdge(adjList[na]);
								}
								if(right == 'W'){
									//(c impliziert nicht c)
									adjList[c].addOutgoingEdge(adjList[nc]);
								}
								if(down == 'W'){
									//(d impliziert nicht d)
									adjList[d].addOutgoingEdge(adjList[nd]);
								}
							}
							else if(down == 'B'){
							//(d oder d) [und (nicht b oder nicht b)] [und (nicht c oder nicht c)] [und (nicht a oder nicht a)]
								//(nicht d impliziert d)
								adjList[nd].addOutgoingEdge(adjList[d]);
								if(top == 'W'){
									//(b impliziert nicht b)
									adjList[b].addOutgoingEdge(adjList[nb]);
								}
								if(right == 'W'){
									//(c impliziert nicht c)
									adjList[c].addOutgoingEdge(adjList[nc]);
								}
								if(down == 'W'){
									//(a impliziert nicht a)
									adjList[a].addOutgoingEdge(adjList[na]);
								}
							}
							//Fuenfter Fall: 0 schwarze Nachbarn
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
		return adjList;
	}
	
	private Vertex[] falseSatInstance(){
		Vertex[] adjList = new Vertex[2];			//Es wird einfach eine AdjList
		adjList[0] = new Vertex(0);					// aufgestellt, die definitiv
		adjList[1] = new Vertex(1);					// einen Cycle enthaelt
		adjList[0].addOutgoingEdge(adjList[1]);		// und somit auf jeden Fall
		adjList[1].addOutgoingEdge(adjList[0]);		// eine Nein-Instanz
		return adjList;								// des SATSolvers ist.
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
