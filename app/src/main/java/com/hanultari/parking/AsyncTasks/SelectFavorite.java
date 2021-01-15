package com.hanultari.parking.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.hanultari.parking.CommonMethod.ipConfig;

public class SelectFavorite extends AsyncTask<Integer, Void, JSONArray> {
  private static final String TAG = "SelectFavorite";

  JSONArray array;



  @Override
  protected JSONArray doInBackground(Integer... id) {
    try {
      String postURL = String.format("%s/getFavorite?id=%s", ipConfig, id[0]);
      Log.d(TAG, "doInBackground: " + postURL);
      URL url = new URL(postURL);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      InputStream is = new BufferedInputStream(conn.getInputStream());
      BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
      StringBuffer builder = new StringBuffer();

      String inputString = null;
      while ((inputString = br.readLine()) != null ) {
        builder.append(inputString);
      }

      String result = builder.toString();
      array = new JSONArray(result);
      br.close();
      is.close();
    } catch (Exception e){
      e.printStackTrace();
    }
    return array;
  }
}
