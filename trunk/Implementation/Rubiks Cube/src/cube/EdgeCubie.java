package cube;

public class EdgeCubie extends Cubie {
	private Facelet primaryFacelet;
	private Facelet secondaryFacelet;
	
	public EdgeCubie (Facelet primaryFacelet, Facelet secondaryFacelet){
		this.primaryFacelet = primaryFacelet;
		this.secondaryFacelet = secondaryFacelet;
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
}
