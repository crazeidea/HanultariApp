package com.hanultari.parking;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;


import com.google.android.material.navigation.NavigationView;
import com.hanultari.parking.Activities.SettingActivity;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.util.FusedLocationSource;

import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {
  private static final String TAG = "MainActivity";

  private ImageButton btnLocateHere;


  /* 네이버 지도 관련 변수 */
  private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
  private FusedLocationSource locationSource;
  private NaverMap naverMap;


  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    /* 레이아웃 관련 변수 */
    btnLocateHere = findViewById(R.id.btnLocateHere);
    /*레이아웃 관련 변수 */
    DrawerLayout mainDrawerLayout = findViewById(R.id.mainDrawerLayout);
    Toolbar mainToolbar = findViewById(R.id.mainToolbar);
    NavigationView navigationView = findViewById(R.id.mainNavigationView);
    SearchView search = findViewById(R.id.searchView);

    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mainDrawerLayout, mainToolbar, R.string.open_drawer, R.string.close_drawer);
    mainDrawerLayout.addDrawerListener(toggle);
    toggle.syncState();

    navigationView.setNavigationItemSelectedListener(this);



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
    this.naverMap = naverMap;

    naverMap.setLocationSource(locationSource);
    naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
    naverMap.setMinZoom(12);
    naverMap.setMaxZoom(16);



    btnLocateHere.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        CameraPosition currentLocation = new CameraPosition(new LatLng(locationSource.getLastLocation().getLatitude(), locationSource.getLastLocation().getLongitude()), 16);
        CameraUpdate currentLocationCamera = CameraUpdate.toCameraPosition(currentLocation).animate(CameraAnimation.Fly);
        naverMap.moveCamera(currentLocationCamera);
      }
    }); //btnLocateHere.onclick
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

}