package cube;

public abstract class Cubie {

	protected Facelet primaryFacelet;
	protected Facelet secondaryFacelet;
	protected Facelet tertiaryFacelet;
	protected byte primaryOrientation = 0; // By default all primary facelets are in the right position
	protected byte secondaryOrientation = 1; // And all secondary facelets are on secondary faces.

	protected abstract void setPrimaryOrientation(byte direction);
	
	protected abstract void setSecondaryOrientation(byte direction);

	public byte getPrimaryOrientation() {
		return primaryOrientation;
	}
	
	public byte getSecondaryOrientation() {
		return secondaryOrientation;
	}
	
	

	/**
	 * If it is an edge it must be between 0 and 1. 
	 * @param i primary if i = 0 secondary if i = 1 and tertiary if i = 2.
	 * @return primary if i = 0 secondary if i = 1 and tertiary if i = 2.
	 * 
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
