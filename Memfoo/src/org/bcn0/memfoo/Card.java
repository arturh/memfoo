package org.bcn0.memfoo;

public final class Card {
	public final int _id;
	public final String kanji;
	public final String kana;
	public final String meaning;
	public final String audio;
	public final long due;
	public final long introduced;
	public final int correct;
	
	public Card(int _id, String kanji, String kana, String meaning, String audio, long due, long introduced, int correct) {
		this._id = _id;
		this.kanji = kanji;
		this.kana = kana;
		this.meaning = meaning;
		this.audio = audio;
		this.due = due;
		this.introduced = introduced;
		this.correct = correct;
	}
	
	public String toString() {
		return "Card(" + Integer.toString(_id) + ", " +
				       kanji + ", " +
				       kana + ", " +
				       meaning + ", " +
				       audio + ", " +
				       due + ", " +
				       introduced + ", " +
				       correct + ", " +
				       ")";
				       
	}
}