package boardgames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JLabel;

import javax.swing.JPanel;

public class BetDisplayer extends JPanel {
	private int die;
	private int amount;
	private JLabel amountLabel;
	private DiceDrawer drawDie;
	private DiceLabel playerStartLabel;
	JLabel theBigX;
	
    BetDisplayer(){
	     setBackground( new Color(0,120,0) );
         setForeground( Color.GREEN );
         //smallFont = new Font("SansSerif", Font.PLAIN, 12);
         //bigFont = new Font("Serif", Font.BOLD, 14);
         setPreferredSize( new Dimension(250, 200));
         setLayout(null);
         playerStartLabel = new DiceLabel("You start");
         playerStartLabel.setBounds(30, 30, 200, 100);
        add(playerStartLabel);
	}
    public void paintComponent(Graphics g){
    	super.paintComponent(g);
    	/*
    	if(amount != 0){
    		DiceDrawer.drawDie(g, die, 150, 100); 	
    		g.drawString(Integer.toString(amount),50 , 100);
    	}
    
    	*/
    }
	
    public void setDie(int i){
    	if(die == 0){
    		remove(playerStartLabel);
    		amountLabel = new JLabel(Integer.toString((amount)));
    		amountLabel.setFont( new Font("Arial",Font.BOLD,48));
    		amountLabel.setBounds(40, 40, 120, 120);

    		theBigX = new JLabel("X");
    		theBigX.setFont( new Font("Arial",Font.BOLD,56));
    		theBigX.setBounds(100,40,70,120);

    		add(amountLabel);
    		add(theBigX);
    		drawDie = new DiceDrawer(i, new Color(0,120,0));
    		drawDie.setBounds(160,75,52,120);
    		add(drawDie);
    	}
    	die = i;
    	drawDie.setDie(i);
    	repaint();
    }
    
    public void setAmount(int i){
    	amountLabel.setText(Integer.toString(i));
    	amount = i;
    	repaint();
    }
    
    public int getAmount(){
    	return amount;
    }
    public int getDie(){
    	return die;
    }
    
    public void clearBet(){
    	if(die != 0){
    		remove(theBigX);
    		remove(drawDie);
    		remove(amountLabel);
    		
    	}
    	remove(playerStartLabel);
    	 playerStartLabel = new DiceLabel("You start");
          playerStartLabel.setBounds(30, 30, 200, 100);
         add(playerStartLabel);
         die = 0;
         amount = 0;
         repaint();
    }
}
