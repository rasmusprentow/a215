package algorithms;

import java.util.EnumSet;

import cube.Cube;
import cube.EdgeCubie;

import cube.Cube.EdgePos;

public class Beginners {
	private Cube cube;
	
	
	public Beginners(Cube cube) {
		this.cube = cube;
	}
	
	private void transpose(){
		
	}
	
	public String solve(){
		
		
		return null;
	}
	
	private void solveFLCross(){
		EnumSet<EdgePos> edges = EnumSet.of(EdgePos.P1S0, EdgePos.P1S1, EdgePos.P1T0, EdgePos.P1T1);
		
		for(EdgePos key: edges){
			EdgeCubie e = cube.getECubie(key);
			
			///START
		}
	}
	
	
}
