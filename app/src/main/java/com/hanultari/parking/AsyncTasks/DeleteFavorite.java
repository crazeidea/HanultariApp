package com.hanultari.parking.AsyncTasks;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.hanultari.parking.CommonMethod.ipConfig;

public class DeleteFavorite extends AsyncTask<Integer, Void, Void> {
  @Override
  protected Void doInBackground(Integer... integers) {
    int parkingid = integers[0];
    int memberid = integers[1];
    try {
      String postURL = String.format("%s/deleteFavoriteAndroid?id=%s&userid=%s", ipConfig, parkingid, memberid);
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
