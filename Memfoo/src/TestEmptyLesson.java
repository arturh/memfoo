import static org.junit.Assert.*;

import java.util.Vector;

import org.bcn0.memfoo.Lesson;
import org.bcn0.memfoo.NoDueCardsException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestEmptyLesson {

	private Lesson empty_lesson;

	@Before
	public void setUp() throws Exception {
		this.empty_lesson = new Lesson(new Vector());
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test_empty_lesson() {
		assertFalse(empty_lesson.hasDue());
		try {
			assertNull(empty_lesson.nextDueCard());
			fail("Expected exception.");
		} catch (NoDueCardsException e) { }
		
		assertEquals(0, empty_lesson.dueCards());
		assertEquals(0, empty_lesson.newCards());
		assertEquals(0, empty_lesson.totalCards());
		
	}

}
