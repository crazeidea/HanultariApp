package com.hanultari.dto;

public class ParkingDTO {

	private String id, name, addr, prev_addr, manager, contact, start_time, end_time, img_path, layout;
	private float lat, lon;
	private int parked, total, fare, added_fare, duration, duration_interval;
	private boolean oper_mon, oper_tue, oper_wed, oper_thu, oper_fri, oper_sat, oper_sun, indoor, smallcar, woman, disabled, paid, payment_cash, payment_card, payment_machine;
	public boolean isPayment_cash() {
		return payment_cash;
	}

	public void setPayment_cash(boolean payment_cash) {
		this.payment_cash = payment_cash;
	}

	public boolean isPayment_card() {
		return payment_card;
	}

	public void setPayment_card(boolean payment_card) {
		this.payment_card = payment_card;
	}

	public boolean isPayment_machine() {
		return payment_machine;
	}

	public void setPayment_machine(boolean payment_machine) {
		this.payment_machine = payment_machine;
	}

	
	public ParkingDTO () {}

	public String getId() {
		return id;
	}

	public boolean isOper_sun() {
		return oper_sun;
	}

	public void setOper_sun(boolean oper_sun) {
		this.oper_sun = oper_sun;
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

	public boolean isOper_mon() {
		return oper_mon;
	}

	public void setOper_mon(boolean oper_mon) {
		this.oper_mon = oper_mon;
	}

	public boolean isOper_tue() {
		return oper_tue;
	}

	public void setOper_tue(boolean oper_tue) {
		this.oper_tue = oper_tue;
	}

	public boolean isOper_wed() {
		return oper_wed;
	}

	public void setOper_wed(boolean oper_wed) {
		this.oper_wed = oper_wed;
	}

	public boolean isOper_thu() {
		return oper_thu;
	}

	public void setOper_thu(boolean oper_thu) {
		this.oper_thu = oper_thu;
	}

	public boolean isOper_fri() {
		return oper_fri;
	}

	public void setOper_fri(boolean oper_fri) {
		this.oper_fri = oper_fri;
	}

	public boolean isOper_sat() {
		return oper_sat;
	}

	public void setOper_sat(boolean oper_sat) {
		this.oper_sat = oper_sat;
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
	};

	
}