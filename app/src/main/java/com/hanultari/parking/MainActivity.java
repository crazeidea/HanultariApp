package com.hanultari.parking;

import android.content.Context;
import android.content.Intent;
import android.graphics.Camera;
import android.graphics.PointF;
import android.icu.text.IDNA;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.hanultari.parking.Activities.SearchActivity;
import com.hanultari.parking.Activities.SettingActivity;
import com.hanultari.parking.Adapter.InfoWindowAdapter;
import com.hanultari.parking.AsyncTasks.SelectMarker;
import com.hanultari.parking.DTO.LatlngDTO;
import com.hanultari.parking.DTO.ParkingAdapter;
import com.hanultari.parking.DTO.ParkingDTO;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.hanultari.parking.AsyncTasks.CommonMethod.ipConfig;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {
  private static final String TAG = "MainActivity";
  public static LatlngDTO latlngDTO = null;

  ArrayList<ParkingDTO> dtos;

  /* 레이아웃 관련 변수 */
  RecyclerView recyclerView;

  /* 네이버 지도 관련 변수 */
  private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
  private FusedLocationSource locationSource;
  public static NaverMap naverMap;

  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    /*레이아웃 관련 변수 */
    DrawerLayout mainDrawerLayout = findViewById(R.id.mainDrawerLayout);
    Toolbar mainToolbar = findViewById(R.id.mainToolbar);
    NavigationView navigationView = findViewById(R.id.mainNavigationView);

    /* 네이버 지도 Fragment 실행 */
    FragmentManager fm = getSupportFragmentManager();
    MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map);
    if (mapFragment == null) {
      mapFragment = MapFragment.newInstance();
      fm.beginTransaction().add(R.id.map, mapFragment).commit();
    }
    mapFragment.getMapAsync(this);

    /* 실시간 위치 정보 수신 */
    locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mainDrawerLayout, mainToolbar, R.string.open_drawer, R.string.close_drawer);
    mainDrawerLayout.addDrawerListener(toggle);
    toggle.syncState();
    navigationView.setNavigationItemSelectedListener(this);






    /* 검색화면 전환 */
    SearchView searchView = findViewById(R.id.mainActivitySearchView);
    searchView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, v.findViewById(R.id.mainActivitySearchView), "searchView" );
        startActivity(intent, options.toBundle());
      }
    });



    /* 주변 주차장 찾기 클릭 */

    Button buttonNear = findViewById(R.id.btnLocateNear);
    buttonNear.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d(TAG, "onClick: buttonNear Clicked!");

        recyclerView = findViewById(R.id.mainRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        ArrayList<ParkingDTO> list = new ArrayList<>();
        ParkingDTO e = new ParkingDTO();
        e.setName("TEST1");
        e.setFare(1000);
        e.setDistance("1000m");
        list.add(e);
        list.add(e);
        list.add(e);
        ParkingAdapter adapter = new ParkingAdapter(list);
        recyclerView.setAdapter(adapter);
        if(recyclerView.getVisibility() == View.GONE) {
          Log.d(TAG, "recyclerView Visibility : " + recyclerView.getVisibility());
          Animation animation = new AlphaAnimation(0, 1);
          animation.setDuration(300);
          recyclerView.setAnimation(animation);
          recyclerView.setVisibility(View.VISIBLE);
        } else if (recyclerView.getVisibility() == View.VISIBLE) {
          Log.d(TAG, "recyclerView Visibility : " + recyclerView.getVisibility());
          Animation animation = new AlphaAnimation(1, 0);
          animation.setDuration(300);
          recyclerView.setAnimation(animation);
          recyclerView.setVisibility(View.GONE);
        }

      }
    });


  } // onCreate()


  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
      if (!locationSource.isActivated()) { // 권한 거부됨
        naverMap.setLocationTrackingMode(LocationTrackingMode.None);
      }
      return;
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }


  @UiThread
  @Override
  public void onMapReady(@NonNull NaverMap naverMap) {

    naverMap.setLocationSource(locationSource);
    naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
    naverMap.setMinZoom(12);
    naverMap.setMaxZoom(21);


    /* 마커 생성 */
    try {
      Log.d(TAG, "onCreate: Initialized Marker Creation");
        JSONArray list = new SelectMarker().execute().get();
        if(list != null) {
          for (int i = 0; i < list.length(); i++) {
            JSONObject object = list.getJSONObject(i);
            LatLng latLng = new LatLng(object.getDouble("lat"), object.getDouble("lng"));
            Marker marker = new Marker();
            marker.setPosition(latLng);
            marker.setTag(object.get("id"));
            marker.setMap(naverMap);
            marker.setOnClickListener(new Overlay.OnClickListener() {
              @Override
              public boolean onClick(@NonNull Overlay overlay) {
                Log.d(TAG, "onClick: Marker Clicked");
                LatLng markerPosition = new LatLng(marker.getPosition().latitude + 0.0015, marker.getPosition().longitude);
                CameraPosition cameraPosition = new CameraPosition(markerPosition, 16);
                CameraUpdate cameraUpdate = CameraUpdate.toCameraPosition(cameraPosition);
                naverMap.moveCamera(cameraUpdate);
                Log.d(TAG, "onClick: " + markerPosition.latitude + ", " + markerPosition.longitude);

                return false;
              }
            });
            };
          }
      } catch(Exception e){
        Log.d(TAG, "onCreate: Marker Creation ERROR");
        e.printStackTrace();
      }

    naverMap.setOnMapClickListener(new NaverMap.OnMapClickListener() {
      @Override
      public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
        Log.d(TAG, "onMapClick: " + latLng.latitude + ", " + latLng.longitude);
        recyclerView = findViewById(R.id.mainRecyclerView);
        if(recyclerView.getVisibility() == View.VISIBLE) {
          recyclerView.setVisibility(View.GONE);
        }
      }
    });

//    naverMap.addOnCameraChangeListener(new NaverMap.OnCameraChangeListener() {
//      @Override
//      public void onCameraChange(int i, boolean b) {
//        recyclerView = findViewById(R.id.mainRecyclerView);
//        if (recyclerView.getVisibility() == View.VISIBLE) {
//          Animation animation = new AlphaAnimation(0, 1);
//          animation.setDuration(300);
//          recyclerView.setAnimation(animation);
//          recyclerView.setVisibility(View.INVISIBLE);
//        }
//      }
//    });


    ImageButton btnLocateHere = findViewById(R.id.btnLocateHere);
    btnLocateHere.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(locationSource.isActivated()) {
          CameraPosition currentLocation = new CameraPosition(new LatLng(locationSource.getLastLocation().getLatitude(), locationSource.getLastLocation().getLongitude()), 16);
          CameraUpdate currentLocationCamera = CameraUpdate.toCameraPosition(currentLocation).animate(CameraAnimation.Fly);
          naverMap.moveCamera(currentLocationCamera);
        } else {
          Toast.makeText(MainActivity.this, "위치 정보를 불러오는 중입니다.", Toast.LENGTH_SHORT).show();
        }
        
      }
    }); //btnLocateHere.onclick

    /* 위치 변경에 따른 카메라 이동
    naverMap.addOnLocationChangeListener(new NaverMap.OnLocationChangeListener() {
      @Override
      public void onLocationChange(@NonNull Location location) {
        if(locationSource.isActivated()) {
          CameraPosition currentLocation = new CameraPosition(new LatLng(locationSource.getLastLocation().getLatitude(), locationSource.getLastLocation().getLongitude()), 16);
          CameraUpdate currentLocationCamera = CameraUpdate.toCameraPosition(currentLocation).animate(CameraAnimation.Fly);
          naverMap.moveCamera(currentLocationCamera);
        } else {
          Toast.makeText(MainActivity.this, "위치 정보를 불러오는 중입니다.", Toast.LENGTH_SHORT).show();
        }
      }
    });

     */


  } // onMapReady()

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item){
    int id = item.getItemId();

    if (id == R.id.navSetting) {
      Intent intent = new Intent(MainActivity.this, SettingActivity.class);
      startActivity(intent);
    }

    return true;
  }

  @Override
  public void onBackPressed() {
    DrawerLayout mainDrawerLayout = findViewById(R.id.mainDrawerLayout);
    if (mainDrawerLayout.isDrawerOpen(GravityCompat.START)){
      mainDrawerLayout.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  public class getMarkerData extends AsyncTask<Void, Void, Void> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
      String json = "";
      try {
        String urlAddr = ipConfig + "/app/getMarkerData";
        URL url = new URL(urlAddr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();


      } catch (Exception e) {
        e.printStackTrace();
      }

      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
    }
  }

}