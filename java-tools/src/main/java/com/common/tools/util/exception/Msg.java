package com.common.tools.util.exception;

/** @author gongliangjun 2019/07/01 11:18 */
public class Msg {
  private String msgTemplate;

  protected Msg() {}

  protected Msg(String msgTemplate) {
    this.msgTemplate = msgTemplate;
  }

  public static Msg of(Object msgTemplate) {
    String s = null == msgTemplate ? "" : msgTemplate.toString();
    return new Msg(s);
  }

  public String getMsgTemplate() {
    return msgTemplate;
  }
}
