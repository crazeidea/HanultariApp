package com.hanultari.parking.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hanultari.parking.R;

public class SettingActivity extends AppCompatActivity {
  private static final String TAG = "SettingActivity";


  @SuppressLint("MissingSuperCall")
  @Override
  protected void onNewIntent(Intent intent) {
    recreate();
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setting);

    ImageButton imageButtonBack = findViewById(R.id.btnBackFavorite);
    Switch switchWoman = findViewById(R.id.switchWoman);
    Switch switchDisabled = findViewById(R.id.switchDisabled);
    Switch switchSmall = findViewById(R.id.switchSmall);
    LinearLayout selectApp = findViewById(R.id.selectApp);
    TextView mapAppName = findViewById(R.id.tvMapAppSubtitle);
    ImageButton appIcon = findViewById(R.id.btnSelectMapapp);


    switchWoman.setOnCheckedChangeListener(new womanSwitchListener());
    switchDisabled.setOnCheckedChangeListener(new disabledSwitchListener());
    switchSmall.setOnCheckedChangeListener(new smallSwitchListener());

    SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
    if (settings.getBoolean("isWoman", false)) {
      switchWoman.setChecked(true);
    } else {
      switchWoman.setChecked(false);
    }
    if (settings.getBoolean("isDisabled", false)) {
      switchDisabled.setChecked(true);
    } else {
      switchDisabled.setChecked(false);
    }
    if (settings.getBoolean("isSmall", false)) {
      switchSmall.setChecked(true);
    } else {
      switchSmall.setChecked(false);
    }

    if(settings.getString("map", "").equals("naver")) {
      mapAppName.setText("네이버 지도");
      appIcon.setImageResource(R.drawable.navermap);
    } else if (settings.getString("map", "").equals("kakao")) {
      mapAppName.setText("카카오맵");
      appIcon.setImageResource(R.drawable.kakaomap);
    }


    imageButtonBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    selectApp.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(SettingActivity.this, AppChoiceActivity.class);
        finish();
        startActivity(intent);
      }
    });

    LinearLayout divWoman = findViewById(R.id.divWoman);
    divWoman.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        switchWoman.toggle();
      }
    });

    LinearLayout divDisabled = findViewById(R.id.divDisabled);
    divDisabled.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        switchDisabled.toggle();
      }
    });

    LinearLayout divSmall = findViewById(R.id.divSmall);
    divSmall.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        switchSmall.toggle();
      }
    });


  }

  class womanSwitchListener implements CompoundButton.OnCheckedChangeListener {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
      SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
      SharedPreferences.Editor editor = settings.edit();
      editor.putBoolean("isWoman", isChecked);
      editor.apply();
      editor.commit();
      Log.d(TAG, "onCheckedChanged: " + settings.getBoolean("isWoman", false));
    }
  }

  class disabledSwitchListener implements  CompoundButton.OnCheckedChangeListener {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
      SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
      SharedPreferences.Editor editor = settings.edit();
      editor.putBoolean("isDisabled", isChecked);
      editor.apply();
      editor.commit();
      Log.d(TAG, "onCheckedChanged: " + settings.getBoolean("isDisabled", false));
    }
  }

  class smallSwitchListener implements CompoundButton.OnCheckedChangeListener {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
      SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
      SharedPreferences.Editor editor = settings.edit();
      editor.putBoolean("isSmall", isChecked);
      editor.apply();
      editor.commit();
      Log.d(TAG, "onCheckedChanged: " + settings.getBoolean("isSmall", false));
    }
  }



  @Override
  public void onBackPressed() {
    super.onBackPressed();
  }
}
