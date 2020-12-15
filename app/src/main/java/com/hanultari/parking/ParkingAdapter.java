package com.hanultari.parking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanultari.parking.DTO.ParkingDTO;

import java.util.ArrayList;

public class ParkingAdapter extends RecyclerView.Adapter<ParkingAdapter.ViewHolder> {

    Context context;
    ArrayList<ParkingDTO> dtos;
    LayoutInflater inflater;

    public ParkingAdapter(Context context, ArrayList<ParkingDTO> dtos) {
        this.context = context;
        this.dtos = dtos;
    }

    public void addDto(ParkingDTO dto) {dtos.add(dto);}

    public ParkingDTO getItem(int position) {return dtos.get(position);}


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.activity_recyclerview, parent, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParkingDTO dto = dtos.get(position);
        holder.setItem(dto);
    }

    @Override
    public int getItemCount() {
        return dtos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView rv_Charge, rv_Park, rv_Distance;
        ImageView rv_Img;

        public ViewHolder(@NonNull View itemView, com.hanultari.parking.ParkingAdapter parkingAdapter) {
            super(itemView);
        }

        public void setItem(ParkingDTO dto) {
        }
    }
}
