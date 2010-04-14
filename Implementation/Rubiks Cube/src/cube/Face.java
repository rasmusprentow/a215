package cube;

public abstract class Face {

	CornerCubicle[] cornerArray = new CornerCubicle[4];
	EdgeCubicle[] edgeArray = new EdgeCubicle[4];
	Facelet faceColor;
	
	public Face(CornerCubicle corner0, CornerCubicle corner1,
			CornerCubicle corner2, CornerCubicle corner3,
			EdgeCubicle edge0, EdgeCubicle edge1,
			EdgeCubicle edge2, EdgeCubicle edge3, Facelet color) {
		
		cornerArray[0] = corner0;
		cornerArray[1] = corner1;
		cornerArray[2] = corner2;
		cornerArray[3] = corner3;
		
		edgeArray[0] = edge0;
		edgeArray[1] = edge1;
		edgeArray[2] = edge2;
		edgeArray[3] = edge3;
		
		faceColor = color;
	}

	public Face(CornerCubicle[] cornerList, EdgeCubicle[] edgeList, Facelet color) {
		if(cornerList.length != 4 || edgeList.length != 4) {
			throw new IllegalArgumentException("Invalid length of array");
		}
		
		for(int i = 0 ; i < 4 ; i++) {
			cornerArray[i] = cornerList[i];
			edgeArray[i] = edgeList[i];
		}
	}


}
