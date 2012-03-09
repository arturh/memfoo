import static org.junit.Assert.*;

import java.util.Vector;

import org.bcn0.memfoo.Card;
import org.bcn0.memfoo.Lesson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class LessonTest {

	private Lesson lesson;

	@Before
	public void setUp() throws Exception {
		Vector cards = new Vector();
		Card new_card = new Card(0, "kanji", "kana", "meaning", "audio", 0, 0, 0);
		cards.add(new_card);
		this.lesson = new Lesson(cards);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInitialCounts() {
		assertEquals(1, lesson.totalCards());
		assertEquals(0, lesson.dueCards());
		assertEquals(1, lesson.newCards());
	}
	
	@Test
	public void testCurrentCard() {
		Card card = lesson.nextNewCard();
		assertEquals(card, lesson.getCurrentCard());
	}
	
	@Test
	public void testForgetCard() {
		Card card = lesson.nextNewCard();
		lesson.forgetCurrentCard();
		assertEquals(0, lesson.newCards());
	}

}
