package com.hanultari.parking.AsyncTasks;

import android.net.http.AndroidHttpClient;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;

import com.hanultari.parking.Custom.AES256Chiper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBaseHC4;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static com.hanultari.parking.CommonMethod.ipConfig;

public class Signup extends AsyncTask<Void, Void, String> {
  private static final String TAG = "Login";

  private String email, pw, name, tel;

  private String state = "";

  HttpClient httpClient;
  HttpPost httpPost;
  HttpResponse httpResponse;
  HttpEntity httpEntity;

  public Signup(String email, String pw, String name, String tel) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
    this.email = email;
    this.pw = AES256Chiper.AES_Decode(pw);
    this.name = name;
    this.tel = tel;
  }

  @Override
  protected String doInBackground(Void... voids) {
    try {
      MultipartEntityBuilder builder = MultipartEntityBuilder.create();
      builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
      builder.setCharset(Charset.forName("UTF-8"));

      builder.addTextBody("email", email, ContentType.create("Multipart/related", "UTF-8"));
      builder.addTextBody("pw", pw, ContentType.create("Multipart/related", "UTF-8"));
      builder.addTextBody("name", name, ContentType.create("Multipart/related", "UTF-8"));
      builder.addTextBody("tel", tel, ContentType.create("Multipart/related", "UTF-8"));

      String postURL = ipConfig + "/signup/android";

      InputStream is = null;
      httpClient = AndroidHttpClient.newInstance("Android");
      httpPost = new HttpPost(postURL);
      httpPost.setEntity(builder.build());
      httpResponse = httpClient.execute(httpPost);
      httpEntity = httpResponse.getEntity();
      is = httpEntity.getContent();

      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
      StringBuilder stringBuilder = new StringBuilder();
      String line = null;
      while ((line = bufferedReader.readLine()) != null) {
        stringBuilder.append(line + "\n");
      }
      state = stringBuilder.toString();
      is.close();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (httpEntity != null) httpEntity = null;
      if (httpResponse != null) httpResponse = null;
      if (httpPost != null) httpPost = null;
      if (httpClient != null) httpClient = null;
    }
    return state;
  }

}
