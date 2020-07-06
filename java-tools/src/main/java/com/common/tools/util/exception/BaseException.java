//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.tools.util.exception;

import com.common.tools.util.StringUtil;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class BaseException extends RuntimeException {

  private String status = "error";

  /** new CommonException(msg) */
  public BaseException(String msg) {
    super(msg);
  }
  /** new CommonException(Msg.of("id:{},error:{}"),id,error) */
  public BaseException(Msg msg, Object... params) {
    super(StringUtil.strFormat(msg.getMsgTemplate(), params));
  }
  /** new CommonException(Status.of("user.not.found"),"id:{},error:{}",id,error) */
  public BaseException(Status status,String msg, Object... params) {
    super(StringUtil.strFormat(msg, params));
    this.status =status.getStatus();
  }

  public BaseException(Throwable throwable, String msg, Object... params) {
    super(StringUtil.strFormat(msg, params), throwable);
  }
  public BaseException(Throwable throwable,Status status, String msg, Object... params) {
    super(StringUtil.strFormat(msg, params), throwable);
    this.status =status.getStatus();
  }

  public String getTrace() {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    this.printStackTrace(ps);
    ps.flush();
    return new String(baos.toByteArray());
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
