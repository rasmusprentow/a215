

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class MiniDicePanel extends JPanel {
	private Dices dices;
	
    public MiniDicePanel(){
	     setBackground( new Color(0,120,0) );
         setForeground( Color.GREEN );
         setPreferredSize( new Dimension(30, 120));
         
         
	}

    public void paintComponent(Graphics g){
    	super.paintComponent(g);
    	int base = 20;
    	
    	if(dices != null){
    		for(int i = 0; i < 4 -dices.getNumDices(); i++){
    			g.setColor(Color.white);
    			g.fillRoundRect(5, base + 23*i ,20,20,5,5);
    			g.setColor(Color.black);
    			g.drawRoundRect(5, base + 23*i ,20,20,5,5);
    			g.fillOval(5 + 3, base + 23*i + 2, 5, 5);
    			g.fillOval(5 + 3, base + 23*i + 8, 5, 5);
    			g.fillOval(5 + 3, base + 23*i + 14, 5, 5);
    			g.fillOval(5 + 12, base + 23*i + 2, 5, 5);
    			g.fillOval(5 + 12, base + 23*i + 8, 5, 5);
    			g.fillOval(5 + 12, base + 23*i + 14, 5, 5);
    			
    		}
    	}
    }
	
    public void setDices(Dices d){
    	dices = d;
    	repaint();
    }
}
