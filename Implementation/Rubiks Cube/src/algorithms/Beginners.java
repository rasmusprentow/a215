package algorithms;

import java.util.EnumSet;
import java.util.LinkedList;

import cube.CornerCubie;
import static cube.CornerPos.*;
import static cube.EdgePos.*;
import cube.CornerPos;
import cube.Cube;
import cube.EdgeCubie;
import cube.Facelet;

import cube.EdgePos;
import cubedrawer.MoveButtons;
import static cubedrawer.MoveButtons.*;

public class Beginners {
	private Cube cube;
	private LinkedList<MoveButtons> moves;

	public Beginners(Cube cube) {
		this.cube = cube;
		moves = new LinkedList<MoveButtons>();
	}


	public LinkedList<MoveButtons> solve(){

		moves.clear();
		if(!cube.isSolved()){
			solveFLCross();
			solveFL();
			solveF2L();
			solveLLCross();
			solveLLCrossPos();
			solveLLCornerPos();
			solveLLCornerOri();
		}
		return moves;
	}

	private void solveFLCross2(){
		EnumSet<EdgePos> edges = EnumSet.of(EdgePos.P1S0, EdgePos.P1S1, EdgePos.P1T0, EdgePos.P1T1);

		for(EdgePos key: edges){ // For all edges in the P1 layer
			EdgeCubie e = cube.getECubie(key);

			if(cube.getFace(e)[0].getFacelet() == Facelet.PRIMARY_1){ // er i gul
				//System.out.println("Den er i gul, mand! " + key);
				if(cube.getECubicle(key).getCubie()== e){ // Hvis den er pŒ plads
					moves.add(cube.FaceToMove(cube.getFace(e)[1].getFacelet(), 2));
					Cube.permute(cube, cube.FaceToMove(cube.getFace(e)[1].getFacelet(), 2));
				}

			} 


			if(cube.getFace(e)[0].getFacelet() != Facelet.PRIMARY_1 || cube.getFace(e)[0].getFacelet() != Facelet.PRIMARY_1) { // cubien er ikke i et primært face!
				EnumSet<EdgePos> secondEdges = EnumSet.of(EdgePos.S1T0, EdgePos.S1T1, EdgePos.S0T0, EdgePos.S0T1);

				for(EdgePos edgeKey: secondEdges){ // For all edges in the second layer

					if(cube.getECubicle(edgeKey).getCubie() == e){

						algorithm1B(edgeKey);

					}
				}
			}


			if(cube.getFace(e)[0].getFacelet() == Facelet.PRIMARY_0){
				while(cube.getFace(e)[1].getFacelet() != e.getFacelet(1)){
					moves.add(U);
					Cube.permute(cube, U);
				}
				moves.add(cube.FaceToMove(cube.getFace(e)[1].getFacelet(), 2));
				Cube.permute(cube, cube.FaceToMove(cube.getFace(e)[1].getFacelet(), 2));
			}

			if(cube.getECubicle(key).getCubie()== e){
				if(e.getPrimaryOrientation()!= 0){  // Is it oriented correctly
					//	System.out.println("på plads vender forkert " + key);
					algorithm1A(key);
				}
			}

		}

	}

	private void solveFLCross(){
		EnumSet<EdgePos> edges = EnumSet.of(EdgePos.P1S0, EdgePos.P1S1, EdgePos.P1T0, EdgePos.P1T1);

		for(EdgePos key: edges){ // For all edges in the P1 layer
			EdgeCubie e = cube.getECubie(key);

			if(cube.getECubicle(key).getCubie()== e){ //Is the cubie in its right place
				//	System.out.println("på plads  " + key);
				if(e.getPrimaryOrientation()!= 0){  // Is it oriented correctly
					//	System.out.println("på plads vender forkert " + key);
					algorithm1A(key);
				}
			}
			else if(cube.getFace(e)[0].getFacelet() == Facelet.PRIMARY_1){
				//System.out.println("Den er i gul, mand! " + key);
				moves.add(cube.FaceToMove(cube.getFace(e)[1].getFacelet(), 2));

				Cube.permute(cube, cube.FaceToMove(cube.getFace(e)[1].getFacelet(), 2));

				while(cube.getFace(e)[1].getFacelet() != e.getFacelet(1)){
					moves.add(U);
					Cube.permute(cube, U);
				}
				moves.add(cube.FaceToMove(cube.getFace(e)[1].getFacelet(), 2));
				Cube.permute(cube, cube.FaceToMove(cube.getFace(e)[1].getFacelet(), 2));
				if(e.getPrimaryOrientation()!= 0){  // Is it oriented correctly
					//	System.out.println("på plads vender forkert " + key);
					algorithm1A(key);
				}
			} 
			else if(cube.getFace(e)[0].getFacelet() == Facelet.PRIMARY_0){


				while(cube.getFace(e)[1].getFacelet() != e.getFacelet(1)){
					moves.add(U);
					Cube.permute(cube, U);
				}

				moves.add(cube.FaceToMove(cube.getFace(e)[1].getFacelet(), 2));
				Cube.permute(cube, cube.FaceToMove(cube.getFace(e)[1].getFacelet(), 2));
				if(e.getPrimaryOrientation()!= 0){  // Is it oriented correctly
					//System.out.println("på plads vender forkert " + key);
					algorithm1A(key);
				}
			} else { // cubien er ikke i et primært face!
				EnumSet<EdgePos> secondEdges = EnumSet.of(EdgePos.S1T0, EdgePos.S1T1, EdgePos.S0T0, EdgePos.S0T1);

				for(EdgePos edgeKey: secondEdges){ // For all edges in the second layer

					if(cube.getECubicle(edgeKey).getCubie() == e){

						algorithm1B(edgeKey);
						//	System.out.println("Moving " + edgeKey);



						while(cube.getFace(e)[1].getFacelet() != e.getFacelet(1)){
							moves.add(U);
							Cube.permute(cube, U);
						}

						moves.add(cube.FaceToMove(cube.getFace(e)[1].getFacelet(), 2));
						Cube.permute(cube, cube.FaceToMove(cube.getFace(e)[1].getFacelet(), 2));
						if(e.getPrimaryOrientation()!= 0){  // Is it oriented correctly
							//	System.out.println("på plads vender forkert " + key);
							algorithm1A(key);
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
				/*System.out.println("på plads  " + key);*/
				while (c.getPrimaryOrientation() != 0){  // Is it oriented wrong
					//System.out.println("på plads vender forkert " + key);
					algorithm2A(key);
				}
			} else if(cube.getFace(c)[0].getFacelet() == Facelet.PRIMARY_1) { // It is in the down face


				for(CornerPos cornerKey: corners){ // For all corners 
					if(cube.getCCubicle(cornerKey).getCubie() == c){ // If the cube in this corner is the cube currently being placed.
						algorithm2B(cornerKey);		// Switches the cubes
						while(cube.getFace(c)[1].getFacelet() != c.getSecondaryFacelet() || cube.getFace(c)[2].getFacelet() != c.getTertiaryFacelet()){ 
							// Rotates as long as the cube is not in its right spot. The means  P0S*T*.
							moves.add(U);
							Cube.permute(cube, U);
						}
						algorithm2B(key);	 // Moves the cube into place.
						while (c.getPrimaryOrientation() != 0){  // As long as it is orientated wrong rotate it.
							//System.out.println("på plads vender forkert " + key);
							algorithm2A(key);
						}
					}
				}


			} else { // It is in the up face
				while(cube.getFace(c)[1].getFacelet() != c.getSecondaryFacelet() || cube.getFace(c)[2].getFacelet() != c.getTertiaryFacelet()){
					moves.add(U);
					Cube.permute(cube, U);
				}
				algorithm2B(key);
				while (c.getPrimaryOrientation() != 0){  // Is it oriented wrong
					//System.out.println("på plads vender forkert " + key);
					algorithm2A(key);
				}

			}



		}
	}

	public void solveF2L(){
		//System.out.println("wassup nigga");
		EnumSet<EdgePos> edges = EnumSet.of(EdgePos.S0T0, EdgePos.S0T1, EdgePos.S1T0, EdgePos.S1T1);

		for(EdgePos key: edges){ // For all edges in the P1 layer
			EdgeCubie e = cube.getECubie(key);
			if(cube.getECubicle(key).getCubie()== e){ //Is the cubie in its right place
				//	System.out.println("på plads  " + key);
				if(e.getPrimaryOrientation()!= 0){  // Is it oriented correctly
					//	System.out.println("på plads vender forkert " + key);
					algorithm3A(key); // denne algoritme er ikke skrevet endnu.
				}
			}
			if(cube.getFace(e)[0].getFacelet() != Facelet.PRIMARY_0){

				for(EdgePos newPlace: edges){ // For all edges in the P1 layer
					if(cube.getECubicle(newPlace).getCubie() == e){
						algorithm3A(newPlace); 
					}

				}

			}

			if(cube.getFace(e)[0].getFacelet() == Facelet.PRIMARY_0){
				//It is in the white face
				if(e.getPrimaryOrientation() == 0){
					while(cube.getFace(e)[1].getFacelet() != e.getFacelet(1)){
						moves.add(U);
						Cube.permute(cube, U);
					}
					EnumSet<EdgePos> secondEdges = EnumSet.of(EdgePos.P0T0, EdgePos.P0T1, EdgePos.P0S0, EdgePos.P0S1);

					for(EdgePos edgeKey: secondEdges){
						if(cube.getECubicle(edgeKey).getCubie() == e){

							if(e.getFacelet(1) == Facelet.TERTIARY_0){
								if(e.getFacelet(0) == Facelet.SECONDARY_0){
									algorithm3B(edgeKey); //alg6 goes right
								} 
								else{
									algorithm3C(edgeKey); //alg7 goes left
								}
							} else {
								if(e.getFacelet(0) == Facelet.SECONDARY_0){
									algorithm3C(edgeKey);
								}
								else{
									algorithm3B(edgeKey);
								}
							}
						}
					}

				}
				else { //orientation is 1
					while(cube.getFace(e)[1].getFacelet() != e.getFacelet(0)){
						moves.add(U);
						Cube.permute(cube, U);
					}

					EnumSet<EdgePos> secondEdges = EnumSet.of(EdgePos.P0T0, EdgePos.P0T1, EdgePos.P0S0, EdgePos.P0S1);

					for(EdgePos edgeKey: secondEdges){
						if(cube.getECubicle(edgeKey).getCubie() == e){

							if(e.getFacelet(0) == Facelet.SECONDARY_0){
								if(e.getFacelet(1) == Facelet.TERTIARY_0){
									algorithm3C(edgeKey); //alg6 goes right
								} 
								else{
									algorithm3B(edgeKey); //alg7 goes left
								}
							} else {
								if(e.getFacelet(1) == Facelet.TERTIARY_0){
									algorithm3B(edgeKey);
								}
								else{
									algorithm3C(edgeKey);
								}
							}
						}
					}

				}
			}
		}


	}

	public void solveLLCross(){
		if(cube.getECubicle(EdgePos.P0S0).getCubie().getPrimaryOrientation() == 1 && cube.getECubicle(EdgePos.P0S1).getCubie().getPrimaryOrientation() == 1 && 	cube.getECubicle(EdgePos.P0T0).getCubie().getPrimaryOrientation() == 1 && cube.getECubicle(EdgePos.P0T1).getCubie().getPrimaryOrientation() == 1){
			algorithm4aA(Facelet.SECONDARY_0);
		}
		if(	cube.getECubicle(EdgePos.P0T0).getCubie().getPrimaryOrientation() == 0 && cube.getECubicle(EdgePos.P0S1).getCubie().getPrimaryOrientation() == 0 && cube.getECubicle(EdgePos.P0T1).getCubie().getPrimaryOrientation() == 1 && cube.getECubicle(EdgePos.P0S0).getCubie().getPrimaryOrientation() == 1){
			algorithm4aA(Facelet.SECONDARY_0);
		} 
		if(	cube.getECubicle(EdgePos.P0T0).getCubie().getPrimaryOrientation() == 0 && cube.getECubicle(EdgePos.P0S0).getCubie().getPrimaryOrientation() == 0 && cube.getECubicle(EdgePos.P0T1).getCubie().getPrimaryOrientation() == 1 && cube.getECubicle(EdgePos.P0S1).getCubie().getPrimaryOrientation() == 1){
			algorithm4aA(Facelet.TERTIARY_1);
		} 
		if(cube.getECubicle(EdgePos.P0S0).getCubie().getPrimaryOrientation() == 0 && cube.getECubicle(EdgePos.P0T1).getCubie().getPrimaryOrientation() == 0 && cube.getECubicle(EdgePos.P0S1).getCubie().getPrimaryOrientation() == 1 && 	cube.getECubicle(EdgePos.P0T0).getCubie().getPrimaryOrientation() == 1){
			algorithm4aA(Facelet.SECONDARY_1);
		} 
		if(cube.getECubicle(EdgePos.P0T1).getCubie().getPrimaryOrientation() == 0 && cube.getECubicle(EdgePos.P0S1).getCubie().getPrimaryOrientation() == 0 && 	cube.getECubicle(EdgePos.P0T0).getCubie().getPrimaryOrientation() == 1 && cube.getECubicle(EdgePos.P0S0).getCubie().getPrimaryOrientation() == 1){
			algorithm4aA(Facelet.TERTIARY_0);
		} 
		if(cube.getECubicle(EdgePos.P0T1).getCubie().getPrimaryOrientation() == 0 && cube.getECubicle(EdgePos.P0T0).getCubie().getPrimaryOrientation() == 0 && cube.getECubicle(EdgePos.P0S1).getCubie().getPrimaryOrientation() == 1 && cube.getECubicle(EdgePos.P0S0).getCubie().getPrimaryOrientation() == 1){
			algorithm4aA(Facelet.SECONDARY_0);
		} 
		if(cube.getECubicle(EdgePos.P0S1).getCubie().getPrimaryOrientation() == 0 && cube.getECubicle(EdgePos.P0S0).getCubie().getPrimaryOrientation() == 0 && cube.getECubicle(EdgePos.P0T1).getCubie().getPrimaryOrientation() == 1 && cube.getECubicle(EdgePos.P0T0).getCubie().getPrimaryOrientation() == 1){
			algorithm4aA(Facelet.TERTIARY_0);
		}
	}

	public void solveLLCrossPos(){
		int i = 0;
		while(i < 2){
			i = 0;
			EnumSet<EdgePos> secondEdges = EnumSet.of(EdgePos.P0T0, EdgePos.P0T1, EdgePos.P0S0, EdgePos.P0S1);

			for(EdgePos edgeKey: secondEdges){
				if(cube.getECubicle(edgeKey).getCubie() == cube.getECubie(edgeKey)){
					i++;
				}
			}
			//System.out.println("Number of correct edges, bitches: " + i);
			if(i < 2){
				moves.add(U);
				Cube.permute(cube, U);
			}
		}

		if(i < 4){
			if(cube.getECubicle(EdgePos.P0S0).getCubie() == cube.getECubie(EdgePos.P0S0) && cube.getECubicle(EdgePos.P0S1).getCubie() == cube.getECubie(EdgePos.P0S1)){
				algorithm4bA(Facelet.SECONDARY_0);
				moves.add(UP);
				Cube.permute(cube, UP);
				//System.out.println("Front 1");
			}
			if(cube.getECubicle(EdgePos.P0T0).getCubie() == cube.getECubie(EdgePos.P0T0) && cube.getECubicle(EdgePos.P0T1).getCubie() == cube.getECubie(EdgePos.P0T1)){
				algorithm4bA(Facelet.TERTIARY_0);
				moves.add(UP);
				Cube.permute(cube, UP);
				//System.out.println("Left 2");
			}
			if(cube.getECubicle(EdgePos.P0S0).getCubie() == cube.getECubie(EdgePos.P0S0) && cube.getECubicle(EdgePos.P0T0).getCubie() == cube.getECubie(EdgePos.P0T0)){
				algorithm4bA(Facelet.SECONDARY_1);
				//System.out.println("Back 3");
			}
			else if(cube.getECubicle(EdgePos.P0S1).getCubie() == cube.getECubie(EdgePos.P0S1) && cube.getECubicle(EdgePos.P0T0).getCubie() == cube.getECubie(EdgePos.P0T0)){
				algorithm4bA(Facelet.TERTIARY_1);
				//System.out.println("Right 4");
			}
			else if(cube.getECubicle(EdgePos.P0S1).getCubie() == cube.getECubie(EdgePos.P0S1) && cube.getECubicle(EdgePos.P0T1).getCubie() == cube.getECubie(EdgePos.P0T1)){
				algorithm4bA(Facelet.SECONDARY_0);
				//System.out.println("Front 5");
			}
			else if(cube.getECubicle(EdgePos.P0S0).getCubie() == cube.getECubie(EdgePos.P0S0) && cube.getECubicle(EdgePos.P0T1).getCubie() == cube.getECubie(EdgePos.P0T1)){
				algorithm4bA(Facelet.TERTIARY_0);
				
			}
		}
	}

	public void solveLLCornerPos(){
		int i = 0;
		EnumSet<CornerPos> corners = EnumSet.of(P0S0T0, CornerPos.P0S0T1, CornerPos.P0S1T0, CornerPos.P0S1T1);
		i = 0;
		for(CornerPos key: corners){
			if(cube.getCCubicle(key).getCubie() == cube.getCCubie(key)){
				i++;
			}
		}
		//System.out.println("Number of correct corners, bitches: " + i);
		if(i < 4){
			if(i == 0){
				algorithm5aA(CornerPos.P0S0T0);
				i = 1;
			}

			if(i == 1){
				for(CornerPos key: corners){
					if(cube.getCCubicle(key).getCubie() == cube.getCCubie(key)){
						algorithm5aA(key);
						i = 0;
						for(CornerPos countKey: corners){
							if(cube.getCCubicle(countKey).getCubie() == cube.getCCubie(countKey)){
								i++;
							}
						}
						if(i == 4){
							return;
						}
						else{
							algorithm5aA(key);
							return;
						}
					}
				}
			}
		}
	}

	public void solveLLCornerOri(){
		EnumSet<CornerPos> corners = EnumSet.of(P0S0T1, P0S1T1, P0S1T0, P0S0T0);
		for(CornerPos key: corners){
			if(cube.getCCubicle(key).getCubie() == cube.getCCubie(key)){

			}
		}
		for(int i = 0; i < 4; i++){
			while(cube.getCCubicle(P0S0T1).getCubie().getPrimaryOrientation() != 0){
				algorithm5bA();
			} 
			moves.add(U);
			Cube.permute(cube, U);
		}
	}

	/**
	 * Turns the edges
	 * @param the edge to be oriented.
	 */
	private void algorithm1A(EdgePos p){
		MoveButtons[] moves;
		switch (p) {
		case P1S0:
			moves = new MoveButtons[]{ FP, D,RP, DP };
			break;

		case P1S1:
			moves = new MoveButtons[]{ B, DP,R, D};
			break;

		case P1T1:
			moves = new MoveButtons[]{ R, DP, F, D};
			break;
		default:
			moves = new MoveButtons[]{ LP, D, FP, DP};
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
	private void algorithm1B(EdgePos p){
		MoveButtons[] moves;
		switch (p) {
		case S0T0:
			moves = new MoveButtons[]{ F, U,FP };
			break;

		case S0T1:
			moves = new MoveButtons[]{ FP, U,F};
			break;

		case S1T0:
			moves = new MoveButtons[]{ BP, U, B};
			break;
		default:
			moves = new MoveButtons[]{ B, U, BP};
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
	private void algorithm2A(CornerPos p){
		MoveButtons[] moves;
		switch(p){
		case P1S0T0:
			moves = new MoveButtons[]{ LP, UP, L, U, LP, UP, L};
			break;
		case P1S0T1:
			moves = new MoveButtons[]{ R, UP, RP, U, R, UP, RP};
			break;
		case P1S1T0:
			moves = new MoveButtons[]{ L, UP, LP, U, L, UP, LP};
			break;
		default:
			moves = new MoveButtons[]{ RP, UP, R, U, RP, UP, R};

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
	private void algorithm2B(CornerPos p){
		MoveButtons[] moves;
		switch(p){
		case P1S0T0:
			moves = new MoveButtons[]{ F, U, FP};
			break;
		case P1S0T1:
			moves = new MoveButtons[]{ R, U, RP};
			break;
		case P1S1T0:
			moves = new MoveButtons[]{ L, U, LP};
			break;
		default:
			moves = new MoveButtons[]{ RP, UP, R};

			break;
		}
		for(int i = 0; i < moves.length; i++){
			this.moves.add(moves[i]);
		}
		Cube.permute(cube, moves);

	}

	private void algorithm3A(EdgePos p){
		MoveButtons[] moves;
		switch(p){
		case S0T0:
			moves = new MoveButtons[]{ UP, LP, U, L, U, F, UP, FP};
			break;
		case S0T1:
			moves = new MoveButtons[]{ U, R, UP, RP, UP, FP, U, F};
			break;
		case S1T0:
			moves = new MoveButtons[]{ U, L, UP, LP, UP, BP, U, B};
			break;
		default:
			moves = new MoveButtons[]{ UP, RP, U, R, U, B, UP, BP};

			break;
		}
		for(int i = 0; i < moves.length; i++){
			this.moves.add(moves[i]);
		}
		Cube.permute(cube, moves);

	}

	private void algorithm3B(EdgePos p){
		MoveButtons[] moves;
		switch(p){
		case P0S0:
			moves = new MoveButtons[]{ U, R, UP, RP, UP, FP, U, F};
			break;
		case P0T1:
			moves = new MoveButtons[]{ U, B, UP, BP, UP, RP, U, R};
			break;
		case P0S1:
			moves = new MoveButtons[]{ U, L, UP, LP, UP, BP, U, B};
			break;
		default:
			moves = new MoveButtons[]{ U, F, UP, FP, UP, LP, U, L};
			break;
		}
		for(int i = 0; i < moves.length; i++){
			this.moves.add(moves[i]);
		}
		Cube.permute(cube, moves);
	}

	private void algorithm3C(EdgePos p){
		MoveButtons[] moves;
		switch(p){
		case P0S0:
			moves = new MoveButtons[]{ UP, LP, U, L, U, F, UP, FP};
			break;
		case P0T1:
			moves = new MoveButtons[]{ UP, FP, U, F, U, R, UP, RP};
			break;
		case P0S1:
			moves = new MoveButtons[]{ UP, RP, U, R, U, B, UP, BP};
			break;
		default:
			moves = new MoveButtons[]{ UP, BP, U, B, U, L, UP, LP};

			break;
		}
		for(int i = 0; i < moves.length; i++){
			this.moves.add(moves[i]);
		}
		Cube.permute(cube, moves);

	}
	private void algorithm4aA(Facelet f){
		MoveButtons[] moves;
		switch (f) {
		case SECONDARY_0:
			moves = new MoveButtons[]{ F, R, U, RP, UP, FP};
			break;

		case SECONDARY_1:
			moves = new MoveButtons[]{ B, L, U, LP, UP, BP};
			break;

		case TERTIARY_0:
			moves = new MoveButtons[]{ L, F, U, FP, UP, LP};
			break;
		default:
			moves = new MoveButtons[]{ R, B, U, BP, UP, RP};
			break;
		}
		for(int i = 0; i < moves.length; i++){
			this.moves.add(moves[i]);
		}
		Cube.permute(cube, moves);
	}

	
	private void algorithm4bA(Facelet f){
		MoveButtons[] moves;
		switch (f) {
		case SECONDARY_0:
			moves = new MoveButtons[]{ R, U, RP, U, R, U2, RP, U};
			/* Front move
			 * 
			 */
			break;
		case TERTIARY_0:
			moves = new MoveButtons[]{ F, U, FP, U, F, U2, FP, U};
			/* Left move
			 * 
			 */
			break;
		case SECONDARY_1:
			moves = new MoveButtons[]{ L, U, LP, U, L, U2, LP, U};
			/* Back move
			 * 
			 */
			break;
		default:
			moves = new MoveButtons[]{ B, U, BP, U, B, U2, BP, U};
			/* Right move
			 * */
			break;

		}
		for(int i = 0; i < moves.length; i++){
			this.moves.add(moves[i]);
		}
		Cube.permute(cube, moves);

	}

	private void algorithm5aA(CornerPos p){
		MoveButtons[] moves;
		switch(p){
		case P0S0T0:
			moves = new MoveButtons[]{ UP, LP, U, R, UP, L, U, RP};
			break;
		case P0S0T1:
			moves = new MoveButtons[]{ UP, FP, U, B, UP, F, U, BP};
			break;
		case P0S1T1:
			moves = new MoveButtons[]{ UP, RP, U, L, UP, R, U, LP};
			break;
		default:
			moves = new MoveButtons[]{ UP, BP, U, F, UP, B, U, FP};
			break;
		}
		for(int i = 0; i < moves.length; i++){
			this.moves.add(moves[i]);
		}
		Cube.permute(cube, moves);
	}


	private void algorithm5bA(){
		MoveButtons[] moves;
		moves = new MoveButtons[]{ RP, DP, R, D,  RP, DP, R, D};

		for(int i = 0; i < moves.length; i++){
			this.moves.add(moves[i]);
		}
		Cube.permute(cube, moves);

	}


}