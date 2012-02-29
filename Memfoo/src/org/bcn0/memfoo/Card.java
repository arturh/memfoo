package org.bcn0.memfoo;

public final class Card {
	public final int _id;
	public final String kanji;
	public final String kana;
	public final String meaning;
	public final String audio;
	public final int due;
	public final int introduced;
	public final int correct;
	
	public Card(int _id, String kanji, String kana, String meaning, String audio, int due, int introduced, int correct) {
		this._id = _id;
		this.kanji = kanji;
		this.kana = kana;
		this.meaning = meaning;
		this.audio = audio;
		this.due = due;
		this.introduced = introduced;
		this.correct = correct;
	}
}