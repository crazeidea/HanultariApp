package com.hanultari.parking.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
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
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import com.hanultari.parking.AsyncTasks.DeleteFavorite;
import com.hanultari.parking.AsyncTasks.InsertFavorite;
import com.hanultari.parking.AsyncTasks.SelectParking;
import com.hanultari.parking.AsyncTasks.CheckFavorite;
import com.hanultari.parking.Custom.CustomScrollView;
import com.hanultari.parking.R;
import com.naver.maps.geometry.LatLng;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static com.hanultari.parking.CommonMethod.ipConfig;
import static com.hanultari.parking.CommonMethod.loginDTO;

public class DetailActivity extends AppCompatActivity{
  private static final String TAG = "DetailActivity";
  ViewGroup layout;
  LatLng latLng;
  Boolean isFavorite;

  List<TextView> seatViewList = new ArrayList<>();
  int seatWidth = 100;
  int seatHeight = 100;
  int seatGaping = 5;

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  @SuppressLint("ClickableViewAccessibility")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

    int parkingid = Integer.parseInt(getIntent().getStringExtra("id"));

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
    TextView label = findViewById(R.id.detailLabel);

    Intent intent = getIntent();

    String url = ipConfig + "/getParkingPanorama?id=" + parkingid;

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
      JSONObject parking = selectParking.execute(parkingid).get();

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
      
      if(parking.getBoolean("paid")) {
       label.setBackgroundResource(R.drawable.label_secondary);
       label.setText("유료");
      }

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
          String currentApp = getSharedPreferences("settings", MODE_PRIVATE).getString("map", "");
          Toast.makeText(DetailActivity.this, currentApp, Toast.LENGTH_SHORT).show();
          try {
            String app = getSharedPreferences("settings", MODE_PRIVATE).getString("map", "");
            String slat = "slat=" + currentLocation.latitude + "&";
            String slng = "slng=" + currentLocation.longitude + "&";
            String dlat = "dlat=" + parking.getDouble("lat") + "&";
            String dlng = "dlng=" + parking.getDouble("lng") + "&";
            if (app.equals("naver")) {
              Log.d(TAG, "Launching Naver Map");
              String url = "nmap://navigation?";
              String sname = URLEncoder.encode("현재 위치", "UTF-8");
              String dname = "dname" + URLEncoder.encode(parking.getString("name"), "EUC-KR") + "&";
              String appname = "appname=com.hanultari.app";
              StringBuffer stringBuffer = new StringBuffer();
              stringBuffer.append(url + dlat + dlng + dname + appname);
              String uri = stringBuffer.toString();
              Log.d(TAG, "Open Map App: " + uri);
              Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
              intent.addCategory(Intent.CATEGORY_BROWSABLE);

              List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
              if(list == null || list.isEmpty()) {
                getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.nhn.android.nmap")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
              } else {
                getApplicationContext().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
              }
            } else if (app.equals("kakao")) {
              String url = "kakaomap://route?";
              String sp = "sp=" + currentLocation.latitude + "," + currentLocation.longitude;
              String ep = "&ep=" + parking.getDouble("lat") +"," + parking.getDouble("lng");
              String type = "&by=CAR";
              StringBuffer sb = new StringBuffer();
              sb.append(url + sp + ep + type);
              String uri = sb.toString();
              Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
              intent.addCategory(Intent.CATEGORY_BROWSABLE);

              List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
              if (list == null || list.isEmpty()) {
                getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=net.daum.android.map")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
              } else {
                getApplicationContext().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
              }

            } else if (app.equals("tmap")) {
              String url = "tmap://route";
              String GoName = "?goalname=" + parking.getString("name");
              String rGoX = "&goalx=" + parking.getDouble("lat");
              String rGoY = "&goaly=" + parking.getDouble("lng");
              StringBuffer sb = new StringBuffer();
              sb.append(url + GoName + rGoX + rGoY);
              String uri = sb.toString();
              Log.d(TAG, "onClick: " + uri);
              Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
              intent.addCategory(Intent.CATEGORY_BROWSABLE);

              List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
              if (list == null || list.isEmpty()) {
                getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.skt.tmap.ku")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
              } else {
                getApplicationContext().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
              }
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
      for (int i = 0; i < seats.indexOf("/", 1); i++) {
        if (Character.toString(seats.charAt(i)).equals("_")) blank++;
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
    } catch (Exception e) {
      e.printStackTrace();
    }

    /* 즐겨찾기 버튼 처리 */
    Button favorite = findViewById(R.id.btnFavoriteDetail);
    isFavorite = false;

    CheckFavorite checkFavorite = new CheckFavorite();
    int memberid = 0;
    try {
      isFavorite = checkFavorite.execute(parkingid, memberid).get();
      if(loginDTO == null) {
        favorite.setVisibility(View.INVISIBLE);
      } else {
        if (isFavorite) {
          favorite.setBackgroundTintList(getResources().getColorStateList(R.color.color_yellow));
        } else {
          favorite.setBackgroundTintList(getResources().getColorStateList(R.color.color_gray));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    Log.d(TAG, "onCreate: " + isFavorite);

    int finalMemberid = memberid;
    favorite.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (isFavorite) {
          try {
            DeleteFavorite df = new DeleteFavorite();
            df.execute(parkingid, finalMemberid);
            Toast.makeText(DetailActivity.this, "즐겨찾기에서 제거되었습니다.", Toast.LENGTH_SHORT).show();
            favorite.setBackgroundTintList(getResources().getColorStateList(R.color.color_gray));
            isFavorite = false;
          } catch (Exception e) {
            e.printStackTrace();
          }
        } else {
          try {
            InsertFavorite insertFavorite = new InsertFavorite();
            insertFavorite.execute(parkingid, finalMemberid);
            Toast.makeText(DetailActivity.this, "즐겨찾기에 추가되었습니다.", Toast.LENGTH_SHORT).show();
            favorite.setBackgroundTintList(getResources().getColorStateList(R.color.color_yellow));
            isFavorite = true;
          } catch (Exception e) {
            e.printStackTrace();
          }
          
        }
      }
    });

  } // onCreate
}
