package com.hanultari.parking.DTO;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanultari.parking.R;

import java.util.ArrayList;

public class LocateNearRecyclerViewAdapter extends RecyclerView.Adapter<LocateNearRecyclerViewAdapter.ViewHolder> {

  private final Context context;
  private final ArrayList<ParkingDTO> dtos;
  private OnItemClickListener listener = null;


  public LocateNearRecyclerViewAdapter(Context context, ArrayList<ParkingDTO> dtos) {
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
      badges = itemView.findViewById(R.id.badgeHolder);
      name = itemView.findViewById(R.id.parkingLotName);
      fare = itemView.findViewById(R.id.parkingLotFare);
      distance = itemView.findViewById(R.id.parkingLotDistance);

    }
  }


  @NonNull
  @Override
  public LocateNearRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.search_item_parking_detail, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull LocateNearRecyclerViewAdapter.ViewHolder holder, int position) {
    holder.name.setText(dtos.get(position).getName());
    holder.fare.setText(dtos.get(position).getFare());
    holder.distance.setText(dtos.get(position).getDistance());
  }

  @Override
  public int getItemCount() {
    return dtos.size();
  }



}