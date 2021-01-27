package com.hanultari.parking.AsyncTasks;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.hanultari.parking.CommonMethod.ipConfig;

public class SelectUser extends AsyncTask<Integer, Void, JSONObject> {

  JSONObject array;

  @Override
  protected JSONObject doInBackground(Integer... integers) {
    try {
      int id = integers[0];
      String postURL = String.format("%s/getUserData?id=%s", ipConfig, id);

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
      array = new JSONObject(result);
      br.close();
      is.close();
    } catch (Exception e){
      e.printStackTrace();
    }
    return array;
  }
}
