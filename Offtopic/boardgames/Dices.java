package boardgames;
/**
 * Simple class for a abject used to maintain a certain number of dices.
 * The class contain several methods for thinking box.
 * @author Rasmus Prentow
 * 
 * 
 *
 */
public class Dices {
	// The number of dices.
	private int numDices; 
	
	//An array with all the dices
	private int[] dices; 
	
	/**
	 * This constructor creates one dice and rolls it.
	 */
	public Dices(){
	
		dices = new int[1];	
		numDices = 1;
		rollDices();
	}
	/** 
	 * Creates i dices and roll those.
	 * @param i Declares the number of dices. Must be > 0
	 */
	public Dices(int i){
		if(i < 1){
			throw new IllegalArgumentException("Must be int higher than 0");
		}
		dices = new int[i];	
		numDices = i;
		rollDices();
		
	}
	
	/**
	 * Roll all dices and sort them in descending order.
	 */
	public void rollDices(){
		for (int i = 0; i < numDices; i++){
			dices[i] = (int)(Math.random()*6) + 1;	
			//System.out.println(i);
		}
		bobbleSortDices();
		//System.out.println("The dices has rolled" + dices[0]);
	}
	
	/**
	 * Roll one dice
	 * @param The dice to be rolled
	 */
	public void rollDices(int i){
		if(numDices < i){
			throw new IllegalArgumentException("No such Die");
		}
		dices[i] = (int)(Math.random()*6) + 1;		
	}
	
	/**
	 * Throws illegalArgumentException if the dice dosen't exist
	 * @param i the id of the dice to be returned.
	 * @return The value of dice i.
	 */
	public int getDie(int i){
		if(numDices < i){
			throw new IllegalArgumentException("No such Die");
		}
		return dices[i]; 
	}
	
	/**
	 * Used if the class is used for only one die.
	 * @return The value of the first die.
	 */
	public int getDie(){	
		return dices[0]; 
	}
	
	/**
	 * 
	 * @return The number of dices in the object.
	 */
	public int getNumDices(){
		return numDices;
	}
	
	/**
	 * 
	 * @return The average value of the dies.
	 */
	public double getAverage(){
		int sum = 0;
		for(int i = 0; i < numDices; i++) {
			sum += dices[i];
		}
		return (double)sum/numDices;
	}
	
	/**
	 * 
	 * @return Returns the sum of all dices.
	 */
	public int sumDices(){
		int sum = 0;
		for(int i = 0; i < numDices; i++) {
			sum += dices[i];
		}
		return sum;
	}
	
	/**
	 * 
	 * @param n the value to check on
	 * @return the number of occurrences of n in the dices
	 */
	public int getAmoutAtValue(int n){
		int amount = 0;
		for(int i = 0; i < numDices; i++) {
			if(n == dices[i]){
				amount++;
			}
		}
		return amount;
	}
	
	/**
	 * Prints all dices in the console.
	 */
	public void printAllDices(){
		for(int i = 0; i < numDices; i++) {
			System.out.print(dices[i] + " ");
		}
	}
	
	/**
	 * Prints all dices in the console with a new line for each.
	 */
	public void printAllDicesln(){
		for(int i = 0; i < numDices; i++) {
			System.out.println(dices[i]);
		}
	}
	
	/**
	 * Gets all the dices.
	 * @return The dices as an int array.
	 */
	public int[] getDices(){
		return dices;
	}
	
	public String getDicesAsString(){
		String s = "";
		for(int i = 0; i < numDices; i++){
			s += Integer.toString(dices[i]);
		}
		
		return s;
	}
	
	
	/**
	 * Compares all dices to see if they are the same.
	 * @return True if they are all equal
	 */
	public boolean compareAllDices(){
		for(int i = 0; i < numDices - 1; i++) {
			if(dices[i] != dices[i + 1]){
				return false;
			}
		}
		return true; 
	}
	
	/**
	 * Adds a new die and roll this die.
	 */
	public void addDice(){
		int[] tempArray = new int[numDices + 1]; 
		for(int i = 0; i < numDices; i++){
			tempArray[i] = dices[i];
		}
		tempArray[numDices] = (int)(Math.random()*6) + 1;
		dices = tempArray;
		numDices++;
	}
	
	/**
	 * Removes the last die.
	 */
	public void removeDice(){
		int[] tempArray = new int[numDices - 1]; 
		for(int i = 0; i < numDices - 1; i++){
			tempArray[i] = dices[i];
		}
		//tempArray[numDices + 1] = (int)(Math.random()*6) + 1;
		dices = tempArray;
		numDices--;
	}
	
	/**
	 * Sorts all the dices using the bobbleSort algorithm.
	 */
	private  void bobbleSortDices(){
		int temp;
		for (int i = 1; i < numDices; i++){
			for (int k = 0; k < numDices - i; k++){
				if(dices[k] > dices[k + 1]){
					temp = dices[k + 1];
					dices[k + 1] = dices[k];
					dices[k] = temp;
				}
			}
		}
	
	
	}
	
	/**
	 * Used to check if the dies are a stair.
	 * @return true if the dies are a stair.
	 */
	public boolean isStair(){
		for(int i = 0; i < numDices; i++){
			if(dices[i] != i + 1){
				return false;
			}
		}
		return true;
	}
	
	

	/**
	 * Find out the most common die in a set.
	 * @return The value of the most common die. If there is no die there are more common than other dies it returns 0.
	 */
	public int getMostCommon(){
		int[] num;
		num = new int[7];
		int whichNum = 0, max = 0;
		for (int i = 0; i < num.length; i++){
			num[i] = 0;
		}
		
		for(int i = 0; i < numDices; i++){
			num[dices[i]] += 1;
		}
		for(int i = 1; i < 7; i++){
			if(num[i] >= max && (i != 1)){
				max = num[i];
				whichNum = i;
			}
		}
		if(max == 1){
			return 0;
		} else { 
			return whichNum;
		}
	}
}
