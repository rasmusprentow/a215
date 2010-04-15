package cubedrawer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

public class MainPanel extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	SidePanel sidePanel;
	DrawPanel cubeDrawer;
	
	public MainPanel() {
		// TODO Auto-generated constructor stub
		this.setLayout(new BorderLayout(3,3));
		//this.setBackground(Color.black);
		cubeDrawer = new DrawPanel();
		this.add(cubeDrawer, BorderLayout.CENTER);
		sidePanel = new SidePanel();
		this.add(sidePanel,BorderLayout.EAST);
		sidePanel.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//for(MoveButtons key : MoveButtons.values()){
			if(e.getSource() ==  sidePanel.getTwistButtons().get(MoveButtons.B)){
				cubeDrawer.getCube().getSecondary()[1].cwTwist();
			}
			if(e.getSource() ==  sidePanel.getTwistButtons().get(MoveButtons.BP)){
				cubeDrawer.getCube().getSecondary()[1].ccwTwist();
			}
			if(e.getSource() ==  sidePanel.getTwistButtons().get(MoveButtons.F)){
				cubeDrawer.getCube().getSecondary()[0].cwTwist();
			}
			if(e.getSource() ==  sidePanel.getTwistButtons().get(MoveButtons.FP)){
				cubeDrawer.getCube().getSecondary()[0].ccwTwist();
			}
			if(e.getSource() ==  sidePanel.getTwistButtons().get(MoveButtons.U)){
				cubeDrawer.getCube().getPrimary()[0].cwTwist();
			}
			if(e.getSource() ==  sidePanel.getTwistButtons().get(MoveButtons.UP)){
				cubeDrawer.getCube().getPrimary()[0].ccwTwist();
			}
			if(e.getSource() ==  sidePanel.getTwistButtons().get(MoveButtons.D)){
				cubeDrawer.getCube().getPrimary()[1].cwTwist();
			}
			if(e.getSource() ==  sidePanel.getTwistButtons().get(MoveButtons.DP)){
				cubeDrawer.getCube().getPrimary()[1].ccwTwist();
			}
			if(e.getSource() ==  sidePanel.getTwistButtons().get(MoveButtons.L)){
				cubeDrawer.getCube().getTertiary()[0].cwTwist();
			}
			if(e.getSource() ==  sidePanel.getTwistButtons().get(MoveButtons.LP)){
				cubeDrawer.getCube().getTertiary()[0].ccwTwist();
			}
			if(e.getSource() ==  sidePanel.getTwistButtons().get(MoveButtons.R)){
				cubeDrawer.getCube().getTertiary()[1].cwTwist();
			}
			if(e.getSource() ==  sidePanel.getTwistButtons().get(MoveButtons.RP)){
				cubeDrawer.getCube().getTertiary()[1].ccwTwist();
			}
			
		//}
		
		
		//cubeDrawer.getCube().
		
		
		cubeDrawer.repaint();
	}

	

}
