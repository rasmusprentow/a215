
import javax.swing.JFrame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ThinkingBoxMain {
	public static void main(String[] args){
		 JFrame window = new JFrame("Thinking Box");
	     ThinkingBoxMainPanel content = new ThinkingBoxMainPanel();
	     //TBMPanel content = new TBMPanel();
	     window.setContentPane(content);
	      window.pack();  // Set size of window to preferred size of its contents.
	      window.setResizable(false);  // User can't change the window's size.
	      window.setLocation(100,100);
	      window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	      window.setVisible(true);
	}
}
 