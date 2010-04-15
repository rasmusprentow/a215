package cubedrawer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

public class MainPanel extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	SidePanel sidePanel;
	DrawPanel cubeDrawer;
	Console console;
	
	public MainPanel() {
		// TODO Auto-generated constructor stub
		this.setLayout(new BorderLayout());
		this.setBackground(Color.black);
	
		sidePanel = new SidePanel();
		sidePanel.setBorder(new EtchedBorder());
		this.add(sidePanel,BorderLayout.EAST);
		sidePanel.addActionListener(this);
		console = new Console();
		this.add(console, BorderLayout.SOUTH);
		
		console.setBorder(new EtchedBorder());
		cubeDrawer = new DrawPanel(console);
		cubeDrawer.setBorder(new EtchedBorder());
		this.add(cubeDrawer, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for(MoveButtons key : MoveButtons.values()){
			if(e.getSource() ==  sidePanel.getTwistButtons().get(key)){
				cubeDrawer.buttonHandler(key);
			}
		}
			
			
		//}
		
		
		//cubeDrawer.getCube().
		
		
		cubeDrawer.repaint();
	}

	

}
