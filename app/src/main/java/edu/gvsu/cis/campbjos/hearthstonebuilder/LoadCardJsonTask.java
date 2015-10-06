package edu.gvsu.cis.campbjos.hearthstonebuilder;

import android.os.AsyncTask;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import edu.gvsu.cis.campbjos.hearthstonebuilder.CustomAdapters.CardAdapter;
import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Card;

/**
 * AsyncTask responsible for gathering the HS card information from the
 * MashApe API.
 *
 * @author HearthBuilder Team
 * @version Fall 2015
 */
public class LoadCardJsonTask extends AsyncTask<Object, Void, Void> {
  private CardAdapter adapter;

  private WeakReference<CardViewFragment> fragmentWeakRef;
  private JsonTaskListener host;

  public LoadCardJsonTask(CardViewFragment hf) {
    this.fragmentWeakRef = new WeakReference<>(hf);
    host = this.fragmentWeakRef.get();
  }


  @Override
  protected void onPostExecute(Void vd) {
    super.onPostExecute(vd);
    if (host != null) {
      host.onTaskComplete();
    }
  }

  /**
   * doInBackground calls on the MashApe HS API, which we process using the
   * {@link JsonUtil} class, and notify the calling fragment onPostExecute.
   *
   * @param params Context, String url, String key, ArrayList deck
   * @return null
   */
  @Override
  @SuppressWarnings(value = "unchecked")
  protected Void doInBackground(Object... params) {
    String url = (String) params[0];
    String key = (String) params[1];
    ArrayList<Card> cardList = (ArrayList<Card>) params[2];

    //initial load
    if (cardList.size() == 0) {
      try {
        String response = NetworkUtil.get(url, "X-Mashape-Key", key);
        JsonUtil.parse(response, cardList);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  /**
   * Public interface to notify Fragment when Cards have been collected from API
   */
  public interface JsonTaskListener {
    void onTaskComplete();
  }
  // We should only use this class for the network task - Josiah 2015/10/05
}
