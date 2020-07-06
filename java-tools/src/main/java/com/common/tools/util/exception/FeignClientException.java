package com.common.tools.util.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 访问远程服务异常
 *
 * @author gongliangjun 2019/07/01 11:18
 */
@Setter
@Getter
public class FeignClientException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private String module;
  private String businessUrl;
  private int errorCode;
  private String remoteMessage;
  private String remoteException;
  private int remoteCode;

  public FeignClientException(String module, String errorMessage) {
    super(errorMessage);
    this.module = module;
    this.errorCode = 10000;
  }

}
