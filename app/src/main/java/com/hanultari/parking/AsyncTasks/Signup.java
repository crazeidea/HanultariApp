package com.hanultari.parking.AsyncTasks;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static com.hanultari.parking.CommonMethod.ipConfig;

public class Signup extends AsyncTask<Void, Void, Boolean> {
  private static final String TAG = "Login";

  private String email, pw, name, tel;

  private String state = "";

  HttpClient httpClient;
  HttpPost httpPost;
  HttpResponse httpResponse;
  HttpEntity httpEntity;

  public Signup(String email, String pw, String name, String tel) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
    this.email = email;
    this.pw = pw;
    this.name = name;
    this.tel = tel;
  }

  @Override
  protected Boolean doInBackground(Void... voids) {
    boolean result = false;
    try {
      JSONObject object = new JSONObject();
      object.put("name", name).put("pw", pw).put("email", email).put("tel", tel);
      String sendObject = URLEncoder.encode(object.toString(), "UTF-8");
      URL url = new URL(ipConfig + "/signup/android?json=" + sendObject);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      InputStream is = new BufferedInputStream(conn.getInputStream());
      BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
      StringBuffer builder = new StringBuffer();
      String inputString;
      while((inputString = br.readLine()) != null) {
        builder.append(inputString);
      }
      result = Boolean.parseBoolean(builder.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

}
