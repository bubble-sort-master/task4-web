package com.innowise.taxi.entity;

public class DriverOption {
  private final int driverShiftId;
  private final int latitude;
  private final int longitude;
  private final double price;

  public DriverOption(int driverShiftId, int latitude, int longitude, double price) {
    this.driverShiftId = driverShiftId;
    this.latitude = latitude;
    this.longitude = longitude;
    this.price = price;
  }

  public int getDriverShiftId() {
    return driverShiftId;
  }

  public int getLatitude() {
    return latitude;
  }

  public int getLongitude() {
    return longitude;
  }

  public double getPrice() {
    return price;
  }
}
