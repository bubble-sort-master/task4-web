package com.innowise.taxi.entity;

public class User extends AbstractEntity {
  private String username;
  private String password;

  public User(Long id, String username, String password) {
    super(id);
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }
}
