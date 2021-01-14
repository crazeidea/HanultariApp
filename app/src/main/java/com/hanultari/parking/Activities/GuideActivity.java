package com.hanultari.parking.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hanultari.parking.Fragment.AppChoiceFragment;
import com.hanultari.parking.Fragment.ClosingFragment;
import com.hanultari.parking.Fragment.WelcomeFragment;
import com.hanultari.parking.MainActivity;
import com.hanultari.parking.R;

public class GuideActivity extends AppCompatActivity {
  private static final String TAG = "GuideActivity";

  FragmentManager fm;
  FragmentTransaction transaction;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_guide);
    fm = getSupportFragmentManager();
    transaction = fm.beginTransaction();
    transaction.add(R.id.fragmentContainer, new WelcomeFragment());
    transaction.commitAllowingStateLoss();
  }

  public void clickHandler (View view) {
    transaction = fm.beginTransaction();
    if(view.getId() == R.id.textNext) {
      transaction.setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_close_exit);
      transaction.replace(R.id.fragmentContainer, new AppChoiceFragment());
      transaction.commitAllowingStateLoss();
    } else if (view.getId() == R.id.selectGoogleMapFrag || view.getId() == R.id.selectKakaoMapFrag || view.getId() == R.id.selectNaverMapFrag) {
      Log.d(TAG, "clickHandler: App Clicked");
      transaction.setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_close_exit);
      transaction.replace(R.id.fragmentContainer, new ClosingFragment());
      transaction.commitAllowingStateLoss();
    } else if (view.getId() == R.id.textStart) {
      Intent intent = new Intent(this, MainActivity.class);
      startActivity(intent);
      finish();
    }
  }
}
