package cubedrawer;


import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Console extends JPanel {
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
		console.append(s + "\n");
		console.setCaretPosition(console.getText().length() - 1);
	}
	
	public void addln(){
		console.append("\n");
		console.setCaretPosition(console.getText().length() - 1);
	}

	public void addText(String s){
		console.append(s + " ");
		console.setCaretPosition(console.getText().length() - 1);
	}
}
