import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

class MainFrame{		
	public static void main(String[] args) throws Exception{
		PatternSolver ps = new PatternSolver();
		init(ps);
		String[] answers = ps.solve();
		System.out.println("Output:");
		for(int i = 0; i < answers.length; i++){
			System.out.println(answers[i]);
		}
	}
	
	private static void init(PatternSolver ps) throws IOException{
		System.out.println("Input:");
		Scanner scannerIn = new Scanner(System.in);
		int k = scannerIn.nextInt();
		for(int i = 0; i < k; i++){
			int n = scannerIn.nextInt();
			int m = scannerIn.nextInt();
			char[][] pattern = new char[n][m];
			for(int j = 0; j < n; j++){
				pattern[j] = scannerIn.next().toCharArray();
			}
			ps.addPattern(new Pattern(n, m, pattern));
		}
		scannerIn.close();
		System.out.println("Input complete, calculating...");
	}
}

class PatternSolver {
	private ArrayList<Pattern> cases = new ArrayList<Pattern>(); 
	
	public String[] solve(){
		String[] output = new String[cases.size()];
		SATSolver sat = new SATSolver();
		for(int i = 0; i < cases.size(); i++){
			if(cases.get(i).getN() == -1){
				output[i] = "**ignored**\n";
			} else {
				Vertex[] adjList = reduceTo2Sat(cases.get(i));
				Graph graph = new Graph(adjList.length);
				graph.setVertices(adjList);
				boolean answer = sat.solve(graph);
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
								adjList[a].addOutgoingEdge(adjList[nc]);
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
								adjList[b].addOutgoingEdge(adjList[nd]);
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

class SATSolver {
	
	//Im bool-array sind a und nicht a immer nebeneinander angeordnet
	public boolean solve(Graph graph){
		graph = transitiveClosureNew(graph);
		for(int i = 0; i < graph.getVertices().length; i = i+2) {									//gehe durch alle "nichtnegierten Listen"
			for(int j = 0; j < graph.getVertices()[i].getOutgoingEdges().size(); j++) {
				
				if(graph.getVertices()[i].getOutgoingEdges().get(j).equals(graph.getVertices()[i+1])) {			//schaue, ob Implikation zu negierter Liste
					for(int k = 0; k < graph.getVertices()[i+1].getOutgoingEdges().size(); k++) {	//falls ja , gehe durch negierte Liste
						if(graph.getVertices()[i+1].getOutgoingEdges().get(k).equals(graph.getVertices()[i])) {
							return false;												//falls dort Implikation zu nichtnegierter dann Cycle also false
						}
					}																	//falls in erster while-Schleife bereits negierte Liste gefunden wurde,
					break;																//jedoch nicht umgekehrt, ist jede weitere Untersuchung dieses Listenpaars unnoetig
				}
			}
		}
		return true; 																	//sonst true
	}
	
	private Graph transitiveClosureNew(Graph e){
		//Logarithmischer Algo
		Graph deltaE = new Graph(e);
		Graph eX = new Graph(e);
		while(!eX.isEmpty()) {
			eX= new Graph(e.getVertices().length);
			Graph deltaENew= new Graph(e.getVertices().length);
			for(int i=0; i<deltaE.getVertices().length;i++) {
				for(int v=0;v< deltaE.getVertices()[i].getOutgoingEdges().size();v++) { 
					for(int j=0;j< deltaE.getVertices()[i].getOutgoingEdge(v).getOutgoingEdges().size();j++) {
						deltaENew.addOutEdge(deltaENew.getVertices()[i], deltaENew.getVertices()[deltaE.getVertices()[i].getOutgoingEdge(v).getOutgoingEdge(j).getID()]);
						if(!e.directConnection(e.getVertices()[i], e.getVertices()[deltaE.getVertices()[i].getOutgoingEdge(v).getOutgoingEdge(j).getID()])) {
							eX.addOutEdge(eX.getVertices()[i], eX.getVertices()[deltaE.getVertices()[i].getOutgoingEdge(v).getOutgoingEdge(j).getID()]);
						}
					}
				}
			}
			deltaE = deltaENew;
			Graph eNew= new Graph(e);
			for(int i=0;i<e.getVertices().length;i++) {
				for(int v=0;v<e.getVertices()[i].getOutgoingEdges().size();v++) {
					for(int j=0;j<deltaE.getVertices()[e.getVertices()[i].getOutgoingEdge(v).getID()].getOutgoingEdges().size();j++) {
						if(!eNew.directConnection(eNew.getVertices()[i], eNew.getVertices()[deltaE.getVertices()[e.getVertices()[i].getOutgoingEdge(v).getID()].getOutgoingEdge(j).getID()])) {
							eNew.addOutEdge(eNew.getVertices()[i], eNew.getVertices()[deltaE.getVertices()[e.getVertices()[i].getOutgoingEdge(v).getID()].getOutgoingEdge(j).getID()]);							
						}
					}
				}
			}
			e.uniteGraphs(eNew, deltaE);
		}
		return e;
	}
}

class Pattern {
	/* Das Pattern repraesentiert eine n x m Matrix aus chars (NICHT m x n !!!) */
	private final int n;
	private final int m;
	private final char[][] pattern;
	
	public Pattern(){
		this.n = -1;
		this.m = -1;
		this.pattern = new char[0][0];
	}
	
	public Pattern(int n, int m, char[][] pattern){
		this.n = n;
		this.m = m;
		this.pattern = pattern;
	}
	
	public int getN(){
		return this.n;
	}
	
	public int getM(){
		return this.m;
	}
	
	public char[][] getPattern(){
		return this.pattern;
	}
	
	public String toString(){
		String s = "Groesse: "+n+"x"+m+"\n";
		for(int i = 0; i < n; i++){
			s += new String(pattern[i]) + "\n";
		}
		return s;
	}
}

class Graph {
	private Vertex[] v;
	private boolean empty;
	
	/* Konstruktor */
	//neuer leerer Graph
	public Graph(int n){
		v = new Vertex[n];
		for(int i = 0; i < v.length; i++){
			v[i] = new Vertex(i);
		}
		empty = true;
	}
	//Kopie
	public Graph(Graph g) {
		this.v = g.getVertices();
		empty = g.isEmpty();
	}
	
	//Getter
	public boolean isEmpty() {
		return this.empty;
	}
	
	public Vertex[] getVertices() {
		return this.v;
	}
	
	//Setter/Adder
	public void notEmpty() {
		empty=false;
	}
	public void addOutEdge(Vertex a, Vertex b) {
		if(isEmpty()) {
			notEmpty();
		}
		a.addOutgoingEdge(b);
	}
	public void addInEdge(Vertex a, Vertex b) {
		if(isEmpty()) {
			notEmpty();
		}
		a.addIncomingEdge(b);
	}
	//Achtung: Nur zum Testen!
	public void setVertices(Vertex[] v) {
		this.v=v;
		notEmpty();
	}
	
	//Ueberpruefe ob direkte Verbindung zwischen zwei Knoten besteht
	public boolean directConnection(Vertex a, Vertex b) {
		return a.getOutgoingEdges().contains(b);
	}
	
	//Vereinige zwei Graphen
	public Graph uniteGraphs(Graph a, Graph b) {
		if(a.getVertices().length!=b.getVertices().length) {
			System.out.println("Fehler, die uebergebenen Graphen muessen gleich gross sein.");
			return a;
		}
		Vertex[] x= a.getVertices();
		Vertex[] y= b.getVertices();
		for(int i=0;i<x.length;i++) {
			for(int j=0;j<y[i].getOutgoingEdges().size();j++) {
				if(!a.directConnection(x[y[i].getID()], x[y[i].getOutgoingEdge(j).getID()])) {
					a.addOutEdge(x[y[i].getID()], x[y[i].getOutgoingEdge(j).getID()]);
				}
			}
		}
		Graph c = new Graph(a);
		return c;
	}
}

class Vertex {
	private final int id;
	private ArrayList<Vertex> outgoingEdges = new ArrayList<Vertex>(); 
	private ArrayList<Vertex> incomingEdges = new ArrayList<Vertex>(); 
	
	/* Konstruktor */
	public Vertex(int id){
		this.id = id;
	}
	
	/* Setter/Adder */
	public void addOutgoingEdge(Vertex v){
		this.outgoingEdges.add(v);
		v.incomingEdges.add(this);
	}
	
	public void addIncomingEdge(Vertex v){
		this.incomingEdges.add(v);
		v.outgoingEdges.add(this);
	}
	
	/* Getter */
	public int getID(){
		return this.id;
	}
	
	public ArrayList<Vertex> getIncomingEdges(){
		return this.incomingEdges;
	}
	
	public ArrayList<Vertex> getOutgoingEdges(){
		return this.outgoingEdges;
	}
	
	public Vertex getIncomingEdge(int i){
		return this.incomingEdges.get(i);
	}
	
	public Vertex getOutgoingEdge(int i){
		return this.outgoingEdges.get(i);
	}
	
	/* Overrides */
	@Override
	public boolean equals(Object o){
		if(o instanceof Vertex){
			if(this.id == ((Vertex) o).getID()){
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString(){
		String s = ((Integer) id).toString();
		return s;
	}
}

class Edge {
	private Vertex tail;
	private Vertex head;
	
	/* Konstruktor */
	public Edge(Vertex tail, Vertex head){
		this.tail = tail;
		this.head = head;
		this.tail.addOutgoingEdge(this.head);
		this.head.addIncomingEdge(this.tail);
	}
	
	/* Overrides */
	@Override
	public boolean equals(Object o){
		if(o instanceof Edge){
			if(this.tail.equals(((Edge) o).getTail()) &&
					this.head.equals(((Edge) o).getHead())){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString(){
		String s = "("+this.tail.toString()+","+this.head.toString()+")\n";
		return s;
	}
	
	/* Getter */	
	public Vertex getHead(){
		return this.head;
	}
	
	public Vertex getTail(){
		return this.tail;
	}

}
