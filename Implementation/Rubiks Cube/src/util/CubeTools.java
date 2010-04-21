package util;

import java.util.LinkedList;
import java.util.ListIterator;

import cubedrawer.MoveButtons;

public class CubeTools {

	/**
	 * Removes all inverse moves from the list. Will not duplicate orignal list. 
	 * @param moves
	 * @return returns the inputed list with removals.
	 */
	public static LinkedList<MoveButtons> eliminateInverses(LinkedList<MoveButtons> moves){
		/* Lidt om iteratorer:
		 * En iterator er en pointer der peger mellem to elementer i en liste. 
		 * Dvs. til at starte med peger den p� et sted lige inden det f�rste element. 
		 * N�r iter.next() bliver udf�rt returnes det object som iteratoren hopper over. 
		 * Remove fjerner det sidste object der blev passeret. 
		 * Iteratorer er v�rd at foretr�kke til denne type operation. Alternativt kunne foreach l�kker bruge.
		 * Men disse er meget ineffective for linkedlist. LinkedLists bliver brugt da remove and add funktioner 
		 * Er meget mere effektive end for arrayList. 
		 * 
		 * ArrayList = effectiv random acces - For each prefered.
		 * LinkedList = effectiv tilf�j og fjern - Iterator prefered.
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
}
