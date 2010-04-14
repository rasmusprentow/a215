package cube;

public class EdgeCubie extends Cubie {
	private Facelet primaryFacelet;
	private Facelet secondaryFacelet;
	private Facelet tertiaryFacelet;
	
	public EdgeCubie (Facelet primaryFacelet, Facelet secondaryFacelet, Facelet tertiaryFacelet){ 
		byte nullCount = 0; 
		if(primaryFacelet == null) {
			nullCount++;
		}
		if(secondaryFacelet == null) {
			nullCount++;
		}
		if(tertiaryFacelet == null) {
			nullCount++;
		}
		if(nullCount ==1){
			this.primaryFacelet = primaryFacelet;
			this.secondaryFacelet = secondaryFacelet;
			this.tertiaryFacelet = tertiaryFacelet;	
		} else {
			throw new IllegalArgumentException ("one of the Facelets shall be null");
		}
	}
	
	
	public void setDirection(byte direction){
		if (direction > 1){
			throw new IllegalArgumentException ("keep it between 0-2");
		} else if (direction < 0){
			throw new IllegalArgumentException ("keep it between 0-2"); 
			
		} else  {
			this.direction = direction;
		}
	}
	public Facelet getPrimaryFacelet() {
		return primaryFacelet;
	}


	public Facelet getSecondaryFacelet() {
		return secondaryFacelet;
	}
	public Facelet getTertiaryFacelet() {
		return tertiaryFacelet;
	}
}
