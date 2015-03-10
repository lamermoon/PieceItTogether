package PieceItTogether.PatternSolver;

import java.util.ArrayList;
import PieceItTogether.SATSolver.SATSolver;

public class PatternSolver {
	final ArrayList<Pattern> cases = new ArrayList<Pattern>(); 
	
	public boolean[] solve(){
		boolean[] output = new boolean[cases.size()];	//Array, das true oder false für jeden übergebenen Fall speichert
		SATSolver sat = new SATSolver();
		for(int i = 0; i < cases.size(); i++){					//für jeden Fall
			boolean[][] adjMtrx = reduceTo2Sat(cases.get(i));	//wird die Reduktion durchgeführt
			output[i] = sat.solve(adjMtrx);						//und das Ergebnis des SATSolvers gespeichert
		}
		return output;
	}
	
	private boolean[][] reduceTo2Sat(Pattern p){
		boolean[][] adjMtrx = new boolean[2*(p.getN()*(p.getM()-1)+(p.getN()-1)*p.getM())][2*(p.getN()*(p.getM()-1)+(p.getN()-1)*p.getM())]; //2*(p.getN()*(p.getM()-1)+(p.getN()-1)*p.getM()) ergibt die Anzahl der Kanten im Pattern
		
		
		
		for(int i = 0; i < adjMtrx.length; i++){		//nachdem die Reduktion durchgeführt ist, sind evtl einige Zellen der Matrix
			for(int j = 0; j < adjMtrx.length; j++){	//noch nicht initialisiert worden.
				if(adjMtrx[i][j]){						//Ist die Zelle mit dem Wert true initialisiert
					//hier passiert nichts
				}else{
					adjMtrx[i][j] = false;				//sonst wird sie mit false initialisiert.
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
