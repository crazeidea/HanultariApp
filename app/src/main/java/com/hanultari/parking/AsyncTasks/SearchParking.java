package com.hanultari.parking.AsyncTasks;

import android.os.AsyncTask;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.hanultari.parking.CommonMethod.ipConfig;

public class SearchParking extends AsyncTask<String, Void, JSONArray> {

  JSONArray list;

  @Override
  protected JSONArray doInBackground(String... Strings) {
    try {
      URL url = new URL(ipConfig + "/searchParking?query=" + Strings[0]);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      InputStream is = new BufferedInputStream(conn.getInputStream());
      BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
      StringBuffer builder = new StringBuffer();

      String inputString = null;
      while ((inputString = br.readLine()) != null ) {
        builder.append(inputString);
      }

      String result = builder.toString();

      list = new JSONArray(result);

      conn.disconnect();
      br.close();
      is.close();
    } catch (Exception e) {
      e.printStackTrace();
    }


    return list;
  }
}
