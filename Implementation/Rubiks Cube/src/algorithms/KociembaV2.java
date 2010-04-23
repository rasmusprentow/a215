package algorithms;

import java.util.EnumSet;

import cube.*;
import cubedrawer.MoveButtons;
import static cubedrawer.MoveButtons.*;

public class KociembaV2 {

	private Cube cube;
	private AlgorithmOutput output;
	private EnumSet<MoveButtons> S = EnumSet.of(U, UP ,U2, D, DP, D2, F, FP, F2,  B, BP, B2, L, LP, L2, R, RP, R2);
	private EnumSet<MoveButtons> A = EnumSet.of(U, UP ,U2, D, DP, D2, F2, B2, L2, R2);
	private EnumSet<MoveButtons> notA = EnumSet.of(F, FP, B, BP, L, LP, R, RP);

	public KociembaV2 (Cube a) {
		cube = a;
	}

	public KociembaV2 (Cube a , AlgorithmOutput output) {
		cube = a;
		this.output = output;
	}

	/**
	 * Solves the cube which is referred to when constructing the Kociemba object
	 * @param maxSMoves Maximum number of S moves
	 * @return The move sequence to solve the cube 
	 */
	public MoveButtons[] solve(int maxSMoves) {

		MoveButtons[] result = null;
		int d = 0;
		int l = Integer.MAX_VALUE;
		MoveButtons[] b,c;
		while (l > d && d <= maxSMoves) {
			output.addTextln("Try solving with depth: " + d);
			System.out.println("Try solving with depth: " + d);
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
			Cube.permute(cube, b);
			try {
				while (true) {
					//System.out.println("Permuting!");

					try {
						c = solveFromH(l - d);
						//c = solveFromH(5);
						if (d + c.length < l) {
							l = d + c.length;
							result = new MoveButtons[l];
							output.addTextln("The solutions of the length " + l + ". The solution is:");
							System.out.println("The solutions of the length " + l + ". The solution is:");
							int j = 0;
							for ( ; j < d; j++) {
								result[j] = b[j];
								output.addText(b[j] + " ");
								System.out.print(b[j] + " ");
							}
							for (int k = 0 ; k < c.length; k++,j++) {
								result[j] = c[k];
								output.addText(c[k] + " ");
								System.out.print(c[k] + " ");
							}
							output.addTextln("");
							System.out.println();
						}

					} catch (InvalidCube e) {
						//System.out.println("Not in H!");
					}
					/*
					System.out.print("S: ");
					for (int i = 0 ; i < b.length; i++) {
						System.out.print(b[i] + " ");
					}
					System.out.println();*/
					try {
						Cube.permute(cube, b[d-1].invert());
						b[d-1].invert();
					} catch (ArrayIndexOutOfBoundsException e) {

					}
					increaseWithSNotEndingWithA(b, d-1);
				}
			} catch (UnableToIncreaseMoveSequenceException e) {

			}
			//Cube.permute(cube, MoveButtons.inverseOf(b));
			//MoveButtons.inverseOf(b);
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
	private void increaseWithSNotEndingWithA(MoveButtons[] moveSequence, int startWith) throws UnableToIncreaseMoveSequenceException {

		int length = moveSequence.length;
		int i = startWith;
		if(startWith < 0 || length <= 0) {
			throw new UnableToIncreaseMoveSequenceException();
		}

		if (i == length - 1) {

			try {
				do {
					moveSequence[i] = (MoveButtons)S.toArray()[moveSequence[i].ordinal() + 1];
				} while(A.contains(moveSequence[i]));
				try {
					if(isSameFace(moveSequence[i], moveSequence[i-1])) {
						increaseWithSNotEndingWithA(moveSequence, i);
					} else {
						Cube.permute(cube, moveSequence[i]);
					}
				} catch (ArrayIndexOutOfBoundsException e2) {
					Cube.permute(cube, moveSequence[i]);
				}
				return;
			} catch (ArrayIndexOutOfBoundsException e) {
				moveSequence[i] = F;
				Cube.permute(cube, moveSequence[i].invert());
				moveSequence[i].invert();
				i--;
				increaseWithSNotEndingWithA(moveSequence, i);
				return;
			}
		} else {
			try {
				moveSequence[i] = (MoveButtons)S.toArray()[moveSequence[i].ordinal() + 1];
				for (int j = i ; j < length; j++) {
					try {
						if(isSameFace(moveSequence[j], moveSequence[j-1])) {
							increaseWithSNotEndingWithA(moveSequence, j);
							return;
						} else {
							Cube.permute(cube, moveSequence[j]);
						}
					} catch (ArrayIndexOutOfBoundsException e2) {
						Cube.permute(cube, moveSequence[j]);
					}
				}
				return; 
			} catch (ArrayIndexOutOfBoundsException e) {
				moveSequence[i] = U;
				Cube.permute(cube, moveSequence[i].invert());
				moveSequence[i].invert();
				i--;
				increaseWithSNotEndingWithA(moveSequence, i);
				return;
			}
		}
	}

	private MoveButtons[] solveFromH(int maxAMoves) throws InvalidCube {

		if (!cube.isInH()) {
			throw new InvalidCube("The cube is not in H!!");
		} 
		if (cube.isSolvedInsideH()) {
			//System.out.println("One solution found! With 0 A moves");
			return new MoveButtons[0];
		} 
		System.out.println("In H");
		MoveButtons[] c;

		for (int d = 1; d < maxAMoves; d++) {
			//output.addTextln("Solving in H, with depth: " + d);
			c = new MoveButtons[d];

			for (int i = 0; i < d; i++) {

				if (i%2 == 0) {
					c[i] = U;
				} else {
					c[i] = D;
				}
			}
			Cube.permute(cube, c);
			try {
				while (true) {



					if (cube.isSolvedInsideH()) {
						//System.out.println("Another solution found!");
						Cube.permute(cube, MoveButtons.inverseOf(c));
						MoveButtons.inverseOf(c); //Inverted twice
						return c;
					} else {
						/*
						System.out.print("A: ");
						for (int i = 0 ; i < c.length; i++) {
							System.out.print(c[i] + " ");
						}
						System.out.println();*/

						Cube.permute(cube, c[d-1].invert());
						c[d-1].invert();
						increaseWithA(c, d-1);
					}
				}

			} catch (UnableToIncreaseMoveSequenceException e) {

			}
			//Cube.permute(cube, MoveButtons.inverseOf(c));
			//MoveButtons.inverseOf(c);

		}

		return new MoveButtons[maxAMoves+1];
	}

	private void increaseWithA(MoveButtons[] moveSequence, int startWith) throws UnableToIncreaseMoveSequenceException {

		int length = moveSequence.length;
		int i = startWith;

		if(startWith < 0 || length <= 0) {
			throw new UnableToIncreaseMoveSequenceException();
		}

		try {
			do {
				moveSequence[i] = (MoveButtons)S.toArray()[moveSequence[i].ordinal() + 1];
			} while(notA.contains(moveSequence[i]));

			for (int j = i ; j < length; j++) {
				try {
					if(isSameFace(moveSequence[j], moveSequence[j-1])) {
						increaseWithA(moveSequence, j);
						return;
					} else {
						Cube.permute(cube, moveSequence[j]);
					}
				} catch (ArrayIndexOutOfBoundsException e2) {
					Cube.permute(cube, moveSequence[j]);
				}
			}
			return; 
		} catch (ArrayIndexOutOfBoundsException e) {
			moveSequence[i] = U;
			Cube.permute(cube, moveSequence[i].invert());
			moveSequence[i].invert();
			i--;
			increaseWithA(moveSequence, i);
			return;
		}
	}
}

