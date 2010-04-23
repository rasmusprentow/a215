package algorithms;

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
		solveF2L();
		solveLLCross();
		solveLLCrossPos();
		//SolveLLCornerPos();
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

						algorithm2(edgeKey);

					}
				}
			}


			if(cube.getFace(e)[0].getFacelet() == Facelet.PRIMARY_0){
				while(cube.getFace(e)[1].getFacelet() != e.getFacelet(1)){
					moves.add(MoveButtons.U);
					Cube.permute(cube, MoveButtons.U);
				}
				moves.add(cube.FaceToMove(cube.getFace(e)[1].getFacelet(), 2));
				Cube.permute(cube, cube.FaceToMove(cube.getFace(e)[1].getFacelet(), 2));
			}

			if(cube.getECubicle(key).getCubie()== e){
				if(e.getPrimaryOrientation()!= 0){  // Is it oriented correctly
					//	System.out.println("på plads vender forkert " + key);
					algorithm1(key);
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
					algorithm1(key);
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
					algorithm1(key);
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
					algorithm1(key);
				}
			} else { // cubien er ikke i et primært face!
				EnumSet<EdgePos> secondEdges = EnumSet.of(EdgePos.S1T0, EdgePos.S1T1, EdgePos.S0T0, EdgePos.S0T1);

				for(EdgePos edgeKey: secondEdges){ // For all edges in the second layer

					if(cube.getECubicle(edgeKey).getCubie() == e){

						algorithm2(edgeKey);
						//	System.out.println("Moving " + edgeKey);



						while(cube.getFace(e)[1].getFacelet() != e.getFacelet(1)){
							moves.add(MoveButtons.U);
							Cube.permute(cube, MoveButtons.U);
						}

						moves.add(cube.FaceToMove(cube.getFace(e)[1].getFacelet(), 2));
						Cube.permute(cube, cube.FaceToMove(cube.getFace(e)[1].getFacelet(), 2));
						if(e.getPrimaryOrientation()!= 0){  // Is it oriented correctly
							//	System.out.println("på plads vender forkert " + key);
							algorithm1(key);
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

	public void solveF2L(){
		System.out.println("wassup nigga");
		EnumSet<EdgePos> edges = EnumSet.of(EdgePos.S0T0, EdgePos.S0T1, EdgePos.S1T0, EdgePos.S1T1);

		for(EdgePos key: edges){ // For all edges in the P1 layer
			EdgeCubie e = cube.getECubie(key);
			if(cube.getECubicle(key).getCubie()== e){ //Is the cubie in its right place
				//	System.out.println("på plads  " + key);
				if(e.getPrimaryOrientation()!= 0){  // Is it oriented correctly
					//	System.out.println("på plads vender forkert " + key);
					algorithm5(key); // denne algoritme er ikke skrevet endnu.
				}
			}
			if(cube.getFace(e)[0].getFacelet() != Facelet.PRIMARY_0){

				for(EdgePos newPlace: edges){ // For all edges in the P1 layer
					if(cube.getECubicle(newPlace).getCubie() == e){
						algorithm5(newPlace); 
					}

				}

			}

			if(cube.getFace(e)[0].getFacelet() == Facelet.PRIMARY_0){
				//It is in the white face
				if(e.getPrimaryOrientation() == 0){
					while(cube.getFace(e)[1].getFacelet() != e.getFacelet(1)){
						moves.add(MoveButtons.U);
						Cube.permute(cube, MoveButtons.U);
					}
					EnumSet<EdgePos> secondEdges = EnumSet.of(EdgePos.P0T0, EdgePos.P0T1, EdgePos.P0S0, EdgePos.P0S1);

					for(EdgePos edgeKey: secondEdges){
						if(cube.getECubicle(edgeKey).getCubie() == e){

							if(e.getFacelet(1) == Facelet.TERTIARY_0){
								if(e.getFacelet(0) == Facelet.SECONDARY_0){
									algorithm6(edgeKey); //alg6 goes right
								} 
								else{
									algorithm7(edgeKey); //alg7 goes left
								}
							} else {
								if(e.getFacelet(0) == Facelet.SECONDARY_0){
									algorithm7(edgeKey);
								}
								else{
									algorithm6(edgeKey);
								}
							}
						}
					}

				}
				else { //orientation is 1
					while(cube.getFace(e)[1].getFacelet() != e.getFacelet(0)){
						moves.add(MoveButtons.U);
						Cube.permute(cube, MoveButtons.U);
					}

					EnumSet<EdgePos> secondEdges = EnumSet.of(EdgePos.P0T0, EdgePos.P0T1, EdgePos.P0S0, EdgePos.P0S1);

					for(EdgePos edgeKey: secondEdges){
						if(cube.getECubicle(edgeKey).getCubie() == e){

							if(e.getFacelet(0) == Facelet.SECONDARY_0){
								if(e.getFacelet(1) == Facelet.TERTIARY_0){
									algorithm7(edgeKey); //alg6 goes right
								} 
								else{
									algorithm6(edgeKey); //alg7 goes left
								}
							} else {
								if(e.getFacelet(1) == Facelet.TERTIARY_0){
									algorithm6(edgeKey);
								}
								else{
									algorithm7(edgeKey);
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
			algorithm8(Facelet.SECONDARY_0);
		}
		if(	cube.getECubicle(EdgePos.P0T0).getCubie().getPrimaryOrientation() == 0 && cube.getECubicle(EdgePos.P0S1).getCubie().getPrimaryOrientation() == 0 && cube.getECubicle(EdgePos.P0T1).getCubie().getPrimaryOrientation() == 1 && cube.getECubicle(EdgePos.P0S0).getCubie().getPrimaryOrientation() == 1){
			algorithm8(Facelet.SECONDARY_0);
		} 
		if(	cube.getECubicle(EdgePos.P0T0).getCubie().getPrimaryOrientation() == 0 && cube.getECubicle(EdgePos.P0S0).getCubie().getPrimaryOrientation() == 0 && cube.getECubicle(EdgePos.P0T1).getCubie().getPrimaryOrientation() == 1 && cube.getECubicle(EdgePos.P0S1).getCubie().getPrimaryOrientation() == 1){
			algorithm8(Facelet.TERTIARY_1);
		} 
		if(cube.getECubicle(EdgePos.P0S0).getCubie().getPrimaryOrientation() == 0 && cube.getECubicle(EdgePos.P0T1).getCubie().getPrimaryOrientation() == 0 && cube.getECubicle(EdgePos.P0S1).getCubie().getPrimaryOrientation() == 1 && 	cube.getECubicle(EdgePos.P0T0).getCubie().getPrimaryOrientation() == 1){
			algorithm8(Facelet.SECONDARY_1);
		} 
		if(cube.getECubicle(EdgePos.P0T1).getCubie().getPrimaryOrientation() == 0 && cube.getECubicle(EdgePos.P0S1).getCubie().getPrimaryOrientation() == 0 && 	cube.getECubicle(EdgePos.P0T0).getCubie().getPrimaryOrientation() == 1 && cube.getECubicle(EdgePos.P0S0).getCubie().getPrimaryOrientation() == 1){
			algorithm8(Facelet.TERTIARY_0);
		} 
		if(cube.getECubicle(EdgePos.P0T1).getCubie().getPrimaryOrientation() == 0 && cube.getECubicle(EdgePos.P0T0).getCubie().getPrimaryOrientation() == 0 && cube.getECubicle(EdgePos.P0S1).getCubie().getPrimaryOrientation() == 1 && cube.getECubicle(EdgePos.P0S0).getCubie().getPrimaryOrientation() == 1){
			algorithm8(Facelet.SECONDARY_0);
		} 
		if(cube.getECubicle(EdgePos.P0S1).getCubie().getPrimaryOrientation() == 0 && cube.getECubicle(EdgePos.P0S0).getCubie().getPrimaryOrientation() == 0 && cube.getECubicle(EdgePos.P0T1).getCubie().getPrimaryOrientation() == 1 && cube.getECubicle(EdgePos.P0T0).getCubie().getPrimaryOrientation() == 1){
			algorithm8(Facelet.TERTIARY_0);
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
				moves.add(MoveButtons.U);
				Cube.permute(cube, MoveButtons.U);
			}
		}

		if(i < 4){
			if(cube.getECubicle(EdgePos.P0S0).getCubie() == cube.getECubie(EdgePos.P0S0) && cube.getECubicle(EdgePos.P0S1).getCubie() == cube.getECubie(EdgePos.P0S1)){
				algorithm9(Facelet.SECONDARY_0);
				moves.add(MoveButtons.UP);
				Cube.permute(cube, MoveButtons.UP);
				//System.out.println("Front 1");
			}
			if(cube.getECubicle(EdgePos.P0T0).getCubie() == cube.getECubie(EdgePos.P0T0) && cube.getECubicle(EdgePos.P0T1).getCubie() == cube.getECubie(EdgePos.P0T1)){
				algorithm9(Facelet.TERTIARY_0);
				moves.add(MoveButtons.UP);
				Cube.permute(cube, MoveButtons.UP);
				//System.out.println("Left 2");
			}
			if(cube.getECubicle(EdgePos.P0S0).getCubie() == cube.getECubie(EdgePos.P0S0) && cube.getECubicle(EdgePos.P0T0).getCubie() == cube.getECubie(EdgePos.P0T0)){
				algorithm9(Facelet.SECONDARY_1);
				//System.out.println("Back 3");
			}
			else if(cube.getECubicle(EdgePos.P0S1).getCubie() == cube.getECubie(EdgePos.P0S1) && cube.getECubicle(EdgePos.P0T0).getCubie() == cube.getECubie(EdgePos.P0T0)){
				algorithm9(Facelet.TERTIARY_1);
				//System.out.println("Right 4");
			}
			else if(cube.getECubicle(EdgePos.P0S1).getCubie() == cube.getECubie(EdgePos.P0S1) && cube.getECubicle(EdgePos.P0T1).getCubie() == cube.getECubie(EdgePos.P0T1)){
				algorithm9(Facelet.SECONDARY_0);
				//System.out.println("Front 5");
			}
			else if(cube.getECubicle(EdgePos.P0S0).getCubie() == cube.getECubie(EdgePos.P0S0) && cube.getECubicle(EdgePos.P0T1).getCubie() == cube.getECubie(EdgePos.P0T1)){
				algorithm9(Facelet.TERTIARY_0);
				System.out.println("Left 6");
			}
		}
	}

	public void solveLLCornerPos(){
		int i = 0;
		EnumSet<CornerPos> corners = EnumSet.of(CornerPos.P0S0T0, CornerPos.P0S0T1, CornerPos.P0S1T0, CornerPos.P0S1T1);

		for(CornerPos key: corners){
			i = 0;
			if(cube.getCCubicle(key).getCubie() == cube.getCCubie(key)){
				i++;
			}
		}
		System.out.println("Number of correct corners, bitches: " + i);
		if(i < 4){
			if(i == 0){
				algorithm10(CornerPos.P0S0T0);
			}

			else if(i == 1){
				for(CornerPos key: corners){
					if(cube.getCCubicle(key).getCubie() == cube.getCCubie(key)){
						algorithm10(key);
						for(CornerPos countKey: corners){
							i = 0;
							if(cube.getCCubicle(countKey).getCubie() == cube.getCCubie(countKey)){
								i++;
							}
						}
						if(i == 4){
							break;
						}
						else{
							algorithm10(key);
						}
					}
				}
			}
		}
	}

	/**
	 * Turns the edges
	 * @param the edge to be oriented.
	 */
	private void algorithm1(EdgePos p){
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
	private void algorithm2(EdgePos p){
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

	private void algorithm5(EdgePos p){
		MoveButtons[] moves;
		switch(p){
		case S0T0:
			moves = new MoveButtons[]{ MoveButtons.UP, MoveButtons.LP, MoveButtons.U, MoveButtons.L, MoveButtons.U, MoveButtons.F, MoveButtons.UP, MoveButtons.FP};
			break;
		case S0T1:
			moves = new MoveButtons[]{ MoveButtons.U, MoveButtons.R, MoveButtons.UP, MoveButtons.RP, MoveButtons.UP, MoveButtons.FP, MoveButtons.U, MoveButtons.F};
			break;
		case S1T0:
			moves = new MoveButtons[]{ MoveButtons.U, MoveButtons.L, MoveButtons.UP, MoveButtons.LP, MoveButtons.UP, MoveButtons.BP, MoveButtons.U, MoveButtons.B};
			break;
		default:
			moves = new MoveButtons[]{ MoveButtons.UP, MoveButtons.RP, MoveButtons.U, MoveButtons.R, MoveButtons.U, MoveButtons.B, MoveButtons.UP, MoveButtons.BP};

			break;
		}
		for(int i = 0; i < moves.length; i++){
			this.moves.add(moves[i]);
		}
		Cube.permute(cube, moves);

	}
	//INSERT ALG5 HERE

	private void algorithm6(EdgePos p){
		MoveButtons[] moves;
		switch(p){
		case P0S0:
			moves = new MoveButtons[]{ MoveButtons.U, MoveButtons.R, MoveButtons.UP, MoveButtons.RP, MoveButtons.UP, MoveButtons.FP, MoveButtons.U, MoveButtons.F};
			break;
		case P0T1:
			moves = new MoveButtons[]{ MoveButtons.U, MoveButtons.B, MoveButtons.UP, MoveButtons.BP, MoveButtons.UP, MoveButtons.RP, MoveButtons.U, MoveButtons.R};
			break;
		case P0S1:
			moves = new MoveButtons[]{ MoveButtons.U, MoveButtons.L, MoveButtons.UP, MoveButtons.LP, MoveButtons.UP, MoveButtons.BP, MoveButtons.U, MoveButtons.B};
			break;
		default:
			moves = new MoveButtons[]{ MoveButtons.U, MoveButtons.F, MoveButtons.UP, MoveButtons.FP, MoveButtons.UP, MoveButtons.LP, MoveButtons.U, MoveButtons.L};

			break;
		}
		for(int i = 0; i < moves.length; i++){
			this.moves.add(moves[i]);
		}
		Cube.permute(cube, moves);

	}

	private void algorithm7(EdgePos p){
		MoveButtons[] moves;
		switch(p){
		case P0S0:
			moves = new MoveButtons[]{ MoveButtons.UP, MoveButtons.LP, MoveButtons.U, MoveButtons.L, MoveButtons.U, MoveButtons.F, MoveButtons.UP, MoveButtons.FP};
			break;
		case P0T1:
			moves = new MoveButtons[]{ MoveButtons.UP, MoveButtons.FP, MoveButtons.U, MoveButtons.F, MoveButtons.U, MoveButtons.R, MoveButtons.UP, MoveButtons.RP};
			break;
		case P0S1:
			moves = new MoveButtons[]{ MoveButtons.UP, MoveButtons.RP, MoveButtons.U, MoveButtons.R, MoveButtons.U, MoveButtons.B, MoveButtons.UP, MoveButtons.BP};
			break;
		default:
			moves = new MoveButtons[]{ MoveButtons.UP, MoveButtons.BP, MoveButtons.U, MoveButtons.B, MoveButtons.U, MoveButtons.L, MoveButtons.UP, MoveButtons.LP};

			break;
		}
		for(int i = 0; i < moves.length; i++){
			this.moves.add(moves[i]);
		}
		Cube.permute(cube, moves);

	}
	private void algorithm8(Facelet f){
		MoveButtons[] moves;
		switch (f) {
		case SECONDARY_0:
			moves = new MoveButtons[]{ MoveButtons.F, MoveButtons.R, MoveButtons.U, MoveButtons.RP, MoveButtons.UP, MoveButtons.FP};
			break;

		case SECONDARY_1:
			moves = new MoveButtons[]{ MoveButtons.B, MoveButtons.L, MoveButtons.U, MoveButtons.LP, MoveButtons.UP, MoveButtons.BP};
			break;

		case TERTIARY_0:
			moves = new MoveButtons[]{ MoveButtons.L, MoveButtons.F, MoveButtons.U, MoveButtons.FP, MoveButtons.UP, MoveButtons.LP};
			break;
		default:
			moves = new MoveButtons[]{ MoveButtons.R, MoveButtons.B, MoveButtons.U, MoveButtons.BP, MoveButtons.UP, MoveButtons.RP};
			break;
		}
		for(int i = 0; i < moves.length; i++){
			this.moves.add(moves[i]);
		}
		Cube.permute(cube, moves);
	}
	/* Cases skal laves om til det rigtige */
	private void algorithm9(Facelet f){
		MoveButtons[] moves;
		switch (f) {
		case SECONDARY_0:
			moves = new MoveButtons[]{ MoveButtons.R, MoveButtons.U, MoveButtons.RP, MoveButtons.U, MoveButtons.R, MoveButtons.U2, MoveButtons.RP, MoveButtons.U};
			/* Front move
			 * 
			 */
			break;
		case TERTIARY_0:
			moves = new MoveButtons[]{ MoveButtons.F, MoveButtons.U, MoveButtons.FP, MoveButtons.U, MoveButtons.F, MoveButtons.U2, MoveButtons.FP, MoveButtons.U};
			/* Left move
			 * 
			 */
			break;
		case SECONDARY_1:
			moves = new MoveButtons[]{ MoveButtons.L, MoveButtons.U, MoveButtons.LP, MoveButtons.U, MoveButtons.L, MoveButtons.U2, MoveButtons.LP, MoveButtons.U};
			/* Back move
			 * 
			 */
			break;
		default:
			moves = new MoveButtons[]{ MoveButtons.B, MoveButtons.U, MoveButtons.BP, MoveButtons.U, MoveButtons.B, MoveButtons.U2, MoveButtons.BP, MoveButtons.U};
			/* Right move
			 * */
			break;

		}
		for(int i = 0; i < moves.length; i++){
			this.moves.add(moves[i]);
		}
		Cube.permute(cube, moves);

	}

	private void algorithm10(CornerPos p){
		MoveButtons[] moves;
		switch(p){
		case P0S0T0:
			moves = new MoveButtons[]{ MoveButtons.UP, MoveButtons.LP, MoveButtons.U, MoveButtons.R, MoveButtons.UP, MoveButtons.L, MoveButtons.U, MoveButtons.RP};
			break;
		case P0S0T1:
			moves = new MoveButtons[]{ MoveButtons.UP, MoveButtons.FP, MoveButtons.U, MoveButtons.B, MoveButtons.UP, MoveButtons.F, MoveButtons.U, MoveButtons.BP};
			break;
		case P0S1T0:
			moves = new MoveButtons[]{ MoveButtons.UP, MoveButtons.RP, MoveButtons.U, MoveButtons.L, MoveButtons.UP, MoveButtons.R, MoveButtons.U, MoveButtons.LP};
			break;
		default:
			moves = new MoveButtons[]{ MoveButtons.UP, MoveButtons.BP, MoveButtons.U, MoveButtons.F, MoveButtons.UP, MoveButtons.B, MoveButtons.U, MoveButtons.FP};
			break;
		}
		for(int i = 0; i < moves.length; i++){
			this.moves.add(moves[i]);
		}
		Cube.permute(cube, moves);

	}


}