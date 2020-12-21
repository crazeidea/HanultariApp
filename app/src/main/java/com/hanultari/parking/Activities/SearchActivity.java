package com.hanultari.parking.Activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hanultari.parking.DTO.ParkingAdapter;
import com.hanultari.parking.DTO.ParkingDTO;
import com.hanultari.parking.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

  ArrayList<ParkingDTO> dtos;
  RecyclerView recyclerView;


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);

    ArrayList<ParkingDTO> list = new ArrayList<>();
    ParkingDTO e = new ParkingDTO();
    e.setName("TEST1");
    e.setFare(1000);
    e.setDistance("1000m");
    list.add(e);
    list.add(e);
    list.add(e);

    RecyclerView recyclerView = findViewById(R.id.searchRecyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    ParkingAdapter adapter = new ParkingAdapter(list);
    recyclerView.setAdapter(adapter);

  } // onCreate()


}
