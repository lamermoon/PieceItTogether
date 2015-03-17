package PieceItTogether.PatternSolver;

public class Pattern {
	/* Das Pattern repraesentiert eine n x m Matrix aus chars (NICHT m x n !!!) */
	private final int n;
	private final int m;
	private final char[][] pattern;
	
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
