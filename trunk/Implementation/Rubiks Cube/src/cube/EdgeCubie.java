package cube;

public class EdgeCubie extends Cubie {
	
	/**
	 * Throws an exception if not one of the facelets is null.
	 * @param primaryFacelet 
	 * @param secondaryFacelet
	 * @param tertiaryFacelet
	 */
	public int name;
	public EdgeCubie (Facelet primaryFacelet, Facelet secondaryFacelet, Facelet tertiaryFacelet, int name){ 
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
		if(nullCount == 1){
			this.primaryFacelet = primaryFacelet;
			this.secondaryFacelet = secondaryFacelet;
			this.tertiaryFacelet = tertiaryFacelet;	
		} else {
			throw new IllegalArgumentException ("one of the Facelets shall be null");
		}
		this.name = name;
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
	
	public Facelet getFacelet(int i){
		if(i == 0){
			if(primaryFacelet != null){
				return primaryFacelet;
			} else {
				return secondaryFacelet;
			}
		} else if(i == 1){
			if(tertiaryFacelet != null){
				return tertiaryFacelet;
			} else {
				return secondaryFacelet;
			}
		} else {
			throw new IllegalArgumentException(" i Must be between 0 and 1");
		}
	}
}
