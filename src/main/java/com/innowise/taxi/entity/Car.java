package com.innowise.taxi.entity;

public class Car {
  private Long id;
  private String model;
  private String plateNumber;
  private int year;

  public Car(Long id, String model, String plateNumber, int year) {
    this.id = id;
    this.model = model;
    this.plateNumber = plateNumber;
    this.year = year;
  }

  public Long getId() {
    return id;
  }

  public int getYear() {
    return year;
  }

  public String getPlateNumber() {
    return plateNumber;
  }

  public String getModel() {
    return model;
  }
}