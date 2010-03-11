

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JLabel;

import javax.swing.JPanel;

public class AmountSelecter extends JPanel {
	private int amount;
	private JLabel amountLabel;

    AmountSelecter(Color c){
    	this.setBackground(c);
    	initialize();

	}
    AmountSelecter(){
    	initialize();

	}
    private void initialize(){
    	JPanel amountPanel = new JPanel();
		setLayout(null);
		amountLabel = new JLabel(Integer.toString((amount)));
		amountLabel.setFont( new Font("Arial",Font.BOLD,60));
		amountLabel.setBounds(50, 40, 120, 120);

		JLabel theBigX = new JLabel("    X  ");
		theBigX.setFont( new Font("Arial",Font.BOLD,72));
		theBigX.setBounds(70,40,200,120);
		add(amountLabel);
		add(theBigX);
    }
    
    public void paintComponent(Graphics g){
    	super.paintComponent(g); 
    }
	
    public void setAmount(int i){
    	amount = i;
    	if(amount == 0){
    		amount = 1;
    	}
    	amountLabel.setText(Integer.toString(i));
    	repaint();
    }
    
    public void decreaseAmount(){
    	amount--;
    	if(amount == 0){
    		amount = 1;
    	}
    	amountLabel.setText(Integer.toString(amount));
    	repaint();
    }
    
    public void increaseAmount(){
    	amount++;
    	amountLabel.setText(Integer.toString(amount));
    	repaint();
    }
   public int getAmount(){
	   return amount;
   }
}
