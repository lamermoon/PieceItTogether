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
						
					/* Wenn das aktuelle Feld ein schwarzes Feld ist: */
					if(pattern[i][j]=='B') {
						//Erster Fall - Left und Right weiss
						if(left=='W'&&right=='W') {
							//nicht a impliziert c
							adjMtrx[(p.getM()-1)*2*i+(j-1)*2+1][(p.getM()-1)*2*i+j*2]=true;
							//a impliziert nicht c
							adjMtrx[(p.getM()-1)*2*i+(j-1)*2][(p.getM()-1)*2*i+j*2+1]=true;
						} //Zweiter Fall - Left und Right nicht weiss 
						else if(left!='W'&&right!='W') {
							//Pattern nicht erfuellbar
							return falseSatInstance();
						} //Dritter Fall - Left weiss, right nicht
						else if(left=='W'&&right!='W') {
							//nicht a impliziert a ist aequivalent zu (a oder a) ist äquivalent zu a
							adjMtrx[(p.getM()-1)*2*i+(j-1)*2+1][(p.getM()-1)*2*i+(j-1)*2]=true;
						} //Vierter Fall - Left nicht weiss, right weiss
						else if(left!='W'&&right=='W') {
							//nicht c impliziert c ist aequivalent zu (c oder c) ist aequivalent zu c
							adjMtrx[(p.getM()-1)*2*i+j*2+1][(p.getM()-1)*2*i+j*2]=true;
						} //Fuenfter Fall - Top und down weiss
						//Neue Betrachtung! Kein else if!
						if(top=='W'&&down=='W') {
							//nicht b impliziert d
							adjMtrx[(p.getM()-1)*2*p.getN()+p.getM()*2*(i-1)+2*j+1][(p.getM()-1)*2*p.getN()+p.getM()*2*i+2*j]=true;
							//nicht d impliziert b
							adjMtrx[(p.getM()-1)*2*p.getN()+p.getM()*2*i+2*j+1][(p.getM()-1)*2*p.getN()+p.getM()*2*(i-1)+2*j]=true;
						} //Sechster Fall - Top und Down nicht weiss
						else if(top!='W'&&down!='W') {
							//Pattern nicht erfuellbar
							return falseSatInstance();
						} //Siebter Fall - Top weiss und down nicht
						else if(top=='W'&&down!='W') {
							//nicht b impliziert b ist aequivalent zu (b oder b) ist aequivalent zu b
							adjMtrx[(p.getM()-1)*2*p.getN()+p.getM()*2*(i-1)+2*j+1][(p.getM()-1)*2*p.getN()+p.getM()*2*(i-1)+2*j]=true;
						} //Achter Fall - Top nicht weiss, down weiss
						else if(top!='W'&&down=='W') {
							//nicht d impliziert d ist aequivalent zu (d oder d) ist aequivalent zu d
							adjMtrx[(p.getM()-1)*2*p.getN()+p.getM()*2*i+2*j+1][(p.getM()-1)*2*p.getN()+p.getM()*2*i+2*j]=true;
						}
					}
					//Betrachte weißen Mittelstein!
					if(pattern[i][j]=='W') {
						//TODO
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
