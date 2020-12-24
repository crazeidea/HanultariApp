package com.hanultari.parking.Adapter;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.hanultari.parking.AsyncTasks.CommonMethod;
import com.hanultari.parking.AsyncTasks.SelectMarker;
import com.hanultari.parking.AsyncTasks.SelectParking;
import com.hanultari.parking.DTO.ParkingDTO;
import com.hanultari.parking.R;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;

import org.json.JSONArray;
import org.json.JSONObject;

public class InfoWindowAdapter extends InfoWindow.DefaultViewAdapter{
  private static final String TAG = "InfoWindowAdapter";

  private final Context context;
  private final ViewGroup parent;
  private final String id;
  private final LatLng marker;
  private final LatLng current;

  public InfoWindowAdapter(Context context, ViewGroup parent, String id, LatLng marker, LatLng current) {
    super(context);
    this.context = context;
    this.parent = parent;
    this.id = id;
    this.marker = marker;
    this.current = current;
  }


  @NonNull
  @Override
  protected View getContentView(@NonNull InfoWindow infoWindow) {
    View view = LayoutInflater.from(context).inflate(R.layout.infowindow, parent, false);

    TextView badge = view.findViewById(R.id.infoWindowBadge);
    TextView distance = view.findViewById(R.id.infoWindowDistance);
    TextView name = view.findViewById(R.id.infoWindowName);
    TextView status = view.findViewById(R.id.infoWindowStatus);

    SelectParking selectParking = new SelectParking();
    try {
      JSONObject list = selectParking.execute(id).get();
      if (list != null ) {
        name.setText(list.getString("name"));
        status.setText("지금 " + (list.getInt("total") - list.getInt("parked")) + "자리 남아있어요.");
        int distanceValue = (int) Math.round(CommonMethod.getDistance(current, marker) * 1000);
        if(distanceValue > 1000) {
          distance.setText("현재 위치에서 " + distanceValue/1000 + "." + Math.round(distanceValue%1000/100)+ "km");
        } else {
          distance.setText("현재 위치에서 " + distanceValue + "m");
        }
        if(list.getString("paid").equals("false")) {
          badge.setText("무료");
          badge.setBackgroundResource(R.drawable.activity_detail_badge_primary);
        } else {
          badge.setText("유료");
          badge.setBackgroundResource(R.drawable.activity_detail_background);
        }



      }
    } catch (Exception e) {

    }

    return view;
  }
}
