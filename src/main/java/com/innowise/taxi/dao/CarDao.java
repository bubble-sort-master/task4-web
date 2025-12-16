package com.innowise.taxi.dao;

import com.innowise.taxi.entity.Car;
import com.innowise.taxi.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface CarDao {
  String ID = "id";
  String MODEL = "model";
  String PLATE_NUMBER = "plate_number";
  String YEAR = "year";

  List<Car> findAll() throws DaoException;
  Car findById(int Id) throws DaoException;
  Optional<Car> findFreeCar() throws DaoException;
}
