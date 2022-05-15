package com.sipios.refactoring.exception;

import io.micrometer.core.lang.Nullable;

public class CustomerException extends Exception {

    private ExceptionErrorCode errorCode;

    public CustomerException() {
        super();
    }

    public CustomerException(ExceptionErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public CustomerException(ExceptionErrorCode errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
    }

    public CustomerException(String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }

    public ExceptionErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ExceptionErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
