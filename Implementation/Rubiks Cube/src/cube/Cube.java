package cube;

import java.util.TreeMap;
import cubedrawer.*;

public class Cube {

	PrimaryFace[] primary;
	SecondaryFace[] secondary;
	TertiaryFace[] tertiary;

	Facelet primary_0;
	Facelet primary_1;
	Facelet secondary_0;
	Facelet secondary_1;
	Facelet tertiary_0;
	Facelet tertiary_1;

	//Map of the cubies
	TreeMap <EdgePos, EdgeCubie> eCubies;
	TreeMap <CornerPos, CornerCubie> cCubies;

	//Map of the cubicles
	TreeMap <EdgePos, EdgeCubicle> eCubicles;
	TreeMap <CornerPos, CornerCubicle> cCubicles;

	//Corner cubicles
	/////////////////////////////
	//Top face cubicles
	CornerCubicle P0S0T1; //Primary 0, secondary 0, tertiary 1
	CornerCubicle P0S0T0;
	CornerCubicle P0S1T0;
	CornerCubicle P0S1T1;
	///////////////////////////
	//Down face cubicles
	CornerCubicle P1S1T0;
	CornerCubicle P1S0T0;
	CornerCubicle P1S0T1;
	CornerCubicle P1S1T1;
	//////////////////////////

	//Edge cubicles
	/////////////////////////////
	//Top face cubicles
	EdgeCubicle P0S0; //Primary 0, secondary 0
	EdgeCubicle P0T0;
	EdgeCubicle P0S1;
	EdgeCubicle P0T1;
	///////////////////////////
	//Down face cubicles
	EdgeCubicle P1T0;
	EdgeCubicle P1S0;
	EdgeCubicle P1T1;
	EdgeCubicle P1S1;
	//////////////////////////
	//Center piece cubicles
	EdgeCubicle S0T1;
	EdgeCubicle S0T0;
	EdgeCubicle S1T0;
	EdgeCubicle S1T1;
	/////////////////////////////




	/**
	 * Contains six faces distributed on three arrays depending on whether they are primary, secondary, or tertiary.
	 * 
	 */

	public Cube(){

		primary = new PrimaryFace[2];
		secondary = new SecondaryFace[2];
		tertiary = new TertiaryFace[2];

		primary_0 = Facelet.PRIMARY_0;
		primary_1 = Facelet.PRIMARY_1;
		secondary_0 = Facelet.SECONDARY_0;
		secondary_1 = Facelet.SECONDARY_1;
		tertiary_0 = Facelet.TERTIARY_0;
		tertiary_1 = Facelet.TERTIARY_1;


		//Cubies
		eCubies = new TreeMap <EdgePos, EdgeCubie>(); // treemap taking positions and gives the cubie
		cCubies = new TreeMap <CornerPos, CornerCubie>(); // treemap of corner positions;

		//Corner cubies
		/////////////////////////////
		//Top face cubies
		cCubies.put(CornerPos.P0S0T1, new CornerCubie(primary_0, secondary_0, tertiary_1));
		cCubies.put(CornerPos.P0S0T0, new CornerCubie(primary_0, secondary_0, tertiary_0));
		cCubies.put(CornerPos.P0S1T0, new CornerCubie(primary_0, secondary_1, tertiary_0));
		cCubies.put(CornerPos.P0S1T1, new CornerCubie(primary_0, secondary_1, tertiary_1));
		///////////////////////////
		//Down face cubies
		cCubies.put(CornerPos.P1S1T0, new CornerCubie(primary_1, secondary_1, tertiary_0));
		cCubies.put(CornerPos.P1S0T0, new CornerCubie(primary_1, secondary_0, tertiary_0));
		cCubies.put(CornerPos.P1S0T1, new CornerCubie(primary_1, secondary_0, tertiary_1));
		cCubies.put(CornerPos.P1S1T1, new CornerCubie(primary_1, secondary_1, tertiary_1));
		//////////////////////////

		//Edge cubies
		/////////////////////////////
		//Top face cubies
		eCubies.put(EdgePos.P0S0, new EdgeCubie(primary_0, secondary_0, null));
		eCubies.put(EdgePos.P0T0, new EdgeCubie(primary_0, null, tertiary_0));
		eCubies.put(EdgePos.P0S1, new EdgeCubie(primary_0, secondary_1, null));
		eCubies.put(EdgePos.P0T1, new EdgeCubie(primary_0, null, tertiary_1));
		///////////////////////////
		//Down face cubies
		eCubies.put(EdgePos.P1T0, new EdgeCubie(primary_1, null, tertiary_0));
		eCubies.put(EdgePos.P1S0, new EdgeCubie(primary_1, secondary_0, null));
		eCubies.put(EdgePos.P1T1, new EdgeCubie(primary_1, null, tertiary_1));
		eCubies.put(EdgePos.P1S1, new EdgeCubie(primary_1, secondary_1, null));
		//////////////////////////
		//Center piece cubies
		eCubies.put(EdgePos.S0T1, new EdgeCubie(null, secondary_0, tertiary_1));
		eCubies.put(EdgePos.S0T0, new EdgeCubie(null, secondary_0, tertiary_0));
		eCubies.put(EdgePos.S1T0, new EdgeCubie(null, secondary_1, tertiary_0));
		eCubies.put(EdgePos.S1T1, new EdgeCubie(null, secondary_1, tertiary_1));
		/////////////////////////////


		//Cubicles
		eCubicles = new TreeMap <EdgePos, EdgeCubicle>();
		cCubicles = new TreeMap <CornerPos, CornerCubicle>();

		//Corner cubicles
		/////////////////////////////
		//Top face cubicles
		P0S0T1 = new CornerCubicle(cCubies.get(CornerPos.P0S0T1));
		P0S0T0 = new CornerCubicle(cCubies.get(CornerPos.P0S0T0));
		P0S1T0 = new CornerCubicle(cCubies.get(CornerPos.P0S1T0));
		P0S1T1 = new CornerCubicle(cCubies.get(CornerPos.P0S1T1));

		cCubicles.put(CornerPos.P0S0T1, P0S0T1);
		cCubicles.put(CornerPos.P0S0T0, P0S0T0);
		cCubicles.put(CornerPos.P0S1T0, P0S1T0);
		cCubicles.put(CornerPos.P0S1T1, P0S1T1);
		///////////////////////////
		//Down face cubicles	
		P1S1T0 = new CornerCubicle(cCubies.get(CornerPos.P1S1T0));
		P1S0T0 = new CornerCubicle(cCubies.get(CornerPos.P1S0T0));
		P1S0T1 = new CornerCubicle(cCubies.get(CornerPos.P1S0T1));
		P1S1T1 = new CornerCubicle(cCubies.get(CornerPos.P1S1T1));

		cCubicles.put(CornerPos.P1S1T0, P1S1T0);
		cCubicles.put(CornerPos.P1S0T0, P1S0T0);
		cCubicles.put(CornerPos.P1S0T1, P1S0T1);
		cCubicles.put(CornerPos.P1S1T1, P1S1T1);
		//////////////////////////

		//Edge cubicles
		/////////////////////////////
		//Top face cubicles
		P0S0 = new EdgeCubicle(eCubies.get(EdgePos.P0S0));
		P0T0 = new EdgeCubicle(eCubies.get(EdgePos.P0T0));
		P0S1 = new EdgeCubicle(eCubies.get(EdgePos.P0S1));
		P0T1 = new EdgeCubicle(eCubies.get(EdgePos.P0T1));

		eCubicles.put(EdgePos.P0S0, P0S0);
		eCubicles.put(EdgePos.P0T0, P0T0);
		eCubicles.put(EdgePos.P0S1, P0S1);
		eCubicles.put(EdgePos.P0T1, P0T1);
		///////////////////////////
		//Down face cubicles
		P1T0 = new EdgeCubicle(eCubies.get(EdgePos.P1T0));
		P1S0 = new EdgeCubicle(eCubies.get(EdgePos.P1S0));
		P1T1 = new EdgeCubicle(eCubies.get(EdgePos.P1T1));
		P1S1 = new EdgeCubicle(eCubies.get(EdgePos.P1S1));

		eCubicles.put(EdgePos.P1T0, P1T0);
		eCubicles.put(EdgePos.P1S0, P1S0);
		eCubicles.put(EdgePos.P1T1, P1T1);
		eCubicles.put(EdgePos.P1S1, P1S1);
		//////////////////////////
		//Center piece cubicles
		S0T1 = new EdgeCubicle(eCubies.get(EdgePos.S0T1));
		S0T0 = new EdgeCubicle(eCubies.get(EdgePos.S0T0));
		S1T0 = new EdgeCubicle(eCubies.get(EdgePos.S1T0));
		S1T1 = new EdgeCubicle(eCubies.get(EdgePos.S1T1));

		eCubicles.put(EdgePos.S0T1, S0T1);
		eCubicles.put(EdgePos.S0T0, S0T0);
		eCubicles.put(EdgePos.S1T0, S1T0);
		eCubicles.put(EdgePos.S1T1, S1T1);
		/////////////////////////////

		//Faces
		primary[0] = new PrimaryFace(P0S1T0, P0S1T1, P0S0T1, P0S0T0, P0S1, P0T1, P0S0, P0T0, primary_0);
		primary[1] = new PrimaryFace(P1S0T0, P1S0T1, P1S1T1, P1S1T0, P1S0, P1T1, P1S1, P1T0, primary_1);

		secondary[0] = new SecondaryFace(P0S0T0, P0S0T1, P1S0T1, P1S0T0, P0S0, S0T1, P1S0, S0T0, secondary_0);
		secondary[1] = new SecondaryFace(P0S1T1, P0S1T0, P1S1T0, P1S1T1, P0S1, S1T0, P1S1, S1T1, secondary_1);

		tertiary[0] = new TertiaryFace(P0S1T0, P0S0T0, P1S0T0, P1S1T0, P0T0, S0T0, P1T0, S1T0, tertiary_0);
		tertiary[1] = new TertiaryFace(P0S0T1, P0S1T1, P1S1T1, P1S0T1, P0T1, S1T1, P1T1, S0T1, tertiary_1);

	}

	public PrimaryFace[] getPrimary(){
		return primary;
	}
	public SecondaryFace[] getSecondary(){
		return secondary;
	}
	public TertiaryFace[] getTertiary(){
		return tertiary;
	}
	public Face[] getFace(int i){
		switch(i){
		case 0:
			return primary;
		case 1:
			return secondary;
		default:
			return tertiary;
		}
	}

	public Face getFace(Facelet f){
		switch(f){
		case PRIMARY_0:
			return primary[0];
		case PRIMARY_1:
			return primary[1];
		case SECONDARY_0:
			return secondary[0];
		case SECONDARY_1:
			return secondary[1];
		case TERTIARY_0:
			return tertiary[0];
		default:
			return tertiary[1];				

		}
	}

	/**
	 * 
	 * @param input The cubie which faces you want to find
	 * @return An array of face which cantains the cubicle with the cubie <b>input</b> 
	 * @throws IllegalArgumentException If the cubie is neither corner nor edge
	 */
	public Face[] getFace(Cubie input) throws IllegalArgumentException{
		Face[] result = null;
		if(input.getClass() == new CornerCubie(primary_0, secondary_0, tertiary_0).getClass()) {
			result = new Face[3];

			for(int i = 0 ; i < 2 ; i++) {
				for(int j = 0 ; j < 4 ; j++) {
					if(primary[i].getCornerCubicle()[j].getCubie() == input) {
						result[0] = primary[i];
					}
					if(secondary[i].getCornerCubicle()[j].getCubie() == input) {
						result[1] = secondary[i];
					}
					if(tertiary[i].getCornerCubicle()[j].getCubie() == input) {
						result[2] = tertiary[i];
					}
				}
			}

		} else if(input.getClass() == new EdgeCubie(primary_0, secondary_0, null).getClass()) {
			result = new Face[2];
			int index = 0;

			for(int i = 0 ; i < 2 ; i++) {
				for(int j = 0 ; j < 4 ; j++) {
					if(primary[i].getEdgeCubicle()[j].getCubie() == input) {
						result[index] = primary[i];
						index++;
						break;
					}
				}
			}
			for(int i = 0 ; i < 2 ; i++) {
				for(int j = 0 ; j < 4 ; j++) {
					if(secondary[i].getEdgeCubicle()[j].getCubie() == input) {
						result[index] = secondary[i];
						index++;
						break;
					}
				}
			}

			if(index <= 1) {
				for(int i = 0 ; i < 2 ; i++) {
					for(int j = 0 ; j < 4 ; j++) {
						if(tertiary[i].getEdgeCubicle()[j].getCubie() == input) {
							result[index] = tertiary[i];
						}
					}
				}
			}
		} else {
			throw new IllegalArgumentException("The cubie must be either a corner or edge");
		}

		return result;
	}

	public EdgeCubie getECubie(EdgePos pos){

		return eCubies.get(pos);
	}

	public CornerCubie getCCubie(CornerPos pos){

		return cCubies.get(pos);
	}

	public EdgeCubicle getECubicle(EdgePos pos){

		return eCubicles.get(pos);
	}

	public CornerCubicle getCCubicle(CornerPos pos){

		return cCubicles.get(pos);
	}

	public boolean isInH() {

		for (int i = 0; i < 4; i++){
			for (int j = 0; j < 2; j++){
				if (primary[j].getCornerCubicle()[i].getCubie().getPrimaryOrientation() != 0 || 
						primary[j].getEdgeCubicle()[i].getCubie().getPrimaryOrientation() != 0){
					return false;
				}
			}
			if (secondary[i/2].getEdgeCubicle()[(i%2)*2+1].getCubie().getPrimaryOrientation() != 0){
				return false;
			} 
			if (secondary[i/2].getEdgeCubicle()[(i%2)*2+1].getCubie().getPrimaryFacelet() != null){
				return false;
			}
		}

		return true;
	}

	public boolean isSolved() {
		if(this.isInH()) {
			CornerPos[] cp = CornerPos.values();
			EdgePos[] ep = EdgePos.values();
			for (int i = 0; i < 6; i++) {

				if (cCubicles.get(cp[i]).getCubie() != cCubies.get(cp[i])) {
					return false;
				}
			}
			for (int i = 0; i < 10; i++) {
				if (eCubicles.get(ep[i]).getCubie() != eCubies.get(ep[i])) {
					return false;
				}
			}

			return true;
		} else {
			return false;
		}
	}

	public Face getFirstFace(EdgeCubie e){

		if(e.getPrimaryFacelet()== null){
			return	getFace(e.getSecondaryFacelet());

		}else {
			return getFace(e.getPrimaryFacelet());
		}
	}

	public Face getLastFace(EdgeCubie e){

		if(e.getTertiaryFacelet()== null){
			return	getFace(e.getSecondaryFacelet());

		}else {
			return getFace(e.getTertiaryFacelet());
		}
	}

	//Static methods

	/**
	 * 
	 * @param a the cube to permute
	 * @param moveSequence the moves to permute <b>a</b>
	 * @return the permuted cube
	 */
	static public Cube permute(Cube a, MoveButtons[] moveSequence) {

		for (int i = 0; i < moveSequence.length; i++) {
			switch(moveSequence[i]){
			case U:
				a.getPrimary()[0].cwTwist();
				break;
			case UP:
				a.getPrimary()[0].ccwTwist();
				break;
			case U2:
				a.getPrimary()[0].cwTwist();
				a.getPrimary()[0].cwTwist();
				break;
			case D:
				a.getPrimary()[1].cwTwist();
				break;
			case DP:
				a.getPrimary()[1].ccwTwist();
				break;
			case D2:
				a.getPrimary()[1].cwTwist();
				a.getPrimary()[1].cwTwist();
				break;
			case F:
				a.getSecondary()[0].cwTwist();
				break;
			case FP:
				a.getSecondary()[0].ccwTwist();
				break;
			case F2:
				a.getSecondary()[0].cwTwist();
				a.getSecondary()[0].cwTwist();
				break;
			case B:
				a.getSecondary()[1].cwTwist();
				break;
			case BP:
				a.getSecondary()[1].ccwTwist();
				break;
			case B2:
				a.getSecondary()[1].cwTwist();
				a.getSecondary()[1].cwTwist();
				break;
			case L:
				a.getTertiary()[0].cwTwist();
				break;
			case LP:
				a.getTertiary()[0].ccwTwist();
				break;
			case L2:
				a.getTertiary()[0].cwTwist();
				a.getTertiary()[0].cwTwist();
				break;
			case R:
				a.getTertiary()[1].cwTwist();
				break;
			case RP:
				a.getTertiary()[1].ccwTwist();
				break;
			case R2:
				a.getTertiary()[1].cwTwist();
				a.getTertiary()[1].cwTwist();
				break;
			}
		}

		return a;
	}
	/**
	 * 
	 * @param facelet The color of the face, which is input.
	 * @param m If m = 0 -- normal move, if m = 1 prime move, if m = 2 double move.
	 * @return returns MoveButtons
	 */
	public MoveButtons FaceToMove(Facelet facelet, int m){

		switch(facelet){
		case PRIMARY_0:
			if(m == 0){
				return MoveButtons.U;
			}
			else if(m == 1){
				return MoveButtons.UP;
			}
			else{
				return MoveButtons.U2;
			}
		case PRIMARY_1:
			if(m == 0){
				return MoveButtons.D;
			}
			else if(m == 1){
				return MoveButtons.DP;
			}
			else{
				return MoveButtons.D2;
			}
		case SECONDARY_0:
			if(m == 0){
				return MoveButtons.F;
			}
			else if(m == 1){
				return MoveButtons.FP;
			}
			else{
				return MoveButtons.F2;
			}
		case SECONDARY_1:
			if(m == 0){
				return MoveButtons.B;
			}
			else if(m == 1){
				return MoveButtons.BP;
			}
			else{
				return MoveButtons.B2;
			}
		case TERTIARY_0:
			if(m == 0){
				return MoveButtons.L;
			}
			else if(m == 1){
				return MoveButtons.LP;
			}
			else{
				return MoveButtons.L2;
			}
		default:
			if(m == 0){
				return MoveButtons.R;
			}
			else if(m == 1){
				return MoveButtons.RP;
			}
			else{
				return MoveButtons.R2;
			}
		}

	}
}
