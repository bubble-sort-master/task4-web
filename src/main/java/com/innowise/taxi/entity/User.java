package com.innowise.taxi.entity;

public class User extends AbstractEntity {
  private String username;
  private String password;
  private String firstName;
  private String lastName;
  private String role;
  private boolean banned;

  public User(String username, String password, String firstName, String lastName) {
    super(null);
    this.username = username;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public User(long id, String username, String password) {
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

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getRole() {
    return role;
  }

  public boolean isBanned() {
    return banned;
  }
}
