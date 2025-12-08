package com.innowise.taxi.entity;

import java.time.LocalDateTime;

public class DriverShift{
  private Long id;
  private Long driverId;
  private Long carId;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private DriverShiftStatus status;
  private int currentLat;
  private int currentLon;

  public Long getId() {
    return id;
  }

  public Long getDriverId() {
    return driverId;
  }

  public Long getCarId() {
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
