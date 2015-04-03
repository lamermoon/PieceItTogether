package PieceItTogether;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import PieceItTogether.PatternSolver.PatternSolver;
import PieceItTogether.PatternSolver.Pattern;

public class MainFrame extends JFrame implements ActionListener{
	private JButton solve, generate;
	private JSplitPane ioPanel, solveCenter;
	private JPanel solveTop, solveBottom, subCenter, inputPanel, outputPanel, solvePanel, generatePanel;
	private JTextArea inputArea, outputArea, errorArea;
	private JScrollPane isp, osp, esp;
	private JTextField n, m;
	private JRadioButton yes, no;
	private ButtonGroup instance;
	private static final long serialVersionUID = 1L;
	
	public MainFrame(String title) throws Exception{
		super(title);
		
		this.solve = new JButton("Solve");
		this.solve.setSize(25, 50);
		this.solve.addActionListener(this);
				
		this.inputArea = new JTextArea();
		this.inputArea.setFont(new Font(Font.MONOSPACED, 5, 16));
		this.inputArea.setSize(750, 700);
		this.inputArea.setVisible(true);
		
		this.isp = new JScrollPane(inputArea);
		
		this.inputPanel = new JPanel(new BorderLayout());
		this.inputPanel.add(new JLabel("INPUT:"), BorderLayout.NORTH);
		this.inputPanel.add(isp, BorderLayout.CENTER);
		
		this.outputArea = new JTextArea();
		this.outputArea.setFont(new Font(Font.MONOSPACED, 5, 16));
		this.outputArea.setSize(250, 700);
		this.outputArea.setMinimumSize(new Dimension(100, 100));
		this.outputArea.setEditable(false);
		this.outputArea.setVisible(true);
		
		this.osp = new JScrollPane(outputArea);
		
		this.outputPanel = new JPanel(new BorderLayout());
		this.outputPanel.add(new JLabel("OUTPUT:"), BorderLayout.NORTH);
		this.outputPanel.add(osp, BorderLayout.CENTER);
		
		this.errorArea = new JTextArea();
		this.errorArea.setFont(new Font(Font.MONOSPACED, 5, 16));
		this.errorArea.setSize(1000, 100);
		this.errorArea.setMinimumSize(new Dimension(1000, 50));
		this.errorArea.setEditable(false);
		this.errorArea.setVisible(true);
		
		this.esp = new JScrollPane(errorArea);
		
		this.solveTop = new JPanel(new BorderLayout());
		this.solveTop.add(new JLabel(" Piece It Together"), BorderLayout.NORTH);
		
		
		this.ioPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputPanel, outputPanel);
		this.ioPanel.setDividerLocation(inputArea.getWidth());
		
		this.subCenter = new JPanel(new BorderLayout());
		this.subCenter.add(new JLabel("ERROR Output:"), BorderLayout.NORTH);
		this.subCenter.add(esp, BorderLayout.CENTER);
		
		this.solveCenter = new JSplitPane(JSplitPane.VERTICAL_SPLIT, ioPanel, subCenter);
		this.solveCenter.setDividerLocation(inputArea.getHeight()-100);
		
		this.solveBottom = new JPanel();
		this.solveBottom.add(solve);
		
		this.solvePanel = new JPanel(new BorderLayout());
		this.solvePanel.add(solveTop, BorderLayout.NORTH);
		this.solvePanel.add(solveCenter, BorderLayout.CENTER);
		this.solvePanel.add(solveBottom, BorderLayout.SOUTH);
		
		this.generate = new JButton("Generate");
		this.generate.addActionListener(this);
		
		this.n = new JTextField();
		this.n.setSize(100, 50);
		this.n.setMinimumSize(new Dimension(100, 50));
		this.n.setMaximumSize(new Dimension(100, 50));
		
		this.m = new JTextField();
		this.m.setSize(100, 50);
		this.m.setMinimumSize(new Dimension(100, 50));
		this.m.setMaximumSize(new Dimension(100, 50));
		
		this.yes = new JRadioButton("Yes");
		this.yes.addActionListener(this);
		
		this.no = new JRadioButton("No");
		this.no.addActionListener(this);
		
		this.instance = new ButtonGroup();
		this.instance.add(yes);
		this.instance.add(no);
		
		this.generatePanel = new JPanel();
		this.generatePanel.setLayout(new BoxLayout(this.generatePanel, BoxLayout.X_AXIS));
		this.generatePanel.add(new JLabel("  Hight: "));
		this.generatePanel.add(n);
		this.generatePanel.add(new JLabel("  Width: "));
		this.generatePanel.add(m);
		this.generatePanel.add(new JLabel("  Instance:"));
		this.generatePanel.add(yes);
		this.generatePanel.add(no);
		this.generatePanel.add(generate);
		
		this.setSize(1000,900);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(solvePanel, BorderLayout.CENTER);
		this.getContentPane().add(generatePanel, BorderLayout.SOUTH);
		
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == this.solve){
			PatternSolver ps = new PatternSolver();
			this.errorArea.setText("");
			try{
				init(ps);
				String[] answers = ps.solve();
				this.outputArea.setText("");
				for(int i = 0; i < answers.length; i++){
					this.outputArea.append(answers[i]);
				}
			}
			catch (IOException e){printError(0);}
		}else if(ae.getSource() == this.generate){
			try{
				int n = Integer.parseInt(this.n.getText());
				int m = Integer.parseInt(this.m.getText());
				if(this.yes.isSelected() || this.no.isSelected()){
					boolean isInstance = this.yes.isSelected();
					generateInstance(n, m, isInstance);
				}else{
					printError(8);
				}
			}catch(NumberFormatException e){
				printError(7);
			}
		}
	}
	
	private void generateInstance(int n, int m, boolean isInstance){
		this.inputArea.setText("1\n"+n+" "+n+"\n");		// <<<<<<<<<< hier das zweite n zu m aendern, um spaeter korrekte Funkton zu haben!!!
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				if(i%2 == 0){
					if(j%2 == 0){
						this.inputArea.append("W");
					}else{
						this.inputArea.append("B");
					}
				}else{
					if(j%2 == 0){
						this.inputArea.append("B");
					}else{
						this.inputArea.append("W");
					}
				}
				
			}
			this.inputArea.append("\n");
		}
		this.errorArea.setText("ALERT: At the moment only pattern of size n x n can be generated, ignoring the type of instance!");
	}
	
	public static void main(String[] args) throws Exception{
		JFrame f = new MainFrame("Piece It Together - Pattern Solver");
		f.setVisible(true);
	}
	
	private void init(PatternSolver ps) throws IOException{
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
			   			try{
				    		String s = input[pos++];
				    		int a = s.indexOf(" ");
				    		try{
				    			n = Integer.parseInt(s.substring(0, a));
				    			m = Integer.parseInt(s.substring(a+1));
				    		
					    		if(1 <= n && 500 >= n  &&  1 <= m && 500 >= m){
						    		pattern = new char[n][];
						    		boolean addTestCase = true;
						    		for(int j = 0; j < n; j++){
						    			char[] c = input[pos++].toCharArray();
							    		if(c.length == m){
							    			pattern[j] = c;
							    		} else {
							    			this.errorArea.setText("ALERT: Test case "+(i+1)+" has a wrong amount of letters in line "+(j+1)+"\n" +
							    					"Test case "+(i+1)+" will be ignored.");
							    			addTestCase = false;
							    		}
						    		}
						    		if(addTestCase){
							    		ps.addPattern(new Pattern(n, m, pattern));
						    		} else {
						    			ps.addPattern(new Pattern());
						    		}
						    		
					    		} else {
					    			this.errorArea.setText("ALERT: The dimension of test case "+(i+1)+"is wrong.\n"
					    					+ "The given patterns must be at least 1x1 and not greater than 500 in eather width or hight.\n"
					    					+ "Test case "+i+" will be ignored.");
					    		}
				    		} 
				    		catch(NumberFormatException e){printError(5);}
				    		catch(ArrayIndexOutOfBoundsException e){printError(5);}
			   			}
			   			catch(ArrayIndexOutOfBoundsException e){printError(6);}
			    	}
			   	} else {printError(3);}
			} else {printError(2);}
		} 
		catch(NumberFormatException e){printError(1);} 
		catch(StringIndexOutOfBoundsException e){printError(4);}
	}
	
	private void printError(int i){
		String s;
		switch(i){
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
					break;
			case 6: s = "ALERT: Not enough test cases given.\n" +
					"Please add test cases or correct the number given in line 1.";
					break;
			case 7: s = "ERROR: Please enter integer between 0 and 500 for width and hight.";
					break;
			case 8: s = "ERROR: Please check 'Yes' or 'No' depending on what kind of instance you want to be created.";
					break;
			
			default: s = "An error has occured. Please restart the programm.";
					break;
		}
		this.errorArea.setText(s);
		//System.out.println(s);
	}
}
