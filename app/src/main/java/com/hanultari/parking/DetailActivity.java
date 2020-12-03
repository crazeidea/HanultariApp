package com.hanultari.parking;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
  ViewGroup layout;

  String seats =
            "_UUUUUUAAAAARRRR_/"
          + "_________________/"
          + "UU__AAAARRRRR__RR/"
          + "UU__UUUAAAAAA__AA/"
          + "AA__AAAAAAAAA__AA/"
          + "AA__AARUUUURR__AA/"
          + "UU__UUUA_RRRR__AA/"
          + "AA__AAAA_RRAA__UU/"
          + "AA__AARR_UUUU__RR/"
          + "AA__UUAA_UURR__RR/"
          + "_________________/"
          + "UU_AAAAAAAUUUU_RR/"
          + "RR_AAAAAAAAAAA_AA/"
          + "AA_UUAAAAAUUUU_AA/"
          + "AA_AAAAAAUUUUU_AA/"
          + "_________________/";

  List<TextView> seatViewList = new ArrayList<>();
  int seatWidth = 70;
  int seatHeight = 100;
  int seatGaping = 5;

  int STATUS_AVAILABLE = 1;
  int STATUS_BOOKED = 2;
  int STATUS_RESERVED = 3;
  String selectedIds = "";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

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
        view.setTag(STATUS_BOOKED);
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
        view.setTag(STATUS_AVAILABLE);
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
        view.setTag(STATUS_RESERVED);
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
  }
}
