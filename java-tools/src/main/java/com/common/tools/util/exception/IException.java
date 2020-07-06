package com.common.tools.util.exception;

public interface IException {
    int getCode();
    String getStatus();
    String getMessage();
}