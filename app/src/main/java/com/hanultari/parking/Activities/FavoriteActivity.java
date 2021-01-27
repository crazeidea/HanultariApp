package com.hanultari.parking.Activities;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hanultari.parking.Adapter.FavoriteRecyclerViewAdapter;
import com.hanultari.parking.AsyncTasks.SelectFavorite;
import com.hanultari.parking.AsyncTasks.SelectParking;
import com.hanultari.parking.DTO.MemberDTO;
import com.hanultari.parking.DTO.ParkingDTO;
import com.hanultari.parking.MainActivity;
import com.hanultari.parking.R;
import com.naver.maps.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.hanultari.parking.CommonMethod.getDistance;
import static com.hanultari.parking.CommonMethod.loginDTO;

public class FavoriteActivity extends AppCompatActivity {
  private static final String TAG = "FavoriteActivity";

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_favorite);

    LocationManager manager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      return;
    }
    Location currentLoc = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    LatLng currentLatlng = new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude());

    SelectFavorite sf = new SelectFavorite();
    MemberDTO dto = loginDTO;
    ArrayList<ParkingDTO> dtos = new ArrayList<>();
    ParkingDTO parkingDTO;
    if (dto != null) {
      try {
        JSONArray result = sf.execute(dto.getId()).get();
        for (int i = 0; i < result.length(); i++) {
          JSONObject object = result.getJSONObject(i);
          int id = object.getInt("parking_id");
          Log.d(TAG, "onCreate: " + id);
          try {
            SelectParking sp = new SelectParking();
            JSONObject parkingObj = sp.execute(id).get();
            parkingDTO = new ParkingDTO();
            parkingDTO.setName(parkingObj.getString("name"));
            parkingDTO.setFare(parkingObj.getInt("fare"));
            parkingDTO.setLat((float) parkingObj.getDouble("lat"));
            parkingDTO.setLng((float) parkingObj.getDouble("lng"));
            parkingDTO.setId(parkingObj.getInt("id"));
            parkingDTO.setDistance(getDistance(currentLatlng, new LatLng(parkingObj.getDouble("lat"), parkingObj.getDouble("lng"))));
            dtos.add(parkingDTO);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        RecyclerView favoriteRecyclerView = findViewById(R.id.favoriteRecyclerView);
        favoriteRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        FavoriteRecyclerViewAdapter adapter = new FavoriteRecyclerViewAdapter(this, dtos);
        favoriteRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new FavoriteRecyclerViewAdapter.OnItemClickListener() {
          @Override
          public void onItemClick(View v, int position) {
            ParkingDTO dto = (ParkingDTO) v.getTag();
            Intent intent = new Intent(FavoriteActivity.this, MainActivity.class);
            intent.putExtra("type", "parking");
            intent.putExtra("lat", dto.getLat());
            intent.putExtra("lng", dto.getLng());
            intent.putExtra("id", dto.getId());
            startActivity(intent);


          }
        });

      }
    } else {
      TextView favoriteTitle = findViewById(R.id.favoriteTitle);
      favoriteTitle.setVisibility(View.VISIBLE);
    }

    ImageButton btnBack = findViewById(R.id.btnBackFavorite);
    btnBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }
}
