package com.common.tools.util.exception;

public class AssertException extends BaseException {

  private static final long serialVersionUID = 1L;
  private IException exception;

  public AssertException(IException exception, String message, Object... params) {
    super(Status.of(exception.getStatus()),message, params);
    this.exception = exception;
  }

  public AssertException(
      IException exception, Throwable cause, String message, Object... params) {
    super(cause,Status.of(exception.getStatus()), message, params);
    this.exception = exception;
  }

  public IException getException() {
    return exception;
  }
}
