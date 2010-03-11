

import java.awt.*;
//import java.awt.event.*;
import javax.swing.*;






public class EndRoundDisplay extends JPanel {
	public JPanel centerPanel;
	public DicePanel playerDicePanel;
	public DicePanel comDicePanel;
	private JPanel comPanel;
	private JPanel playerPanel;
	public JButton newGameBtn;
	
	EndRoundDisplay(){
		initialize();
	}
	EndRoundDisplay(String winner, Dices[] dices, int players, int bettetDie, int bettetAmount, boolean betWithHeld, boolean gameOver){
		this.setPreferredSize(new Dimension(200,300));
		this.setBackground(new Color(0,120,0));
		this.setLayout(new BorderLayout());
		this.requestFocus();
		if(gameOver){
			add(new DiceLabel(" " + winner + " won the game",  new Font("Arial",Font.BOLD,30)) , BorderLayout.NORTH);
		} else {
			add(new DiceLabel(" " + winner + " won this round",  new Font("Arial",Font.BOLD,30)) , BorderLayout.NORTH);
		}
		centerPanel = new JPanel();
		centerPanel.setBackground(new Color(0,120,0));
		centerPanel.setLayout(new FlowLayout());
		
		comPanel = new JPanel();
		playerPanel = new JPanel();
		comPanel.setLayout(new BorderLayout());
		playerPanel.setLayout(new BorderLayout());
		
		playerDicePanel = new DicePanel();
		comDicePanel = new DicePanel();
	//	dices[0].printAllDices();
		playerDicePanel.setDices(dices[0]);
		comDicePanel.setDices(dices[1]);
		playerPanel.setBackground(new Color(0,120,0));
		comPanel.setBackground(new Color(0,120,0));
		
		
		JPanel playerFlow = new JPanel();
		JPanel comFlow = new JPanel();
		playerFlow.setLayout(new FlowLayout());
		playerFlow.setBackground(new Color(0,120,0));
		
		comFlow.setLayout(new FlowLayout());
		comFlow.setBackground(new Color(0,120,0));
		Font myFont = new Font("Arial",Font.BOLD,22);

		playerFlow.add(playerDicePanel);
		comFlow.add(comDicePanel);
		
		
		playerPanel.add(new DiceLabel("Your dices were:   ", myFont), BorderLayout.NORTH);
		comPanel.add(new DiceLabel("The coms dices were:  ", myFont), BorderLayout.NORTH);
		playerPanel.add(playerFlow, BorderLayout.CENTER);
		comPanel.add(comFlow, BorderLayout.CENTER);
		
		JPanel betFlow = new JPanel();
		betFlow.setLayout(new BorderLayout());
		
		betFlow.add(new DiceLabel("The bet were:"), BorderLayout.NORTH);
		betFlow.setBackground(new Color(0,120,0));
		BetDisplayer betDisplay = new BetDisplayer();
		betDisplay.setDie(bettetDie);
		betDisplay.setAmount(bettetAmount);
		betFlow.add(betDisplay, BorderLayout.CENTER);
		
		if(betWithHeld){
			betFlow.add(new DiceLabel("The bet was true!!"), BorderLayout.SOUTH);
		} else {
			betFlow.add(new DiceLabel("The bet was a lie!!"), BorderLayout.SOUTH);
		}
		
		centerPanel.add(playerPanel);
		centerPanel.add(comPanel);
		centerPanel.add(betFlow);
		if(gameOver){
			newGameBtn = new JButton("New game");
			add(newGameBtn, BorderLayout.SOUTH);       
		}

		this.add(centerPanel, BorderLayout.CENTER);
		//repaint();
		//JPanel dieCounter = newJPanel();
		//dieCounter.setLayout(new FlowLayout());
		//dieCounter.add(new )
		
	}
	
	public void initialize(){
		
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
	}
	
}
