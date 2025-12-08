package com.innowise.taxi.entity;

public class User extends AbstractEntity {
  private String username;
  private String password;
  private String firstName;
  private String lastName;
  private Role role;
  private boolean banned;
  private int bonusPoints;

  public User(String username, String password, String firstName, String lastName) {
    super(null);
    this.username = username;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public User(long id, String username, String password, Role role, String firstName, String lastName, int bonusPoints, boolean banned) {
    super(id);
    this.username = username;
    this.password = password;
    this.role = role;
    this.firstName = firstName;
    this.lastName = lastName;
    this.bonusPoints = bonusPoints;
    this.banned = banned;
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

  public Role getRole() {
    return role;
  }

  public boolean isBanned() {
    return banned;
  }

  public int getBonusPoints() {
    return bonusPoints;
  }
}
