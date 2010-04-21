package algorithms;

import java.util.EnumSet;

import cube.Cube;
import cubedrawer.MoveButtons;
import static cubedrawer.MoveButtons.*;

public class Kociemba {
	
	private Cube cube;
	private EnumSet<MoveButtons> S = EnumSet.of(U, UP ,U2, D, DP, D2, F, FP, F2,  B, BP, B2, L, LP, L2, R, RP, R2);
	private EnumSet<MoveButtons> A = EnumSet.of(U, UP ,U2, D, DP, D2, F2, B2, L2, R2);
	private EnumSet<MoveButtons> notA = EnumSet.of(F, FP, B, BP, L, LP, R, RP);
	
	public Kociemba (Cube a) {
		cube = a;
	}
	
	public MoveButtons[] solve(int maxSMoves) {
		
		MoveButtons[] result = new MoveButtons[29];
		int d = 0;
		int l = Integer.MAX_VALUE;
		MoveButtons[] b;
		
		while (l > d) {
			b = new MoveButtons[d];
			
			
			
			
		}
		
		
		return result;
		
	}
	
	public MoveButtons[] increaseWithSNotEndingWithA(MoveButtons[] moveSequence) {
		int length = moveSequence.length;
		
		
		try {
		moveSequence[length-1] = (MoveButtons)notA.toArray()[moveSequence[length-1].ordinal() + 1];
		} catch (ArrayIndexOutOfBoundsException e) {
			moveSequence[length-1] = F;
			for (int i = 2; i <= length; i++) {
				try {
				moveSequence[length-i] = (MoveButtons)notA.toArray()[moveSequence[length-i].ordinal() + 1];
				break;
				} catch (ArrayIndexOutOfBoundsException e1) {
					if (length - i != 0) {
						moveSequence[length-i] = U;
					} else {
						throw 
					}
				}
			}
		}
		
		
		return moveSequence;
		
	}
}
