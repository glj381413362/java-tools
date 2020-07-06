package com.common.tools.util.exception;

/** @author gongliangjun 2019/07/01 11:18 */
public class Status {
  private String status;

  protected Status() {}

  protected Status(String status) {
    this.status = status;
  }

  public static Status of(Object status) {
    String s = null == status ? "" : status.toString();
    return new Status(s);
  }

  public String getStatus() {
    return status;
  }
}
