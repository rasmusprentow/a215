package algorithms;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;

import cube.CornerCubie;
import cube.CornerPos;
import cube.Cube;
import cube.EdgeCubie;
import cube.Facelet;

import cube.EdgePos;
import cubedrawer.MoveButtons;

public class Beginners {
	private Cube cube;
	private LinkedList<MoveButtons> moves;

	public Beginners(Cube cube) {
		this.cube = cube;
		moves = new LinkedList<MoveButtons>();
	}


	public LinkedList<MoveButtons> solve(){

		moves.clear();
		solveFLCross();
		solveFL();
		//solveSL();
		return moves;
	}


	private void solveFLCross(){
		EnumSet<EdgePos> edges = EnumSet.of(EdgePos.P1S0, EdgePos.P1S1, EdgePos.P1T0, EdgePos.P1T1);

		for(EdgePos key: edges){ // For all edges in the P1 layer
			EdgeCubie e = cube.getECubie(key);

			if(cube.getECubicle(key).getCubie()== e){ //Is the cubie in its right place
			//	System.out.println("på plads  " + key);
				if(e.getPrimaryOrientation()!= 0){  // Is it oriented correctly
				//	System.out.println("på plads vender forkert " + key);
					algortihm1(key);
				}
			}
			else if(cube.getFace(e)[0].getFacelet() == Facelet.PRIMARY_1){
				//System.out.println("Den er i gul, mand! " + key);
				moves.add(cube.FaceToMove(cube.getFace(e)[1].getFacelet(), 2));

				Cube.permute(cube, cube.FaceToMove(cube.getFace(e)[1].getFacelet(), 2));

				while(cube.getFace(e)[1].getFacelet() != e.getFacelet(1)){
					moves.add(MoveButtons.U);
					Cube.permute(cube, MoveButtons.U);
				}
				moves.add(cube.FaceToMove(cube.getFace(e)[1].getFacelet(), 2));
				Cube.permute(cube, cube.FaceToMove(cube.getFace(e)[1].getFacelet(), 2));
				if(e.getPrimaryOrientation()!= 0){  // Is it oriented correctly
				//	System.out.println("på plads vender forkert " + key);
					algortihm1(key);
				}
			} 
			else if(cube.getFace(e)[0].getFacelet() == Facelet.PRIMARY_0){


				while(cube.getFace(e)[1].getFacelet() != e.getFacelet(1)){
					moves.add(MoveButtons.U);
					Cube.permute(cube, MoveButtons.U);
				}

				moves.add(cube.FaceToMove(cube.getFace(e)[1].getFacelet(), 2));
				Cube.permute(cube, cube.FaceToMove(cube.getFace(e)[1].getFacelet(), 2));
				if(e.getPrimaryOrientation()!= 0){  // Is it oriented correctly
					//System.out.println("på plads vender forkert " + key);
					algortihm1(key);
				}
			} else { // cubien er ikke i et primært face!
				EnumSet<EdgePos> secondEdges = EnumSet.of(EdgePos.S1T0, EdgePos.S1T1, EdgePos.S0T0, EdgePos.S0T1);

				for(EdgePos edgeKey: secondEdges){ // For all edges in the second layer

					if(cube.getECubicle(edgeKey).getCubie() == e){

						algortihm2(edgeKey);
					//	System.out.println("Moving " + edgeKey);
						
						
						
						while(cube.getFace(e)[1].getFacelet() != e.getFacelet(1)){
							moves.add(MoveButtons.U);
							Cube.permute(cube, MoveButtons.U);
						}

						moves.add(cube.FaceToMove(cube.getFace(e)[1].getFacelet(), 2));
						Cube.permute(cube, cube.FaceToMove(cube.getFace(e)[1].getFacelet(), 2));
						if(e.getPrimaryOrientation()!= 0){  // Is it oriented correctly
						//	System.out.println("på plads vender forkert " + key);
							algortihm1(key);
						}
						break;
					}
				}
			}
		}
	}
	
	/**
	 * Solves the corners of the first Layer.
	 */
	private void solveFL(){ // 
		EnumSet<CornerPos> corners = EnumSet.of(CornerPos.P1S0T0, CornerPos.P1S1T0, CornerPos.P1S0T1, CornerPos.P1S1T1);
		//EnumSet<CornerPos> oppCorners = EnumSet.of(CornerPos.P0S0T0, CornerPos.P0S1T0, CornerPos.P1S0T1, CornerPos.P1S1T1);
		
		
		for(CornerPos key: corners){ // For all corners in the P1 layer
			CornerCubie c = cube.getCCubie(key);
			if(cube.getCCubicle(key).getCubie()== c){ //Is the cubie in its right place
				System.out.println("på plads  " + key);
				while (c.getPrimaryOrientation() != 0){  // Is it oriented wrong
					//System.out.println("på plads vender forkert " + key);
					algorithm3(key);
				}
			} else if(cube.getFace(c)[0].getFacelet() == Facelet.PRIMARY_1) { // It is in the down face
				

				for(CornerPos cornerKey: corners){ // For all corners 
					if(cube.getCCubicle(cornerKey).getCubie() == c){ // If the cube in this corner is the cube currently being placed.
						algorithm4(cornerKey);		// Switches the cubes
						while(cube.getFace(c)[1].getFacelet() != c.getSecondaryFacelet() || cube.getFace(c)[2].getFacelet() != c.getTertiaryFacelet()){ 
							// Rotates as long as the cube is not in its right spot. The means  P0S*T*.
							moves.add(MoveButtons.U);
							Cube.permute(cube, MoveButtons.U);
						}
						algorithm4(key);	 // Moves the cube into place.
						while (c.getPrimaryOrientation() != 0){  // As long as it is orientated wrong rotate it.
							//System.out.println("på plads vender forkert " + key);
							algorithm3(key);
						}
					}
				}
				
				
			} else { // It is in the up face
				while(cube.getFace(c)[1].getFacelet() != c.getSecondaryFacelet() || cube.getFace(c)[2].getFacelet() != c.getTertiaryFacelet()){
					moves.add(MoveButtons.U);
					Cube.permute(cube, MoveButtons.U);
				}
				algorithm4(key);
				while (c.getPrimaryOrientation() != 0){  // Is it oriented wrong
					//System.out.println("på plads vender forkert " + key);
					algorithm3(key);
				}
				
			}
			
			
			
		}
	}
	
	private void solveF2L(){
		EnumSet<EdgePos> edges = EnumSet.of(EdgePos.S0T0, EdgePos.S0T1, EdgePos.S1T0, EdgePos.S1T1);
		
		for(EdgePos key: edges){ // For all edges in the P1 layer
			EdgeCubie e = cube.getECubie(key);
			if(cube.getECubicle(key).getCubie()== e){ //Is the cubie in its right place
				//	System.out.println("på plads  " + key);
					if(e.getPrimaryOrientation()!= 0){  // Is it oriented correctly
					//	System.out.println("på plads vender forkert " + key);
						algortihm5(key);
					}
				}
			else if(cube.getFace(e)[0].getFacelet() == Facelet.PRIMARY_0){
				//It is in the white face
				
			}
		}
		
		
	}
	
	
	/**
	 * Turns the edges
	 * @param the edge to be oriented.
	 */
	private void algortihm1(EdgePos p){
		MoveButtons[] moves;
		switch (p) {
		case P1S0:
			moves = new MoveButtons[]{ MoveButtons.FP, MoveButtons.D,MoveButtons.RP, MoveButtons.DP };
			break;

		case P1S1:
			moves = new MoveButtons[]{ MoveButtons.B, MoveButtons.DP,MoveButtons.R, MoveButtons.D};
			break;

		case P1T1:
			moves = new MoveButtons[]{ MoveButtons.R, MoveButtons.DP, MoveButtons.F, MoveButtons.D};
			break;
		default:
			moves = new MoveButtons[]{ MoveButtons.LP, MoveButtons.D, MoveButtons.FP, MoveButtons.DP};
			break;
		}
		for(int i = 0; i < moves.length; i++){
			this.moves.add(moves[i]);
		}
		Cube.permute(cube, moves);
	}

	/**
	 * Moves the cube from middle layer to up layer.
	 * @param The position of the edge the cubie is in. 
	 */
	private void algortihm2(EdgePos p){
		MoveButtons[] moves;
		switch (p) {
		case S0T0:
			moves = new MoveButtons[]{ MoveButtons.F, MoveButtons.U,MoveButtons.FP };
			break;

		case S0T1:
			moves = new MoveButtons[]{ MoveButtons.FP, MoveButtons.U,MoveButtons.F};
			break;

		case S1T0:
			moves = new MoveButtons[]{ MoveButtons.BP, MoveButtons.U, MoveButtons.B};
			break;
		default:
			moves = new MoveButtons[]{ MoveButtons.B, MoveButtons.U, MoveButtons.BP};
			break;
		}
		for(int i = 0; i < moves.length; i++){
			this.moves.add(moves[i]);
		}
		Cube.permute(cube, moves);
	}

	/**
	 * Turns the corner Cubie.
	 * @param The position
	 */
	private void algorithm3(CornerPos p){
		MoveButtons[] moves;
		switch(p){
		case P1S0T0:
			moves = new MoveButtons[]{ MoveButtons.LP, MoveButtons.UP, MoveButtons.L, MoveButtons.U, MoveButtons.LP, MoveButtons.UP, MoveButtons.L};
			break;
		case P1S0T1:
			moves = new MoveButtons[]{ MoveButtons.R, MoveButtons.UP, MoveButtons.RP, MoveButtons.U, MoveButtons.R, MoveButtons.UP, MoveButtons.RP};
			break;
		case P1S1T0:
			moves = new MoveButtons[]{ MoveButtons.L, MoveButtons.UP, MoveButtons.LP, MoveButtons.U, MoveButtons.L, MoveButtons.UP, MoveButtons.LP};
			break;
		default:
			moves = new MoveButtons[]{ MoveButtons.RP, MoveButtons.UP, MoveButtons.R, MoveButtons.U, MoveButtons.RP, MoveButtons.UP, MoveButtons.R};

			break;
		}
		for(int i = 0; i < moves.length; i++){
			this.moves.add(moves[i]);
		}
		Cube.permute(cube, moves);
		
	}
	

		/**
		 * Svitches the cube in the P1 on layer with the same cube in the p0 layer.
		 * @param p The P1 position of the switch.
		 */
		private void algorithm4(CornerPos p){
		MoveButtons[] moves;
		switch(p){
		case P1S0T0:
			moves = new MoveButtons[]{ MoveButtons.F, MoveButtons.U, MoveButtons.FP};
			break;
		case P1S0T1:
			moves = new MoveButtons[]{ MoveButtons.R, MoveButtons.U, MoveButtons.RP};
			break;
		case P1S1T0:
			moves = new MoveButtons[]{ MoveButtons.L, MoveButtons.U, MoveButtons.LP};
			break;
		default:
			moves = new MoveButtons[]{ MoveButtons.RP, MoveButtons.UP, MoveButtons.R};

			break;
		}
		for(int i = 0; i < moves.length; i++){
			this.moves.add(moves[i]);
		}
		Cube.permute(cube, moves);
		
	}
}



