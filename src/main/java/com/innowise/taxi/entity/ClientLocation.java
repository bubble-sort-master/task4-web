package com.innowise.taxi.entity;

public class ClientLocation {
  private Long id;
  private Long clientId;
  private int latitude;
  private int longitude;

  public Long getId() {
    return id;
  }

  public int getLongitude() {
    return longitude;
  }

  public int getLatitude() {
    return latitude;
  }

  public Long getClientId() {
    return clientId;
  }
}
