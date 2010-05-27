package cube;

public class EdgeCubicle extends Cubicle {
	private EdgeCubie cubie;
	
	public EdgeCubicle(){
	
	}
	public EdgeCubicle (EdgeCubie edgeCubie){
		setCubie(edgeCubie);
	}
	public void setCubie(EdgeCubie edgeCubie) {
		this.cubie = edgeCubie;
	}
	public EdgeCubie getCubie() {
		return cubie;
	}
	
}
