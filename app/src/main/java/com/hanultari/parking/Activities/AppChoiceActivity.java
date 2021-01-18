package com.hanultari.parking.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hanultari.parking.R;

public class AppChoiceActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_appchoice);


    ImageButton imageButtonBack = findViewById(R.id.imageButtonBack);

    imageButtonBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    LinearLayout selectNaverMap = findViewById(R.id.selectNaverMap);
    LinearLayout selectKakaoMap = findViewById(R.id.selectKakaoMap);

    selectNaverMap.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("map", "naver");
        editor.apply();
        editor.commit();
        itemSelected(v);
      }
    });

    selectKakaoMap.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("map", "kakao");
        editor.apply();
        editor.commit();
        itemSelected(v);
      }
    });
  }

  @Override
  public void onBackPressed() {
    itemSelected(getWindow().getDecorView().getRootView());
  }

  public void itemSelected(View v){
    Intent refresh = new Intent(this, SettingActivity.class);
    startActivity(refresh);
    this.finish();

  }
}
