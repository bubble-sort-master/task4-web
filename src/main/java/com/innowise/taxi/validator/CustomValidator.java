package com.innowise.taxi.validator;

public interface CustomValidator<T> {
  boolean isValid(T object);
}
