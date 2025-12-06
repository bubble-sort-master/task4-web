package com.innowise.taxi.auth;

import com.innowise.taxi.entity.User;

public class AuthResult {
  private final AuthResultType type;
  private final User user;

  public AuthResult(AuthResultType type, User user) {
    this.type = type;
    this.user = user;
  }

  public AuthResultType getType() {
    return type;
  }

  public User getUser() {
    return user;
  }
}