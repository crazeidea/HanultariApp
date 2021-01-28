package com.hanultari.parking.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.hanultari.parking.DTO.ReviewDTO;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.hanultari.parking.CommonMethod.ipConfig;

public class InsertReview extends AsyncTask<ReviewDTO, Void, Boolean> {
  private static final String TAG = "SelectFavorite";

  Boolean result;

  @Override
  protected Boolean doInBackground(ReviewDTO... reviewDTOS) {
    ReviewDTO dto = reviewDTOS[0];
    int memberid = dto.getMember_id();
    int parkingid = dto.getParking_id();
    int rating = dto.getRating();
    String content = dto.getContent();
    try {
      String postURL = String.format("%s/reviewAndroid?member_id=%s&parking_id=%s&rating=%s&content=%s", ipConfig, memberid, parkingid,rating, content);
      URL url = new URL(postURL);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      InputStream is = new BufferedInputStream(conn.getInputStream());
      BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
      StringBuffer builder = new StringBuffer();

      String inputString = null;
      while((inputString = br.readLine()) != null) {
        builder.append(inputString);
      }

      String resultString = builder.toString();
      result = Boolean.valueOf(resultString);
      br.close();
      is.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }
}
