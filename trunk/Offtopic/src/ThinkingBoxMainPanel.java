

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;








/**
 * Draws a die with a givin int.
 * @author rasmus
 *
 */
public class ThinkingBoxMainPanel extends JPanel implements MouseListener {
	public JPanel mainPanel;
	private EndRoundDisplay ERD;
	private EndRoundDisplay EGD;
	public JPanel upperLeftCorner;
	public JPanel upperRightCorner;
	public JPanel lowerLeftCorner;
	public JPanel lowerRightCorner;
	
	public DicePanel playerDicePanel;
	public DieSelecter dieSelecter;
	private AmountSelecter amountSelecter;
	
	public static Color globalBC = new Color(0,120,0);
	//public int playersAmount;
	
	public JLabel playerAmountLabel;
	public JButton incrementButton;
	public JButton decrementButton;
	public JButton betBtn;
	public JButton aNewGameBtn;
	
	private DiceLabel progressLabel;
	private BetDisplayer betDisplay;
	
	
	private Dices[] dices;
	//private Bet[] bet;
	private int numberOfPlayer = 2;
	private int numberOfDices = 4;
	private JButton tempRollBtn;
	private JButton raiseBtn;
	private JButton liftBtn;
	private boolean gameInProgress = false;
	private int playerInTurn;
	private int turn = 0;
	private int winner;
	private MiniDicePanel comMiniPanel = new MiniDicePanel();
	private MiniDicePanel playerMiniPanel = new MiniDicePanel();
	private boolean computerShouldStart;
	private DiceAI comAI;
	
	public ThinkingBoxMainPanel(){

		setLayout(new BorderLayout());
		setBackground( globalBC );
		mainPanel = new JPanel();
		mainPanel.setLayout( new GridLayout(2,2) );
		mainPanel.setForeground( globalBC );

	
		
		/////// ULC
		upperLeftCorner = constructCornerFrame(upperLeftCorner);
		playerDicePanel = new DicePanel();
		upperLeftCorner.add( new DiceLabel(" Your dices:"), BorderLayout.NORTH);
		JPanel flowPanel = new JPanel(); 
		flowPanel.setLayout(new FlowLayout());
		flowPanel.setBackground(globalBC);
		flowPanel.add(playerDicePanel);
		upperLeftCorner.add(flowPanel, BorderLayout.CENTER);
		//tempRollBtn = new JButton("Roll");
		//tempRollBtn.addMouseListener(this);
		//upperLeftCorner.add(tempRollBtn, BorderLayout.SOUTH);
		mainPanel.add(upperLeftCorner);
		upperLeftCorner.add(playerMiniPanel, BorderLayout.WEST);
		////// END ULC
		
	
		
	
		
		
		/////// URC 
		upperRightCorner = constructCornerFrame(upperRightCorner);
		liftBtn = new JButton("Lift");
		liftBtn.addMouseListener(this);
		betDisplay = new BetDisplayer();
		betDisplay.setLayout(null);
		upperRightCorner.add(new DiceLabel(" Computers bet:"), BorderLayout.NORTH);
		upperRightCorner.add(betDisplay, BorderLayout.CENTER);
		upperRightCorner.add(liftBtn, BorderLayout.SOUTH);
		upperRightCorner.add(comMiniPanel, BorderLayout.EAST);
		mainPanel.add(upperRightCorner);
		////// END URC
		
		
		/////////// LLC
		lowerLeftCorner = constructCornerFrame(lowerLeftCorner);
		amountSelecter = new AmountSelecter(globalBC);
		
		JPanel buttonPanel = new JPanel();
		incrementButton = new JButton("+");
		decrementButton = new JButton("-");
		betBtn = new JButton("Bet");
		betBtn.addMouseListener(this);
		incrementButton.addMouseListener(this);
		decrementButton.addMouseListener(this);
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.setBackground(globalBC);
		buttonPanel.add(decrementButton);
		buttonPanel.add(incrementButton);
		buttonPanel.add(betBtn);
		lowerLeftCorner.add(new DiceLabel("Raise By:"), BorderLayout.NORTH);
		lowerLeftCorner.add(amountSelecter, BorderLayout.CENTER);
		lowerLeftCorner.add(buttonPanel, BorderLayout.SOUTH);
		mainPanel.add(lowerLeftCorner);
		/////// END LLC
		
		
		
		///////////// LRC
		lowerRightCorner = constructCornerFrame(lowerRightCorner);
		dieSelecter = new DieSelecter(globalBC);
		lowerRightCorner.add(dieSelecter);
		dieSelecter.addMouseListener(this);
		mainPanel.add(lowerRightCorner);
		////// END LRC
		
		
		add(mainPanel, BorderLayout.CENTER);
		progressLabel = new DiceLabel(" ");
		add(progressLabel, BorderLayout.SOUTH);

		//setBorder(BorderFactory.createLineBorder( new Color(130,50,40), 3) );
		newGame();
	}

	private void newGame(){
		playerInTurn = (int)(Math.random()*numberOfPlayer);
		turn = 0;
		gameInProgress = true;
		comAI = new DiceAI();
		dices = new Dices[numberOfPlayer];
		//bet = new Bet[numberOfPlayer];
		for(int i = 0; i < numberOfPlayer; i++){
			dices[i] = new Dices(numberOfDices);
			//bet[i] = new Bet(i);
		}
		amountSelecter.setAmount(1);
		playerDicePanel.setDices(dices[0]);
		if(playerInTurn != 0){
			computerShouldStart = true;
			computerTurn();
		} else {
			progressLabel.setText("Your turn!");
		}
		comMiniPanel.setDices(dices[1]);
		playerMiniPanel.setDices(dices[0]);
	}
	
	private void computerTurn(){
		int playersAmount = amountSelecter.getAmount();
		int playerDie = dieSelecter.getHighlightedDie();
		int die = 0;
		int amount = 0;
		int players = numberOfPlayer;
		
		
		/*
		
		double theMistrustFactor = 2.5; //High for a big mistrust
		int maxOfAKind, cubesLeft;
		System.out.println("Turn " + turn  + " players " + players);
		cubesLeft = players * 4 - (players - 1) * turn;
		if(computerShouldStart){  /// The computer starts
			computerShouldStart = false;
				amount = cubesLeft/3 + 1;
				if(dices[playerInTurn].isStair()){
					die = 6;
				} else if(dices[playerInTurn].compareAllDices()){ // All dicves are equal
					die = dices[playerInTurn].getDie(0);
				} else if(dices[playerInTurn].getMostCommon() != 0){
					if(playerDie < dices[playerInTurn].getMostCommon()){
						die = dices[playerInTurn].getMostCommon();
					} else {
						die = dices[playerInTurn].getMostCommon();
					}
				} else {	
					die = 6;
				}
		} else { // Other  player Starts
			if(!dices[playerInTurn].isStair()){
				maxOfAKind = (int)Math.round((cubesLeft - dices[playerInTurn].getNumDices())/theMistrustFactor)  + dices[playerInTurn].getAmoutAtValue(playerDie) +  dices[playerInTurn].getAmoutAtValue(1);
			} else {
				maxOfAKind = (int)Math.round((cubesLeft - dices[playerInTurn].getNumDices())/theMistrustFactor) + dices[playerInTurn].getNumDices() + 1;
			}
			if(maxOfAKind == 0){ 
				maxOfAKind = 1;
			}
			if(playersAmount > maxOfAKind){
				progressLabel.setText("The computer Lifted");
				System.out.println(playerInTurn + " the com lifted");
	

				lift(playerInTurn);
				return;
			} else { // determine the next move
				//if(dices[playerInTurn].getNumdices[playerInTurn]() > 1){
					if(dices[playerInTurn].isStair()){
						amount = playersAmount + 1;
						die = playerDie;
					} else if(dices[playerInTurn].compareAllDices()){ // All dicves are equal
						amount = playersAmount + 1;
						die = dices[playerInTurn].getDie(0);
					} else if(dices[playerInTurn].getMostCommon() != 0){
						if(playerDie < dices[playerInTurn].getMostCommon()){
							amount = playersAmount;
							die = dices[playerInTurn].getMostCommon();
						} else {
							amount = playersAmount + 1;
							die = dices[playerInTurn].getMostCommon();
						}
					} else {
						if(playerDie < 6){
							amount = playersAmount;
							die = playerDie + 1;
						} else {
							amount = playersAmount + 1;
							die = playerDie;
						}
					}				
			}
			
			*/
			if(comAI.makeTurn(computerShouldStart, turn, numberOfPlayer, playerDie, playersAmount, dices[playerInTurn])){
				progressLabel.setText("The computer Lifted");
				System.out.println(playerInTurn + " the com lifted");
	

				lift(playerInTurn);
				return;
			} else {
				die = comAI.getDie();
				amount = comAI.getAmount();
			}
			computerShouldStart = false;
			playerInTurn = 0;
			//System.out.println("Player " + player + " bet " + amount + " " + die + "'s");
			//return true;
			
			
			
		
		if(die == 1){
			die = 6;
		}
		//playerDie = lowerRightCorner.getHighlightedDie();
		//betDisplay.clearBet();
		betDisplay.setDie(die);
		betDisplay.setAmount(amount);
		if(die != 6){
			amountSelecter.setAmount(amount);
			dieSelecter.setHighlightedDie(die + 1);
		} else {
			amountSelecter.setAmount(amount + 1);
			dieSelecter.setHighlightedDie(2);	
		}
		playerInTurn = 0;
		//repaint();
		this.progressLabel.setText("The computer has made its bet!!");
	}
	
	private void lift(int player){
		System.out.println(player);
		int bettetDie, total = 0, bettetAmount;
		String s;
		if(player == 1){
			bettetDie = dieSelecter.getDie();
			bettetAmount = amountSelecter.getAmount();
		} else {
			bettetDie = betDisplay.getDie();
			bettetAmount = betDisplay.getAmount();
		}
		for(int i = 0; i < numberOfPlayer; i++){
			if(dices[i].isStair()){
				total += dices[i].getNumDices();
				total += 1;
			} else {
			total += dices[i].getAmoutAtValue(bettetDie);
			total += dices[i].getAmoutAtValue(1);
			}
		}
		System.out.println("the toal" + total);
		if(total >= bettetAmount){
			// ifter wins
			System.out.println("Lifter Looses");

			if(player == 0){
				//System.out.println("winner is zero");
				winner = 1;
			} else {
				winner = 0;
			}
		} else {
			winner = player;
		}
		//System.out.println(winner);
		if(winner == 1){
			s = "Computer";
		} else {
			s = "You";
		}
		if(dices[winner].getNumDices() == 1){
			
			gameInProgress = false;
			
			this.remove(mainPanel);
			EGD = new EndRoundDisplay(s, dices, numberOfPlayer, bettetDie, bettetAmount, (winner != player), true);
			aNewGameBtn = EGD.newGameBtn;
			aNewGameBtn.addMouseListener(this);
			add(EGD, BorderLayout.CENTER);
			repaint();
			
			progressLabel.setText(" ");
		} else {
			//progressLabel.setText(winner + " won a rond " + dices[0].getDicesAsString() + " " +  dices[1].getDicesAsString());
			
			//// End of round tabel;
			
			
			
			System.out.println("AHAHAHAHAHA");
			remove(mainPanel);
			
			System.out.println(ERD);
			ERD = new EndRoundDisplay(s, dices, numberOfPlayer, bettetDie, bettetAmount, (winner != player), false);
			add(ERD, BorderLayout.CENTER);
			ERD.addMouseListener(this);
			progressLabel.setText("You lifted!");
			repaint();
			

		}
		
	}
	
	private void prepareForNewRound(){
		System.out.println("Starting preparing for new round");
		this.remove(ERD);
		ERD.removeMouseListener(this);
		ERD = null;
		add(mainPanel);
		repaint();
		dices[winner].removeDice();
		for(int i = 0; i < numberOfPlayer; i++){
			dices[i].rollDices();
		}
		betDisplay.clearBet();
		playerDicePanel.setDices(dices[0]);
		turn++;
		comMiniPanel.setDices(dices[1]);
		playerMiniPanel.setDices(dices[0]);
		amountSelecter.setAmount(1);
		if(winner == 1000){
			System.out.println("The winner is 1000");
		}
		if(winner == 1){
			playerInTurn = 0;
			System.out.println("We prepare a new round com won and you start");
		} else {
			computerShouldStart = true;
			System.out.println("We prepare a new round You Won so computer will start");
			playerInTurn = 1;
			computerTurn();
		}
		winner = 1000;
		repaint();
	}
	
	public void mousePressed(MouseEvent evt) {
		Component source = (Component)evt.getSource();
		if(!gameInProgress){
			if(source == tempRollBtn){
				newGame();
			}
		}
	
		if(source == dieSelecter){
			int dieClicked = dieSelecter.whichDieIsThis(evt.getX(), evt.getY());
			if((dieClicked >= 2)){
				dieSelecter.setHighlightedDie(dieClicked);
			}
			// System.out.println(source);
		}
		if((betDisplay.getAmount() < amountSelecter.getAmount()) && amountSelecter.getAmount() > 0){
			if(source == decrementButton){
				//playersAmount -= 1;
				if(amountSelecter.getAmount() > 0 ){
				amountSelecter.decreaseAmount();
				}
				//playerAmountLabel.setText(Integer.toString(playersAmount));
				// System.out.println(source);
			}
		}
		if(source == incrementButton){
			//playersAmount += 1;
			amountSelecter.increaseAmount();
			// System.out.println(source);
		}
		
		if(source == ERD){
			prepareForNewRound();
		}
		
		//System.out.println(source);
		if(source == aNewGameBtn){
			this.remove(EGD);
			aNewGameBtn.removeMouseListener(this);
			EGD = null;
			add(mainPanel);
			repaint();
			newGame();
		}
		

		if(source == liftBtn){
		
			if(gameInProgress){
				if(betDisplay.getAmount() > 0){
					lift(0);
				} else {
					this.progressLabel.setText("You can't distrust nothing!");
				}
			} else {
				progressLabel.setText("Face it! The games over!");
			}
			
		}
		if(source == betBtn){
			if(gameInProgress){
				if((betDisplay.getAmount() < amountSelecter.getAmount()) || (betDisplay.getDie() < dieSelecter.getHighlightedDie() && betDisplay.getAmount() == amountSelecter.getAmount())){
					if(amountSelecter.getAmount() > 0){
					playerInTurn++;
					//turn++;
					System.out.println(playerInTurn + " should be 1");
					computerShouldStart = false;
					computerTurn();
					}
				} else {
					this.progressLabel.setText("Illigal bet! Must be atleast " + betDisplay.getAmount()+ " X " + (betDisplay.getDie() + 1) + " or " + (betDisplay.getAmount() + 1) + " X " + betDisplay.getDie() );
				}
			} else {
				progressLabel.setText("Face it! The games over!");
			}
			
		}
		
	}

	public void mouseClicked(MouseEvent evt) { }
	public void mouseReleased(MouseEvent evt) { }
	public void mouseEntered(MouseEvent evt) { }
	public void mouseExited(MouseEvent evt) { }

	
		
	private JPanel constructCornerFrame(JPanel p){
		p = new JPanel();
		p.setBackground(globalBC);
		p.setForeground(globalBC);
		p.setLayout(new BorderLayout());
		//mainPanel.add(p);
		return p;
	}
}
