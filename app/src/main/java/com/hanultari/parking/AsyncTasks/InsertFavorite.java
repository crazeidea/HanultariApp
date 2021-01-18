package com.hanultari.parking.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.hanultari.parking.CommonMethod.ipConfig;

public class InsertFavorite extends AsyncTask<Integer, Void, Void> {
  private static final String TAG = "SelectFavorite";

  JSONArray array;



  @Override
  protected Void doInBackground(Integer... id) {
    int parkingid = id[0];
    int memberid = id[1];
    try {
      String postURL = String.format("%s/insertFavoriteAndroid?id=%s&userid=%s", ipConfig, parkingid, memberid);
      Log.d(TAG, "doInBackground: " + postURL);
      URL url = new URL(postURL);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      InputStream is = new BufferedInputStream(conn.getInputStream());
      is.close();
    } catch (Exception e){
      e.printStackTrace();
    }
    return null;
  }
}
