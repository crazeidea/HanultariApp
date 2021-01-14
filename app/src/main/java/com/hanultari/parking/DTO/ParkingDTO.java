package com.hanultari.parking.DTO;

public class ParkingDTO implements Comparable<ParkingDTO> {
  private String  name, addr, prev_addr, manager, contact, oper_mon, oper_tue, oper_wed, oper_thu, oper_fri, oper_sat, oper_sun, start_time, end_time, payment_cash, payment_card, payment_machine, layout, indoor, smallcar, woman, disabled,  distance;
  private float lat, lng;
  private int id, parked, total, fare, added_fare, duration, duration_interval;
  private boolean paid;

  public ParkingDTO() {}

  public ParkingDTO(int id, String name, String addr, String prev_addr, String manager, String contact, String oper_mon, String oper_tue, String oper_wed, String oper_thu, String oper_fri, String oper_sat, String oper_sun, String start_time, String end_time, String payment_cash, String payment_card, String payment_machine, String layout, String indoor, String smallcar, String woman, String disabled, boolean paid, String distance, float lat, float lng, int parked, int total, int fare, int added_fare, int duration, int duration_interval) {
    this.id = id;
    this.name = name;
    this.addr = addr;
    this.prev_addr = prev_addr;
    this.manager = manager;
    this.contact = contact;
    this.oper_mon = oper_mon;
    this.oper_tue = oper_tue;
    this.oper_wed = oper_wed;
    this.oper_thu = oper_thu;
    this.oper_fri = oper_fri;
    this.oper_sat = oper_sat;
    this.oper_sun = oper_sun;
    this.start_time = start_time;
    this.end_time = end_time;
    this.payment_cash = payment_cash;
    this.payment_card = payment_card;
    this.payment_machine = payment_machine;
    this.layout = layout;
    this.indoor = indoor;
    this.smallcar = smallcar;
    this.woman = woman;
    this.disabled = disabled;
    this.paid = paid;
    this.distance = distance;
    this.lat = lat;
    this.lng = lng;
    this.parked = parked;
    this.total = total;
    this.fare = fare;
    this.added_fare = added_fare;
    this.duration = duration;
    this.duration_interval = duration_interval;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
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

  public String getOper_mon() {
    return oper_mon;
  }

  public void setOper_mon(String oper_mon) {
    this.oper_mon = oper_mon;
  }

  public String getOper_tue() {
    return oper_tue;
  }

  public void setOper_tue(String oper_tue) {
    this.oper_tue = oper_tue;
  }

  public String getOper_wed() {
    return oper_wed;
  }

  public void setOper_wed(String oper_wed) {
    this.oper_wed = oper_wed;
  }

  public String getOper_thu() {
    return oper_thu;
  }

  public void setOper_thu(String oper_thu) {
    this.oper_thu = oper_thu;
  }

  public String getOper_fri() {
    return oper_fri;
  }

  public void setOper_fri(String oper_fri) {
    this.oper_fri = oper_fri;
  }

  public String getOper_sat() {
    return oper_sat;
  }

  public void setOper_sat(String oper_sat) {
    this.oper_sat = oper_sat;
  }

  public String getOper_sun() {
    return oper_sun;
  }

  public void setOper_sun(String oper_sun) {
    this.oper_sun = oper_sun;
  }

  public String getStart_time() {
    return start_time;
  }

  public void setStart_time(String start_time) {
    this.start_time = start_time;
  }

  public String getEnd_time() {
    return end_time;
  }

  public void setEnd_time(String end_time) {
    this.end_time = end_time;
  }

  public String getPayment_cash() {
    return payment_cash;
  }

  public void setPayment_cash(String payment_cash) {
    this.payment_cash = payment_cash;
  }

  public String getPayment_card() {
    return payment_card;
  }

  public void setPayment_card(String payment_card) {
    this.payment_card = payment_card;
  }

  public String getPayment_machine() {
    return payment_machine;
  }

  public void setPayment_machine(String payment_machine) {
    this.payment_machine = payment_machine;
  }

  public String getLayout() {
    return layout;
  }

  public void setLayout(String layout) {
    this.layout = layout;
  }

  public String getIndoor() {
    return indoor;
  }

  public void setIndoor(String indoor) {
    this.indoor = indoor;
  }

  public String getSmallcar() {
    return smallcar;
  }

  public void setSmallcar(String smallcar) {
    this.smallcar = smallcar;
  }

  public String getWoman() {
    return woman;
  }

  public void setWoman(String woman) {
    this.woman = woman;
  }

  public String getDisabled() {
    return disabled;
  }

  public void setDisabled(String disabled) {
    this.disabled = disabled;
  }

  public boolean getPaid() {
    return paid;
  }

  public void setPaid(boolean paid) {
    this.paid = paid;
  }

  public String getDistance() {
    return distance;
  }

  public void setDistance(String distance) {
    this.distance = distance;
  }

  public float getLat() {
    return lat;
  }

  public void setLat(float lat) {
    this.lat = lat;
  }

  public float getLng() {
    return lng;
  }

  public void setLng(float lng) {
    this.lng = lng;
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

  @Override
  public int compareTo(ParkingDTO o) {
    return 0;
  }
}