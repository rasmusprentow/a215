package cubedrawer;
public enum MoveButtons { 
	 U, UP ,U2, D, DP, D2, F, FP, F2,  B, BP, B2, L, LP, L2, R, RP, R2,
	 SCREWDRIVER, SCRAMBLE, YOU_KNOW, UNDO, STATISTIC, KOCIEMBA, BEGINNERS, TEST, TEST2,
	 TOGGLEVIEW, BEGINNERSLOW, SUPERFLIP;
	
	@Override
	public String toString(){
		String old = super.toString();
		try {
			if(old.charAt(1) == 'P'){
				return old.charAt(0) + "'";
			} else if(this == SCREWDRIVER){
				return "<html><center>Screw<br>driver</center></html>";
			} else if(this == SCRAMBLE) {
				return "<html><center>Scramble</center></html>";
			} else if(this == YOU_KNOW){
				return "<html><center>Dance</center></html>";
			} else if(this == KOCIEMBA) {
				return "<html><center>Kociemba's<br>solver</center></html>";
			} else if(this == TOGGLEVIEW){
				return "<html><center>Toggle<br>view</center></html>";
			} else if(this == BEGINNERSLOW){
				return "<html><center>Beginners<br>Extra</center></html>";
			} else if(this == BEGINNERS) {
				return "<html><center>Beginner's<br>algorithm</center></html>";
			} else if(this == STATISTIC) {
				return "<html><center>Statistics</center></html>";
			} else {
				return (old.substring(0, 1)) + (old.substring(1)).toLowerCase();
			}
		} catch (IndexOutOfBoundsException e){
			return old;
		}

	}

	/**
	 * This method finds the inverse of the move which calls it.
	 * @return the inverse of the move calling the function
	 * @throws IllegalAccessError if the caller is not a move
	 */
	public MoveButtons invert() throws IllegalAccessError {
		if(this.ordinal() >= 18) {
			throw new IllegalAccessError("Unable to invert " + this);
		}
		if(this.ordinal()%3 == 0) {
			//If we are here then the move object calling this method is a
			//clockwise(not prime) move
			return MoveButtons.values()[this.ordinal() + 1];
		} else if((this.ordinal()-1)%3 == 0) {
			//If we are here, the move is a prime move
			return MoveButtons.values()[this.ordinal() - 1];
		} else {
			//It is not clockwise, nor prime, it must be double twist
			return this;
		}
	}




	//Static methods

	/**
	 * This method finds the inverse of <b>move</b>.
	 * @param move the move which you want to find the inverse
	 * @return the inverse of <b>move</b>
	 * @throws IllegalAccessError if <b>move</b> is not a move
	 */
	public static MoveButtons inverseOf(MoveButtons move) throws IllegalAccessError {
		return move.invert();
	}

	/**
	 * 
	 * @param move
	 * @return
	 * @throws IllegalAccessError
	 */
	public static MoveButtons[] inverseOf(MoveButtons[] move) throws IllegalAccessError {

		MoveButtons temp;

		if(move.length%2 == 1) {
			move[move.length/2] = move[move.length/2].invert();
		} 
		try {
			for(int i = 0 , j = move.length-1 ; i < j ; i++ , j--) {
				temp = move[i];
				move[i] = move[j];
				move[j] = temp;
				move[i] = move[i].invert();
				move[j] = move[j].invert();
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			
		}
		
		return move;
	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean isSameFace(MoveButtons a , MoveButtons b)
	throws IllegalAccessError {
		if(a.ordinal() >= 18 || a.ordinal() >= 18) {
			throw new IllegalAccessError("One of the moves is not a face");
		}

		if(a.ordinal()/3 == b.ordinal()/3) {
			return true;
		}

		return false;
	}
}