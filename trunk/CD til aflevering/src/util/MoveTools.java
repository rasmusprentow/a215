package util;

import java.util.LinkedList;
import java.util.ListIterator;

import cubedrawer.MoveButtons;

public class MoveTools {


	private static LinkedList<MoveButtons> splitList(LinkedList<MoveButtons> moves){
		ListIterator<MoveButtons> iter = moves.listIterator();
		MoveButtons move;
		while(iter.hasNext()){
			move = iter.next();
			if(move.toString().substring(1).equals("2")){ // If it is a double
				iter.remove();
				iter.add(MoveButtons.values()[move.ordinal()-2]);
				iter.add(MoveButtons.values()[move.ordinal()-2]);
			}
		}

		return moves;

	}

	public static LinkedList<MoveButtons> eliminateAll(LinkedList<MoveButtons> moves){
	
		moves = splitList(moves);
		moves = eliminateInverses(moves);
		moves = eliminateQuads(moves);
		moves = eliminateTri(moves);
		moves = eliminateInverses(moves);
		moves = eliminateQuads(moves);
		moves = eliminateTri(moves);
		moves = combineList(moves);
		return moves; 
	}
	/**
	 * Removes all inverse moves from the list. Will not duplicate orignal list. 
	 * @param moves
	 * @return returns the inputed list with removals.
	 */
	public static LinkedList<MoveButtons> eliminateInverses(LinkedList<MoveButtons> moves){
		/* Lidt om iteratorer:
		 * En iterator er en pointer der peger mellem to elementer i en liste. 
		 * Dvs. til at starte med peger den på et sted lige inden det første element. 
		 * Når iter.next() bliver udført returnes det object som iteratoren hopper over. 
		 * Remove fjerner det sidste object der blev passeret. 
		 * Iteratorer er værd at foretrække til denne type operation. Alternativt kunne foreach løkker bruge.
		 * Men disse er meget ineffective for linkedlist. LinkedLists bliver brugt da remove and add funktioner 
		 * Er meget mere effektive end for arrayList. 
		 * 
		 * ArrayList = effectiv random acces - For each prefered.
		 * LinkedList = effectiv tilføj og fjern - Iterator prefered.
		 */
		boolean foundInverses = true;
		while(foundInverses){
			foundInverses = false;
			ListIterator<MoveButtons> iter = moves.listIterator();
			MoveButtons move;
			if(iter.hasNext()){
				MoveButtons previous = iter.next();
				while(iter.hasNext()){
					move = iter.next();
					if(move.equals(MoveButtons.inverseOf(previous))){
						foundInverses = true;
						iter.remove();
						iter.previous();
						iter.remove();
						move = iter.next();
					}
					previous = move;
				}
			}
		}
		return moves;
	}


	
	
	public static LinkedList<MoveButtons> eliminateTri(LinkedList<MoveButtons> moves){
		/* Lidt om iteratorer:
		 * En iterator er en pointer der peger mellem to elementer i en liste. 
		 * Dvs. til at starte med peger den på et sted lige inden det første element. 
		 * Når iter.next() bliver udført returnes det object som iteratoren hopper over. 
		 * Remove fjerner det sidste object der blev passeret. 
		 * Iteratorer er værd at foretrække til denne type operation. Alternativt kunne foreach løkker bruge.
		 * Men disse er meget ineffective for linkedlist. LinkedLists bliver brugt da remove and add funktioner 
		 * Er meget mere effektive end for arrayList. 
		 * 
		 * ArrayList = effectiv random acces - For each prefered.
		 * LinkedList = effectiv tilføj og fjern - Iterator prefered.
		 */
		boolean foundInverses = true;

		ListIterator<MoveButtons> iter = moves.listIterator();
		MoveButtons move;
		int dups = 0;
		if(iter.hasNext()){
			MoveButtons previous = iter.next();
			while(iter.hasNext()){
				move = iter.next();
				if(previous.equals(move)){
					dups++;
				} else  {

					if(dups == 2){
						MoveButtons theUndone = null;
						iter.previous();
						for(int i = 0; i < dups + 1; i++){
							theUndone = iter.previous();
							iter.remove();
						}
						if(theUndone.toString().substring(1).equals("'")){ // If it is a prime Den skal være den omvendte.
							iter.add(MoveButtons.values()[theUndone.ordinal()-1]);
						} else {
							iter.add(MoveButtons.values()[theUndone.ordinal()+1]); 
						}
						move = iter.next();
					}
					dups = 0;
				}
				previous = move;

			}

		}
		return moves;
	}

	public static LinkedList<MoveButtons> combineList(LinkedList<MoveButtons> moves){
		

		ListIterator<MoveButtons> iter = moves.listIterator();
		MoveButtons move;
		if(iter.hasNext()){
			MoveButtons previous = iter.next();
			while(iter.hasNext()){
				move = iter.next();
				if(move.equals(previous) && !move.toString().substring(1).equals("2")){
					iter.remove();
					MoveButtons theMoved = iter.previous();
					iter.remove();
					//System.out.println("Removing two " + theMoved);
					if(theMoved.toString().substring(1).equals("'")){ // If it is a prime
						iter.add(MoveButtons.values()[theMoved.ordinal()+1]);
						//System.out.println("Replacing with " + MoveButtons.values()[theMoved.ordinal()+1]);
					} else {
						iter.add(MoveButtons.values()[theMoved.ordinal()+2]);
						//System.out.println("Replacing with " + MoveButtons.values()[theMoved.ordinal()+2]);
					}
				}
				previous = move;
			}
		}
		return moves;
	}
	
	public static LinkedList<MoveButtons> eliminateQuads(LinkedList<MoveButtons> moves){
		boolean foundInverses = true;

		ListIterator<MoveButtons> iter = moves.listIterator();
		MoveButtons move;
		int dups = 0;
		if(iter.hasNext()){
			MoveButtons previous = iter.next();
			while(iter.hasNext()){
				move = iter.next();
				if(previous.equals(move)){
					dups++;
				} else  {
					if(dups == 3){
						iter.previous();
						for(int i = 0; i < dups + 1; i++){
							iter.previous();
							iter.remove();
						}
						move = iter.next();
					}
					dups = 0;
				}
				previous = move;

			}

		}

		return moves;

	}
}