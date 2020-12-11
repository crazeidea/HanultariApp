package com.hanultari.parking;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

  public static Dialog dialog;
  SupportMapFragment mapFragment;
  GoogleMap map;
  Button btnLocateHere, btnLocateNear;  //Here 내위치 확인, Near 주변주차장 찾기
  SearchView searchView;  //검색바
  MarkerOptions mymarker; //위치표시
  private MainActivity mainActivity;

  ArrayList<ParkingDTO> dtos;
  Point size;

  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    checkDangerousPermissions();

    // 디바이스 사이즈 구하기
    size = getDeviceSize();
    dtos = new ArrayList<>();
    btnLocateNear = findViewById(R.id.btnLocateNear);
    btnLocateHere = findViewById(R.id.btnLocateHere);




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

    btnLocateNear.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
       showAlertDialog();
      }
    });

  }

  private void showAlertDialog() {
    LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
    View view = inflater.inflate(R.layout.activity_recyclerview_main, null);
    RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this,
            LinearLayoutManager.VERTICAL, false));

    ParkingAdapter adapter = new ParkingAdapter(MainActivity.this, dtos);
    adapter.addDto(new ParkingDTO("5000", "한울주차장", "500m"));
    adapter.addDto(new ParkingDTO("4000", "가나주차장", "1500m"));
    adapter.addDto(new ParkingDTO("4000", "다라주차장", "300m"));
    adapter.addDto(new ParkingDTO("5500", "마바주차장", "200m"));
    adapter.addDto(new ParkingDTO("10000", "사나주차장", "450m"));
    recyclerView.setAdapter(adapter);



    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    builder.setTitle("주차장 리스트");
    builder.setView(view);
    builder.setNegativeButton("종료", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        if(dialog != null){
          dialog.dismiss();
        }
      }
    });
    dialog = builder.create();
    dialog.show();

    // 디바이스 사이즈를 받아 팝업 크기창을 조절한다
    int sizeX = size.x; // 넓이
    int sizeY = size.y; // 높이

    // 다이얼로그창에서 위치, 크기 수정
    WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
    params.x = (int) Math.round(sizeX * 0.005); // x 위치
    params.y = (int) Math.round(sizeY * 0.01); // y 위치
    params.width = (int) Math.round(sizeX * 0.9);
    params.height = (int) Math.round(sizeY * 0.8);
    dialog.getWindow().setAttributes(params);
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

  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
  private Point getDeviceSize() {
    Display display = getWindowManager().getDefaultDisplay(); // 액티비티일때
    // getactivity().getWindowManager().getDefaultDisplay(); // 프래그먼트 일때
    Point size = new Point();
    display.getRealSize(size); // or getSize();
    int width = size.x;
    int height = size.y;

    return size;
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

