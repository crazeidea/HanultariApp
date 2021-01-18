package com.hanultari.parking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hanultari.parking.AsyncTasks.SelectParking;
import com.hanultari.parking.CommonMethod;
import com.hanultari.parking.R;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.overlay.InfoWindow;

import org.json.JSONObject;

public class PlaceInfoWindowAdapter extends InfoWindow.DefaultViewAdapter{
  private static final String TAG = "InfoWindowAdapter";

  private final Context context;
  private final ViewGroup parent;
  private final String address;
  private final String name;

  public PlaceInfoWindowAdapter(Context context, ViewGroup parent, String address, String name) {
    super(context);
    this.context = context;
    this.parent = parent;
    this.address = address;
    this.name = name;
  }


  @NonNull
  @Override
  protected View getContentView(@NonNull InfoWindow infoWindow) {
    View view = LayoutInflater.from(context).inflate(R.layout.infowindow_place, parent, false);

    TextView namefield = view.findViewById(R.id.infoWindowName);
    namefield.setText(name);
    TextView addr = view.findViewById(R.id.infoWindowAddr);
    addr.setText(address);

    return view;
  }
}
