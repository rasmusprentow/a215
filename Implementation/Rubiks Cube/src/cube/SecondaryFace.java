package cube;

public class SecondaryFace extends Face {

	public SecondaryFace(CornerCubicle corner0, CornerCubicle corner1,
			CornerCubicle corner2, CornerCubicle corner3, EdgeCubicle edge0,
			EdgeCubicle edge1, EdgeCubicle edge2, EdgeCubicle edge3,
			Facelet color) {
		super(corner0, corner1, corner2, corner3, edge0, edge1, edge2, edge3,
				color);

	}

	public SecondaryFace(CornerCubicle[] cornerList, EdgeCubicle[] edgeList,
			Facelet color) throws IllegalArgumentException {
		super(cornerList, edgeList, color);

	}


	/**
	 * The function twists the face clock wise
	 */

	public void cwTwist(){
		//First we call the super version of this method, which puts the cubies in
		//the right cubicles.
		super.cwTwist();
		
		//Now we orient correctly.
		for(int i = 0; i < 4; i++){
			//Corner orientation
			//if the primary orientation is 0 it will become 2 and vice versa 
			if((int)cornerArray[i].getCornerCubie().getPrimaryOrientation() == 0){
				cornerArray[i].getCornerCubie().setPrimaryOrientation((byte)2);

			}else if((int)cornerArray[i].getCornerCubie().getPrimaryOrientation() == 2){
				cornerArray[i].getCornerCubie().setPrimaryOrientation((byte)0);
			}
			
			//if the primary orientation is 0 it will become 2 and vice versa 
			if((int)cornerArray[i].getCornerCubie().getSecondaryOrientation() == 0){
				cornerArray[i].getCornerCubie().setSecondaryOrientation((byte)2);

			}else if((int)cornerArray[i].getCornerCubie().getSecondaryOrientation() == 2){
				cornerArray[i].getCornerCubie().setSecondaryOrientation((byte)0);
			}
			
			//if the primary orientation is 0 it will become 2 and vice versa 
			if((int)cornerArray[i].getCornerCubie().getTertiaryOrientation() == 0){
				cornerArray[i].getCornerCubie().setTertiaryOrientation((byte)2);

			}else if((int)cornerArray[i].getCornerCubie().getTertiaryOrientation() == 2){
				cornerArray[i].getCornerCubie().setTertiaryOrientation((byte)0);
			}

			//Edge orientation
			//Swapping primary orientation
			if((int)edgeArray[i].getEdgeCubie().getPrimaryOrientation() == 0){
				edgeArray[i].getEdgeCubie().setPrimaryOrientation((byte)1);

			}else{
				edgeArray[i].getEdgeCubie().setPrimaryOrientation((byte)0);
			}
			
			//Swapping secondary orientation
			if((int)edgeArray[i].getEdgeCubie().getSecondaryOrientation() == 0){
				edgeArray[i].getEdgeCubie().setSecondaryOrientation((byte)1);

			}else{
				edgeArray[i].getEdgeCubie().setSecondaryOrientation((byte)0);
			}
		}
	}
	/**
	 * The function twists the face counter clock wise
	 */
	public void ccwTwist(){
		//First we call the super version of this method, which puts the cubies in
		//the right cubicles.
		super.ccwTwist();
		
		//Now we orient correctly.
		for(int i = 0; i < 4; i++){
			//Corner orientation
			//if the primary orientation is 0 it will become 2 and vice versa 
			if((int)cornerArray[i].getCornerCubie().getPrimaryOrientation() == 0){
				cornerArray[i].getCornerCubie().setPrimaryOrientation((byte)2);

			}else if((int)cornerArray[i].getCornerCubie().getPrimaryOrientation() == 2){
				cornerArray[i].getCornerCubie().setPrimaryOrientation((byte)0);
			}
			
			//if the primary orientation is 0 it will become 2 and vice versa 
			if((int)cornerArray[i].getCornerCubie().getSecondaryOrientation() == 0){
				cornerArray[i].getCornerCubie().setSecondaryOrientation((byte)2);

			}else if((int)cornerArray[i].getCornerCubie().getSecondaryOrientation() == 2){
				cornerArray[i].getCornerCubie().setSecondaryOrientation((byte)0);
			}
			
			//if the primary orientation is 0 it will become 2 and vice versa 
			if((int)cornerArray[i].getCornerCubie().getTertiaryOrientation() == 0){
				cornerArray[i].getCornerCubie().setTertiaryOrientation((byte)2);

			}else if((int)cornerArray[i].getCornerCubie().getTertiaryOrientation() == 2){
				cornerArray[i].getCornerCubie().setTertiaryOrientation((byte)0);
			}

			//Edge orientation
			//Swapping primary orientation
			if((int)edgeArray[i].getEdgeCubie().getPrimaryOrientation() == 0){
				edgeArray[i].getEdgeCubie().setPrimaryOrientation((byte)1);

			}else{
				edgeArray[i].getEdgeCubie().setPrimaryOrientation((byte)0);
			}
			
			//Swapping secondary orientation
			if((int)edgeArray[i].getEdgeCubie().getSecondaryOrientation() == 0){
				edgeArray[i].getEdgeCubie().setSecondaryOrientation((byte)1);

			}else{
				edgeArray[i].getEdgeCubie().setSecondaryOrientation((byte)0);
			}
		}
	}
}