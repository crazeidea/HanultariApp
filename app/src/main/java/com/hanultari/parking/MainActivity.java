package com.hanultari.parking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

  SupportMapFragment mapFragment;
  GoogleMap map;
  Button btnLocateHere, btnLocateNear;  //Here 내위치 확인, Near 주변주차장 찾기
  SearchView searchView;  //검색바
  MarkerOptions mymarker; //위치표시
  Fragment fragment1; //내 정보
  Fragment fragment2; //설정

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    checkDangerousPermissions();
    btnLocateNear = findViewById(R.id.btnLocateNear);
    btnLocateHere = findViewById(R.id.btnLocateHere);

    fragment1 = new Fragment();
    fragment2 = new Fragment();

    mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

    mapFragment.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        try {
          map.setMyLocationEnabled(true);
        } catch (SecurityException e) {

        }

      }
    });
    MapsInitializer.initialize(this);

    btnLocateHere.setOnClickListener(new View.OnClickListener() { //내위치 찾기
      @Override
      public void onClick(View v) {
        requestMyLocation();
      }
    });
  }

  private void requestMyLocation() {
    LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    try {
      long minTime = 10000;
      float minDistance = 0;
      manager.requestLocationUpdates(
              LocationManager.GPS_PROVIDER,
              minTime,
              minDistance,
              new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                  showCurrentLocation(location);
                }
              }
      );
    }catch (SecurityException e) {

    }
  }

  private void showCurrentLocation(Location location) {
    LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
    String msg = "Latitude : " + curPoint.latitude + "\nLongitude : " + curPoint.longitude;
    map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 18));

    //실시간 위,경도 구현하는건 찾아볼게요~
    //hear 버튼 누르면 내가 설정한 위치로 이동해요
    Location targetLocation = new Location("");
    targetLocation.setLatitude(35.2103);
    targetLocation.setLongitude(126.8628);
    showMyLocationMarker(targetLocation);
  }

  private void showMyLocationMarker(Location location) {
    if (mymarker == null) {
      mymarker = new MarkerOptions();
      mymarker.position(new LatLng(location.getLatitude(), location.getLongitude()));
      map.addMarker(mymarker);
    }
  }

  private void checkDangerousPermissions() {
    String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    int permissionCheck = PackageManager.PERMISSION_GRANTED;
    for (int i = 0; i < permissions.length; i++) {
      permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
      if (permissionCheck == PackageManager.PERMISSION_DENIED) {
        break;
      }
    }

    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
      Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
    } else {
      Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();

      if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
        Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();
      } else {
        ActivityCompat.requestPermissions(this, permissions, 1);
      }
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    if (requestCode == 1) {
      for (int i = 0; i < permissions.length; i++) {
        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
          Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
        } else {
          Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
        }
      }
    }
  }

}

