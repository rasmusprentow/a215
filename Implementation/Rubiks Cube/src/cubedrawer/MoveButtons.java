package cubedrawer;

enum MoveButtons { 
	 U, UP, D, DP, F, FP,  B, BP, L, LP, R, RP ,SCREWDRIVER;
	
	@Override
	public String toString(){
		String old = super.toString();
		try {
			if(old.charAt(1) == 'P'){
				return old.charAt(0) + "'";
			} else if(this == SCREWDRIVER){
				return "<html><center>Screw<br>driver</center></html>";
			}
		} catch (IndexOutOfBoundsException e){
			return old;
		}
		return old;
		
	}
}