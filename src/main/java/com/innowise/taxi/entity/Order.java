package com.innowise.taxi.entity;

import java.time.LocalDateTime;
import java.util.Locale;

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
  private boolean isPaid;
  private double price;

  public Order(int clientId, int pickupLat, int pickupLon, int dropOffLat, int dropOffLon, double price) {
    this.id = 0;
    this.clientId = clientId;
    this.pickupLat = pickupLat;
    this.pickupLon = pickupLon;
    this.dropOffLat = dropOffLat;
    this.dropOffLon = dropOffLon;
    this.price = price;
    this.isPaid = false;
    this.createdAt = LocalDateTime.now();
    this.driverShiftId = 0;
    this.status = null;
  }

  public Order(int id, int clientId, int driverShiftId, OrderStatus status, int pickupLat,
               int pickupLon,int dropOffLat, int dropOffLon, double price,
               boolean paid, LocalDateTime createdAt) {
    this(clientId, pickupLat, pickupLon, dropOffLat, dropOffLon, price);
    this.id = id;
    this.isPaid = paid;
    this.status=status;
    this.createdAt = createdAt;
    this.driverShiftId = driverShiftId;
  }

  public Order() {}

  public void setId(int id) {
    this.id = id;
  }

  public void setDriverShiftId(int driverShiftId) {
    this.driverShiftId = driverShiftId;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
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

  public String toJson() {
    return String.format(Locale.US,
        """
        {
          "id":%d,
          "pickupLat":%d,
          "pickupLon":%d,
          "dropoffLat":%d,
          "dropoffLon":%d,
          "price":%.2f,
          "status":"%s"
        }
        """,
            id, pickupLat, pickupLon, dropOffLat, dropOffLon, price, status.name()
    );
  }

}

