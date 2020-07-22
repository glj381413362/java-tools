package com.common.tools.util.exception;

public interface IExceptionAssert {

  BaseException newException(Object... args);

  /**
   * 创建带模板错误信息
   *
   * @param msg
   * @param args
   */
  BaseException newException(String msg, Object... args);

  /**
   * 创建带模板错误信息
   *
   * @param t
   * @param args
   */
  BaseException newException(Throwable t, String msg, Object... args);

  /**
   * 断言对象obj非空。如果对象obj为空，则抛出异常
   *
   * @param obj
   * @author gongliangjun 2020-06-17 12:27 PM
   */
  default void assertNotNull(Object obj) {
    if (obj == null) {
      throw newException();
    }
  }
  /**
   * 断言对象obj非空。如果对象obj为空，则抛出异常
   *
   * @param obj
   * @author gongliangjun 2020-06-17 12:27 PM
   */
  default void assertNotNull(Object obj, String msg) {
    if (obj == null) {
      throw newException(msg);
    }
  }
  /**
   * 断言对象obj非空。如果对象obj为空，则抛出异常
   * @param obj
   * @param args
   * @author gongliangjun 2020-06-17 12:28 PM
   */
  default void assertNotNull(Object obj, Object... args) {
    if (obj == null) {
      throw newException(args);
    }
  }

  /**
   * 断言对象obj非空。如果对象obj为空，则抛出异常
   * 异常信息支持传递参数方式，避免在判断之前进行字符串拼接操作
   * @param obj
   * @param msg
   * @param args
   * @author gongliangjun 2020-06-17 12:28 PM
   */
  default void assertNotNull(Object obj, String msg, Object... args) {

    if (obj == null) {
      throw newException(msg, args);
    }
  }
  /**
   * 抛出一个异常
   *
   *
   * @author gongliangjun 2020-06-17 12:29 PM
   */
  default void throwE() {
      throw newException();
  }
  /**
   * 抛出一个异常和异常信息
   *
   * @param msg
   * @param args
   * @author gongliangjun 2020-06-17 12:29 PM
   */
  default void throwE(String msg, Object... args) {
      throw newException(msg,args);
  }
  /**
   * 抛出一个异常和异常信息
   *
   * @param t
   * @param msg
   * @param args
   * @author gongliangjun 2020-06-17 12:29 PM
   */
  default void throwE(Throwable t ,String msg, Object... args) {
      throw newException(t,msg,args);
  }
}
