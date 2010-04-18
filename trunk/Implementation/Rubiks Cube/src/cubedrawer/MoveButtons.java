package cubedrawer;
enum MoveButtons { 
	 U, UP ,U2, D, DP, D2, F, FP, F2,  B, BP, B2, L, LP, L2, R, RP, R2, SCREWDRIVER, SCRAMBLE, YOU_KNOW;
	
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
				return "<html><center>Dance!!</center></html>";
			}
		} catch (IndexOutOfBoundsException e){
			return old;
		}
		return old;
		
	}
	
	/**
	 * This method finds the inverse of the move which calls it.
	 * @return the inverse of the move calling the function
	 * @throws IllegalAccessError if the caller is not a move
	 */
	public MoveButtons inverse() throws IllegalAccessError {
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
	
	/**
	 * This method finds the inverse of <b>move</b>.
	 * @param move the move which you want to find the inverse
	 * @return the inverse of <b>move</b>
	 * @throws IllegalAccessError if <b>move</b> is not a move
	 */
	public static MoveButtons inverseOf(MoveButtons move) throws IllegalAccessError {
		return move.inverse();
	}
}