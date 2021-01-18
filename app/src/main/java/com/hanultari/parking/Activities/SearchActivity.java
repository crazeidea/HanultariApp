package com.hanultari.parking.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hanultari.parking.AsyncTasks.SearchLocation;
import com.hanultari.parking.AsyncTasks.SearchParking;
import com.hanultari.parking.Adapter.SearchRecyclerViewAdapter;
import com.hanultari.parking.DTO.ParkingDTO;
import com.hanultari.parking.DTO.PlaceDTO;
import com.hanultari.parking.MainActivity;
import com.hanultari.parking.R;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.util.FusedLocationSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SearchActivity extends AppCompatActivity {
  private static final String TAG = "SearchActivity";

  ArrayList<PlaceDTO> places = new ArrayList<>();
  ArrayList<ParkingDTO> parkings = new ArrayList<>();



  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);

    TextView searchTitle = findViewById(R.id.searchTitle);
    searchTitle.setVisibility(View.GONE);

    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      // TODO: Consider calling
      //    ActivityCompat#requestPermissions
      // here to request the missing permissions, and then overriding
      //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
      //                                          int[] grantResults)
      // to handle the case where the user grants the permission. See the documentation
      // for ActivityCompat#requestPermissions for more details.
      return;
    }
    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());


    EditText searchEditText = findViewById(R.id.searchActivityText);

    searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_SEARCH) {

          InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
          imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);

          TextView searchTitle = findViewById(R.id.searchTitle);
          searchTitle.setVisibility(View.VISIBLE);
          String query = searchEditText.getText().toString();
          SearchLocation searchLocation = new SearchLocation();
          SearchParking searchParking = new SearchParking();
          places.clear();
          parkings.clear();

          try {

            JSONArray placeResult = searchLocation.execute(query).get();
            /* 장소 검색 결과 수신 */
            for (int i = 0; i < placeResult.length(); i++) {
              JSONObject object = placeResult.getJSONObject(i);
              PlaceDTO place = new PlaceDTO();
              place.setName(object.getString("title").replace("<b>","").replace("</b>", ""));
              place.setAddress(object.getString("address"));
              place.setMapx(object.getInt("mapx"));
              place.setMapy(object.getInt("mapy"));
              places.add(place);
            }



            JSONArray parkingResult = searchParking.execute(query).get();
            /* 주차장 검색 결과 수신 */
            for(int i = 0; i < parkingResult.length(); i++) {
              JSONObject object = parkingResult.getJSONObject(i);
              ParkingDTO parking = new ParkingDTO();
              parking.setId(object.getInt("id"));
              parking.setName(object.getString("name"));
              parking.setAddr(object.getString("addr"));
              parking.setLat((float) object.getDouble("lat"));
              parking.setLng((float) object.getDouble("lng"));
              parkings.add(parking);
            }

          } catch (ExecutionException e) {
            e.printStackTrace();
          } catch (InterruptedException e) {
            e.printStackTrace();
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }

        if(places.size() > 0 || parkings.size() > 0) {
          RecyclerView recyclerView = findViewById(R.id.searchRecyclerView);
          recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
          SearchRecyclerViewAdapter adapter = new SearchRecyclerViewAdapter(getApplicationContext(), places, parkings, currentLatLng);
          recyclerView.setAdapter(adapter);


          adapter.setOnItemClickListener(new SearchRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
              String type = (String) v.getTag(R.string.ITEM_TYPE);
              float lat = Float.parseFloat(v.getTag(R.string.ITEM_LAT).toString());
              float lng = Float.parseFloat(v.getTag(R.string.ITEM_LNG).toString());
              String title = (String) v.getTag(R.string.ITEM_TITLE);
              String name = (String) v.getTag(R.string.ITEM_NAME);
              Intent intent = new Intent(getApplicationContext(), MainActivity.class);
              intent.putExtra("type", type);
              intent.putExtra("lat", lat);
              intent.putExtra("lng", lng);
              intent.putExtra("title", title);
              if(type.equals("place")) intent.putExtra("name", name);
              startActivity(intent);
            }
          });
        }
        return true;
      }


    });




  } // onCreate()


}
