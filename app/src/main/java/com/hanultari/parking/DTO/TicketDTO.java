package com.hanultari.parking.DTO;

import java.util.Date;

public class TicketDTO {

  private int no, id, readcnt, root, status;
  private String title, content, writer, name;
  private String writedate;

  public int getNo() {
    return no;
  }

  public void setNo(int no) {
    this.no = no;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getReadcnt() {
    return readcnt;
  }

  public void setReadcnt(int readcnt) {
    this.readcnt = readcnt;
  }

  public int getRoot() {
    return root;
  }

  public void setRoot(int root) {
    this.root = root;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getWriter() {
    return writer;
  }

  public void setWriter(String writer) {
    this.writer = writer;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getWritedate() {
    return writedate;
  }

  public void setWritedate(String writedate) {
    this.writedate = writedate;
  }
}
