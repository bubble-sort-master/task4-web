package com.innowise.taxi.entity;

public abstract class AbstractEntity {
  private Long id;

  protected AbstractEntity(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
