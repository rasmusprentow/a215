package cubedrawer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public class MainPanel extends JPanel {
	SidePanel sidePanel;
	CubeDraw cubeDrawer;
	
	public MainPanel() {
		// TODO Auto-generated constructor stub
		this.setLayout(new BorderLayout(3,3));
		this.setBackground(Color.black);
		cubeDrawer = new CubeDraw();
		this.add(cubeDrawer, BorderLayout.CENTER);
		sidePanel = new SidePanel();
		this.add(sidePanel,BorderLayout.EAST);
	}

	

}
