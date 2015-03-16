package PieceItTogether;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;

import PieceItTogether.PatternSolver.PatternSolver;
import PieceItTogether.PatternSolver.Pattern;

public class MainFrame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) throws NumberFormatException, IOException{
			PatternSolver ps = new PatternSolver();
			String path = "";
		/* Falls Uebergabeparameter übergeben wurde */
		try{
			path = args[0];
		/* Sonst */	
		} catch (ArrayIndexOutOfBoundsException e) {printError(0);}
			
		if(path.contains("\\")){
			path.replace('\\', '/');
		}
		init(ps, path);
		boolean[] b = ps.solve();
		for(int i = 0; i < b.length; i++){
			System.out.println(b[i] ? "Yes" : "No");
		}
	}
	
	private static void init(PatternSolver ps, String path) throws NumberFormatException, IOException{
		try{
			FileReader fr = new FileReader(path);
		    BufferedReader input = new BufferedReader(fr);
		    
		    char[][] pattern;
		    int k = Integer.parseInt(input.readLine());
		    int n;
		    int m;
		    
		    if(k <= 100){
		    	if(k > 0){
		    		for(int i = 0; i < k; i++){
			    		String s = input.readLine();
			    		int a = s.indexOf(" ");
			    		n = Integer.parseInt(s.substring(0, a));
			    		m = Integer.parseInt(s.substring(a+1));
			    		
			    		if(1 <= n && 500 >= n  &&  1 <= m && 500 >= m){
				    		pattern = new char[n][];
				    		
				    		for(int j = 0; j < n; j++){
				    			char[] c = input.readLine().toCharArray();
				    			pattern[j] = c;
				    		}
				    		Pattern p = new Pattern(n, m, pattern);
				    		ps.addPattern(p);
			    		} else {
			    			System.out.println("The dimension of test case "+i+"is wrong.\n"
			    					+ "The given patterns must be at least 1x1 and not greater than 500 in width or hight.\n"
			    					+ "Test case "+i+" will be ignored.");
			    		}
			    	}			    		
		    	} else {printError(3);}
		    } else {printError(2);}
		    
		    input.close();
		    
		} catch (FileNotFoundException e){
			printError(1);
		}
	}
	
	private static void printError(int n){
		String s = "";
		switch(n){
			case 0:	s = "Please specify a file path.";
					break;
			case 1: s = "File not found, please try again!\n"
					+ "Input must be the path to a file in .txt format containing the input";
					break;
			case 2: s = "The amount of test cases must not be greater than 100!";
					break;
			case 3: s = "No test cases specified!";
					break;
			
			default: s = "An error has occured. Please restart the programm.";
					break;
		}
		System.err.println(s);
		System.exit(1);
	}
	
}
