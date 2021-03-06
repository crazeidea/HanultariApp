package com.hanultari.parking;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.UiThread;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.hanultari.parking.Activities.DetailActivity;
import com.hanultari.parking.Activities.FavoriteActivity;
import com.hanultari.parking.Activities.LoginActivity;
import com.hanultari.parking.Activities.NoticeActivity;
import com.hanultari.parking.Activities.SearchActivity;
import com.hanultari.parking.Activities.SettingActivity;
import com.hanultari.parking.Activities.SignupActivity;
import com.hanultari.parking.Activities.TicketActivity;
import com.hanultari.parking.Adapter.LocateNearRecyclerViewAdapter;
import com.hanultari.parking.Adapter.MarkerInfoWindowAdapter;
import com.hanultari.parking.Adapter.PlaceInfoWindowAdapter;
import com.hanultari.parking.AsyncTasks.SelectMarker;
import com.hanultari.parking.AsyncTasks.SelectParking;
import com.hanultari.parking.Custom.OnSwipeTouchListener;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import static com.hanultari.parking.CommonMethod.getDistance;
import static com.hanultari.parking.CommonMethod.loginDTO;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
  private static final String TAG = "MainActivity";

  /* 레이아웃 관련 변수 */ RecyclerView recyclerView;

  /* 네이버 지도 관련 변수 */
  private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
  public FusedLocationSource locationSource;
  public static NaverMap naverMap;

  /* 전역 변수 */
  public LatLng currentLatLng;
  public RecyclerView mainRecycler;
  public InfoWindow parkingInfo;
  public Marker parkingMarker;
  public InfoWindow placeInfo;
  public Marker placeMarker;
  public InfoWindow symbolInfo;

  private NavigationView navigationView;


  @SuppressLint("ClickableViewAccessibility")
  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    try {
      setContentView(R.layout.activity_main);
    } catch (Exception e) {
      e.printStackTrace();
    }

    invalidateOptionsMenu();

    Log.d(TAG, "onCreate: LoginDTO = " + loginDTO);

    /*레이아웃 관련 변수 */
    DrawerLayout mainDrawerLayout = findViewById(R.id.mainDrawerLayout);
    navigationView = findViewById(R.id.mainNavigationView);

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

    LocationManager lm = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
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
    currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

    showMenu();

    /* 상단 Toolbar */
    ImageView menuButton = findViewById(R.id.mainMenuImage);
    menuButton.setOnClickListener(new View.OnClickListener() {
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



    /* Navigation Drawer에 Listener 부착 */
    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
          case R.id.navSetting:
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
            break;
          case R.id.navLogin:
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            break;
          case R.id.navFavorite:
            startActivity(new Intent(MainActivity.this, FavoriteActivity.class));
            break;
          case R.id.navSignup:
            startActivity(new Intent(MainActivity.this, SignupActivity.class));
            finish();
            break;
          case R.id.navNotice:
            startActivity(new Intent(MainActivity.this, NoticeActivity.class));
            break;
          case R.id.navTicket:
            startActivity(new Intent(MainActivity.this, TicketActivity.class));
            break;
          case R.id.navLogout:
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("정말 로그아웃하시겠습니까?");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                loginDTO = null;
                showMenu();
              }
            });
            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
              }
            });
            builder.show();
        }
        return true;
      }
    });

    DisplayMetrics displayMetrics = new DisplayMetrics();

    mainRecycler = findViewById(R.id.mainRecyclerView);
    mainRecycler.setOnFlingListener(new RecyclerView.OnFlingListener() {
      @Override
      public boolean onFling(int velocityX, int velocityY) {
        if (!mainRecycler.canScrollVertically(-1)) { // 최상단 도달 시
          if (velocityY < 0) { // 아래로 스크롤 시
            Animation animation = new Animation() {
              @Override
              protected void applyTransformation(float interpolatedTime, Transformation t) {
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mainRecycler.getLayoutParams();

                params.topToTop = R.id.btnLocateNear;
                params.topMargin = Math.round(64 * getResources().getDisplayMetrics().density);
                mainRecycler.setLayoutParams(params);
              }
            };
            animation.setDuration(300);
            mainRecycler.startAnimation(animation);
          }
        } else {
          if (velocityY > 0) {
            Animation animation = new Animation() {
              @Override
              protected void applyTransformation(float interpolatedTime, Transformation t) {
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mainRecycler.getLayoutParams();

                params.topToTop = R.id.mainConstraint;
                params.topMargin = Math.round(128 * getResources().getDisplayMetrics().density);
                mainRecycler.setLayoutParams(params);
              }
            };
            animation.setDuration(300);
            mainRecycler.startAnimation(animation);
          }

        }

        Log.d(TAG, "onFling: " + velocityX + ", " + velocityY);
        return false;

      };
    });
  }// onCreate()


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
          marker.setTag(object.getInt("id"));
          marker.setMap(naverMap);

          /* 마커 클릭 시 이벤트 */
          marker.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
              LatLng markerLocation = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
              CameraPosition cameraPosition = new CameraPosition(markerLocation, 16);
              CameraUpdate cameraUpdate = CameraUpdate.toCameraPosition(cameraPosition);
              naverMap.moveCamera(cameraUpdate);
              ViewGroup rootView = findViewById(R.id.mainmap);
              infoWindow.setAdapter(new MarkerInfoWindowAdapter(MainActivity.this, rootView, (Integer) marker.getTag(), currentLatLng, markerLocation));
              infoWindow.open(marker);
              infoWindow.setOnClickListener(new Overlay.OnClickListener() {
                @Override
                public boolean onClick(@NonNull Overlay overlay) {
                  Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                  intent.putExtra("id", (int) marker.getTag());
                  intent.putExtra("currentLocation", currentLatLng);
                  SelectParking selectParking = new SelectParking();
                  try {
                    intent.putExtra("markerName", selectParking.execute(Integer.valueOf(marker.getTag().toString())).get().toString());
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
//        recyclerView = findViewById(R.id.mainRecyclerView);
//        if (recyclerView.getVisibility() == View.VISIBLE) {
//          Animation animation = new AlphaAnimation(1, 0);
//          animation.setDuration(300);
//          recyclerView.setVisibility(View.GONE);
//        }
      }
    });

    /* 지도 스크롤 시 이벤트 */
    naverMap.addOnCameraChangeListener(new NaverMap.OnCameraChangeListener() {
      @Override
      public void onCameraChange(int i, boolean b) {
        /* 카메라 이동 시 주변 주차장 화면 제거 */


        /* 카메라 이동 시 InfoWindow 제거 */
        infoWindow.close();
      }
    });

    /* 내 위치로 이동 버튼 클릭 이벤트 */
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

    /* 주변 주차장 찾기 버튼 클릭 이벤트 */
    Button btnLocateNear = findViewById(R.id.btnLocateNear);
    btnLocateNear.setOnClickListener(new View.OnClickListener() {
      @SuppressLint("ClickableViewAccessibility")
      @Override
      public void onClick(View v) {
        ArrayList<ParkingDTO> array = new ArrayList<>();
        try {
          SelectMarker sm = new SelectMarker();
          JSONArray list = sm.execute().get();
          for (int i = 0; i < list.length(); i++) {
            JSONObject object = list.getJSONObject(i);
            double distance = getDistance(currentLatLng, new LatLng(object.getDouble("lat"), object.getDouble("lng")));
            Log.d(TAG, "Distance : " + distance);
            if (distance < 1000) {
              ParkingDTO dto = new ParkingDTO();
              try {
                int id = object.getInt("id");
                SelectParking sp = new SelectParking();
                JSONObject parking = sp.execute(id).get();
                dto.setId(id);
                dto.setName(parking.getString("name"));
                dto.setFare(parking.getInt("fare"));
                dto.setPaid(parking.getBoolean("paid"));
                dto.setDistance((int) distance);
                array.add(dto);
                Collections.sort(array, new Comparator<ParkingDTO>() {
                  @Override
                  public int compare(ParkingDTO o1, ParkingDTO o2) {
                    return o1.getDistance() - o2.getDistance();
                  }
                });
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          mainRecycler = findViewById(R.id.mainRecyclerView);
          mainRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
          LocateNearRecyclerViewAdapter adapter = new LocateNearRecyclerViewAdapter(getApplicationContext(), array);
          mainRecycler.setAdapter(adapter);

          if (mainRecycler.getVisibility() == View.VISIBLE) {
            Animation animation = new Animation() {
              @Override
              protected void applyTransformation(float interpolatedTime, Transformation t) {
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mainRecycler.getLayoutParams();
                params.topToTop = R.id.mainConstraint;
                params.topMargin = Math.round(128 * getResources().getDisplayMetrics().density);
                mainRecycler.setLayoutParams(params);
              }
            };
            animation.setDuration(300);
            mainRecycler.startAnimation(animation);
          }

          adapter.setOnItemClickListener(new LocateNearRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
              ParkingDTO dto = (ParkingDTO) v.getTag();
              int id = dto.getId();
              Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
              intent.putExtra("id", id);
              startActivity(intent);
            }
          });
        }
      }
    }); // btnLocateNear Click

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
      if (getIntent().getStringExtra("type").equals("place")) {
        String address = intent.getStringExtra("title");
        CameraPosition placePosition = new CameraPosition(latlng, 16);
        CameraUpdate cameraUpdate = CameraUpdate.toCameraPosition(placePosition).animate(CameraAnimation.Fly);
        naverMap.moveCamera(cameraUpdate);
        placeMarker = new Marker();
        placeMarker.setPosition(latlng);
        placeMarker.setMap(naverMap);
        placeInfo = new InfoWindow();
        ViewGroup rootView = findViewById(R.id.mainmap);
        String name = intent.getStringExtra("name");
        placeInfo.setAdapter(new PlaceInfoWindowAdapter(MainActivity.this, rootView, address, name));
        placeInfo.open(placeMarker);
        placeInfo.setOnClickListener(new Overlay.OnClickListener() {
          @Override
          public boolean onClick(@NonNull Overlay overlay) {
            ArrayList<ParkingDTO> array = new ArrayList<>();
            try {
              SelectMarker sm = new SelectMarker();
              JSONArray markers = sm.execute().get();
              for (int i = 0; i < markers.length(); i++) {
                JSONObject object = markers.getJSONObject(i);
                double distance = getDistance(latlng, new LatLng(object.getDouble("lat"), object.getDouble("lng")));
                if (distance < 1000) {
                  ParkingDTO dto = new ParkingDTO();
                  try {
                    int id = object.getInt("id");
                    SelectParking sp = new SelectParking();
                    JSONObject parking = sp.execute(id).get();
                    dto.setName(parking.getString("name"));
                    dto.setFare(parking.getInt("fare"));
                    dto.setPaid(parking.getBoolean("paid"));
                    dto.setDistance((int) distance);
                    array.add(dto);
                    Collections.sort(array, new Comparator<ParkingDTO>() {
                      @Override
                      public int compare(ParkingDTO o1, ParkingDTO o2) {
                        return o1.getDistance() - o2.getDistance();
                      }
                    });
                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                }
              }
            } catch (Exception e) {
              e.printStackTrace();
            } finally {
              mainRecycler = findViewById(R.id.mainRecyclerView);
              mainRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
              LocateNearRecyclerViewAdapter adapter = new LocateNearRecyclerViewAdapter(getApplicationContext(), array);
              mainRecycler.setAdapter(adapter);
              if (mainRecycler.getVisibility() == View.GONE) {
                mainRecycler.setVisibility(View.VISIBLE);
              } else if (mainRecycler.getVisibility() == View.VISIBLE) {
                mainRecycler.setVisibility(View.GONE);
              }
            }
            return true;
          }
        });
      } else if (getIntent().getStringExtra("type").equals("parking")) {
        int id = getIntent().getIntExtra("id", 0);
        CameraPosition parkingPosition = new CameraPosition(latlng, 16);
        CameraUpdate cameraUpdate = CameraUpdate.toCameraPosition(parkingPosition).animate(CameraAnimation.Fly);
        naverMap.moveCamera(cameraUpdate);
        parkingMarker = new Marker();
        parkingMarker.setPosition(latlng);
        parkingMarker.setMap(naverMap);
        parkingInfo = new InfoWindow();
        ViewGroup rootView = findViewById(R.id.mainmap);
        parkingInfo.setAdapter(new MarkerInfoWindowAdapter(this, rootView, id, latlng, currentLatLng));
        parkingInfo.open(parkingMarker);
        parkingInfo.setOnClickListener(new Overlay.OnClickListener() {
          @Override
          public boolean onClick(@NonNull Overlay overlay) {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("currentLocation", currentLatLng);
            SelectParking sp = new SelectParking();
            try {
              intent.putExtra("markerName", sp.execute(id).get().toString());
            } catch (Exception e) {
              e.printStackTrace();
            }
            startActivity(intent);
            return false;
          }
        });
      }
    }
    

  }// onMapReady()


  /* 사이드바 뒤로가기 처리 */
  private long time = 0;

  @Override
  public void onBackPressed() {
    DrawerLayout mainDrawerLayout = findViewById(R.id.mainDrawerLayout);
    if (mainDrawerLayout.isDrawerOpen(GravityCompat.START)) {
      mainDrawerLayout.closeDrawer(GravityCompat.START);
    } else if (((ConstraintLayout.LayoutParams)mainRecycler.getLayoutParams()).topToTop == R.id.mainConstraint) {
      ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mainRecycler.getLayoutParams();
      params.topToTop = R.id.btnLocateNear;
      params.topMargin = Math.round(64 * getResources().getDisplayMetrics().density);
      mainRecycler.setLayoutParams(params);
    } else if (placeInfo != null) {
      placeMarker.setMap(null);
      placeInfo = null;
    } else if (parkingInfo != null) {
      parkingMarker.setMap(null);
      parkingInfo = null;
    } else {
      if (System.currentTimeMillis() - time >= 2000) {
        time = System.currentTimeMillis();
        Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
      } else if (System.currentTimeMillis() - time <= 2000) {
        finish();
      }
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

  public void showMenu() {
    Menu menu = navigationView.getMenu();
    if(loginDTO != null) { // 로그인 시
      menu.findItem(R.id.navSignup).setVisible(false);
      menu.findItem(R.id.navLogin).setVisible(false);
      menu.findItem(R.id.navLogout).setVisible(true);
    } else { // 미로그인
      menu.findItem(R.id.navSignup).setVisible(true);
      menu.findItem(R.id.navLogin).setVisible(true);
      menu.findItem(R.id.navLogout).setVisible(false);
    }
  }

}

