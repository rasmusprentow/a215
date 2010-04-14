package cube;

public class CornerCubicle extends Cubicle {
	
	private CornerCubie cornerCubie;
	
	public CornerCubicle(){
	
	}
	public CornerCubicle (CornerCubie cornerCubie){
		setCornerCubie(cornerCubie);
	}
	public void setCornerCubie(CornerCubie cornerCubie) {
		this.cornerCubie = cornerCubie;
	}
	public CornerCubie getCornerCubie() {
		return cornerCubie;
	}
}
