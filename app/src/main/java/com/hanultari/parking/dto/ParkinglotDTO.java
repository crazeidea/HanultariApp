package com.hanultari.parking.dto;

public class ParkinglotDTO {

  private String id, name, addr, prev_addr, manager, contact, oper_days, start_time, begin_time, payment, img_path, layout;
  private float lat, lon;
  private int parked, total, fare, added_fare, duration, duration_interval;
  private boolean indoor, smallcar, woman, disabled, paid, subscription;

  public ParkinglotDTO() {};

  public ParkinglotDTO(String id, String name, String addr, String prev_addr, String manager, String contact, String oper_days, String start_time, String begin_time, String payment, String img_path, String layout, float lat, float lon, int parked, int total, int fare, int added_fare, int duration, int duration_interval, boolean indoor, boolean smallcar, boolean woman, boolean disabled, boolean paid, boolean subscription) {
    this.id = id;
    this.name = name;
    this.addr = addr;
    this.prev_addr = prev_addr;
    this.manager = manager;
    this.contact = contact;
    this.oper_days = oper_days;
    this.start_time = start_time;
    this.begin_time = begin_time;
    this.payment = payment;
    this.img_path = img_path;
    this.layout = layout;
    this.lat = lat;
    this.lon = lon;
    this.parked = parked;
    this.total = total;
    this.fare = fare;
    this.added_fare = added_fare;
    this.duration = duration;
    this.duration_interval = duration_interval;
    this.indoor = indoor;
    this.smallcar = smallcar;
    this.woman = woman;
    this.disabled = disabled;
    this.paid = paid;
    this.subscription = subscription;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddr() {
    return addr;
  }

  public void setAddr(String addr) {
    this.addr = addr;
  }

  public String getPrev_addr() {
    return prev_addr;
  }

  public void setPrev_addr(String prev_addr) {
    this.prev_addr = prev_addr;
  }

  public String getManager() {
    return manager;
  }

  public void setManager(String manager) {
    this.manager = manager;
  }

  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  public String getOper_days() {
    return oper_days;
  }

  public void setOper_days(String oper_days) {
    this.oper_days = oper_days;
  }

  public String getStart_time() {
    return start_time;
  }

  public void setStart_time(String start_time) {
    this.start_time = start_time;
  }

  public String getBegin_time() {
    return begin_time;
  }

  public void setBegin_time(String begin_time) {
    this.begin_time = begin_time;
  }

  public String getPayment() {
    return payment;
  }

  public void setPayment(String payment) {
    this.payment = payment;
  }

  public String getImg_path() {
    return img_path;
  }

  public void setImg_path(String img_path) {
    this.img_path = img_path;
  }

  public String getLayout() {
    return layout;
  }

  public void setLayout(String layout) {
    this.layout = layout;
  }

  public float getLat() {
    return lat;
  }

  public void setLat(float lat) {
    this.lat = lat;
  }

  public float getLon() {
    return lon;
  }

  public void setLon(float lon) {
    this.lon = lon;
  }

  public int getParked() {
    return parked;
  }

  public void setParked(int parked) {
    this.parked = parked;
  }

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public int getFare() {
    return fare;
  }

  public void setFare(int fare) {
    this.fare = fare;
  }

  public int getAdded_fare() {
    return added_fare;
  }

  public void setAdded_fare(int added_fare) {
    this.added_fare = added_fare;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public int getDuration_interval() {
    return duration_interval;
  }

  public void setDuration_interval(int duration_interval) {
    this.duration_interval = duration_interval;
  }

  public boolean isIndoor() {
    return indoor;
  }

  public void setIndoor(boolean indoor) {
    this.indoor = indoor;
  }

  public boolean isSmallcar() {
    return smallcar;
  }

  public void setSmallcar(boolean smallcar) {
    this.smallcar = smallcar;
  }

  public boolean isWoman() {
    return woman;
  }

  public void setWoman(boolean woman) {
    this.woman = woman;
  }

  public boolean isDisabled() {
    return disabled;
  }

  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
  }

  public boolean isPaid() {
    return paid;
  }

  public void setPaid(boolean paid) {
    this.paid = paid;
  }

  public boolean isSubscription() {
    return subscription;
  }

  public void setSubscription(boolean subscription) {
    this.subscription = subscription;
  }
}
