package edu.calpoly.android.lab4;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class JokeView extends RelativeLayout implements OnClickListener, OnCheckedChangeListener, Checkable{

	private Button m_vwExpandButton;
	private RadioButton m_vwLikeButton;
	private RadioButton m_vwDislikeButton;
	private RadioGroup m_vwLikeGroup;
	private TextView m_vwJokeText;
	private Joke m_joke;
	private OnJokeChangeListener m_onJokeChangeListener;

	public static final String EXPAND = " + ";
	public static final String COLLAPSE = " - ";

	/**
	 * Basic Constructor that takes only takes in an application Context.
	 * 
	 * @param context
	 *            The application Context in which this view is being added. 
	 *            
	 * @param joke
	 * 			  The Joke this view is responsible for displaying.
	 */
	public JokeView(Context context, Joke joke) {
		super(context);
		
		// Inflate the layout and set this JokeView as the root ViewGroup.
		((LayoutInflater)context
		 .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
		 .inflate(R.layout.joke_view, this, true);
		
		// Initialize View member variables
		m_vwExpandButton = (Button)findViewById(R.id.expandButton);
		m_vwExpandButton.setOnClickListener(this);
		
		m_vwLikeGroup = (RadioGroup)findViewById(R.id.ratingRadioGroup);
		m_vwLikeGroup.setOnCheckedChangeListener(this);

		m_vwLikeButton = (RadioButton)findViewById(R.id.likeButton);

		m_vwDislikeButton = (RadioButton)findViewById(R.id.dislikeButton);
		
		m_vwJokeText= (TextView)findViewById(R.id.jokeTextView);

        m_onJokeChangeListener = null;

		setJoke(joke);
		
		collapseJokeView();
	}

	/**
	 * Mutator method for changing the Joke object this View displays. This View
	 * will be updated to display the correct contents of the new Joke.
	 * 
	 * @param joke
	 *            The Joke object which this View will display.
	 */
	public void setJoke(Joke joke) {
		m_joke = joke;
		m_vwJokeText.setText(m_joke.getJoke());
		if (m_joke.getRating() == Joke.LIKE) {
			m_vwLikeButton.setChecked(true);
		}
		else if (m_joke.getRating() == Joke.DISLIKE) {
			m_vwDislikeButton.setChecked(true);
		}
		else {
			m_vwLikeGroup.clearCheck();
		}
        notifyOnJokeChangeListener();
	}

	/**
	 * This method encapsulates the logic necessary to update this view so that
	 * it displays itself in its "Expanded" form:
	 * 
	 *  - Shows the complete text of the joke. 
	 * 
	 *  - Brings the RadioGroup of rating Buttons back into view.
	 */
	private void expandJokeView() {
		m_vwJokeText.setEllipsize(null);
		m_vwExpandButton.setText(COLLAPSE);
		m_vwLikeGroup.setVisibility(VISIBLE);
		requestLayout();
	}

	/**
	 * This method encapsulates the logic necessary to update this view so that
	 * it displays itself in its "Collapsed" form:
	 * 
	 *  - Shows only the first line of text of the joke.
	 *  
	 *  - If the joke is longer than one line, it appends an ellipsis to the end.
	 *  
	 *  - Removes the RadioGroup of rating Buttons from view.
	 */
	private void collapseJokeView() {
		m_vwJokeText.setEllipsize(TruncateAt.END);
		m_vwExpandButton.setText(EXPAND);
		m_vwLikeGroup.setVisibility(GONE);
		requestLayout();
	}

	@Override
	public void onClick(View v) {
		if (m_vwExpandButton.getText().toString()== EXPAND) {
			expandJokeView();
		}
		else {
			collapseJokeView();
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int checkedId) {
		if (checkedId == R.id.likeButton) {
			m_joke.setRating(Joke.LIKE);
		}
		else if (checkedId == R.id.dislikeButton) {
			m_joke.setRating(Joke.DISLIKE);
		}
		else if (checkedId == -1) {
			m_joke.setRating(Joke.UNRATED);
		}
	}

	@Override
	public boolean isChecked() {
		if (m_vwExpandButton.getText().toString()== EXPAND) {
			return false;
		}
		else {
			return true;
		}
	}

	@Override
	public void setChecked(boolean checked) {
		if (checked) {
			expandJokeView();
		}
		else {
			collapseJokeView();
		}
	}

	@Override
	public void toggle() {
		setChecked(!isChecked());
	}

	/**
	 * Mutator method for changing the OnJokeChangeListener object this JokeView
	 * notifies when the state its underlying Joke object changes.
	 * 
	 * It is possible and acceptable for m_onJokeChangeListener to be null, you
	 * should allow for this.
	 * 
	 * @param listener
	 *            The OnJokeChangeListener object that should be notified when
	 *            the underlying Joke changes state.
	 */
	public void setOnJokeChangeListener(OnJokeChangeListener listener) {
        m_onJokeChangeListener = listener;
	}

	/**
	 * This method should always be called after the state of m_joke is changed.
	 * 
	 * It is possible and acceptable for m_onJokeChangeListener to be null, you
	 * should test for this.
	 * 
	 * This method should not be called if setJoke(...) is called, since the
	 * internal state of the Joke object that m_joke references is not be
	 * changed. Rather, m_joke reference is being changed to reference a
	 * different Joke object.
	 */
	protected void notifyOnJokeChangeListener() {
        if (m_onJokeChangeListener != null)
		    m_onJokeChangeListener.onJokeChanged(this, m_joke);

	}

	/**
	 * Interface definition for a callback to be invoked when the underlying
	 * Joke is changed in this JokeView object.
	 * 
	 */
	public static interface OnJokeChangeListener {

		/**
		 * Called when the underlying Joke in a JokeView object changes state.
		 * 
		 * @param view
		 *            The JokeView in which the Joke was changed.
		 * @param joke
		 *            The Joke that was changed.
		 */
		public void onJokeChanged(JokeView view, Joke joke);
	}

}
