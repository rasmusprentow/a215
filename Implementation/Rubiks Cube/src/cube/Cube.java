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
		
	TreeMap <EdgePos, EdgeCubie> eCubies;
	TreeMap <CornerPos, CornerCubie> cCubies;
	
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

		eCubies = new TreeMap <EdgePos, EdgeCubie>(); // treemap taking positions and gives the cubie
		cCubies = new TreeMap <CornerPos, CornerCubie>(); // treemap of corner positions;
		//Corner cubicles
		/////////////////////////////
		//Top face cubicles
		cCubies.put(CornerPos.P0S0T1, new CornerCubie(primary_0, secondary_0, tertiary_1));
		cCubies.put(CornerPos.P0S0T0, new CornerCubie(primary_0, secondary_0, tertiary_0));
		cCubies.put(CornerPos.P0S1T0, new CornerCubie(primary_0, secondary_1, tertiary_0));
		cCubies.put(CornerPos.P0S1T1, new CornerCubie(primary_0, secondary_1, tertiary_1));
		
		P0S0T1 = new CornerCubicle(cCubies.get(CornerPos.P0S0T1));
		P0S0T0 = new CornerCubicle(cCubies.get(CornerPos.P0S0T0));
		P0S1T0 = new CornerCubicle(cCubies.get(CornerPos.P0S1T0));
		P0S1T1 = new CornerCubicle(cCubies.get(CornerPos.P0S1T1));
		///////////////////////////
		//Down face cubicles
		cCubies.put(CornerPos.P1S1T0, new CornerCubie(primary_1, secondary_1, tertiary_0));
		cCubies.put(CornerPos.P1S0T0, new CornerCubie(primary_1, secondary_0, tertiary_0));
		cCubies.put(CornerPos.P1S0T1, new CornerCubie(primary_1, secondary_0, tertiary_1));
		cCubies.put(CornerPos.P1S1T1, new CornerCubie(primary_1, secondary_1, tertiary_1));
		
		P1S1T0 = new CornerCubicle(cCubies.get(CornerPos.P1S1T0));
		P1S0T0 = new CornerCubicle(cCubies.get(CornerPos.P1S0T0));
		P1S0T1 = new CornerCubicle(cCubies.get(CornerPos.P1S0T1));
		P1S1T1 = new CornerCubicle(cCubies.get(CornerPos.P1S1T1));
		//////////////////////////

		//Edge cubicles
		/////////////////////////////
		//Top face cubicles
		eCubies.put(EdgePos.P0S0, new EdgeCubie(primary_0, secondary_0, null));
		eCubies.put(EdgePos.P0T0, new EdgeCubie(primary_0, null, tertiary_0));
		eCubies.put(EdgePos.P0S1, new EdgeCubie(primary_0, secondary_1, null));
		eCubies.put(EdgePos.P0T1, new EdgeCubie(primary_0, null, tertiary_1));
		
		P0S0 = new EdgeCubicle(eCubies.get(EdgePos.P0S0));
		P0T0 = new EdgeCubicle(eCubies.get(EdgePos.P0T0));
		P0S1 = new EdgeCubicle(eCubies.get(EdgePos.P0S1));
		P0T1 = new EdgeCubicle(eCubies.get(EdgePos.P0T1));
		///////////////////////////
		//Down face cubicles
		eCubies.put(EdgePos.P1T0, new EdgeCubie(primary_1, null, tertiary_0));
		eCubies.put(EdgePos.P1S0, new EdgeCubie(primary_1, secondary_0, null));
		eCubies.put(EdgePos.P1T1, new EdgeCubie(primary_1, null, tertiary_1));
		eCubies.put(EdgePos.P1S1, new EdgeCubie(primary_1, secondary_1, null));
		
		P1T0 = new EdgeCubicle(eCubies.get(EdgePos.P1T0));
		P1S0 = new EdgeCubicle(eCubies.get(EdgePos.P1S0));
		P1T1 = new EdgeCubicle(eCubies.get(EdgePos.P1T1));
		P1S1 = new EdgeCubicle(eCubies.get(EdgePos.P1S1));
		//////////////////////////
		//Center piece cubicles
		eCubies.put(EdgePos.S0T1, new EdgeCubie(null, secondary_0, tertiary_1));
		eCubies.put(EdgePos.S0T0, new EdgeCubie(null, secondary_0, tertiary_0));
		eCubies.put(EdgePos.S1T0, new EdgeCubie(null, secondary_1, tertiary_0));
		eCubies.put(EdgePos.S1T1, new EdgeCubie(null, secondary_1, tertiary_1));
		
		S0T1 = new EdgeCubicle(eCubies.get(EdgePos.S0T1));
		S0T0 = new EdgeCubicle(eCubies.get(EdgePos.S0T0));
		S1T0 = new EdgeCubicle(eCubies.get(EdgePos.S1T0));
		S1T1 = new EdgeCubicle(eCubies.get(EdgePos.S1T1));
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
	
	public EdgeCubie getECubie(EdgePos pos){
		
		return eCubies.get(pos);
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
			
			
			return true;
		} else {
			return false;
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
}
