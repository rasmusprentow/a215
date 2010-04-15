package cube;

public class EdgeCubie extends Cubie {

	/**
	 * Throws an exception if not one of the facelets is null.
	 * @param primaryFacelet 
	 * @param secondaryFacelet
	 * @param tertiaryFacelet
	 */
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
	
	
	public void setPrimaryOrientation(byte o){
		if (o > 1){
			throw new IllegalArgumentException ("keep it between 0-2");
		} else if (o < 0){
			throw new IllegalArgumentException ("keep it between 0-2"); 
			
		} else  {
			this.primaryOrientation = o;
		}
	}
	
	public void setSecondaryOrientation(byte o){
		if (o > 1){
			throw new IllegalArgumentException ("keep it between 0-2");
		} else if (o < 0){
			throw new IllegalArgumentException ("keep it between 0-2"); 
			
		} else  {
			this.secondaryOrientation = o;
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
