package com.hanultari.parking.DTO;

public class PlaceDTO {

  private String name, address;
  private int mapx, mapy;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public int getMapx() {
    return mapx;
  }

  public void setMapx(int mapx) {
    this.mapx = mapx;
  }

  public int getMapy() {
    return mapy;
  }

  public void setMapy(int mapy) {
    this.mapy = mapy;
  }
}
