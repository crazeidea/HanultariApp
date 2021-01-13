package com.hanultari.parking;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.UiThread;
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
import com.hanultari.parking.Adapter.MarkerInfoWindowAdapter;
import com.hanultari.parking.Adapter.PlaceInfoWindowAdapter;
import com.hanultari.parking.AsyncTasks.SelectMarker;
import com.hanultari.parking.AsyncTasks.SelectParking;
import com.hanultari.parking.DTO.LocateNearRecyclerViewAdapter;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static com.hanultari.parking.CommonMethod.getDistance;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {
  private static final String TAG = "MainActivity";

  /* 레이아웃 관련 변수 */ RecyclerView recyclerView;

  /* 네이버 지도 관련 변수 */
  private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
  public FusedLocationSource locationSource;
  public static NaverMap naverMap;

  /* 전역 변수 */
  public LatLng currentLocation;

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    try {
      setContentView(R.layout.activity_main);
    } catch (Exception e) {
      e.printStackTrace();
    }

    /*레이아웃 관련 변수 */
    DrawerLayout mainDrawerLayout = findViewById(R.id.mainDrawerLayout);
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

    /* 상단 Toolbar */
    ImageView menu = findViewById(R.id.mainMenuImage);
    menu.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mainDrawerLayout.openDrawer(Gravity.LEFT);
      }
    });

    ImageView search = findViewById(R.id.mainSearchImage);
    search.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
        startActivity(intent);
      }
    });

    RelativeLayout toolbar = findViewById(R.id.mainToolbar);
    toolbar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
        startActivity(intent);
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
      if (list != null) {
        for (int i = 0; i < list.length(); i++) {
          JSONObject object = list.getJSONObject(i);
          LatLng latLng = new LatLng(object.getDouble("lat"), object.getDouble("lng"));
          Marker marker = new Marker();
          /* 상태 별 마커 아이콘 변경 */
          if ((object.getInt("total") - object.getInt("parked")) == 0) {
            marker.setIcon(OverlayImage.fromResource(R.drawable.ic_marker_na));
          } else {
            if (object.getInt("paid") == 1)
              marker.setIcon(OverlayImage.fromResource(R.drawable.ic_marker_paid));
            else marker.setIcon(OverlayImage.fromResource(R.drawable.ic_marker_free));
          }
          marker.setPosition(latLng);
          marker.setTag(object.get("id"));
          marker.setMap(naverMap);

          /* 마커 클릭 시 이벤트 */
          marker.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
              currentLocation = new LatLng(locationSource.getLastLocation().getLatitude(), locationSource.getLastLocation().getLongitude());
              LatLng markerLocation = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
              CameraPosition cameraPosition = new CameraPosition(markerLocation, 16);
              CameraUpdate cameraUpdate = CameraUpdate.toCameraPosition(cameraPosition);
              naverMap.moveCamera(cameraUpdate);
              ViewGroup rootView = findViewById(R.id.mainmap);
              infoWindow.setAdapter(new MarkerInfoWindowAdapter(MainActivity.this, rootView, marker.getTag().toString(), currentLocation, markerLocation));
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
                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                  startActivity(intent);

                  return true;
                }
              });
              return false;
            }
          });
        }
        ;
      }
    } catch (Exception e) {
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
        if (recyclerView.getVisibility() == View.VISIBLE) {
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


    Button btnLocateHere = findViewById(R.id.btnLocateHere);
    btnLocateHere.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (locationSource.isActivated()) {
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

    /* 검색 결과 클릭 이벤트 수신 */
    Intent intent = getIntent();
    if (intent.getStringExtra("type") != null) {
      String type = intent.getStringExtra("type");
      LatLng latlng = new LatLng(intent.getFloatExtra("lat", 0), intent.getFloatExtra("lng", 0));
      String address = intent.getStringExtra("title");
      if (getIntent().getStringExtra("type").equals("place")) {
        CameraPosition placePosition = new CameraPosition(latlng, 16);
        CameraUpdate cameraUpdate = CameraUpdate.toCameraPosition(placePosition).animate(CameraAnimation.Fly);
        naverMap.moveCamera(cameraUpdate);
        Marker placeMarker = new Marker();
        placeMarker.setPosition(latlng);
        placeMarker.setMap(naverMap);
        InfoWindow placeInfo = new InfoWindow();
        ViewGroup rootView = findViewById(R.id.mainmap);
        placeInfo.setAdapter(new PlaceInfoWindowAdapter(MainActivity.this, rootView, address));
        placeInfo.open(placeMarker);
        placeInfo.setOnClickListener(new Overlay.OnClickListener() {
          @Override
          public boolean onClick(@NonNull Overlay overlay) {
            Log.d(TAG, "Place InfoWindow Clicked");
            ArrayList<ParkingDTO> dtos = null;
            try {
              LocationManager lm = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
              @SuppressLint("MissingPermission") Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
              LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

              SelectMarker sm = new SelectMarker();
              JSONArray array = sm.execute().get();
              dtos = new ArrayList<>();
              for (int i = 0; i < array.length(); i++) {
                JSONObject object = (JSONObject) array.get(i);
                double distance = getDistance(latlng, new LatLng(object.getDouble("lat"), object.getDouble("lng")));
                if (distance < 1000) {
                  ParkingDTO dto = new ParkingDTO();
                  //TODO 주차장 정보 수신
                  dto.setName("test");
                  dto.setFare(1);
                  dto.setDistance(String.valueOf(distance));
                  dtos.add(dto);
                }
              }

            } catch (Exception e) {
              e.printStackTrace();
            } finally {
              RecyclerView recyclerView = findViewById(R.id.mainRecyclerView);
              recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
              LocateNearRecyclerViewAdapter adapter = new LocateNearRecyclerViewAdapter(getApplicationContext(), dtos);
              recyclerView.setAdapter(adapter);
              recyclerView.setVisibility(View.VISIBLE);

              return false;
            }
          }
        });
      }
    }
  }// onMapReady()

      @Override public boolean onNavigationItemSelected (@NonNull MenuItem item){
        int id = item.getItemId();

        if (id == R.id.navSetting) {
          Intent intent = new Intent(MainActivity.this, SettingActivity.class);
          startActivity(intent);
        }

        return true;
      }

      /* 사이드바 뒤로가기 처리 */
      @Override public void onBackPressed () {
        DrawerLayout mainDrawerLayout = findViewById(R.id.mainDrawerLayout);
        if (mainDrawerLayout.isDrawerOpen(GravityCompat.START)) {
          mainDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
          super.onBackPressed();
        }
      }

      @Override public void onRequestPermissionsResult ( int requestCode,
      @NonNull String[] permissions, @NonNull int[] grantResults){
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
          if (!locationSource.isActivated()) { // 권한 거부됨
            naverMap.setLocationTrackingMode(LocationTrackingMode.None);
          }
          return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      }

    }

