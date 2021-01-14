package com.hanultari.parking.DTO;

public class MemberDTO {

  private String email, pw, name, nickname, tel;
  private int id;
  private boolean admin;

  public MemberDTO() {};

  public MemberDTO(String email, String pw, String name, String nickname, String tel, int id, boolean admin) {
    super();
    this.email = email;
    this.pw = pw;
    this.name = name;
    this.nickname = nickname;
    this.tel = tel;
    this.id = id;
    this.admin = admin;
  }

  public String getTel() {
    return tel;
  }
  public void setTel(String tel) {
    this.tel = tel;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getPw() {
    return pw;
  }
  public void setPw(String pw) {
    this.pw = pw;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getNickname() {
    return nickname;
  }
  public void setNickname(String nickname) {
    this.nickname = nickname;
  }
  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public boolean getAdmin() {
    return admin;
  }
  public void setAdmin(boolean admin) {
    this.admin = admin;
  }

}
