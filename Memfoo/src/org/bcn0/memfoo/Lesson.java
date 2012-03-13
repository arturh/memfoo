/**
 * 
 */
package org.bcn0.memfoo;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * @author arturh@gmail.com
 * 
 */
public final class Lesson {
	private List<Card> cards;
	private Card currentCard;

	public Lesson(List<Card> cards) {
		this.cards = new ArrayList<Card>(cards);
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

	public int newCardsCount() {
		// TODO Auto-generated method stub
		return newCards().size();
	}

	public int totalCards() {
		return cards.size();
	}

	public Card nextNewCard() {
		// TODO Auto-generated method stub
		currentCard = cards.get(0);
		return currentCard;
	}

	public Card getCurrentCard() {
		return currentCard;
	}

	public void forgetCurrentCard() {
		final long introduced;
		if (currentCard.introduced == 0) {
			introduced = (new Date()).getTime();
		} else {
			introduced = currentCard.introduced;
		}
		updateCurrentCard(currentCard._id, currentCard.kanji,
				currentCard.kana, currentCard.meaning, currentCard.audio,
				currentCard.due, introduced, 0);
	}

	private void updateCurrentCard(int _id, String kanji, String kana,
			String meaning, String audio, long due, long introduced, int correct) {
		
			Card newCard = new Card(
					_id, kanji, kana, meaning, audio, due, introduced, correct);
			int current_card_location = cards.indexOf(currentCard);
			
			cards.set(current_card_location, newCard);
			currentCard = newCard;
	}

	public List<Card> newCards() {
		ArrayList<Card> new_cards = new ArrayList<Card>();
		for (Card card : cards) {
			if (card.introduced == 0) {
				new_cards.add(card);
			}
		}
		return new_cards;
	}
}
