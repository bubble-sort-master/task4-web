package com.innowise.taxi.service;

import com.innowise.taxi.entity.Car;
import com.innowise.taxi.exception.ServiceException;

import java.util.List;

public interface CarService {
  public List<Car> findAllCars() throws ServiceException;
}
