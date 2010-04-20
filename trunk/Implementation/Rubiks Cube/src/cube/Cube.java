package cube;

import cubedrawer.*;

public class Cube {

	PrimaryFace[] primary = new PrimaryFace[2];
	SecondaryFace[] secondary = new SecondaryFace[2];
	TertiaryFace[] tertiary = new TertiaryFace[2];

	Facelet primary_0 = Facelet.PRIMARY_0;
	Facelet primary_1 = Facelet.PRIMARY_1;
	Facelet secondary_0 = Facelet.SECONDARY_0;
	Facelet secondary_1 = Facelet.SECONDARY_1;
	Facelet tertiary_0 = Facelet.TERTIARY_0;
	Facelet tertiary_1 = Facelet.TERTIARY_1;


	//Corner cubicles
	/////////////////////////////
	//Top face cubicles
	CornerCubicle P0S0T1 = new CornerCubicle(new CornerCubie(primary_0, secondary_0, tertiary_1));
	CornerCubicle P0S0T0 = new CornerCubicle(new CornerCubie(primary_0, secondary_0, tertiary_0));
	CornerCubicle P0S1T0 = new CornerCubicle(new CornerCubie(primary_0, secondary_1, tertiary_0));
	CornerCubicle P0S1T1 = new CornerCubicle(new CornerCubie(primary_0, secondary_1, tertiary_1));
	///////////////////////////
	//Down face cubicles
	CornerCubicle P1S1T0 = new CornerCubicle(new CornerCubie(primary_1, secondary_1, tertiary_0));
	CornerCubicle P1S0T0 = new CornerCubicle(new CornerCubie(primary_1, secondary_0, tertiary_0));
	CornerCubicle P1S0T1 = new CornerCubicle(new CornerCubie(primary_1, secondary_0, tertiary_1));
	CornerCubicle P1S1T1 = new CornerCubicle(new CornerCubie(primary_1, secondary_1, tertiary_1));
	//////////////////////////

	//Edge cubicles
	/////////////////////////////
	//Top face cubicles
	EdgeCubicle P0S0 = new EdgeCubicle(new EdgeCubie(primary_0, secondary_0, null));
	EdgeCubicle P0T0 = new EdgeCubicle(new EdgeCubie(primary_0, null, tertiary_0));
	EdgeCubicle P0S1 = new EdgeCubicle(new EdgeCubie(primary_0, secondary_1, null));
	EdgeCubicle P0T1 = new EdgeCubicle(new EdgeCubie(primary_0, null, tertiary_1));
	///////////////////////////
	//Down face cubicles
	EdgeCubicle P1T0 = new EdgeCubicle(new EdgeCubie(primary_1, null, tertiary_0));
	EdgeCubicle P1S0 = new EdgeCubicle(new EdgeCubie(primary_1, secondary_0, null));
	EdgeCubicle P1T1 = new EdgeCubicle(new EdgeCubie(primary_1, null, tertiary_1));
	EdgeCubicle P1S1 = new EdgeCubicle(new EdgeCubie(primary_1, secondary_1, null));
	//////////////////////////
	//Center piece cubicles
	EdgeCubicle S0T1 = new EdgeCubicle(new EdgeCubie(null, secondary_0, tertiary_1));
	EdgeCubicle S0T0 = new EdgeCubicle(new EdgeCubie(null, secondary_0, tertiary_0));
	EdgeCubicle S1T0 = new EdgeCubicle(new EdgeCubie(null, secondary_1, tertiary_0));
	EdgeCubicle S1T1 = new EdgeCubicle(new EdgeCubie(null, secondary_1, tertiary_1));
	/////////////////////////////




	/**
	 * Contains six faces distributed on three arrays depending on whether they are primary, secondary, or tertiary.
	 * 
	 */

	public Cube(){

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
	static public Cube permute(Cube a, MoveButtons[] moveSequence) {
		
		
		
		for (int i = 0; i < moveSequence.length; i++) {
		
		
		switch(moveSequence[i]){
		case U:
			cube.getPrimary()[0].cwTwist();
			break;
		case UP:
			cube.getPrimary()[0].ccwTwist();
			break;
		case U2:
			cube.getPrimary()[0].cwTwist();
			cube.getPrimary()[0].cwTwist();
			break;
		case D:
			cube.getPrimary()[1].cwTwist();
			break;
		case DP:
			cube.getPrimary()[1].ccwTwist();
			break;
		case D2:
			cube.getPrimary()[1].cwTwist();
			cube.getPrimary()[1].cwTwist();
			break;
		case F:
			cube.getSecondary()[0].cwTwist();
			break;
		case FP:
			cube.getSecondary()[0].ccwTwist();
			break;
		case F2:
			cube.getSecondary()[0].cwTwist();
			cube.getSecondary()[0].cwTwist();
			break;
		case B:
			cube.getSecondary()[1].cwTwist();
			break;
		case BP:
			cube.getSecondary()[1].ccwTwist();
			break;
		case B2:
			cube.getSecondary()[1].cwTwist();
			cube.getSecondary()[1].cwTwist();
			break;
		case L:
			cube.getTertiary()[0].cwTwist();
			break;
		case LP:
			cube.getTertiary()[0].ccwTwist();
			break;
		case L2:
			cube.getTertiary()[0].cwTwist();
			cube.getTertiary()[0].cwTwist();
			break;
		case R:
			cube.getTertiary()[1].cwTwist();
			break;
		case RP:
			cube.getTertiary()[1].ccwTwist();
			break;
		case R2:
			cube.getTertiary()[1].cwTwist();
			cube.getTertiary()[1].cwTwist();
			break;
		
		
		return null;
		
	}
}
