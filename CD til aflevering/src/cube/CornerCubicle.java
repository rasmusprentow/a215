package cube;

public class CornerCubicle extends Cubicle {
	
	private CornerCubie cubie;
	
	public CornerCubicle(){
	
	}
	
	public CornerCubicle(CornerCubie cornerCubie){
		setCubie(cornerCubie);
	}
	
	public void setCubie(CornerCubie cornerCubie) {
		this.cubie = cornerCubie;
	}
	
	public CornerCubie getCubie() {
		return cubie;
	}
}
