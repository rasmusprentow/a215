package cube;

public class EdgeCubicle extends Cubicle {
	private EdgeCubie edgeCubie;
	
	public EdgeCubicle(){
	
	}
	public EdgeCubicle (EdgeCubie edgeCubie){
		setEdgeCubie(edgeCubie);
	}
	public void setEdgeCubie(EdgeCubie edgeCubie) {
		this.edgeCubie = edgeCubie;
	}
	public EdgeCubie getEdgeCubie() {
		return edgeCubie;
	}
}
