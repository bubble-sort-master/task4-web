package com.innowise.taxi.entity;

import java.time.LocalDateTime;

public class Order{
  private Long id;
  private Long clientId;
  private Long driverShiftId;
  private OrderStatus status;
  private int pickupLat;
  private int pickupLon;
  private int dropOffLat;
  private int dropOffLon;
  private LocalDateTime createdAt;
  private double price;
  private boolean isPaid;

  public boolean isPaid() {
    return isPaid;
  }

  public double getPrice() {
    return price;
  }

  public int getDropOffLon() {
    return dropOffLon;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public int getDropOffLat() {
    return dropOffLat;
  }

  public int getPickupLon() {
    return pickupLon;
  }

  public int getPickupLat() {
    return pickupLat;
  }

  public Long getDriverShiftId() {
    return driverShiftId;
  }

  public Long getClientId() {
    return clientId;
  }

  public Long getId() {
    return id;
  }

  public OrderStatus getStatus() {
    return status;
  }
}

