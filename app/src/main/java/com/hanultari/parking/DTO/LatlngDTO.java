package com.hanultari.parking.DTO;

public class LatlngDTO {
  private int id;
  private double lat, lng;

  public LatlngDTO(int id, double lat, double lng) {
    this.id = id;
    this.lat = lat;
    this.lng = lng;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public double getLat() {
    return lat;
  }

  public void setLat(float lat) {
    this.lat = lat;
  }

  public double getLng() {
    return lng;
  }

  public void setLng(float lng) {
    this.lng = lng;
  }
}
