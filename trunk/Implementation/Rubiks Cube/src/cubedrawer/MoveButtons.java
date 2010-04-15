package cubedrawer;

enum MoveButtons { 
	 U, UP, D, DP, F, FP,  B, BP, L, LP, R, RP ,RESET;
	
	@Override
	public String toString(){
		String old = super.toString();
		try {
			if(old.charAt(1) == 'P'){
				return old.charAt(0) + "'";
			}
		} catch (IndexOutOfBoundsException e){
			return old;
		}
		return old;
		
	}
}