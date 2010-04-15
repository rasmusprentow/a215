package cubedrawer;


import java.awt.GridLayout;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JPanel;

import cubedrawer.CubeDraw.MoveButtons;


public class SidePanel extends JPanel {
	TreeMap<MoveButtons, JButton> moveButtons;

	public SidePanel() {
		this.setLayout(new GridLayout(0,2));
		moveButtons = new TreeMap<MoveButtons, JButton>();
		for(MoveButtons key : MoveButtons.values()){
			moveButtons.put(key, new JButton(key.toString()));
			this.add(moveButtons.get(key));
		}
		
	}

	
}
