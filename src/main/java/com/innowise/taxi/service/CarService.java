package com.innowise.taxi.service;

import com.innowise.taxi.entity.Car;
import com.innowise.taxi.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface CarService {
  List<Car> findAllCars() throws ServiceException;
  Car findById(int id) throws ServiceException;
  Optional<Car> findFreeCar() throws ServiceException;
}
