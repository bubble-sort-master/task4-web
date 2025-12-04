package com.innowise.taxi.validator.impl;

import com.innowise.taxi.entity.User;
import com.innowise.taxi.validator.CustomValidator;

public class UserValidator implements CustomValidator<User> {
  private static final String NAME_PATTERN = "^\\p{L}+(-\\p{L}+)?$";
  private static final int MIN_PASSWORD_LENGTH = 6;

  @Override
  public boolean isValid(User user) {
    return isValidUsername(user.getUsername())
            && isValidPassword(user.getPassword())
            && isValidFirstName(user.getFirstName())
            && isValidLastName(user.getLastName());
  }

  private boolean isValidUsername(String username) {
    return username != null && !username.isBlank();
  }

  private boolean isValidPassword(String password) {
    return password != null && !password.isBlank() && password.length() >= MIN_PASSWORD_LENGTH;
  }

  private boolean isValidFirstName(String firstName) {
    return firstName != null && !firstName.isBlank()
            && firstName.matches(NAME_PATTERN);
  }

  private boolean isValidLastName(String lastName) {
    if (lastName == null || lastName.isBlank()) {
      return true;
    }
    return lastName.matches(NAME_PATTERN);
  }
}
