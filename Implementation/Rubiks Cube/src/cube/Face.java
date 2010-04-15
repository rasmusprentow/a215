package cube;

public abstract class Face {

	CornerCubicle[] cornerArray = new CornerCubicle[4];
	EdgeCubicle[] edgeArray = new EdgeCubicle[4];
	Facelet faceColor;
	
	/**
	 * The corners and edges must be given in clockwise order
	 * @param corner0
	 * @param corner1
	 * @param corner2
	 * @param corner3
	 * @param edge0
	 * @param edge1
	 * @param edge2
	 * @param edge3
	 * @param color the color of the center cubie of this face
	 */
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

	/**
	 * The corners and edges must be given in clockwise order
	 * @param cornerList
	 * @param edgeList
	 * @param color the color of the center cubie of this face
	 * @throws IllegalArgumentException if the arrays are not both 4 in length
	 */
	public Face(CornerCubicle[] cornerList, EdgeCubicle[] edgeList, Facelet color)
		throws IllegalArgumentException {
		if(cornerList.length != 4 || edgeList.length != 4) {
			throw new IllegalArgumentException("Invalid length of array");
		}
		
		for(int i = 0 ; i < 4 ; i++) {
			cornerArray[i] = cornerList[i];
			edgeArray[i] = edgeList[i];
		}
	}

	/**
	 * 
	 * @return the color of the center cubie of this face
	 */
	public Facelet getFacelet() {
		return faceColor;
	}
	
	public void cwTwist() {
		CornerCubie tempCorner;
		tempCorner = cornerArray[0].getCornerCubie();
			
		cornerArray[0].setCornerCubie(cornerArray[3].getCornerCubie());
		cornerArray[3].setCornerCubie(cornerArray[2].getCornerCubie());
		cornerArray[2].setCornerCubie(cornerArray[1].getCornerCubie());
		cornerArray[1].setCornerCubie(tempCorner);
		
		EdgeCubie tempEdge;
		tempEdge = edgeArray[0].getEdgeCubie();
			
		edgeArray[0].setEdgeCubie(edgeArray[3].getEdgeCubie());
		edgeArray[3].setEdgeCubie(edgeArray[2].getEdgeCubie());
		edgeArray[2].setEdgeCubie(edgeArray[1].getEdgeCubie());
		edgeArray[1].setEdgeCubie(tempEdge);
	}
	
	public void ccwTwist() {
		CornerCubie tempCorner;
		tempCorner = cornerArray[0].getCornerCubie();
			
		cornerArray[0].setCornerCubie(cornerArray[1].getCornerCubie());
		cornerArray[1].setCornerCubie(cornerArray[2].getCornerCubie());
		cornerArray[2].setCornerCubie(cornerArray[3].getCornerCubie());
		cornerArray[3].setCornerCubie(tempCorner);
		
		EdgeCubie tempEdge;
		tempEdge = edgeArray[0].getEdgeCubie();
			
		edgeArray[0].setEdgeCubie(edgeArray[1].getEdgeCubie());
		edgeArray[1].setEdgeCubie(edgeArray[2].getEdgeCubie());
		edgeArray[2].setEdgeCubie(edgeArray[3].getEdgeCubie());
		edgeArray[3].setEdgeCubie(tempEdge);
	}
	
	/**
	 * @return cornerArray which contains the four corners of this face.
	 */
	public CornerCubicle[] getCornerCubicle() {
		return cornerArray;
	}
	
	/**
	 * @return edgeArray which contains the four corners of this face.
	 */
	public EdgeCubicle[] getEdgeCubicle() {
		return edgeArray;
	}
}
