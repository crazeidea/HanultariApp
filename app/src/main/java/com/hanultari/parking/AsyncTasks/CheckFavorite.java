package com.hanultari.parking.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.hanultari.parking.CommonMethod.ipConfig;

public class CheckFavorite extends AsyncTask<Integer, Void, Boolean> {
  @Override
  protected Boolean doInBackground(Integer... integers) {
    String result = null;
    Boolean isFavorite = null;
    int id = integers[0];
    int userid = integers[1];
    try {
      String postURL = String.format("%s/checkFavoriteAndroid?id=%s&userid=%s", ipConfig, id, userid);
      URL url = new URL(postURL);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      InputStream is = new BufferedInputStream(conn.getInputStream());
      BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
      StringBuffer builder = new StringBuffer();
      String inputString = null;
      while ((inputString = br.readLine()) != null ) {
        builder.append(inputString);
      }
      result = builder.toString();
      isFavorite = Boolean.parseBoolean(result);
      br.close();
      is.close();


    } catch (Exception e){
      e.printStackTrace();
    }
    return isFavorite;
  }
}
