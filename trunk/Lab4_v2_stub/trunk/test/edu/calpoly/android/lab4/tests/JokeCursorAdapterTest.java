package edu.calpoly.android.lab4.tests;

import java.lang.reflect.Field;

import android.content.Context;
import android.database.Cursor;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.View;
import android.widget.Adapter;
import edu.calpoly.android.lab4.Joke;
import edu.calpoly.android.lab4.JokeCursorAdapter;
import edu.calpoly.android.lab4.JokeView;
import edu.calpoly.android.lab4.JokeView.OnJokeChangeListener;

import edu.calpoly.android.lab4_key.JokeDBAdapter;

public class JokeCursorAdapterTest extends AndroidTestCase {
	private Context m_context;
	private JokeDBAdapter m_dbAdapter;
	private Cursor m_jokeCursor;
	private JokeCursorAdapter m_adapter;
	private OnJokeChangeListener m_listener;
	private long m_nSelectedID;
	
	private static final String N_SELECTED_ID = "m_nSelectedID";
	private static final String LISTENER = "m_listener";
	
	@Override
	/**
	 * This also Tests to make sure that all member variables and their types have not 
	 * been changed. 
	 */
	public void setUp() throws Exception {
		m_context = this.getContext();
		
		m_dbAdapter= new JokeDBAdapter(m_context);
		m_dbAdapter.open();
		
		m_jokeCursor = m_dbAdapter.getAllJokes();

		// Make sure the database has at least one joke.
		if (!m_jokeCursor.moveToFirst()) {
			m_dbAdapter.insertJoke(new Joke("testJoke", "testAuthor"));
			m_jokeCursor.requery();
			m_jokeCursor.moveToFirst();
		}
		
		m_adapter = new JokeCursorAdapter(m_context, m_jokeCursor);
		m_adapter.setOnJokeChangeListener(new OnJokeChangeListener() {
			@Override
			public void onJokeChanged(JokeView view, Joke joke) {}
		});
		
		m_listener = retrieveHiddenMember(LISTENER, m_listener, m_adapter);
		m_nSelectedID = retrieveHiddenMember(N_SELECTED_ID, m_nSelectedID, m_adapter);
	}

	@Override
	public void tearDown() throws Exception {
		m_jokeCursor.close();
		m_dbAdapter.close();
		super.tearDown();
	}
	

	@SmallTest
	/**
	 * Test Constructor
	 */
	public void testJokeCursorAdapter() {
		m_adapter = new JokeCursorAdapter(m_context, m_jokeCursor);		
		m_listener = retrieveHiddenMember(LISTENER, m_listener, m_adapter);
		m_nSelectedID = retrieveHiddenMember(N_SELECTED_ID, m_nSelectedID, m_adapter);
		
		assertTrue("JokeCursorAdapter should extend android.widget.CursorAdapter", m_adapter instanceof android.widget.CursorAdapter);
		assertEquals("m_nSelectedID should be initialized to Adapter.NO_SELECTION", Adapter.NO_SELECTION, m_nSelectedID);
		assertEquals("m_listener should be initialized to \"null\"", null, m_listener);
	}

	@SmallTest
	/**
	 * Test Accessor Method
	 */
	public void testGetSelectedID() {
		assertEquals("The getSelectedID() method does not return the current value of m_nSelectedID", m_nSelectedID, m_adapter.getSelectedID());
	}

	@SmallTest
	/**
	 * Test Mutator Method
	 */
	public void testSetOnJokeChangeListener() {
		OnJokeChangeListener testListener = new OnJokeChangeListener() {
			@Override
			public void onJokeChanged(JokeView view, Joke joke) {}
		};
		m_adapter.setOnJokeChangeListener(testListener);
		m_listener = retrieveHiddenMember(LISTENER, m_listener, m_adapter);
		assertEquals("The setOnJokeChangeListener(...) method does not properly set m_listener", testListener, m_listener);
	}

	public void testBindViewViewContextCursor() {
		
		//Setup the JokeView to recycle
		JokeView recycledView = new JokeView(m_context, new Joke("Blah", "Author", Joke.UNRATED, 999));
		JokeView sameView = recycledView;
		
		OnJokeChangeListener oldListener = new OnJokeChangeListener() {
			@Override
			public void onJokeChanged(JokeView view, Joke joke) {}
		};
		recycledView.setOnJokeChangeListener(oldListener);
		
		//Initialize the cursor & joke the JokeView will use and Bind the View
		m_jokeCursor.moveToFirst();
		Joke newJoke = JokeDBAdapter.getJokeFromCursor(m_jokeCursor);
		m_adapter.bindView(recycledView, m_context, m_jokeCursor);
		
		//Test the BindView didn't create a new JokeView
		assertEquals("BindView should not create a new JokeView, it should re-use the JokeView passed into it", sameView, recycledView);
		
		//Test that BindView called setJoke() using the joke from the cursor
		Joke joke = null;
		joke = this.retrieveHiddenMember("m_joke", joke, recycledView);		
		assertEquals("BindView did not properly update the Joke for the JokeView passed in. It should use the Joke pointed to by the cursor passed in.", newJoke, joke);

		//Test that the BindView called setOnJokeChangeListener using the JokeCursorAdapters OnJokeChangeListener
		OnJokeChangeListener listener = null;
		listener = this.retrieveHiddenMember("m_onJokeChangeListener", listener, recycledView);	
		assertEquals("BindView did not properly update the OnJokeChangeListener for the JokeView passed in. It should use the JokeCursorAdapters OnJokeChangeListener.", m_listener, listener);
	}

	public void testNewViewContextCursorViewGroup() {		
		//Initialize the cursor & joke the JokeView will use and get the new View
		m_jokeCursor.moveToFirst();
		Joke newJoke = JokeDBAdapter.getJokeFromCursor(m_jokeCursor);
		View view = m_adapter.newView(m_context, m_jokeCursor, null);
		
		//Test the NewView created a new JokeView
		assertTrue("BindView should return a new JokeView", view instanceof JokeView && view != null);
		
		//Test that NewView called setJoke() using the joke from the cursor
		Joke joke = null;
		joke = this.retrieveHiddenMember("m_joke", joke, view);		
		assertEquals("BindView did not properly update the Joke for the JokeView passed in. It should use the Joke pointed to by the cursor passed in.", newJoke, joke);

		//Test that the BindView called setOnJokeChangeListener using the JokeCursorAdapters OnJokeChangeListener
		OnJokeChangeListener listener = null;
		listener = this.retrieveHiddenMember("m_onJokeChangeListener", listener, view);	
		assertEquals("BindView did not properly update the OnJokeChangeListener for the JokeView passed in. It should use the JokeCursorAdapters OnJokeChangeListener.", m_listener, listener);
	}

	@SmallTest
	/**
	 * Test OnItemLongClick listener interface
	 */
	public void testOnItemLongClick() {
		int testID = 999; 
		assertFalse("The onItemLongClick(...) method should not consume the OnItemLongClick Event.", m_adapter.onItemLongClick(null, null, 0, testID));
		
		m_nSelectedID = retrieveHiddenMember(N_SELECTED_ID, m_nSelectedID, m_adapter);
		assertEquals("The onItemLongClick(...) method does not properly update m_selectedID with the ID of the currently selected Joke", testID, m_nSelectedID);
	}

	/*************************************/
	/**	Java Friend-Class Helper Method **/
	/*************************************/
	@SuppressWarnings("unchecked")
	public <T> T retrieveHiddenMember(String memberName, T type, Object sourceObj) {
		Field field = null;
		T returnVal = null;
		//Test for proper existence
		try {
			field = sourceObj.getClass().getDeclaredField(memberName);
		} catch (NoSuchFieldException e) {
			fail("The field \"" + memberName + "\" was renamed or removed. Do not rename or remove this member variable.");
		}
		field.setAccessible(true);
		
		//Test for proper type
		try {
			returnVal = (T)field.get(sourceObj);
		} catch (ClassCastException exc) {
			fail("The field \"" + memberName + "\" had its type changed. Do not change the type on this member variable.");
		}  
		
		// Boiler Plate Exception Checking. If any of these Exceptions are 
		// throw it was becuase this method was called improperly.
		catch (IllegalArgumentException e) {
			fail ("This is an Error caused by the UnitTest!\n Improper user of retrieveHiddenMember(...) -- IllegalArgumentException:\n Passed in the wrong object to Field.get(...)");
		} catch (IllegalAccessException e) {
			fail ("This is an Error caused by the UnitTest!\n Improper user of retrieveHiddenMember(...) -- IllegalAccessException:\n Field.setAccessible(true) should be called.");
		}
		return returnVal; 
	}
}
