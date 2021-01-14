package com.hanultari.parking.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hanultari.parking.MainActivity;
import com.hanultari.parking.R;

public class AppChoiceFragment extends Fragment implements View.OnClickListener {

  public AppChoiceFragment(){};

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_appchoice, container, false);
  }



  @Override
  public void onClick(View v) {
    switch(v.getId()) {
      case R.id.selectGoogleMapFrag: {
        SharedPreferences settings = this.getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("map", "google");
        editor.apply();
        break;
      }
      case R.id.selectNaverMapFrag: {
        SharedPreferences settings = this.getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("map", "naver");
        editor.apply();
        break;
      }
      case R.id.selectKakaoMap: {
        SharedPreferences settings = this.getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("map", "kakao");
        editor.apply();
        break;
      }
    }
  }
}
