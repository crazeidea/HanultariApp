package com.hanultari.parking.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hanultari.parking.Fragment.NoticeDetailFragment;
import com.hanultari.parking.Fragment.NoticeListFragment;
import com.hanultari.parking.R;

public class NoticeActivity extends AppCompatActivity {
  private static final String TAG = "NoticeActivity";



  FragmentManager fm;
  FragmentTransaction transaction;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_notice);

    fm = getSupportFragmentManager();
    transaction = fm.beginTransaction();
    transaction.add(R.id.noticeFragContainer, new NoticeListFragment());
    transaction.commitAllowingStateLoss();

    ImageButton btnBack = findViewById(R.id.btnBackNotice);
    btnBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        NoticeDetailFragment detail = (NoticeDetailFragment) getSupportFragmentManager().findFragmentByTag("Detail");
        if(detail != null && detail.isVisible()) {
          Log.d(TAG, "onBackPressed: clicked");
          FragmentManager fm = getSupportFragmentManager();
          FragmentTransaction transaction = fm.beginTransaction();
          transaction.replace(R.id.noticeFragContainer, new NoticeListFragment(), "List").commit();
        } else {
          finish();
        }
      }
    });
  }

  @Override
  public void onBackPressed() {
    NoticeDetailFragment detail = (NoticeDetailFragment) getSupportFragmentManager().findFragmentByTag("Detail");
    if(detail != null && detail.isVisible()) {
      Log.d(TAG, "onBackPressed: clicked");
      FragmentManager fm = getSupportFragmentManager();
      FragmentTransaction transaction = fm.beginTransaction();
      transaction.replace(R.id.noticeFragContainer, new NoticeListFragment(), "List").commit();
    } else {
      finish();
    }
  }



}
