package com.hanultari.parking.AsyncTasks;

import android.os.AsyncTask;

import com.hanultari.parking.DTO.TicketDTO;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.hanultari.parking.CommonMethod.ipConfig;
import static com.hanultari.parking.CommonMethod.loginDTO;

public class SelectTicket extends AsyncTask<Integer, Void, JSONArray> {

  JSONArray array;

  @Override
  protected JSONArray doInBackground(Integer... integers) {
    try {
      String writer = loginDTO.getName();
      String postURL = String.format("%s/getTicket?writer=%s", ipConfig, writer);

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
