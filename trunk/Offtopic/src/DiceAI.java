
public class DiceAI {
	private int turn;
	private int players;
	private String profile;
	private int die;
	private int amount;
	
	
	DiceAI(){
		profile = "aha";
	}
	
	DiceAI(String p){
		
		profile = p;
	}
	
	public boolean makeTurn(boolean starting, int turn_, int players_, int bettetDie, int bettetAmount, Dices comDices){
		if(profile.equals("John")){
			
				
			return john(starting, turn_,players_,bettetDie, bettetAmount, comDices);
		} else {
			return defaultProfile(starting, turn_,players_,bettetDie, bettetAmount, comDices);
		}
		
	}
	
	private boolean neverTooBeCalledExampleMethod(boolean starting, int turn_, int players_, int bettetDie, int bettetAmount, Dices comDices){
		
		int dicesLeft = players * 4 - (players - 1) * turn; // This calculates the number of dies left.
		//  The dices class:
		comDices.compareAllDices(); //  This is to check wheter all dices is the same. IT will return the die if they are else it returns 0;
		comDices.isStair(); // Checks you got a stair;
		comDices.getAmoutAtValue(2); // checks the number of dices with a given value;
		comDices.getMostCommon(); // Find the most common die. 0 if none is more common than the other.
		// There are more methods but you'll have to find them for your selves. 

		
		

		// When you know what dices you will bet declare them as following
		die = 3;
		amount = 4;
		// in this case the bet is 4 X 3. 
		// Remember to return false;
		
		
		return true;  // Return true if the computer should lift;
	}
	
	
	private boolean defaultProfile(boolean starting, int turn_, int players_, int bettetDie, int bettetAmount, Dices comDices){
		int playerDie = bettetDie;
		int playersAmount = bettetAmount;
		turn = turn_;
		players = players_;
		double theMistrustFactor = 2.5; //High for a big mistrust
		int maxOfAKind, cubesLeft;
		System.out.println("Turn " + turn  + " players " + players);
		cubesLeft = players * 4 - (players - 1) * turn;
		if(starting){  /// The computer starts
			amount = cubesLeft/3 + 1;
			if(comDices.isStair()){
				die = 6;
			} else if(comDices.compareAllDices()){ // All dicves are equal
				die = comDices.getDie(0);
			} else if(comDices.getMostCommon() != 0){
				if(playerDie < comDices.getMostCommon()){
					die = comDices.getMostCommon();
				} else {
					die = comDices.getMostCommon();
				}
			} else {	
				die = 6;
			}
		} else { // Other  player Starts
			if(!comDices.isStair()){
				maxOfAKind = (int)Math.round((cubesLeft - comDices.getNumDices())/theMistrustFactor)  + comDices.getAmoutAtValue(playerDie) +  comDices.getAmoutAtValue(1);
			} else {
				maxOfAKind = (int)Math.round((cubesLeft - comDices.getNumDices())/theMistrustFactor) + comDices.getNumDices() + 1;
			}
			if(maxOfAKind == 0){ 
				maxOfAKind = 1;
			}
			if(playersAmount > maxOfAKind){

				return true;
			} else { // determine the next move
				//if(comDices.getNumcomDices() > 1){
				if(comDices.isStair()){
					amount = playersAmount + 1;
					die = playerDie;
				} else if(comDices.compareAllDices()){ // All dicves are equal
					amount = playersAmount + 1;
					die = comDices.getDie(0);
				} else if(comDices.getMostCommon() != 0){
					if(playerDie < comDices.getMostCommon()){
						amount = playersAmount;
						die = comDices.getMostCommon();
					} else {
						amount = playersAmount + 1;
						die = comDices.getMostCommon();
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
		}
		return false;
	}
	
	private boolean john(boolean starting, int turn_, int players_, int bettetDie, int bettetAmount, Dices comDices){
		
		return true;
	}
	

	
	
	public int getDie(){
		return die;
	}
	
	public int getAmount(){
		return amount;
	}
	
}
