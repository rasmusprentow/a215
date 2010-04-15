package cube;

public abstract class Cubie {

	protected Facelet primaryFacelet;
	protected Facelet secondaryFacelet;
	protected Facelet tertiaryFacelet;
	protected byte direction;

	protected void setDirection(byte direction) {

	}

	public byte getDirection() {
		return direction;
	}

	/**
	 * 
	 * @param i
	 * @return primary if i = 0 second if 1 = 2 and so on.
	 */
	public Facelet getFacelet(int i){
		switch (i){
		case 0:
			return primaryFacelet;
		case 1:
			return secondaryFacelet;
		case 2:
			return tertiaryFacelet;
		}
		return null;
	}
}
