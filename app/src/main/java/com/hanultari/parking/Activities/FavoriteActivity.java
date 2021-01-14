package com.hanultari.parking.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hanultari.parking.AsyncTasks.SelectFavorite;
import com.hanultari.parking.R;

public class FavoriteActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_favorite);

    SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
  }
}
