package com.common.tools.util.exception;

/**
 * <p>
 * 异常断言类
 * 后续系统需要新增其他异常类型，只需要实现该接口添加对应的枚举值即可
 * </p>
 *
 * @author gongliangjun 2020/06/12 5:26 PM
 */
public interface IAssertException extends IException, IExceptionAssert {

  @Override
  default BaseException newException(Object... args) {
    return new AssertException(this, this.getMessage(), args);
  }

  @Override
  default BaseException newException(String msg, Object... args) {
    StringBuilder append = new StringBuilder(this.getMessage()).append(" -- ").append(msg);
    return new AssertException(this, append.toString(), args);
  }

  @Override
  default BaseException newException(Throwable t, String msg, Object... args) {
    StringBuilder append = new StringBuilder(this.getMessage()).append(" -- ").append(msg);
    return new AssertException(this, t, append.toString(), args);
  }

}
