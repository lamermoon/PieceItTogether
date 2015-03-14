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
						
					/* Wenn das aktuelle Feld ein schwarzes Feld ist: */
						if(pattern[i][j]=='B') {
							//Erster Fall: 4 weisse Nachbarn
							if(left == 'W' && right == 'W' && top == 'W' && down == 'W') {
								
							//Zweiter Fall: 3 weisse Nachbarn
							}else if((left == 'W' && top == 'W' && right == 'W') || 
									(down == 'W' && top == 'W' && right == 'W') || 
									(left == 'W' && down == 'W' && right == 'W') || 
									(left == 'W' && top == 'W' && down == 'W')){
								
								
							//Dritter Fall: 2 weisse Nachbarn
							}else if((right == 'W' && down == 'W') ||
									(top == 'W' && down == 'W') ||
									(top == 'W' && right == 'W') ||
									(left == 'W' && down == 'W') ||
									(left == 'W' && right == 'W') ||
									(left == 'W' && top == 'W')){
								
								
							//Vierter Fall: 1 oder 0 weisse Nachbarn
							}else{
								System.out.println("Ich war hier!");
								return falseSatInstance();
							}
							
					/* Wenn das aktuelle Feld ein weisses Feld ist: */
						} else {
							//Erster Fall:
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
