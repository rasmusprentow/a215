package cube;

public class CornerCubie extends Cubie {
		
		public CornerCubie (Facelet primaryFacelet, Facelet secondaryFacelet, Facelet tertiaryFacelet){
			this.primaryFacelet = primaryFacelet;
			this.secondaryFacelet = secondaryFacelet;
			this.tertiaryFacelet = tertiaryFacelet;
		}
		
		/**
		 * Sets the direction. Will throw an exception if the new direction is not 0 , 1 or 2. 
		 */
		public void setPrimaryOrientation(byte o){
			if (o > 2){
				throw new IllegalArgumentException ("keep it between 0-2");
			} else if (o < 0){
				throw new IllegalArgumentException ("keep it between 0-2"); 
				
			} else  {
				this.primaryOrientation = o;
			}
		}
		
		public void setSecondaryOrientation(byte o){
			if (o > 2){
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


