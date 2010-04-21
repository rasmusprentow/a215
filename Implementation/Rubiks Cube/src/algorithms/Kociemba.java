package algorithms;

import java.util.EnumSet;

import cube.*;
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

			for (int i = 0; i < b.length; i++) {

				if (b.length - i == 1) {
					b[b.length - 1] = F;
				} else if (i%2 == 0) {
					b[i] = U;
				} else {
					b[i] = D;
				}
			}
			try {
				while (true) {
					Cube.permute(cube, b);
					if ()

						increaseWithSNotEndingWithA(b, b.length-1);
				}
			} catch (UnableToIncreaseMoveSequenceException e) {

			}
			d++;
		}
		return result;
	}

	/**
	 * Needs to be written! Btw, recursive method.
	 * @param moveSequence
	 * @param startWith
	 * @return
	 * @throws UnableToIncreaseMoveSequenceException
	 */
	private MoveButtons[] increaseWithSNotEndingWithA(MoveButtons[] moveSequence, int startWith) throws UnableToIncreaseMoveSequenceException {
		int length = moveSequence.length;
		int i = length - startWith;

		try {
			if (i == 1) {
				i++;
				do {
					moveSequence[length-1] = (MoveButtons)S.toArray()[moveSequence[length-1].ordinal() + 1];
				} while(A.contains(moveSequence[length-1]));

			} else {
				throw new ArrayIndexOutOfBoundsException();
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			moveSequence[length-1] = F;
			for (; i <= length; i++) {
				try {
					moveSequence[length-i] = (MoveButtons)S.toArray()[moveSequence[length-i].ordinal() + 1];
					break;
				} catch (ArrayIndexOutOfBoundsException e1) {
					if (length - i != 0) {
						moveSequence[length-i] = U;
					} else {
						throw new UnableToIncreaseMoveSequenceException();
					}
				}
			}
		}

		for ( ; i > 0; i--) {
			try {
				if(isSameFace(moveSequence[length-i], moveSequence[length-i-1])) {
					increaseWithSNotEndingWithA(moveSequence, length-i);
					break;
				}
			} catch (ArrayIndexOutOfBoundsException e2) {

			}
		}
		return moveSequence;
	}

	private MoveButtons[] solveFromH() throws InvalidCube {

		if (!cube.isInH()) {
			throw new InvalidCube("The cube is not in H!!");
		} 
		if (cube.isSolvedInsideH()) {
			return null;
		} 

		MoveButtons[] c;

		for (int d = 1; d <= 18; d++) {
			c = new MoveButtons[d];

			for (int i = 0; i < c.length; i++) {

				if (i%2 == 0) {
					c[i] = U;
				} else {
					c[i] = D;
				}
			}
			try {
				while (true) {
					Cube.permute(cube, c);
					if (cube.isSolvedInsideH()) {
						return c;
					}
				}
			} catch (UnableToIncreaseMoveSequenceException e) {
				
			}
		}




		return c;

	}

	private MoveButtons[] increaseWithA(MoveButtons[] moveSequence, int startWith) throws UnableToIncreaseMoveSequenceException {
		int length = moveSequence.length;
		int i = length - startWith;

		for (; i <= length; i++) {
			try {
				do {
					moveSequence[length-i] = (MoveButtons)S.toArray()[moveSequence[length-i].ordinal() + 1];
				} while(notA.contains(moveSequence[length-i]));
				break;
			} catch (ArrayIndexOutOfBoundsException e1) {
				if (length - i != 0) {
					moveSequence[length-i] = U;
				} else {
					throw new UnableToIncreaseMoveSequenceException();
				}
			}
		}

		for ( ; i > 0; i--) {
			try {
				if(isSameFace(moveSequence[length-i], moveSequence[length-i-1])) {
					increaseWithA(moveSequence, length-i);
					break;
				}
			} catch (ArrayIndexOutOfBoundsException e2) {

			}
		}

		return moveSequence;
	}
}
