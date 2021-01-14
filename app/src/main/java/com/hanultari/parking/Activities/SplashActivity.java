package com.hanultari.parking.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;

import com.hanultari.parking.MainActivity;
import com.hanultari.parking.R;

public class SplashActivity extends Activity {

  private static final String TAG = "SplashActivity";

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
    SharedPreferences.Editor editor = settings.edit();
    editor.putBoolean("firstInit", true);
    editor.apply();

    if(settings.getBoolean("firstInit", false)) {
      editor = settings.edit();
      editor.putBoolean("firstInit", false);
      editor.apply();

      Intent intent = new Intent(this, GuideActivity.class);
      startActivity(intent);
      finish();

    } else {
      Intent intent = new Intent(this, MainActivity.class);
      startActivity(intent);
      finish();
    }
  }

}
