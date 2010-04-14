package cubedrawer;



import java.awt.Graphics;
import java.awt.LayoutManager;

import javax.swing.JFrame;
import javax.swing.JPanel;

import cube.Cube;

public class CubeDraw extends JPanel {
	private Cube cube;
	
	public CubeDraw() {
		// TODO Auto-generated constructor stub
		
		
		cube = new Cube();
		
		
	}

	public void paintComponent(Graphics g){
		
	}

	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		 JFrame window = new JFrame("Graph");
		 CubeDraw content = new CubeDraw();
	     window.setContentPane(content);
	      window.pack();  // Set size of window to preferred size of its contents.
	      window.setResizable(true);  // User can't change the window's size.
	      window.setLocation(50,0);
	      window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	      window.setVisible(true);
		
		
	}

}
