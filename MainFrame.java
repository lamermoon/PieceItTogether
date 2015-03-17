package PieceItTogether;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
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
	private JSplitPane ioPanel, center;
	private JPanel top, bottom, subCenter;
	private JTextArea inputArea, outputArea, errorArea;
	private JScrollPane isp, osp, esp;
	private JProgressBar progBar;
	private static final long serialVersionUID = 1L;
	
	public MainFrame(String title) throws Exception{
		super(title);
		
		this.button = new JButton("Solve");
		this.button.setSize(25, 50);
		this.button.addActionListener(this);
		
		this.inputArea = new JTextArea();
		this.inputArea.setFont(new Font(Font.MONOSPACED, 5, 16));
		this.inputArea.setSize(750, 700);
		this.inputArea.setMinimumSize(new Dimension(100, 100));
		this.inputArea.setVisible(true);
		
		this.isp = new JScrollPane(inputArea);
		
		this.outputArea = new JTextArea();
		this.outputArea.setFont(new Font(Font.MONOSPACED, 5, 16));
		this.outputArea.setSize(250, 700);
		this.outputArea.setMinimumSize(new Dimension(100, 100));
		this.outputArea.setEditable(false);
		this.outputArea.setVisible(true);
		
		this.osp = new JScrollPane(outputArea);
		
		this.errorArea = new JTextArea();
		this.errorArea.setFont(new Font(Font.MONOSPACED, 5, 16));
		this.errorArea.setSize(1000, 100);
		this.errorArea.setMinimumSize(new Dimension(1000, 50));
		this.errorArea.setEditable(false);
		this.errorArea.setVisible(true);
		
		this.esp = new JScrollPane(errorArea);
		
		//TODO: ProgressBar zeigt Fortschritt an
		this.progBar = new JProgressBar();
		this.progBar.setVisible(false);
		
		this.top = new JPanel();
		this.top.setLayout(new BorderLayout());
		this.top.add(new JLabel(" INPUT:"), BorderLayout.WEST);
		this.top.add(new JLabel("OUTPUT: "), BorderLayout.EAST);
		
		this.ioPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, isp, osp);
		this.ioPanel.setDividerLocation(inputArea.getWidth());
		
		this.subCenter = new JPanel();
		this.subCenter.setLayout(new BorderLayout());
		this.subCenter.add(new JLabel("ERROR Output:"), BorderLayout.NORTH);
		this.subCenter.add(esp, BorderLayout.CENTER);
		
		this.center = new JSplitPane(JSplitPane.VERTICAL_SPLIT, ioPanel, subCenter);
		this.center.setDividerLocation(inputArea.getHeight()-100);
		
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
	
	public void actionPerformed(ActionEvent arg0) {
		PatternSolver ps = new PatternSolver();
		
		try{
			init(ps);
			boolean[] b = ps.solve();
			this.outputArea.setText("");
			for(int i = 0; i < b.length; i++){
				this.outputArea.append(b[i] ? "Yes\n" : "No\n");
			}
		}
		catch (IOException e){printError(0);}
	}
	
	public static void main(String[] args) throws Exception{
		JFrame f = new MainFrame("Piece It Together - Pattern Solver");
		f.setVisible(true);
	}
	
	private void init(PatternSolver ps) throws IOException{
		//TODO: Fehleranfaelligkeit verringern (zu viele Abstuerze bei fehlerhaften Eingaben)
		String[] input = inputArea.getText().split("\\n");
		char[][] pattern;
		try{
			int k = Integer.parseInt(input[0]);
			int n;
			int m;
			int pos = 1;
			if(k <= 100){
			  	if(k > 0){
			   		for(int i = 0; i < k; i++){
			    		String s = input[pos++];
			    		int a = s.indexOf(" ");
			    		try{
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
				    			this.errorArea.setText("ALERT: The dimension of test case "+(i+1)+"is wrong.\n"
				    					+ "The given patterns must be at least 1x1 and not greater than 500 in eather width or hight.\n"
				    					+ "Test case "+i+" will be ignored.");
				    		}
			    		} 
			    		catch(NumberFormatException e){printError(5);}
			    	}
			   	} else {printError(3);}
			} else {printError(2);}
		} 
		catch(NumberFormatException e){printError(1);} 
		catch(StringIndexOutOfBoundsException e){printError(4);}
	}
	
	private void printError(int n){
		String s;
		switch(n){
			case 0: s = "ERROR: IOException occured during init().";
					break;
			case 1: s = "ERROR: First line must contain a number between 0 and 100!";
					break;
			case 2: s = "ERROR: The amount of test cases must not exceed 100!";
					break;
			case 3: s = "ERROR: No test cases specified!";
					break;
			case 4: s = "ERROR: Format of input incorrect. Please check your input.";
					break;
			case 5: s = "ERROR: First line of each pattern must contain hight and width of the pattern separated by a blank sysmbol";
			
			default: s = "An error has occured. Please restart the programm.";
					break;
		}
		this.errorArea.setText(s);
		//System.out.println(s);
	}
}
