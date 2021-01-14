package com.hanultari.parking.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.util.Strings;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
import java.util.HashMap;

import static com.hanultari.parking.CommonMethod.ipConfig;

public class Login extends AsyncTask<HashMap<String, String>, Void, JSONObject> {
  private static final String TAG = "Login";

  JSONObject member;

  @Override
  protected JSONObject doInBackground(HashMap<String, String>... hashMaps) {
    HashMap<String, String> logininfo = hashMaps[0];
    String email = logininfo.get("email");
    String pw = logininfo.get("pw");
    try {
      URL url = new URL(ipConfig + "/login/android?email=" + email + "&pw=" + pw);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      InputStream is = new BufferedInputStream(conn.getInputStream());
      BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
      StringBuffer builder = new StringBuffer();
      String inputString = null;
      while((inputString = br.readLine()) != null) {
        builder.append(inputString);
      }
      String result = builder.toString();
      member = new JSONObject(result);
      conn.disconnect();
      br.close();
      is.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

    return member;
  }

}
