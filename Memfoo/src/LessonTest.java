import static org.junit.Assert.*;

import java.util.Vector;

import org.bcn0.memfoo.Card;
import org.bcn0.memfoo.Lesson;
import org.bcn0.memfoo.NoDueCardsException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import android.util.Log;


public class LessonTest {

	private Lesson lesson;
	private Lesson empty_lesson;
	
	
	@Test
	public void test_empty_lesson() {
		assertFalse(empty_lesson.hasDue());
		try {
			assertNull(empty_lesson.nextDueCard());
			fail("Expected exception.");
		} catch (NoDueCardsException e) { }
		
		assertEquals(0, empty_lesson.dueCards());
		assertEquals(0, empty_lesson.newCardsCount());
		assertEquals(0, empty_lesson.totalCards());
		
	}


	@Before
	public void setUp() throws Exception {
		Vector<Card> cards = new Vector<Card>();
		Card new_card = new Card(0, "kanji", "kana", "meaning", "audio", 0, 0, 0);
		cards.add(new_card);
		this.lesson = new Lesson(cards);
		
		this.empty_lesson = new Lesson(new Vector<Card>());
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInitialCounts() {
		assertEquals(1, lesson.totalCards());
		assertEquals(0, lesson.dueCards());
		assertEquals(1, lesson.newCardsCount());
	}
	
	@Test
	public void testCurrentCard() {
		Card card = lesson.nextNewCard();
		assertEquals(card, lesson.getCurrentCard());
	}
	
	@Test
	public void testForgetCard() {
		assertEquals(1, lesson.newCardsCount());
		lesson.nextNewCard();
		lesson.forgetCurrentCard();
		assertEquals(0, lesson.newCardsCount());
	}


}
