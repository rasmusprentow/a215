package cube;

import java.awt.Color;


public enum Facelet {
	
	PRIMARY_0,
	PRIMARY_1,
	SECONDARY_0,
	SECONDARY_1,
	TERTIARY_0,
	TERTIARY_1;
	
	public Color toColor(){
		switch(this){
		case PRIMARY_0:
			return Color.white;
			
		case PRIMARY_1:
			return Color.yellow;
			
		case SECONDARY_0:
			return Color.blue;
		
		case SECONDARY_1:
			return Color.green;
			
		case TERTIARY_0:
			return Color.orange;
			
		case TERTIARY_1:
			return Color.red;
		}
		return null;
	}

}

