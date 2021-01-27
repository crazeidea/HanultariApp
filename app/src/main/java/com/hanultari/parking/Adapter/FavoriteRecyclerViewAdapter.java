package com.hanultari.parking.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanultari.parking.DTO.ParkingDTO;
import com.hanultari.parking.R;

import java.util.ArrayList;

public class FavoriteRecyclerViewAdapter extends RecyclerView.Adapter<FavoriteRecyclerViewAdapter.ViewHolder> {
  private static final String TAG = "FavoriteRecyclerViewAda";

  private final Context context;
  private ArrayList<ParkingDTO> dtos;
  private OnItemClickListener listener = null;

  public FavoriteRecyclerViewAdapter(Context context, ArrayList<ParkingDTO> dtos) {
    this.context = context;
    this.dtos = dtos;
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    private final LinearLayout badges;
    private final TextView name;
    private final TextView fare;
    private final TextView distance;


    public ViewHolder(View itemView) {
      super(itemView) ;
      badges = itemView.findViewById(R.id.badgeHolderDetail);
      name = itemView.findViewById(R.id.parkingNameDetail);
      fare = itemView.findViewById(R.id.parkingFareDetail);
      distance = itemView.findViewById(R.id.parkingDistanceDetail);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          int pos = getAdapterPosition();
          if (pos != RecyclerView.NO_POSITION) {
            if (listener != null) {
              listener.onItemClick(v, pos);
            }
          }
        }
      });
    }
  }

  public interface OnItemClickListener {
    void onItemClick(View v, int position);
  }

  public void setOnItemClickListener(OnItemClickListener listener) {this.listener = listener;}

  @NonNull
  @Override
  public FavoriteRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.search_item_parking_detail, parent, false);
    return new ViewHolder(view);
  }


  @Override
  public void onBindViewHolder(@NonNull FavoriteRecyclerViewAdapter.ViewHolder holder, int position) {
    Log.d(TAG, "onBindViewHolder: " + dtos);
    String name = dtos.get(position).getName();
    int fare = dtos.get(position).getFare();
    int distance = dtos.get(position).getDistance();
    holder.name.setText(name);
    holder.fare.setText(fare + "원");
    if(distance > 1000) {
      holder.distance.setText(distance/1000 + "." + Math.round(distance%1000/100) + "km");
    } else {
      holder.distance.setText(distance + "m");
    }
    holder.itemView.setTag(dtos.get(position));
  }

  @Override
  public int getItemCount() {
    return dtos.size();
  }
}
