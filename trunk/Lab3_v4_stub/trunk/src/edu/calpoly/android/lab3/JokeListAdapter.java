package edu.calpoly.android.lab3;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import java.util.List;

public class JokeListAdapter extends BaseAdapter implements AdapterView.OnItemLongClickListener {

    /**
     * The application Context in which this JokeListAdapter is being used.
     */
    private Context m_context;

    /**
     * The dataset to which this JokeListAdapter is bound.
     */
    private List<Joke> m_jokeList;

    /**
     * The position in the dataset of the currently selected Joke.
     */
    private int m_nSelectedPosition;

    /**
     * Parameterized constructor that takes in the application Context in which
     * it is being used and the Collection of Joke objects to which it is bound.
     * m_nSelectedPosition will be initialized to Adapter.NO_SELECTION.
     *
     * @param context  The application Context in which this JokeListAdapter is being
     *                 used.
     * @param jokeList The Collection of Joke objects to which this JokeListAdapter
     *                 is bound.
     */
    public JokeListAdapter(Context context, List<Joke> jokeList) {
        m_context = context;
        m_jokeList = jokeList;
        m_nSelectedPosition = Adapter.NO_SELECTION;
    }

    /**
     * Accessor method for retrieving the position in the dataset of the
     * currently selected Joke.
     *
     * @return an integer representing the position in the dataset of the
     *         currently selected Joke.
     */
    public int getSelectedPosition() {
        return m_nSelectedPosition;
    }

    @Override
    public int getCount() {
        return m_jokeList != null && !m_jokeList.isEmpty() ? m_jokeList.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return m_jokeList != null ? m_jokeList.get(i) : null;
    }

    @Override
    public long getItemId(int i) {
        return getItem(i) != null ? (long) i : -1l;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view != null) {
            ((JokeView) view).setJoke((Joke) getItem(i));
            return view;
        } else {
            return new JokeView(m_context, (Joke) getItem(i));
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        m_nSelectedPosition = i;
        return false;
    }
}
