package com.devskiller.dao;

public class PageRequest {

  private final int count;

  /**
   * First page == 0
   */
  private final int pageNumber;

  public PageRequest(int pageNumber, int count) {
    assert pageNumber >= 0;
    assert count > 0;

    this.pageNumber = pageNumber;
    this.count = count;
  }

  public int getCount() {
    return count;
  }

  public int getPageNumber() {
    return pageNumber;
  }
}
