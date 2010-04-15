package cube;

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
	CornerCubicle P0S0T0 = new CornerCubicle(new CornerCubie(primary_0, secondary_0, tertiary_0,0));
	CornerCubicle P0S0T1 = new CornerCubicle(new CornerCubie(primary_0, secondary_0, tertiary_1,1));
	CornerCubicle P0S1T1 = new CornerCubicle(new CornerCubie(primary_0, secondary_1, tertiary_1,2));
	CornerCubicle P0S1T0 = new CornerCubicle(new CornerCubie(primary_0, secondary_1, tertiary_0,3));
	///////////////////////////
	//Down face cubicles
	CornerCubicle P1S1T1 = new CornerCubicle(new CornerCubie(primary_1, secondary_1, tertiary_1,4));
	CornerCubicle P1S0T1 = new CornerCubicle(new CornerCubie(primary_1, secondary_0, tertiary_1,5));
	CornerCubicle P1S0T0 = new CornerCubicle(new CornerCubie(primary_1, secondary_0, tertiary_0,6));
	CornerCubicle P1S1T0 = new CornerCubicle(new CornerCubie(primary_1, secondary_1, tertiary_0,7));
	//////////////////////////

	//Edge cubicles
	/////////////////////////////
	//Top face cubicles
	EdgeCubicle P0S0 = new EdgeCubicle(new EdgeCubie(primary_0, secondary_0, null));
	EdgeCubicle P0T1 = new EdgeCubicle(new EdgeCubie(primary_0, null, tertiary_1));
	EdgeCubicle P0S1 = new EdgeCubicle(new EdgeCubie(primary_0, secondary_1, null));
	EdgeCubicle P0T0 = new EdgeCubicle(new EdgeCubie(primary_0, null, tertiary_0));
	///////////////////////////
	//Down face cubicles
	EdgeCubicle P1T1 = new EdgeCubicle(new EdgeCubie(primary_1, null, tertiary_1));
	EdgeCubicle P1S0 = new EdgeCubicle(new EdgeCubie(primary_1, secondary_0, null));
	EdgeCubicle P1T0 = new EdgeCubicle(new EdgeCubie(primary_1, null, tertiary_0));
	EdgeCubicle P1S1 = new EdgeCubicle(new EdgeCubie(primary_1, secondary_1, null));
	//////////////////////////
	//Center piece cubicles
	EdgeCubicle S0T0 = new EdgeCubicle(new EdgeCubie(null, secondary_0, tertiary_0));
	EdgeCubicle S0T1 = new EdgeCubicle(new EdgeCubie(null, secondary_0, tertiary_1));
	EdgeCubicle S1T1 = new EdgeCubicle(new EdgeCubie(null, secondary_1, tertiary_1));
	EdgeCubicle S1T0 = new EdgeCubicle(new EdgeCubie(null, secondary_1, tertiary_0));
	/////////////////////////////

	
	
	
	/**
	 * Indeholder seks faces fordelt på tre arrays alt efter om de er primary, secondary eller tertiary.
	 * 
	 * 
	 */
	
	public Cube(){

		primary[0] = new PrimaryFace(P0S0T0, P0S0T1, P0S1T1, P0S1T0, P0S0, P0T1, P0S1, P0T0, primary_0);
		primary[1] = new PrimaryFace(P1S1T1, P1S0T1, P1S0T0, P1S1T0, P1T1, P1S0, P1T0, P1S1, primary_1);

		secondary[0] = new SecondaryFace(P0S0T1, P0S0T0, P1S0T0, P1S0T1, P0S0, S0T0, P1S0, S0T1, secondary_0);
		secondary[1] = new SecondaryFace(P0S1T0, P0S1T1, P1S1T1, P1S1T0, P0S1, S1T1, P1S1, S1T0, secondary_1);

		tertiary[0] = new TertiaryFace(P0S0T0, P0S1T0, P1S1T0, P1S0T0, P0T0, S1T0, P1T0, S0T0, tertiary_0);
		tertiary[1] = new TertiaryFace(P0S1T1, P0S0T1, P1S0T1, P1S1T1, P0T1, S0T1, P1T1, S1T1, tertiary_1);
		
		

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

}
