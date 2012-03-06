package edu.calpoly.android.lab3;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

public class JokeView extends LinearLayout implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, Checkable {

    private Button m_vwExpandButton;
    private RadioButton m_vwLikeButton;
    private RadioButton m_vwDislikeButton;
    private RadioGroup m_vwLikeGroup;
    private TextView m_vwJokeText;
    private Joke m_joke;
    private boolean checked;

    public static final String EXPAND = " + ";
    public static final String COLLAPSE = " - ";

    /**
     * Basic Constructor that takes only takes in an application Context.
     *
     * @param context The application Context in which this view is being added.
     * @param joke    The Joke this view is responsible for displaying.
     */
    public JokeView(Context context, Joke joke) {
        super(context);
        checked = false;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.joke_view, this, true);

        m_vwLikeButton = (RadioButton) findViewById(R.id.likeButton);
        m_vwDislikeButton = (RadioButton) findViewById(R.id.dislikeButton);
        m_vwLikeGroup = (RadioGroup) findViewById(R.id.ratingRadioGroup);
        m_vwJokeText = (TextView) findViewById(R.id.jokeTextView);
        m_vwExpandButton = (Button) findViewById(R.id.expandButton);
        setJoke(joke);
        collapseJokeView();
        m_vwExpandButton.setOnClickListener(this);
        m_vwLikeGroup.setOnCheckedChangeListener(this);
    }

    /**
     * Mutator method for changing the Joke object this View displays. This View
     * will be updated to display the correct contents of the new Joke.
     *
     * @param joke The Joke object which this View will display.
     */
    public void setJoke(Joke joke) {
        m_joke = joke;
        m_vwJokeText.setText(joke != null ? joke.getJoke() : "");
        m_vwLikeGroup.clearCheck();
        int rating = m_joke.getRating();
        if (rating == Joke.LIKE)
            m_vwLikeButton.setChecked(true);
        else if (rating == Joke.DISLIKE)
            m_vwDislikeButton.setChecked(false);
    }

    /**
     * This method encapsulates the logic necessary to update this view so that
     * it displays itself in its "Expanded" form:
     * - Shows the complete text of the joke.
     * - Brings the RadioGroup of rating Buttons back into view.
     */
    private void expandJokeView() {
        m_vwJokeText.setEllipsize(null);
        m_vwExpandButton.setText(COLLAPSE);
        m_vwLikeGroup.setVisibility(View.VISIBLE);
        requestLayout();
    }

    /**
     * This method encapsulates the logic necessary to update this view so that
     * it displays itself in its "Collapsed" form:
     * - Shows only the first line of text of the joke.
     * - If the joke is longer than one line, it appends an ellipsis to the end.
     * - Removes the RadioGroup of rating Buttons from view.
     */
    private void collapseJokeView() {
        m_vwJokeText.setEllipsize(TextUtils.TruncateAt.END);
        m_vwExpandButton.setText(EXPAND);
        m_vwLikeGroup.setVisibility(View.GONE);
        requestLayout();
    }

    @Override
    public void onClick(View view) {
        if (!isChecked()/*EXPAND.equals(m_vwExpandButton.getText().toString())*/)
            expandJokeView();
        else /*if (COLLAPSE.equals(m_vwExpandButton.getText().toString()))*/
            collapseJokeView();
        toggle();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (m_vwLikeButton.isChecked())
            m_joke.setRating(Joke.LIKE);
        else if (m_vwDislikeButton.isChecked())
            m_joke.setRating(Joke.DISLIKE);
        else
            m_joke.setRating(Joke.UNRATED);
    }

    @Override
    public boolean isChecked() {
        return checked;
    }


    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public void toggle() {
        checked = !checked;
    }
}
