package com.hanultari.parking;

import java.io.Serializable;

public class ParkingDTO implements Serializable {
    String charge, park, distance;
    int resId;

    public ParkingDTO(String charge, String park, String distance, int resId) {
        this.charge = charge;
        this.park = park;
        this.distance = distance;
        this.resId = resId;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getPark() {
        return park;
    }

    public void setPark(String park) {
        this.park = park;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
