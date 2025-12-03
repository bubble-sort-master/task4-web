package com.innowise.taxi.entity;

public class User extends AbstractEntity {
  private String login;
  private String password;

  public User(Long id, String login, String password) {
    super(id);
    this.login = login;
    this.password = password;
  }

  public String getLogin() {
    return login;
  }

  public String getPassword() {
    return password;
  }
}
