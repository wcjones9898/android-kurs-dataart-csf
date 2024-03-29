package edu.calpoly.android.lab2;

import android.test.suitebuilder.annotation.SmallTest;
import edu.calpoly.android.lab2.Joke;
import junit.framework.TestCase;

public class JokeTest extends TestCase {

	@SmallTest
	/**
	 * Test Default Constructor
	 */
	public void testJoke() {
		Joke joke = new Joke();		
		
		assertTrue("m_strJoke should be initialized to \"\".", joke.getJoke().equals(""));
		assertEquals("m_nRating should be initialized to Joke.UNRATED.", Joke.UNRATED, joke.getRating());
	}

	@SmallTest
	/**
	 * Test Parameterized Constructor: Joke(String strJoke)
	 */
	public void testJokeString() {
		String strJoke = "testJoke";
		Joke joke = new Joke(strJoke);
		assertEquals("m_strJoke should be initialized to \"testJoke\".", strJoke, joke.getJoke());
		assertEquals("m_nRating should be initialized to Joke.UNRATED.", Joke.UNRATED, joke.getRating());
	}

	@SmallTest
	/**
	 * Test Parameterized Constructor: Joke(String strJoke, int nRating)
	 */
	public void testJokeStringInt() {
		String strJoke = "testJoke";
		Joke joke = new Joke(strJoke, Joke.DISLIKE);
		assertEquals("m_strJoke should be initialized to \"testJoke\".", strJoke, joke.getJoke());
		assertEquals("m_nRating should be initialized to Joke.DISLIKE.", Joke.DISLIKE, joke.getRating());
	}
	
	@SmallTest
	/**
	 * Test Mutator Method
	 */
	public void testSetJoke() {
		String strJoke = "testJoke";
		Joke joke = new Joke();
		joke.setJoke(strJoke);
		assertEquals("m_strJoke should be set to \"testJoke\".", strJoke, joke.getJoke());
	}

	@SmallTest
	/**
	 * Test Mutator Method
	 */
	public void testSetRating() {
		Joke joke = new Joke();
		joke.setRating(Joke.LIKE);
		assertEquals("m_nRating should be set to Joke.LIKE.", Joke.LIKE, joke.getRating());
	}

	@SmallTest
	public void testEquals() {
		String strJoke = "testJoke";
		String strJokeEQ = "testJoke";
		Joke joke = new Joke(strJoke);
		Joke jokeEQ = new Joke(strJoke);
		Joke jokeEQ2 = new Joke(strJokeEQ);
		Joke jokeNEQ = new Joke("different");
		
		assertFalse("equals(Object obj) should return false. Testing against null", joke.equals(null));
		assertFalse("equals(Object obj) should return false. Not comparing two obj is not an instance of Joke", joke.equals(strJoke));
		assertFalse("equals(Object obj) should return false. The two jokes have different m_strJoke values", joke.equals(jokeNEQ));
		assertTrue("equals(Object obj) should return true. The testing against itself", joke.equals(joke));
		assertTrue("equals(Object obj) should return true. The testing against different Joke containing a reference to the same String object", joke.equals(jokeEQ));
		assertTrue("equals(Object obj) should return true. The testing against different Joke containing different String with same text", joke.equals(jokeEQ2));
	}

	@SmallTest
	public void testToString() {
		String strJoke = "testJoke";
		Joke joke = new Joke(strJoke);
		assertEquals("toString() should be return \"testJoke\".", strJoke, joke.toString());
		assert(joke.toString().equals(strJoke));
	}

}
