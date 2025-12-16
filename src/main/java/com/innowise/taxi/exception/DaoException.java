package com.innowise.taxi.exception;

public class DaoException extends Exception {
  private final DaoErrorCode errorCode;

  public DaoException(String message, Throwable cause, DaoErrorCode errorCode) {
    super(message, cause);
    this.errorCode = errorCode;
  }

  public DaoException(Throwable cause) {
    super(cause);
    this.errorCode = DaoErrorCode.UNKNOWN;
  }

  public DaoException(String message) {
    super(message);
    this.errorCode = DaoErrorCode.UNKNOWN;
  }

  public DaoErrorCode getErrorCode() {
    return errorCode;
  }
}
