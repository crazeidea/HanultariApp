package com.hanultari.parking.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
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
import androidx.fragment.app.FragmentManager;


import com.hanultari.parking.AsyncTasks.SelectParking;
import com.hanultari.parking.R;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.hanultari.parking.AsyncTasks.CommonMethod.ipConfig;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {
  private static final String TAG = "DetailActivity";
  ViewGroup layout;
  LatLng latLng;

  List<TextView> seatViewList = new ArrayList<>();
  int seatWidth = 70;
  int seatHeight = 100;
  int seatGaping = 5;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

    /* 레이아웃 관련 변수 */
    TextView name = findViewById(R.id.detailName);
    TextView status = findViewById(R.id.detailCurrent);
    TextView fare = findViewById(R.id.detailFare);
    TextView addedFare = findViewById(R.id.detailAddedFare);
    TextView opertime = findViewById(R.id.detailOperTime);
    TextView addr = findViewById(R.id.detailAddr);
    TextView prevAddr = findViewById(R.id.detailPrevAddr);
    TextView manager = findViewById(R.id.detailManager);
    TextView contact = findViewById(R.id.detailManagerContact);
    Button share = findViewById(R.id.detailShare);
    Button call = findViewById(R.id.detailCall);
    Button nav = findViewById(R.id.detailNav);
    WebView pano = findViewById(R.id.detailPanorama);

    Intent intent = getIntent();
    String url = ipConfig + "/getParkingPanorama?id=" + intent.getStringExtra("id");
    Log.d(TAG, "onCreate: " + url);
    pano.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
    pano.setWebViewClient(new WebViewClient());
    pano.setWebChromeClient(new WebChromeClient());
    pano.setNetworkAvailable(true);
    pano.getSettings().setJavaScriptEnabled(true);

    //// Sets whether the DOM storage API is enabled.
    pano.getSettings().setDomStorageEnabled(true);
    ////
    pano.loadUrl(url);

    SelectParking selectParking = new SelectParking();
    LatLng currentLocation = intent.getParcelableExtra("currentLocation");
    try {
      JSONObject parking = selectParking.execute(intent.getStringExtra("id")).get();

      name.setText(parking.getString("name"));
      status.setText("현재 " + (parking.getInt("total") - parking.getInt("parked")) + "자리 남아있어요.");
      fare.setText("기본요금 " + parking.getInt("fare") + "원");
      addedFare.setText("추가 요금 " + parking.getInt("added_fare"));
      opertime.setText(parking.getString("start_time") + " ~ " + parking.getString("end_time"));
      addr.setText(parking.getString("addr"));
      prevAddr.setText(parking.getString("prev_addr"));
      manager.setText(parking.getString("manager"));
      contact.setText(parking.getString("contact"));
      String number = parking.getString("contact");
      number.replace("-", "");
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
    layoutSeat.setPadding(8 * seatGaping, 8 * seatGaping, 8 * seatGaping, 8 * seatGaping);
    layout.addView(layoutSeat);

    LinearLayout layout = null;

    int count = 0;

    for (int index = 0; index < seats.length(); index++) {
      if (seats.charAt(index) == '/') {
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layoutSeat.addView(layout);
      } else if (seats.charAt(index) == 'U') {
        count++;
        TextView view = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatWidth, seatHeight);
        layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
        view.setLayoutParams(layoutParams);
        view.setPadding(0, 0, 0, 2 * seatGaping);
        view.setId(count);
        view.setGravity(Gravity.CENTER);
        view.setBackgroundResource(R.drawable.activity_detail_badge_primary);
        view.setTextColor(Color.WHITE);
        view.setText(count + "");
        view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
        layout.addView(view);
        seatViewList.add(view);
      } else if (seats.charAt(index) == 'A') {
        count++;
        TextView view = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatWidth, seatHeight);
        layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
        view.setLayoutParams(layoutParams);
        view.setPadding(0, 0, 0, 2 * seatGaping);
        view.setId(count);
        view.setGravity(Gravity.CENTER);
        view.setBackgroundResource(R.drawable.activity_detail_badge_primary);
        view.setText(count + "");
        view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
        view.setTextColor(Color.BLACK);
        layout.addView(view);
        seatViewList.add(view);
      } else if (seats.charAt(index) == 'R') {
        count++;
        TextView view = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatWidth, seatHeight);
        layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
        view.setLayoutParams(layoutParams);
        view.setPadding(0, 0, 0, 2 * seatGaping);
        view.setId(count);
        view.setGravity(Gravity.CENTER);
        view.setBackgroundResource(R.drawable.activity_detail_badge_primary);
        view.setText(count + "");
        view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
        view.setTextColor(Color.WHITE);
        layout.addView(view);
        seatViewList.add(view);
      } else if (seats.charAt(index) == '_') {
        TextView view = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatWidth, seatHeight);
        layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
        view.setLayoutParams(layoutParams);
        view.setBackgroundColor(Color.TRANSPARENT);
        view.setText("");
        layout.addView(view);
      }
    }

    //상단 뱃지 출력
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
