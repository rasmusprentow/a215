package cube;

public class PrimaryFace extends Face {

	public PrimaryFace(CornerCubicle corner0, CornerCubicle corner1,
			CornerCubicle corner2, CornerCubicle corner3, EdgeCubicle edge0,
			EdgeCubicle edge1, EdgeCubicle edge2, EdgeCubicle edge3,
			Facelet color) {
		super(corner0, corner1, corner2, corner3, edge0, edge1, edge2, edge3,
				color);

	}

	public PrimaryFace(CornerCubicle[] cornerList, EdgeCubicle[] edgeList,
			Facelet color) throws IllegalArgumentException {
		super(cornerList, edgeList, color);

	}
	/**
	 * The function twists the face clock wise
	 */

	public void cwTwist(){
		super.cwTwist();
		for(int i = 0; i < 4; i++){
			if((int)cornerArray[i].getCornerCubie().getPrimaryDirection() == 1){
				cornerArray[i].getCornerCubie().setPrimaryOrientation((byte)2);
				System.out.println("Changing 1 to 2 of cubie: " + cornerArray[i].getCornerCubie());
			} 
			
			else if((int)cornerArray[i].getCornerCubie().getPrimaryDirection() == 2){
				cornerArray[i].getCornerCubie().setPrimaryOrientation((byte)1);
				System.out.println("Changing 2 to 1 of cubie: " + cornerArray[i].getCornerCubie());
			} 
			
			else {
				System.out.println("Changing 0 to 0 of cubie: " + cornerArray[i].getCornerCubie());
			}
		}
	}
	/**
	 * The function twists the face counter clock wise
	 */
	public void ccwTwist(){
		super.ccwTwist();
		for(int i = 0; i < 4; i++){
			if((int)cornerArray[i].getCornerCubie().getPrimaryDirection() == 1){
				cornerArray[i].getCornerCubie().setPrimaryOrientation((byte)2);
				System.out.println("Changing 1 to 2 of cubie: " + cornerArray[i].getCornerCubie());
			} 
			
			else if((int)cornerArray[i].getCornerCubie().getPrimaryDirection() == 2){
				cornerArray[i].getCornerCubie().setPrimaryOrientation((byte)1);
				System.out.println("Changing 2 to 1 of cubie: " + cornerArray[i].getCornerCubie());
			} 
			
			else {
				System.out.println("Changing 0 to 0 of cubie: " + cornerArray[i].getCornerCubie());
			}
		}
	}
}
