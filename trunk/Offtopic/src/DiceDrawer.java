


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class DiceDrawer extends JPanel {
	private int diceVal;
    //public static void main(String[] args){	}
    
    public DiceDrawer(int i){
    	this.setPreferredSize(new Dimension(51,51));
    	diceVal = i;
    }
    public DiceDrawer(int i, Color c){
    	this.setBackground(c);
    	this.setPreferredSize(new Dimension(51,51));
    	diceVal = i;
    }
    
	public static void drawDie(Graphics g, int val, int x, int y){
		int w = 13;
		g.setColor(Color.white);
		g.fillRoundRect(x, y, 50, 50, 12, 12);
		g.setColor(Color.black);
		g.drawRoundRect(x, y, 50, 50, 12, 12);
		if(val%2 != 0){
			g.fillOval(x + 18, y + 18, w, w); // midle dot
		}
		if(val > 1){
			g.fillOval(x + 32, y + 4, w, w); // up right
		}
		if(val > 1){
			g.fillOval(x + 4, y + 32, w, w); //down left
		}
		if(val > 3){
			g.fillOval(x + 32, y + 32, w, w); //down right
			g.fillOval(x + 4, y + 4, w, w); // up left
		}
		if(val == 6){
			g.fillOval(x + 32, y + 18, w, w); // middle right
			g.fillOval(x + 4, y + 18, w, w); // umiddle left
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if(diceVal != 0){
			drawDie(g, diceVal, 0,0);
		}
	}
	
	public void setDie(int i){
		diceVal = i;
		repaint();
	}
}
