/**
 * 
 */
package org.bcn0.memfoo;

import java.util.List;
import java.util.Vector;

/**
 * @author arturh@gmail.com
 *
 */
public final class Lesson {
	private List<Card> cards;
	private Card currentCard;
	
	public Lesson(List<Card> cards) {
		this.cards = new Vector<Card>(cards);
	}
	public Card nextDueCard() throws NoDueCardsException {
		// TODO Auto-generated method stub
		throw new NoDueCardsException();
	}
	public boolean hasDue() {
		// TODO Auto-generated method stub
		return false;
	}
	public int dueCards() {
		// TODO Auto-generated method stub
		return 0;
	}
	public int newCards() {
		// TODO Auto-generated method stub
		return cards.size();
	}
	public int totalCards() {
		// TODO Auto-generated method stub
		return cards.size();
	}
	public Card nextNewCard() {
		// TODO Auto-generated method stub
		currentCard = cards.get(0);
		return currentCard;
	}
	public Card getCurrentCard() {
		// TODO Auto-generated method stub
		return currentCard;
	}
	public void forgetCurrentCard() {
		// TODO Auto-generated method stub
		
	}

}
