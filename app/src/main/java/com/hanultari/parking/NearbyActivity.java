package com.hanultari.parking;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NearbyActivity extends AppCompatActivity {

  RecyclerView recyclerView;
  ArrayList<ParkingDTO> dtos;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recyclerview_main);

    dtos = new ArrayList<>();

    recyclerView = findViewById(R.id.recyclerView);

    LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
    recyclerView.setLayoutManager(layoutManager);

    com.hanultari.parking.ParkingAdapter adapter = new com.hanultari.parking.ParkingAdapter(this, dtos);


    recyclerView.setAdapter(adapter);
  }
}