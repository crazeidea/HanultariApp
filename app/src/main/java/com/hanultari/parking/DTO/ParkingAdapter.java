package com.hanultari.parking.DTO;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanultari.parking.R;

import java.util.ArrayList;

public class ParkingAdapter extends RecyclerView.Adapter<ParkingAdapter.ViewHolder> {

  private ArrayList<ParkingDTO> dtos = null;

  public class ViewHolder extends RecyclerView.ViewHolder {
    LinearLayout badges;
    TextView name;
    TextView fare;
    TextView distance;
    ImageView image;


    ViewHolder(View itemView) {
      super(itemView) ;

      // 뷰 객체에 대한 참조. (hold strong reference)
       badges = itemView.findViewById(R.id.badgeHolder);
       name = itemView.findViewById(R.id.parkingLotName);
       fare = itemView.findViewById(R.id.parkingLotFare);
       distance = itemView.findViewById(R.id.parkingLotDistance);
       image = itemView.findViewById(R.id.parkingLotImage);

    }
  }

  public ParkingAdapter(ArrayList<ParkingDTO> list) {
    dtos = list;
  }

  @NonNull
  @Override
  public ParkingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    View view = inflater.inflate(R.layout.search_view, parent, false);
    ParkingAdapter.ViewHolder vh = new ParkingAdapter.ViewHolder(view);

    return vh;
  }

  @Override
  public void onBindViewHolder(@NonNull ParkingAdapter.ViewHolder holder, int position) {
    String text = dtos.get(position).getName();
    int fare = dtos.get(position).getFare();
    String distance = dtos.get(position).getDistance();
    holder.name.setText(text);
    holder.fare.setText(fare + "원");
    holder.distance.setText("현재 위치로부터 " + distance);

  }

  @Override
  public int getItemCount() {
    return dtos.size();
  }
}
