package com.common.tools.util.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常枚举类 用法： 1、USER_NOT_FOUND.assertNotNull(obj);
 * 2、USER_NOT_FOUND.assertNotNull(obj,"用户名:{}","bob");
 *
 * @author gongliangjun 2020/06/12 5:25 PM
 */
@Getter
@AllArgsConstructor
public enum BusinessExceptionAssert implements IAssertException {
  /** 构建响应异常 */
  RES_BUILD_EXCEPTION(5000, "res.build.exception", "构建响应异常"),
  /** 业务异常 */
  BUSINESS_EXCEPTION(6000, "business.exception", "业务异常"),
  /** 业务异常 */
  ILLEGAL_OPERATION_EXCEPTION(7000, "illegal.operation.exception", "不合理的操作"),
  /** 不存在的用户 */
  USER_NOT_FOUND(6001, "user.not.found", "用户不存在");

  /** 返回码 */
  private int code;
  /** 返回状态 */
  private String status;
  /** 返回消息 */
  private String message;
}
