package cubedrawer;

import javax.swing.JFrame;

public class CubeDrawerMain {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		JFrame window = new JFrame("Rubik's Cube");
		MainPanel content = new MainPanel();
		window.setContentPane(content);
		window.pack();  // Set size of window to preferred size of its contents.
		window.setResizable(true);  // User can't change the window's size.
		window.setLocation(50,0);
		window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		window.setVisible(true);


	}
}
