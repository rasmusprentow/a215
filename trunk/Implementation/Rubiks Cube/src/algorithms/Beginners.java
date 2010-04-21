package algorithms;

import java.util.ArrayList;
import java.util.EnumSet;

import cube.Cube;
import cube.EdgeCubie;
import cube.Facelet;

import cube.EdgePos;
import cubedrawer.MoveButtons;

public class Beginners {
	private Cube cube;
	private ArrayList<MoveButtons> moves;

	public Beginners(Cube cube) {
		this.cube = cube;
		moves = new ArrayList<MoveButtons>();
	}


	public ArrayList<MoveButtons> solve(){

		moves.clear();
		solveFLCross();

		return moves;
	}

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
		cube.permute(cube, moves);
	}

	private void solveFLCross(){
		EnumSet<EdgePos> edges = EnumSet.of(EdgePos.P1S0, EdgePos.P1S1, EdgePos.P1T0, EdgePos.P1T1);

		for(EdgePos key: edges){ // For all edges in the P1 layer
			EdgeCubie e = cube.getECubie(key);

			if(cube.getECubicle(key).getCubie()== e){ //Is the cubie in its right place
				System.out.println("på plads  " + key);
				if(e.getPrimaryOrientation()!= 0){  // Is it oriented correctly
					System.out.println("på plads vender forkert " + key);
					algortihm1(key);
				}
			}
			else if(cube.getFace(e)[0].getFacelet() == Facelet.PRIMARY_1){
				System.out.println("Den er i gul, mand! " + key);
				moves.add(cube.FaceToMove(cube.getFace(e)[1].getFacelet(), 2));
				
				Cube.permute(cube, cube.FaceToMove(cube.getFace(e)[1].getFacelet(), 2));
				
				do{
					moves.add(MoveButtons.U);
					Cube.permute(cube, MoveButtons.U);
				}while(cube.getFace(e)[1].getFacelet() != e.getFacelet(1));
				
				moves.add(cube.FaceToMove(cube.getFace(e)[1].getFacelet(), 2));
				Cube.permute(cube, cube.FaceToMove(cube.getFace(e)[1].getFacelet(), 2));
			}
			

			///START
		}
	}


}
