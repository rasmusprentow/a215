package cube;

public class CornerCubie extends Cubie {
		private Facelet primaryFacelet;
		private Facelet secondaryFacelet;
		private Facelet tertiaryFacelet;
	
		
		public CornerCubie (Facelet primaryFacelet, Facelet secondaryFacelet, Facelet tertiaryFacelet){
			this.primaryFacelet = primaryFacelet;
			this.secondaryFacelet = secondaryFacelet;
			this.tertiaryFacelet = tertiaryFacelet;
		}
		
		public void setDirection(byte direction){
			if (direction > 2){
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


