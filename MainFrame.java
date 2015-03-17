package PieceItTogether;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import PieceItTogether.PatternSolver.PatternSolver;
import PieceItTogether.PatternSolver.Pattern;

public class MainFrame extends JFrame implements ActionListener{
	private JButton button;
	private JSplitPane center;
	private JPanel top, bottom;
	private JTextArea inputArea, outputArea;
	private JScrollPane isp, osp;
	private JProgressBar progBar;
	private static final long serialVersionUID = 1L;
	
	public MainFrame(String title) throws Exception{
		super(title);
		
		this.button = new JButton("Solve");
		this.button.setSize(25, 50);
		this.button.addActionListener(this);
		
		this.inputArea = new JTextArea();
		this.inputArea.setSize(750, 800);
		this.inputArea.setMinimumSize(new Dimension(100, 100));
		this.inputArea.setVisible(true);
		
		this.isp = new JScrollPane(inputArea);
		
		this.outputArea = new JTextArea();
		this.outputArea.setSize(100, 800);
		this.outputArea.setMinimumSize(new Dimension(100, 100));
		this.outputArea.setEditable(false);
		this.outputArea.setVisible(true);
		
		this.osp = new JScrollPane(outputArea);
		
		//TODO: ProgressBar zeigt Fortschritt an
		this.progBar = new JProgressBar();
		this.progBar.setVisible(false);
		
		this.top = new JPanel();
		this.top.setLayout(new BorderLayout());
		this.top.add(new JLabel(" INPUT:"), BorderLayout.WEST);
		this.top.add(new JLabel("OUTPUT: "), BorderLayout.EAST);
		
		this.center = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, isp, osp);
		this.center.setDividerLocation(inputArea.getWidth());
		
		this.bottom = new JPanel();
		this.bottom.add(button);
		this.bottom.add(progBar);
		
		this.setSize(1000,800);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(top, BorderLayout.NORTH);
		this.getContentPane().add(center, BorderLayout.CENTER);
		this.getContentPane().add(bottom, BorderLayout.SOUTH);
		
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	

	@Override
	public void actionPerformed(ActionEvent arg0) {
		PatternSolver ps = new PatternSolver();
		
		try{
			init(ps);
		} catch (IOException e){
			printError(4);
		}
		boolean[] b = ps.solve();
		this.outputArea.setText("");
		for(int i = 0; i < b.length; i++){
			this.outputArea.append(b[i] ? "Yes\n" : "No\n");
		}
	}
	
	public static void main(String[] args) throws Exception{
		JFrame f = new MainFrame("Piece It Together - Pattern Solver");
		f.setVisible(true);
	}
	
	private void init(PatternSolver ps) throws NumberFormatException, IOException{
		//TODO: Fehleranfälligkeit verringern (zu viele Abstürze bei fehlerhaften Eingaben)
		String[] input = inputArea.getText().split("\\n");
		char[][] pattern;
		int k = Integer.parseInt(input[0]);
		int n;
		int m;
		int pos = 1;
		if(k <= 100){
		  	if(k > 0){
		   		for(int i = 0; i < k; i++){
		    		String s = input[pos++];
		    		int a = s.indexOf(" ");
		    		n = Integer.parseInt(s.substring(0, a));
		    		m = Integer.parseInt(s.substring(a+1));
		    		
		    		if(1 <= n && 500 >= n  &&  1 <= m && 500 >= m){
			    		pattern = new char[n][];
			    		
			    		for(int j = 0; j < n; j++){
			    			char[] c = input[pos++].toCharArray();
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
	}
	
	private void printError(int n){
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
			case 4: s = "IOException occured during init().";
			
			default: s = "An error has occured. Please restart the programm.";
					break;
		}
		System.err.println(s);
		System.exit(1);
	}
}
