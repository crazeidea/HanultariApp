package com.hanultari.parking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class MainActivity extends AppCompatActivity {

  SupportMapFragment mapFragment;
  GoogleMap map;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

    mapFragment.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        try {
          map.setMyLocationEnabled(true);
        }catch (SecurityException e){

        }

      }
    });
    MapsInitializer.initialize(this);
  }
}