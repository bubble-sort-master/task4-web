package com.innowise.taxi.entity;

import java.time.LocalDateTime;

public class Order{
  private int id;
  private int clientId;
  private int driverShiftId;
  private OrderStatus status;
  private int pickupLat;
  private int pickupLon;
  private int dropOffLat;
  private int dropOffLon;
  private LocalDateTime createdAt;
  private double price;

  public Order(int clientId, int pickupLat, int pickupLon, int dropOffLat, int dropOffLon, double price) {
    this.clientId = clientId;
    this.pickupLat = pickupLat;
    this.pickupLon = pickupLon;
    this.dropOffLat = dropOffLat;
    this.dropOffLon = dropOffLon;
    this.price = price;
  }

  public Order() {}

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public void setPaid(boolean paid) {
    isPaid = paid;
  }

  private boolean isPaid;

  public void setId(int id) {
    this.id = id;
  }

  public void setClientId(int clientId) {
    this.clientId = clientId;
  }

  public void setDriverShiftId(int driverShiftId) {
    this.driverShiftId = driverShiftId;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public void setPickupLat(int pickupLat) {
    this.pickupLat = pickupLat;
  }

  public void setPickupLon(int pickupLon) {
    this.pickupLon = pickupLon;
  }

  public void setDropOffLat(int dropOffLat) {
    this.dropOffLat = dropOffLat;
  }

  public void setDropOffLon(int dropOffLon) {
    this.dropOffLon = dropOffLon;
  }

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

  public int getDriverShiftId() {
    return driverShiftId;
  }

  public int getClientId() {
    return clientId;
  }

  public int getId() {
    return id;
  }

  public OrderStatus getStatus() {
    return status;
  }
}

