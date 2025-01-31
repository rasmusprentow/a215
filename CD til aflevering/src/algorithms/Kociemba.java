package algorithms;

import java.util.EnumSet;

import cube.*;
import cubedrawer.MoveButtons;
import static cubedrawer.MoveButtons.*;

public class Kociemba {

	private Cube cube;
	private AlgorithmOutput output;
	private EnumSet<MoveButtons> S = EnumSet.of(U, UP ,U2, D, DP, D2, F, FP, F2,  B, BP, B2, L, LP, L2, R, RP, R2);
	private EnumSet<MoveButtons> A = EnumSet.of(U, UP ,U2, D, DP, D2, F2, B2, L2, R2);
	private EnumSet<MoveButtons> notA = EnumSet.of(F, FP, B, BP, L, LP, R, RP);

	public Kociemba (Cube a) {
		cube = a;
	}
	
	public Kociemba (Cube a , AlgorithmOutput output) {
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
			
			try {
				while (true) {
					//System.out.println("Permuting!");
					Cube.permute(cube, b);
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

					} catch (InvalidCubeException e) {
						//System.out.println("Not in H!");
					}
/*
					System.out.print("S: ");
					for (int i = 0 ; i < b.length; i++) {
						System.out.print(b[i] + " ");
					}
					System.out.println();*/
					
					Cube.permute(cube, MoveButtons.inverseOf(b));
					MoveButtons.inverseOf(b);					
					
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
		if(length <= 0) {
			throw new UnableToIncreaseMoveSequenceException();
		}

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
			if (length == 1 ) {
				throw new UnableToIncreaseMoveSequenceException();
			}
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

	private MoveButtons[] solveFromH(int maxAMoves) throws InvalidCubeException {

		if (!cube.isInH()) {
			throw new InvalidCubeException("The cube is not in H!!");
		} 
		if (cube.isSolvedInsideH()) {
			//System.out.println("One solution found! With 0 A moves");
			return new MoveButtons[0];
		} 
		
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
			try {
				while (true) {

					Cube.permute(cube, c);

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
						
						Cube.permute(cube, MoveButtons.inverseOf(c));
						MoveButtons.inverseOf(c);

						increaseWithA(c, d-1);
					}
				}

			} catch (UnableToIncreaseMoveSequenceException e) {

			}
		}
		return new MoveButtons[maxAMoves+1];
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
