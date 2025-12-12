package com.innowise.taxi.service.impl;

import com.innowise.taxi.dao.impl.CarDaoImpl;
import com.innowise.taxi.dao.impl.UserDaoImpl;
import com.innowise.taxi.entity.Car;
import com.innowise.taxi.exception.DaoException;
import com.innowise.taxi.exception.ServiceException;
import com.innowise.taxi.service.CarService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class CarServiceImpl implements CarService {
  private static final Logger logger = LogManager.getLogger();
  private final CarDaoImpl carDaoImpl = new CarDaoImpl();
  private static final CarServiceImpl instance = new CarServiceImpl();

  private CarServiceImpl() {}

  public static CarServiceImpl getInstance() {
    return instance;
  }

  @Override
  public List<Car> findAllCars() throws ServiceException {
    try {
      return carDaoImpl.findAll();
    } catch (DaoException e) {
      logger.error("Service error while finding all cars", e);
      throw new ServiceException(e);
    }
  }

}
