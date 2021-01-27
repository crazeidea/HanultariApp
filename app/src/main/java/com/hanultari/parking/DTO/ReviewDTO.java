package com.hanultari.parking.DTO;

public class ReviewDTO {

  private int parking_id, member_id;
  private String content;
  private int rating;

  public ReviewDTO() {};

  public ReviewDTO(int parking_id, int member_id, String content, int rating) {
    super();
    this.parking_id = parking_id;
    this.member_id = member_id;
    this.content = content;
    this.rating = rating;
  }

  public int getParking_id() {
    return parking_id;
  }

  public void setParking_id(int parking_id) {
    this.parking_id = parking_id;
  }

  public int getMember_id() {
    return member_id;
  }

  public void setMember_id(int member_id) {
    this.member_id = member_id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }




}