package com.hanultari.parking.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.hanultari.parking.AsyncTasks.SelectParking;
import com.hanultari.parking.Custom.CustomScrollView;
import com.hanultari.parking.R;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.hanultari.parking.CommonMethod.ipConfig;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {
  private static final String TAG = "DetailActivity";
  ViewGroup layout;
  LatLng latLng;

  List<TextView> seatViewList = new ArrayList<>();
  int seatWidth = 100;
  int seatHeight = 100;
  int seatGaping = 5;

  @SuppressLint("ClickableViewAccessibility")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

    /* 레이아웃 관련 변수 */
    CustomScrollView scrollView = findViewById(R.id.detailScrollView);
    TextView name = findViewById(R.id.detailName);
    TextView status = findViewById(R.id.detailCurrent);
    TextView fare = findViewById(R.id.detailFare);
    TextView addedFare = findViewById(R.id.detailAddedFare);
    TextView opertime = findViewById(R.id.detailOperTime);
    TextView addr = findViewById(R.id.detailAddr);
    TextView prevAddr = findViewById(R.id.detailPrevAddr);
    TextView manager = findViewById(R.id.detailManager);
    TextView contact = findViewById(R.id.detailManagerContact);
    Button share = findViewById(R.id.labelIcon);
    Button call = findViewById(R.id.detailCall);
    Button nav = findViewById(R.id.detailNav);
    WebView pano = findViewById(R.id.detailPanorama);
    LinearLayout labels = findViewById(R.id.detailLabels);

    Intent intent = getIntent();

    String url = ipConfig + "/getParkingPanorama?id=" + intent.getStringExtra("id");

    /* 상단 로드뷰 구현 */
    pano.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
    pano.setWebViewClient(new WebViewClient());
    pano.setWebChromeClient(new WebChromeClient());
    pano.setNetworkAvailable(true);
    pano.getSettings().setJavaScriptEnabled(true);
    pano.getSettings().setDomStorageEnabled(true);
    pano.loadUrl(url);

    /* 로드뷰 스크롤시 ScrollView 스크롤 방지 */
    pano.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        scrollView.setEnableScrolling(event.getAction() != MotionEvent.ACTION_SCROLL);
        return false;
      }
    });


    SelectParking selectParking = new SelectParking();
    LatLng currentLocation = intent.getParcelableExtra("currentLocation");
    try {
      JSONObject parking = selectParking.execute(Integer.valueOf(intent.getStringExtra("id"))).get();

      name.setText(parking.getString("name"));
      status.setText("현재 " + (parking.getInt("total") - parking.getInt("parked")) + "자리 남아있어요.");
      fare.setText("기본요금 " + parking.getInt("fare") + "원");
      addedFare.setText("추가 요금 " + parking.getInt("added_fare") + "원");
      opertime.setText(parking.getString("start_time") + " ~ " + parking.getString("end_time"));
      addr.setText(parking.getString("addr"));
      prevAddr.setText(parking.getString("prev_addr"));
      manager.setText(parking.getString("manager"));
      contact.setText(parking.getString("contact"));
      String number = parking.getString("contact").replace("-", "");
      latLng = new LatLng(parking.getDouble("lat"), parking.getDouble("lng"));
      
      Uri callnumber = Uri.parse("tel:" + number);

      call.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent callIntent = new Intent("android.permission.CALL_PHONE");
          callIntent.setData(callnumber);
          startActivity(callIntent);
        }
      });

      nav.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          try {
            String app = getSharedPreferences("settings", MODE_PRIVATE).getString("app", "");
          if (app.equals("naver")) {
            Log.d(TAG, "Launching Naver Map");
            String url = "nmap://route/car?";
            String slat = "slat=" + currentLocation.latitude + "&";
            String slng = "slng=" + currentLocation.longitude + "&";
            String sname = "현재 위치";
            String dlat = "dlat=" + parking.getDouble("lat") + "&";
            String dlng = "dlng=" + parking.getDouble("lng") + "&";
            String dname = "dname" + parking.getString("name") + "&";
            String appname = "appname=com.hanultari.app";
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(url + slat + slng + sname + dlat + dlng + dname + appname);
            String uri = stringBuffer.toString();
            Log.d(TAG, "Open Map App: " + uri);
            Intent naverIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(naverIntent);
          } else if (app.equals("google")){
//            Log.d(TAG, "Launching Google Map");
//            Uri gmmIntentURI = Uri.parse("google.navigation:q=" + dlat + "," + dlng);
//            Intent googleIntent = new Intent(Intent.ACTION_VIEW, gmmIntentURI);
//            googleIntent.setPackage("com.google.android.apps.maps");
//            startActivity(googleIntent);

          }
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
      });



      String seats = parking.getString("layout");


    //주차장 레이아웃 구현
    layout = findViewById(R.id.layoutDetailStatus);

    seats = "/" + seats;

    LinearLayout layoutSeat = new LinearLayout(this);
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    layoutSeat.setOrientation(LinearLayout.VERTICAL);
    layoutSeat.setLayoutParams(params);
    layoutSeat.setPadding(0, 8 * seatGaping, 0, 8 * seatGaping);
    layout.addView(layoutSeat);

    LinearLayout layout = null;

    int count = 0;
    int blank = 0;
    for(int i = 0; i < seats.indexOf("/", 1); i++) {
      if(Character.toString(seats.charAt(i)).equals("_")) blank++;
    }
    int weightsum = seats.indexOf("/", 1) - 1;
      Log.d(TAG, "Weightsum : " + weightsum);

    for (int index = 0; index < seats.length(); index++) {
      if (seats.charAt(index) == '/') {
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setWeightSum(weightsum);
        layoutSeat.addView(layout);
      } else if (seats.charAt(index) == 'A') { // 일반 좌석
        count++;
        TextView view = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatWidth, seatHeight, 1);
        view.setLayoutParams(layoutParams);
        view.setId(count);
        view.setGravity(Gravity.CENTER);
        view.setBackgroundResource(R.drawable.seat_primary);
        view.setText(count + "");
        view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
        view.setTextColor(Color.WHITE);
        layout.addView(view);
        seatViewList.add(view);
      } else if (seats.charAt(index) == 'B') { // 장애인 좌석
        count++;
        TextView view = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatWidth, seatHeight, 1);
        view.setLayoutParams(layoutParams);
        view.setId(count);
        view.setGravity(Gravity.CENTER);
        view.setBackgroundResource(R.drawable.seat_disabled);
        view.setText(count + "");
        view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
        view.setTextColor(Color.WHITE);
        layout.addView(view);
        seatViewList.add(view);
      } else if (seats.charAt(index) == 'C') { // 여성 좌석
        count++;
        TextView view = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatWidth, seatHeight, 1);
        view.setLayoutParams(layoutParams);
        view.setId(count);
        view.setGravity(Gravity.CENTER);
        view.setBackgroundResource(R.drawable.seat_woman);
        view.setText(count + "");
        view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
        view.setTextColor(Color.WHITE);
        layout.addView(view);
        seatViewList.add(view);
      } else if (seats.charAt(index) == 'N') { // 빈 좌석
        count++;
        TextView view = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatWidth, seatHeight, 1);
        view.setLayoutParams(layoutParams);
        view.setId(count);
        view.setGravity(Gravity.CENTER);
        view.setBackgroundResource(R.drawable.seat_na);
        view.setText(count + "");
        view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
        view.setTextColor(Color.WHITE);
        layout.addView(view);
        seatViewList.add(view);
      } else if (seats.charAt(index) == '_') {
        TextView view = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatWidth, seatHeight, 1);
        view.setLayoutParams(layoutParams);
        view.setBackgroundColor(Color.TRANSPARENT);
        view.setText("");
        layout.addView(view);
      }
    }

    /* 결제 정보 라벨 출력 */
      if (parking.getInt("payment_cash") == 1){

      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onMapReady(@NonNull NaverMap naverMap) {

    CameraPosition cameraPosition = new CameraPosition(latLng, 16);
    CameraUpdate cameraUpdate = CameraUpdate.toCameraPosition(cameraPosition);
    naverMap.moveCamera(cameraUpdate);

  }
}
