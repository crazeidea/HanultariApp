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
import com.hanultari.parking.Activities.DetailActivity;
import com.hanultari.parking.Activities.SearchActivity;
import com.hanultari.parking.Activities.SettingActivity;
import com.hanultari.parking.Adapter.InfoWindowAdapter;
import com.hanultari.parking.AsyncTasks.SelectMarker;
import com.hanultari.parking.AsyncTasks.SelectParking;
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
import java.util.Locale;
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
    MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.mainmap);
    if (mapFragment == null) {
      mapFragment = MapFragment.newInstance();
      fm.beginTransaction().add(R.id.mainmap, mapFragment).commit();
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



  @UiThread
  @Override
  public void onMapReady(@NonNull NaverMap naverMap) {

    naverMap.setLocationSource(locationSource);
    naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
    naverMap.setLocale(Locale.KOREA);
    naverMap.setMinZoom(6);
    naverMap.setMaxZoom(21);


    /* 마커 생성 */
    InfoWindow infoWindow = new InfoWindow();
    try {
      Log.d(TAG, "onCreate: Initialized Marker Creation");
        JSONArray list = new SelectMarker().execute().get();
        if(list != null) {
          for (int i = 0; i < list.length(); i++) {
            JSONObject object = list.getJSONObject(i);
            LatLng latLng = new LatLng(object.getDouble("lat"), object.getDouble("lng"));
            Marker marker = new Marker();
            /* 상태 별 마커 아이콘 변경 */
            if((object.getInt("total") - object.getInt("parked")) == 0) {
              marker.setIcon(OverlayImage.fromResource(R.drawable.ic_marker_na));
            } else {
              if (object.getInt("paid") == 1) marker.setIcon(OverlayImage.fromResource(R.drawable.ic_marker_paid));
              else marker.setIcon(OverlayImage.fromResource(R.drawable.ic_marker_free));
            }
            marker.setPosition(latLng);
            marker.setTag(object.get("id"));
            marker.setMap(naverMap);
            /* 마커 클릭 시 이벤트 */
            marker.setOnClickListener(new Overlay.OnClickListener() {
              @Override
              public boolean onClick(@NonNull Overlay overlay) {
                Log.d(TAG, "onClick: Marker Clicked / MarkerID : " + marker.getTag());

                LatLng currentLocation = new LatLng(locationSource.getLastLocation().getLatitude(), locationSource.getLastLocation().getLongitude());
                LatLng markerLocation = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                CameraPosition cameraPosition = new CameraPosition(markerLocation, 16);
                CameraUpdate cameraUpdate = CameraUpdate.toCameraPosition(cameraPosition);
                naverMap.moveCamera(cameraUpdate);
                Log.d(TAG, "onClick: " + markerLocation.latitude + ", " + markerLocation.longitude);

                ViewGroup rootView = findViewById(R.id.mainmap);
                infoWindow.setAdapter(new InfoWindowAdapter(MainActivity.this, rootView, marker.getTag().toString(), currentLocation, markerLocation));
                infoWindow.open(marker);
                infoWindow.setOnClickListener(new Overlay.OnClickListener() {
                  @Override
                  public boolean onClick(@NonNull Overlay overlay) {
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("id", marker.getTag().toString());
                    intent.putExtra("currentLocation", currentLocation);
                    SelectParking selectParking = new SelectParking();
                    try {
                      intent.putExtra("markerName", selectParking.execute(marker.getTag().toString()).get().toString());
                    } catch (Exception e){
                      e.printStackTrace();
                    }
                    startActivity(intent);

                    return true;
                  }
                });

                return false;
              }
            });
            };
          }
      } catch(Exception e){
        Log.d(TAG, "onCreate: Marker Creation ERROR");
        e.printStackTrace();
      }

    /* 지도 클릭 시 이벤트 */
    naverMap.setOnMapClickListener(new NaverMap.OnMapClickListener() {
      @Override
      public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
        Log.d(TAG, "onMapClick: " + latLng.latitude + ", " + latLng.longitude);
        /* 지도 클릭 시 주변 주차장 화면 제거 */
        recyclerView = findViewById(R.id.mainRecyclerView);
        if(recyclerView.getVisibility() == View.VISIBLE) {
          Animation animation = new AlphaAnimation(1, 0);
          animation.setDuration(300);
          recyclerView.setVisibility(View.GONE);
        }
      }
    });

    /* 지도 스크롤 시 이벤트 */
    naverMap.addOnCameraChangeListener(new NaverMap.OnCameraChangeListener() {
      @Override
      public void onCameraChange(int i, boolean b) {
        /* 카메라 이동 시 주변 주차장 화면 제거 */
        recyclerView = findViewById(R.id.mainRecyclerView);
        if (recyclerView.getVisibility() == View.VISIBLE) {
          Animation animation = new AlphaAnimation(1, 0);
          animation.setDuration(300);
          recyclerView.setAnimation(animation);
          recyclerView.setVisibility(View.GONE);
        }
        /* 카메라 이동 시 InfoWindow 제거 */
          infoWindow.close();
      }
    });


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

  /* 사이드바 뒤로가기 처리 */
  @Override
  public void onBackPressed() {
    DrawerLayout mainDrawerLayout = findViewById(R.id.mainDrawerLayout);
    if (mainDrawerLayout.isDrawerOpen(GravityCompat.START)){
      mainDrawerLayout.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

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

}

