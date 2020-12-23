package com.hanultari.parking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hanultari.parking.AsyncTasks.SelectMarker;
import com.hanultari.parking.AsyncTasks.SelectParking;
import com.hanultari.parking.DTO.ParkingDTO;
import com.hanultari.parking.R;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;

import org.json.JSONArray;
import org.json.JSONObject;

public class InfoWindowAdapter extends InfoWindow.DefaultViewAdapter{

  private final Context context;
  private final ViewGroup parent;
  private final String id;

  public InfoWindowAdapter(Context context, ViewGroup parent, String id) {
    super(context);
    this.context = context;
    this.parent = parent;
    this.id = id;
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
        status.setText("지금 " + list.getInt("parked") + "자리 남아있어요.");

      }
    } catch (Exception e) {

    }

    return view;
  }
}
