package cube;

public abstract class Cubie {

	protected Facelet primaryFacelet;
	protected Facelet secondaryFacelet;
	protected Facelet tertiaryFacelet;
	protected byte primaryOrientation;
	protected byte secondaryOrientation;

	protected abstract void setPrimaryOrientation(byte direction);
	
	protected abstract void setSecondaryOrientation(byte direction);

	public byte getPrimaryOrientation() {
		return primaryOrientation;
	}
	
	public byte getSecondaryOrientation() {
		return secondaryOrientation;
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
