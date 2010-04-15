package cube;

public class TertiaryFace extends Face {

	public TertiaryFace(CornerCubicle corner0, CornerCubicle corner1,
			CornerCubicle corner2, CornerCubicle corner3, EdgeCubicle edge0,
			EdgeCubicle edge1, EdgeCubicle edge2, EdgeCubicle edge3,
			Facelet color) {
		super(corner0, corner1, corner2, corner3, edge0, edge1, edge2, edge3,
				color);
		// TODO Auto-generated constructor stub
	}

	public TertiaryFace(CornerCubicle[] cornerList, EdgeCubicle[] edgeList,
			Facelet color) throws IllegalArgumentException {
		super(cornerList, edgeList, color);
		// TODO Auto-generated constructor stub
	}

	public void cwTwist(){
		//First we call the super version of this method, which puts the cubies in
		//the right cubicles.
		super.cwTwist();
		
		//Now we orient correctly.
		for(int i = 0; i < 4; i++){
			//Corner orientation
			//Swapping 0 and 1
			if((int)cornerArray[i].getCornerCubie().getPrimaryOrientation() == 0){
				cornerArray[i].getCornerCubie().setPrimaryOrientation((byte)1);

			}else if((int)cornerArray[i].getCornerCubie().getPrimaryOrientation() == 1){
				cornerArray[i].getCornerCubie().setPrimaryOrientation((byte)0);
			}
			
			if((int)cornerArray[i].getCornerCubie().getSecondaryOrientation() == 0){
				cornerArray[i].getCornerCubie().setSecondaryOrientation((byte)1);

			}else if((int)cornerArray[i].getCornerCubie().getSecondaryOrientation() == 1){
				cornerArray[i].getCornerCubie().setSecondaryOrientation((byte)0);
			}
			
			/*
			if((int)cornerArray[i].getCornerCubie().getTertiaryOrientation() == 0){
				cornerArray[i].getCornerCubie().setTertiaryOrientation((byte)1);

			}else if((int)cornerArray[i].getCornerCubie().getTertiaryOrientation() == 1){
				cornerArray[i].getCornerCubie().setTertiaryOrientation((byte)0);
			}
			*/

			//The orientation of the edges are not changed.
		}
		
	}
	
	public void ccwTwist(){
		//First we call the super version of this method, which puts the cubies in
		//the right cubicles.
		super.ccwTwist();
		
		//Now we orient correctly.
		for(int i = 0; i < 4; i++){
			//Corner orientation
			//Swapping 0 and 1
			if((int)cornerArray[i].getCornerCubie().getPrimaryOrientation() == 0){
				cornerArray[i].getCornerCubie().setPrimaryOrientation((byte)1);

			}else if((int)cornerArray[i].getCornerCubie().getPrimaryOrientation() == 1){
				cornerArray[i].getCornerCubie().setPrimaryOrientation((byte)0);
			}
			
			if((int)cornerArray[i].getCornerCubie().getSecondaryOrientation() == 0){
				cornerArray[i].getCornerCubie().setSecondaryOrientation((byte)1);

			}else if((int)cornerArray[i].getCornerCubie().getSecondaryOrientation() == 1){
				cornerArray[i].getCornerCubie().setSecondaryOrientation((byte)0);
			}
			
			/*
			if((int)cornerArray[i].getCornerCubie().getTertiaryOrientation() == 0){
				cornerArray[i].getCornerCubie().setTertiaryOrientation((byte)1);

			}else if((int)cornerArray[i].getCornerCubie().getTertiaryOrientation() == 1){
				cornerArray[i].getCornerCubie().setTertiaryOrientation((byte)0);
			}
			*/

			//The orientation of the edges are not changed.
		}
		
	}
}
