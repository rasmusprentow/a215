
 import java.awt.*;

import javax.swing.JPanel;

public class DieSelecter extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int highlightedDie = 2;
	private int base = 20;  
	private int highXY = 60;
	private int highY = 120;

	
	public DieSelecter(){
		//setPreferredSize( new Dimension(300, 200));
		this.setBackground(Color.GREEN);
	}
	public DieSelecter(Color c){
		//setPreferredSize( new Dimension(300, 200));
		this.setBackground(c);
	}
	public void paintComponent(Graphics g) { 
	    super.paintComponent(g);  // fill with background color.
	    int x = 0;
	    int y = 0;
	  
	    switch(highlightedDie){
	    case 2:
	    	x = base;
	    	y = base;
	    	break;
	    case 3:
	    	y = base;
	    	x = base + 60;
	    	break;
	    case 4:
	    	y = base + 60;
	    	x = base;
	    	break;
	    case 5:
	    	x = base + 60;
	    	y = base + 60;
	    	break;
	    default :
	    	x = base + 30;
	    	y = base + 120;
	    	break;
	    }
	    g.setColor(Color.YELLOW);
	    g.fillRoundRect(x - 4, y - 4, base + 38, base + 38, 12, 12);
	    DiceDrawer.drawDie(g,2, base, base);
	    DiceDrawer.drawDie(g,3, base + 60, base);        
	    DiceDrawer.drawDie(g,4, base, base + 60);    
	    DiceDrawer.drawDie(g,5, base + 60, base + 60);
	    DiceDrawer.drawDie(g,6, base  + 30, base + 120);
	}
	
	public void setHighlightedDie(int i){
		highlightedDie = i;
		repaint();
	}
	
	public int getHighlightedDie(){
		return highlightedDie;
	}
	public int getDie(){
		return highlightedDie;
	}
	
	public int whichDieIsThis(int x, int y){
		x = x - base;
		y = y - base;
		if(y >= highY && y < highY + 51){
			if(x > 30 && x < 80){
				return 6;
			}
		} 
		if(y < highY){
			if(y > highXY){
				if(x <= highXY && x >= 0){
					return 4;
				}
				if(x >= highXY && x <= highXY + 51){
					return 5;
				}
				
			} 
			if (y < highXY){
				if(x <= highXY && x >= 0){
					return 2;
				}
				if(x >= highXY && x <= highXY + 51){
					return 3;
				}
			}
		}
		return 0;
	}
}
