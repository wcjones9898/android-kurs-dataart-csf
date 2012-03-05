package edu.calpoly.android.lab2;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import java.util.ArrayList;

public class SimpleJokeList extends Activity {

    /**
     * Contains the list Jokes the Activity will present to the user *
     */
    protected ArrayList<Joke> m_arrJokeList;

    /**
     * LinearLayout used for maintaining a list of Views that each display Jokes
     */
    protected LinearLayout m_vwJokeLayout;

    /**
     * EditText used for entering text for a new Joke to be added to
     * m_arrJokeList.
     */
    protected EditText m_vwJokeEditText;

    /**
     * Button used for creating and adding a new Joke to m_arrJokeList using the
     * text entered in m_vwJokeEditText.
     */
    protected Button m_vwJokeButton;

    /**
     * Background Color values used for alternating between light and dark rows
     * of Jokes.
     */
    protected int m_nDarkColor;
    protected int m_nLightColor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();

        m_arrJokeList = new ArrayList<Joke>();
        m_nDarkColor = this.getResources().getColor(R.color.dark);
        m_nLightColor = this.getResources().getColor(R.color.light);

        String[] jokes = this.getResources().getStringArray(R.array.jokeList);
        if (jokes != null && jokes.length > 0)
            for (String joke : jokes)
                addJoke(joke);

        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(m_vwJokeEditText.getWindowToken(), 0);

        initAddJokeListeners();
    }


    /**
     * Method used to encapsulate the code that initializes and sets the Layout
     * for this Activity.
     */
    protected void initLayout() {
        m_vwJokeButton = new Button(this);
        m_vwJokeButton.setText(R.string.add_joke_button);

        m_vwJokeEditText = new EditText(this);
        m_vwJokeEditText.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        m_vwJokeEditText.setHint(R.string.add_joke_hint);
        m_vwJokeEditText.setMaxLines(1);

        LinearLayout addJokePanel = new LinearLayout(this);
        addJokePanel.setOrientation(LinearLayout.HORIZONTAL);
        addJokePanel.addView(m_vwJokeButton);
        addJokePanel.addView(m_vwJokeEditText);

        m_vwJokeLayout = new LinearLayout(this);
        m_vwJokeLayout.setOrientation(LinearLayout.VERTICAL);

        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(m_vwJokeLayout);

        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.addView(addJokePanel);
        mainLayout.addView(scrollView);

        setContentView(mainLayout);
    }

    /**
     * Method used to encapsulate the code that initializes and sets the Event
     * Listeners which will respond to requests to "Add" a new Joke to the
     * list.
     */
    protected void initAddJokeListeners() {
        m_vwJokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newJoke = m_vwJokeEditText.getText().toString();
                if (newJoke != null && !("").equals(newJoke))
                    addJoke(newJoke);
                m_vwJokeEditText.setText("");

                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                imm.hideSoftInputFromWindow(m_vwJokeEditText.getWindowToken(), 0);
            }
        });

        m_vwJokeEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER) {
                    String newJoke = m_vwJokeEditText.getText().toString().replaceAll("\n", "");
                    if (newJoke != null && !("").equals(newJoke)) {
                        addJoke(newJoke);
                    }
                    m_vwJokeEditText.setText("");

                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    imm.hideSoftInputFromWindow(m_vwJokeEditText.getWindowToken(), 0);
                }
                return false;
            }
        });
    }

    /**
     * Method used for encapsulating the logic necessary to properly initialize
     * a new joke, add it to m_arrJokeList, and display it on screen.
     *
     * @param strJoke A string containing the text of the Joke to add.
     */
    protected void addJoke(String strJoke) {
        m_arrJokeList.add(new Joke(strJoke));

        TextView textView = new TextView(this);
        textView.setText(strJoke);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 16);
        textView.setBackgroundColor(m_arrJokeList.size() % 2 == 0 ? m_nDarkColor : m_nLightColor);

        m_vwJokeLayout.addView(textView);
    }
}
