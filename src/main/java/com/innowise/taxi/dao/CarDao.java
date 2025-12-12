package com.innowise.taxi.dao;

import com.innowise.taxi.entity.Car;
import com.innowise.taxi.exception.DaoException;

import java.util.List;

public interface CarDao {
  String ID = "id";
  String MODEL = "model";
  String PLATE_NUMBER = "plate_number";
  String YEAR = "year";

  public List<Car> findAll() throws DaoException;
}
