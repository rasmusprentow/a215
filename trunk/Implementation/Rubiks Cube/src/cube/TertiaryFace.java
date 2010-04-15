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
		
		super.cwTwist();
		for(int i = 0; i < 4; i++){
			if((int)cornerArray[i].getCornerCubie().getDirection() == 0){
				cornerArray[i].getCornerCubie().setDirection((byte)1);

			}else if((int)cornerArray[i].getCornerCubie().getDirection() == 1){
				cornerArray[i].getCornerCubie().setDirection((byte)0);
			}

			//The orientation of the edges are not changed.
		}
		
	}
	
	public void ccwTwist(){
		
		super.cwTwist();
		for(int i = 0; i < 4; i++){
			if((int)cornerArray[i].getCornerCubie().getDirection() == 0){
				cornerArray[i].getCornerCubie().setDirection((byte)1);

			}else if((int)cornerArray[i].getCornerCubie().getDirection() == 1){
				cornerArray[i].getCornerCubie().setDirection((byte)0);
			}

			//The orientation of the edges are not changed.
		}
		
	}
}
