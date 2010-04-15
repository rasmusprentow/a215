package cube;

import java.awt.Color;


public enum Facelet {
	
	PRIMARY_0,
	PRIMARY_1,
	SECONDARY_0,
	SECONDARY_1,
	TERTIARY_0,
	TERTIARY_1;
	
	public String toString(){
		switch(this){
		case PRIMARY_0:
			return "white";
			
		case PRIMARY_1:
			return "yellow";
			
		case SECONDARY_0:
			return "blue";
		
		case SECONDARY_1:
			return "green";
			
		case TERTIARY_0:
			return "red";
			
		case TERTIARY_1:
			return "orange";
		}
		return null;
	}
	
	
	
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
			return Color.red;
			
		case TERTIARY_1:
			return Color.orange;
		}
		return null;
	}

}

