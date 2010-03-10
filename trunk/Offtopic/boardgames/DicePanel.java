package boardgames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class DicePanel extends JPanel {
	private Dices dices;
	
    public DicePanel(){
	     setBackground( new Color(0,120,0) );
         setForeground( Color.GREEN );
         //smallFont = new Font("SansSerif", Font.PLAIN, 12);
         //bigFont = new Font("Serif", Font.BOLD, 14);
         setPreferredSize( new Dimension(111, 131));
         
         
	}

    public void paintComponent(Graphics g){
    	super.paintComponent(g);
    	int base = 20;
    	int basex = 0;
    	if(dices != null){
    		for(int i = 0; i < dices.getNumDices(); i++){
    			if(i < 2){
    				DiceDrawer.drawDie(g, dices.getDie(i), basex + i * 60, base);
    			} else {
    				DiceDrawer.drawDie(g, dices.getDie(i), basex + (i - 2) * 60, base + 60);
    			}
    			
    		}
    		
    	}
    	
    }
	
    public void setDices(Dices d){
    	dices = d;
    	repaint();
    }
}
