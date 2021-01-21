package com.hanultari.parking.DTO;

import java.util.Date;

public class NoticeDTO {
  private int no, id, readcnt, root, step, indent;
  private String title, content, writer, name, filename, filepath;
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

  public int getStep() {
    return step;
  }

  public void setStep(int step) {
    this.step = step;
  }

  public int getIndent() {
    return indent;
  }

  public void setIndent(int indent) {
    this.indent = indent;
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

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public String getFilepath() {
    return filepath;
  }

  public void setFilepath(String filepath) {
    this.filepath = filepath;
  }

  public String getWritedate() {
    return writedate;
  }

  public void setWritedate(String writedate) {
    this.writedate = writedate;
  }
}
