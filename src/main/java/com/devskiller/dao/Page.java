package com.devskiller.dao;

import java.util.List;

public class Page<T> {

  private List<T> content;

  private int totalElements;

  private int pageNumber;

  public Page(List<T> content, int pageNumber, int totalElements) {
    this.content = content;
    this.pageNumber = pageNumber;
    this.totalElements = totalElements;
  }

  public List<T> getContent() {
    return content;
  }

  public int getTotalElements() {
    return totalElements;
  }

  public int getPageNumber() {
    return pageNumber;
  }
}
