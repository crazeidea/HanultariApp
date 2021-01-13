package com.hanultari.parking.Adapter;

import android.content.Context;
import android.content.Intent;
import android.location.GpsSatellite;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanultari.parking.Activities.SearchActivity;
import com.hanultari.parking.DTO.ParkingDTO;
import com.hanultari.parking.DTO.PlaceDTO;
import com.hanultari.parking.MainActivity;
import com.hanultari.parking.R;
import com.hanultari.parking.CommonMethod;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.Tm128;

import java.util.ArrayList;
import java.util.List;

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private static final String TAG = "SearchRecyclerViewAdapt";

  private static final int TYPE_PLACE = 1;
  private static final int TYPE_PARKING = 2;
  private final Context context;
  private final ArrayList<PlaceDTO> places;
  private final ArrayList<ParkingDTO> parkings;
  private final LatLng currentLocation;
  private OnItemClickListener listener = null;

  public SearchRecyclerViewAdapter(Context context, ArrayList<PlaceDTO> places, ArrayList<ParkingDTO> parkings, LatLng currentLocation) {
    this.context = context;
    this.places = places;
    this.parkings = parkings;
    this.currentLocation = currentLocation;

  }

  public interface OnItemClickListener {
    void onItemClick(View v, int position);
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    this.listener = listener;
  }



  class ParkingViewHoder extends RecyclerView.ViewHolder {
    private final TextView parkingLotName;
    private final TextView parkingLotFare;
    private final TextView parkingLotDistance;

    public ParkingViewHoder(@NonNull View itemView) {
      super(itemView);
      parkingLotName = itemView.findViewById(R.id.parkingLotName);
      parkingLotFare = itemView.findViewById(R.id.parkingLotFare);
      parkingLotDistance = itemView.findViewById(R.id.parkingLotDistance);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          int pos = getAdapterPosition();
          if (pos != RecyclerView.NO_POSITION) {
            if(listener != null) {
              listener.onItemClick(v, pos);
            }
          }
        }
      });
    }

    private void setParkingDetail(ParkingDTO dto) {
      parkingLotName.setText(dto.getName());
      if(dto.getPaid() == 0){
        parkingLotFare.setText("이용요금 무료");
      } else {
        parkingLotFare.setText(dto.getFare() + " / " + dto.getAdded_fare());
      }
      int distance = (int) CommonMethod.getDistance(currentLocation, new LatLng(dto.getLat(), dto.getLng()));
      if(distance > 1000) parkingLotDistance.setText((int) distance / 1000 + "." + Math.round(distance % 1000/100) + "km");
      else parkingLotDistance.setText(distance + "m");
    }
  }

  class PlaceViewHolder extends RecyclerView.ViewHolder {
    private final TextView placeName;
    private final TextView placeAddress;

    public PlaceViewHolder(@NonNull View itemView) {
      super(itemView);
      placeName = itemView.findViewById(R.id.placeName);
      placeAddress = itemView.findViewById(R.id.placeAddress);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          int pos = getAdapterPosition();
          if (pos != RecyclerView.NO_POSITION) {
            if(listener != null) {
              listener.onItemClick(v, pos);
            }
          }
        }
      });
    }

    private void setPlaceDetail(PlaceDTO dto) {
      placeName.setText(dto.getName());
      placeAddress.setText(dto.getAddress());
    }
  }



  @Override
  public int getItemViewType(int position) {
    if (position < places.size()) return TYPE_PLACE;
    else return TYPE_PARKING;
  }


  @Override
  public int getItemCount() {
    return (places.size() + parkings.size());
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view;
    if (viewType == TYPE_PLACE) {
      view = LayoutInflater.from(context).inflate(R.layout.search_item_place, parent, false);
      return new PlaceViewHolder(view);
    } else {
      view = LayoutInflater.from(context).inflate(R.layout.search_item_parking, parent, false);
      return new ParkingViewHoder(view);
    }

  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    if(getItemViewType(position) == TYPE_PLACE) {
      ((PlaceViewHolder)holder).setPlaceDetail(places.get(position));
      holder.itemView.setTag(R.string.ITEM_TYPE, "place");
      holder.itemView.setTag(R.string.ITEM_LAT, new Tm128(places.get(position).getMapx(), places.get(position).getMapy()).toLatLng().latitude);
      holder.itemView.setTag(R.string.ITEM_LNG, new Tm128(places.get(position).getMapx(), places.get(position).getMapy()).toLatLng().longitude);
      holder.itemView.setTag(R.string.ITEM_TITLE, places.get(position).getAddress());
    } else {
      ((ParkingViewHoder)holder).setParkingDetail(parkings.get(position - places.size()));
      holder.itemView.setTag(R.string.ITEM_TYPE, "parking");
      holder.itemView.setTag(R.string.ITEM_LAT, parkings.get(position - places.size()).getLat());
      holder.itemView.setTag(R.string.ITEM_LNG, parkings.get(position - places.size()).getLng());
      holder.itemView.setTag(R.string.ITEM_TITLE, parkings.get(position - places.size()).getId());
    }
  }
}

