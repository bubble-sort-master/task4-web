package com.innowise.taxi.entity;

import java.time.LocalDateTime;

public class DriverShift{
  private int id;
  private int driverId;
  private int carId;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private DriverShiftStatus status;
  private int currentLat;
  private int currentLon;

  public DriverShift(int id, int driverId, int carId, LocalDateTime startTime, LocalDateTime endTime, DriverShiftStatus status, int currentLat, int currentLon) {
    this.id = id;
    this.driverId = driverId;
    this.carId = carId;
    this.startTime = startTime;
    this.endTime = endTime;
    this.status = status;
    this.currentLat = currentLat;
    this.currentLon = currentLon;
  }

  public int getId() {
    return id;
  }

  public int getDriverId() {
    return driverId;
  }

  public int getCarId() {
    return carId;
  }

  public LocalDateTime getEndTime() {
    return endTime;
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public DriverShiftStatus getStatus() {
    return status;
  }

  public int getCurrentLon() {
    return currentLon;
  }

  public int getCurrentLat() {
    return currentLat;
  }
}
