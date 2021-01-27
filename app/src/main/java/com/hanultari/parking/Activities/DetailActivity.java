package com.hanultari.parking.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import com.hanultari.parking.AsyncTasks.DeleteFavorite;
import com.hanultari.parking.AsyncTasks.InsertFavorite;
import com.hanultari.parking.AsyncTasks.SelectParking;
import com.hanultari.parking.AsyncTasks.CheckFavorite;
import com.hanultari.parking.AsyncTasks.SelectReview;
import com.hanultari.parking.AsyncTasks.SelectUser;
import com.hanultari.parking.Custom.CustomScrollView;
import com.hanultari.parking.DTO.ReviewDTO;
import com.hanultari.parking.R;
import com.naver.maps.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
  private Menu menu;

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

    int parkingid = getIntent().getIntExtra("id", 0);

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
    Button share = findViewById(R.id.btnShare);
    Button call = findViewById(R.id.detailCall);
    Button nav = findViewById(R.id.detailNav);
    WebView pano = findViewById(R.id.detailPanorama);
    TextView label = findViewById(R.id.detailLabel);
    LinearLayout badges = findViewById(R.id.detailLabels);
    LinearLayout reviewLinear = findViewById(R.id.linearReviews);


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
    SelectReview selectReview = new SelectReview();
    LatLng currentLocation = intent.getParcelableExtra("currentLocation");
    ArrayList<ReviewDTO> array = new ArrayList<>();
    try {
      JSONObject parking = selectParking.execute(parkingid).get();
      JSONArray reviews = selectReview.execute(parkingid).get();
      for (int i = 0; i < reviews.length(); i++) {
        JSONObject review = (JSONObject) reviews.get(i);
        ReviewDTO dto = new ReviewDTO();
        dto.setMember_id(review.getInt("member_id"));
        dto.setRating(review.getInt("rating"));
        dto.setContent(review.getString("content"));
        array.add(dto);
      }
      Log.d(TAG, "onCreate: " + array.size());
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

      badges.setGravity(Gravity.CENTER_VERTICAL);
      if(parking.getBoolean("payment_cash")) {
        ImageView cash = new ImageView(this);
        cash.setImageResource(R.drawable.ic_cash);
        TextView cashText = new TextView(this);
        cashText.setText("현금 결제");
        cashText.setTextColor(getResources().getColor(R.color.black));
        badges.addView(cash);
        badges.addView(cashText);
      }
      if (parking.getBoolean("payment_card")) {
        ImageView card = new ImageView(this);
        card.setImageResource(R.drawable.ic_card);
        TextView cardText = new TextView(this);
        cardText.setText("카드 결제");
        cardText.setTextColor(getResources().getColor(R.color.black));
        badges.addView(card);
        badges.addView(cardText);
      }
      if (parking.getBoolean("payment_machine")) {
        ImageView machine = new ImageView(this);
        machine.setImageResource(R.drawable.ic_machine);
        TextView machineText = new TextView(this);
        machineText.setText("무인결제기");
        machineText.setTextColor(getResources().getColor(R.color.black));
        badges.addView(machine);
        badges.addView(machineText);
      }

      if(array.size() > 0) {
        for (int i = 0; i < array.size(); i++) {
          if (i < 2) {
            LinearLayout review = new LinearLayout(this);
            review.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0, 0, 8, 0);
            TextView nickname = new TextView(this);
            nickname.setLayoutParams(layoutParams);
            nickname.setTextColor(getResources().getColor(R.color.black));
            try {
              SelectUser su = new SelectUser();
              JSONObject object = su.execute(array.get(i).getMember_id()).get();
              nickname.setText(object.getString("nickname"));
            } catch (Exception e) {
              e.printStackTrace();
            }
            TextView rating = new TextView(this);
            rating.setText("★" + String.valueOf(array.get(i).getRating()));
            rating.setLayoutParams(layoutParams);
            rating.setTextColor(getResources().getColor(R.color.yellow));
            TextView content = new TextView(this);
            content.setText(array.get(i).getContent());
            content.setLayoutParams(layoutParams);
            content.setTextColor(getResources().getColor(R.color.black));

            review.addView(nickname);
            review.addView(rating);
            review.addView(content);
            reviewLinear.addView(review);
          }
        }
      } else {
        TextView message = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        message.setLayoutParams(layoutParams);
        message.setText("등록된 이용 후기가 없습니다.");
        message.setTextColor(getResources().getColor(R.color.black));
        message.setTextSize(16);
        reviewLinear.addView(message);
      }

      Uri callnumber = Uri.parse("tel:" + number);
      call.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent callIntent = new Intent(Intent.ACTION_DIAL);
          callIntent.setData(callnumber);
          startActivity(callIntent);
        }
      });

      share.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent sendIntent = new Intent();
          sendIntent.setAction(Intent.ACTION_SEND);
          sendIntent.putExtra(Intent.EXTRA_TEXT, addr.getText().toString());
          sendIntent.setType("text/plain");

          Intent shareIntent = Intent.createChooser(sendIntent, null);
          startActivity(shareIntent);
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
              String url = "nmap://navigation?";
              String sname = URLEncoder.encode("현재 위치", "UTF-8");
              String dname = "dname" + URLEncoder.encode(parking.getString("name"), "EUC-KR") + "&";
              String appname = "appname=com.hanultari.app";
              StringBuffer stringBuffer = new StringBuffer();
              stringBuffer.append(url + dlat + dlng + dname + appname);
              String uri = stringBuffer.toString();
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

      //주차장 레이아웃 구현
      String seats = parking.getString("layout");
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
        } else if (seats.charAt(index) == 'C') { // 장애인 좌석
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
        } else if (seats.charAt(index) == 'E') { // 여성 좌석
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
        } else if (seats.charAt(index) == 'N' || seats.charAt(index) == 'B' || seats.charAt(index) == 'D' || seats.charAt(index) == 'F') { // 주차된 좌석
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
    if(loginDTO != null) {
      memberid = loginDTO.getId();
      try {
        isFavorite = checkFavorite.execute(parkingid, memberid).get();
        if (isFavorite) {
          favorite.setBackgroundTintList(getResources().getColorStateList(R.color.color_yellow));
        } else {
          favorite.setBackgroundTintList(getResources().getColorStateList(R.color.color_gray));
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else if (loginDTO == null) {
      favorite.setVisibility(View.INVISIBLE);
    }
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
