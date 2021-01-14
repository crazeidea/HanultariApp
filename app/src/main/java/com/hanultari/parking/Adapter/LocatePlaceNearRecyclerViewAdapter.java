package com.hanultari.parking.Adapter;

import android.content.Context;
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

public class LocatePlaceNearRecyclerViewAdapter extends RecyclerView.Adapter<LocatePlaceNearRecyclerViewAdapter.ViewHolder> {

  private final Context context;
  private final ArrayList<ParkingDTO> dtos;
  private OnItemClickListener listener = null;


  public LocatePlaceNearRecyclerViewAdapter(Context context, ArrayList<ParkingDTO> dtos) {
    this.context = context;
    this.dtos = dtos;
  }

  public interface OnItemClickListener {
    void onItemClick(View v, int position);
  }

  public void setOnItemClickListener(OnItemClickListener listener) {this.listener = listener;}


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
    }
  }


  @NonNull
  @Override
  public LocatePlaceNearRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.search_item_parking_detail, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull LocatePlaceNearRecyclerViewAdapter.ViewHolder holder, int position) {
    String name = dtos.get(position).getName();
    int fare = dtos.get(position).getFare();
    String distance = dtos.get(position).getDistance();
    holder.name.setText(name);
    holder.fare.setText(fare + "원");
    holder.distance.setText(distance);
  }

  @Override
  public int getItemCount() {
    return dtos.size();
  }



}