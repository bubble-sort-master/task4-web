package com.innowise.taxi.exception;

public enum DaoErrorCode {
  DUPLICATE_KEY,
  NULL_VALUE,
  FOREIGN_KEY_VIOLATION,
  SQL_ERROR,
  CONNECTION_ERROR,
  UNKNOWN
}