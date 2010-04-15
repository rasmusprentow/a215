package cubedrawer;

enum MoveButtons { 
	 U, UP ,U2, D, DP, D2, F, FP,F2,  B, BP, B2, L, LP, L2, R, RP , R2,SCREWDRIVER, SCRAMBLE;
	
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