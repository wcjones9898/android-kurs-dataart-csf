package edu.calpoly.android.lab3;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Scanner;

public class AdvancedJokeList extends Activity {

    /**
     * Contains the name of the Author for the jokes.
     */
    protected String m_strAuthorName;

    /**
     * Contains the list of Jokes the Activity will present to the user.
     */
    protected ArrayList<Joke> m_arrJokeList;

    /**
     * Adapter used to bind an AdapterView to List of Jokes.
     */
    protected JokeListAdapter m_jokeAdapter;

    /**
     * ViewGroup used for maintaining a list of Views that each display Jokes.
     */
    protected ListView m_vwJokeLayout;

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

    protected ArrayList<Joke> filteredJokeList;

    /**
     * Context-Menu MenuItem ID's
     * IMPORTANT: You must use these when creating your MenuItems or the tests
     * used to grade your submission will fail.
     */
    protected static final int REMOVE_JOKE_MENUITEM = Menu.FIRST;
    protected static final int UPLOAD_JOKE_MENUITEM = Menu.FIRST + 1;
    protected static final String successServerResponse = "1 record added";
    protected static final String uploadSuccessMsg = "Upload Succeeded!";
    protected static final String uploadFailedMsg = "Upload Failed!";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();

        m_strAuthorName = this.getResources().getString(R.string.author_name);
        m_arrJokeList = new ArrayList<Joke>();
        filteredJokeList = new ArrayList<Joke>();
        m_nDarkColor = this.getResources().getColor(R.color.dark);
        m_nLightColor = this.getResources().getColor(R.color.light);
        m_jokeAdapter = new JokeListAdapter(this, m_arrJokeList);
        m_vwJokeLayout.setAdapter(m_jokeAdapter);
        m_vwJokeLayout.setOnItemLongClickListener(m_jokeAdapter);

        String[] jokes = this.getResources().getStringArray(R.array.jokeList);
        if (jokes != null && jokes.length > 0) {
            for (String joke : jokes)
                addJoke(new Joke(joke, m_strAuthorName));
        }

        initAddJokeListeners();

    }

    /**
     * Method is used to encapsulate the code that initializes and sets the
     * Layout for this Activity.
     */
    protected void initLayout() {
        setContentView(R.layout.advanced);

        m_vwJokeButton = (Button) findViewById(R.id.addJokeButton);
        m_vwJokeLayout = (ListView) findViewById(R.id.jokeListViewGroup);
        registerForContextMenu(m_vwJokeLayout);
        m_vwJokeLayout.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        m_vwJokeEditText = (EditText) findViewById(R.id.newJokeEditText);
        m_vwJokeEditText.setMaxLines(1);
    }

    /**
     * Method is used to encapsulate the code that initializes and sets the
     * Event Listeners which will respond to requests to "Add" a new Joke to the
     * list.
     */
    protected void initAddJokeListeners() {
        m_vwJokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newJoke = m_vwJokeEditText.getText().toString();
                if (newJoke != null && !("").equals(newJoke))
                    addJoke(new Joke(newJoke, m_strAuthorName));
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
                        addJoke(new Joke(newJoke, m_strAuthorName));
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
     * Method used for encapsulating the logic necessary to properly add a new
     * Joke to m_arrJokeList, and display it on screen.
     *
     * @param joke The Joke to add to list of Jokes.
     */
    protected void addJoke(Joke joke) {
        m_arrJokeList.add(joke);
        m_jokeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuItem removeItem = menu.add(0, REMOVE_JOKE_MENUITEM, 0, R.string.remove_menuitem);
        MenuItem uploadItem = menu.add(0, UPLOAD_JOKE_MENUITEM, 0, R.string.upload_menuitem);

        removeItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                m_arrJokeList.remove(m_jokeAdapter.getSelectedPosition());
                m_jokeAdapter.notifyDataSetChanged();
                return true;
            }
        });
        uploadItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                uploadJokeToServer(m_arrJokeList.get(m_jokeAdapter.getSelectedPosition()));
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dislike_menuitem:
                applyFilter(Joke.DISLIKE);
                return true;
            case R.id.like_menuitem:
                applyFilter(Joke.LIKE);
                return true;
            case R.id.unrated_menuitem:
                applyFilter(Joke.UNRATED);
                return true;
            case R.id.show_all_menuitem:
                m_vwJokeLayout.setAdapter(m_jokeAdapter);
                m_jokeAdapter.notifyDataSetChanged();
                return true;
            case R.id.download_menuitem:
                getJokesFromServer();
        }
        return false;
    }

    public void applyFilter(int param) {
        filteredJokeList.clear();
        for (Joke joke : m_arrJokeList)
            if (joke.getRating() == param)
                filteredJokeList.add(joke);
        JokeListAdapter adapter = new JokeListAdapter(this, filteredJokeList);
        m_vwJokeLayout.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Method used to retrieve Jokes from online server. The getJoke script
     * takes a single optional parameter, which should be encode in "UTF-8".
     * This parameter allows tells script to only retrieve Jokes whose author
     * name matches the value in the parameter.
     * <p/>
     * param-1) "author": The author of the joke.
     * <p/>
     * URL: http://simexusa.com/aac/getJokes.php?
     */
    protected void getJokesFromServer() {
        try {
            URL url = new URL("http://simexusa.com/aac/getAllJokes.php");
            Scanner scanner = new Scanner(url.openStream());
            scanner.useDelimiter("\n");
            while (scanner.hasNext()) {
                addJoke(new Joke(scanner.next(), this.getResources().getString(R.string.author_name)));
            }
            scanner.close();
        } catch (Exception e) {

        }
    }

    /**
     * This method uploads a single Joke to the server. This method should test
     * the response from the server and display success or failure to the user
     * via a Toast Notification
     * <p/>
     * The addJoke script on the server requires two parameters, both of which
     * should be encode in "UTF-8":
     * <p/>
     * param-1) "joke": The text of the joke.
     * <p/>
     * param-2) "author": The author of the joke.
     * <p/>
     * URL: http://simexusa.com/aac/addJoke.php?
     *
     * @param joke The Joke to be uploaded to the server.
     */
    protected void uploadJokeToServer(Joke joke) {
        try {
            URL url = new URL("http://simexusa.com/aac/addOneJoke.php?joke=" +
                    URLEncoder.encode(joke.getJoke(), "UTF-8") +
                    "&author=" + URLEncoder.encode(joke.getAuthor(), "UTF-8"));
            Scanner scanner = new Scanner(url.openStream());
            scanner.useDelimiter("\n");
            String response = "";
            while (scanner.hasNext())
                response += scanner.next();
            if (successServerResponse.equals(response))
                Toast.makeText(this, uploadSuccessMsg, Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, uploadFailedMsg, Toast.LENGTH_SHORT).show();
            scanner.close();
        } catch (Exception e) {
        }
    }

}