package com.innowise.taxi.entity;

public class User {
  private int id;
  private final String username;
  private final String password;
  private final String firstName;
  private final String lastName;
  private UserRole role;
  private boolean banned;
  private int bonusPoints;

  public User(String username, String password, String firstName, String lastName) {
    this.username = username;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public User(int id, String username, String password, UserRole role, String firstName, String lastName, int bonusPoints, boolean banned) {
    this.id = id;
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

  public UserRole getRole() {return role;}

  public boolean isBanned() {
    return banned;
  }

  public int getBonusPoints() {return bonusPoints;}

  public int getId() {return id;}
}
