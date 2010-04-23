package cubedrawer;


import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import algorithms.AlgorithmOutput;

public class Console extends JPanel implements AlgorithmOutput{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea console;
	private JScrollPane scrollPane;


	public Console(){
		console = new JTextArea(10,60);
		console.setBackground(this.getBackground());
		console.setEditable(false);
		this.scrollPane = new JScrollPane(this.console);
		this.add(this.scrollPane);
		scrollPane.setWheelScrollingEnabled(true);
	}

	public void addTextln(String s){
		console.append(s);
		console.append("\n");
		console.setCaretPosition(console.getText().length());
		
		repaint();
	}
	
	public void addln(){
		console.append("\n");
		console.setCaretPosition(console.getText().length());
		
		
		repaint();
	}

	public void addText(String s){
		console.append(s + " ");
		console.setCaretPosition(console.getText().length());
		repaint();
	}
}
