package com.hanultari.parking.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.hanultari.parking.DTO.ParkingDTO;
import com.hanultari.parking.R;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
  ViewGroup layout;

  ParkingDTO dto = new ParkingDTO();


  String seats =
            "_ABBAABBB_/" +
            "__________/" +
            "A_AABBBAA_/" +
            "B_AAABBBA_/" +
            "__________/";

  private static final String TAG = "DetailActivity";



  List<TextView> seatViewList = new ArrayList<>();
  int seatWidth = 70;
  int seatHeight = 100;
  int seatGaping = 5;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

    ArrayList<ParkingDTO> dtos = new ArrayList<>();



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

  }
}
