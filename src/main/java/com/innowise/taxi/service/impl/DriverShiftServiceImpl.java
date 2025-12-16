package com.innowise.taxi.service.impl;

import com.innowise.taxi.dao.DriverShiftDao;
import com.innowise.taxi.dao.impl.DriverShiftDaoImpl;
import com.innowise.taxi.entity.Car;
import com.innowise.taxi.entity.DriverShift;
import com.innowise.taxi.exception.DaoException;
import com.innowise.taxi.exception.ServiceException;
import com.innowise.taxi.service.CarService;
import com.innowise.taxi.service.DriverShiftService;

import java.util.Optional;

public class DriverShiftServiceImpl implements DriverShiftService {
  private static final DriverShiftServiceImpl instance = new DriverShiftServiceImpl();
  private final DriverShiftDao driverShiftDao = new DriverShiftDaoImpl();
  private final CarService carService = CarServiceImpl.getInstance();

  private DriverShiftServiceImpl() {}

  public static DriverShiftServiceImpl getInstance() {
    return instance;
  }

  @Override
  public boolean closeDriverShift(int shiftId) throws ServiceException {
    try {
      return driverShiftDao.updateStatusToClosed(shiftId);
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public Optional<DriverShift> startDriverShift(int userId) throws ServiceException {
    try {
      Optional<Car> freeCar = carService.findFreeCar();
      if (freeCar.isPresent()) {
        Car car = freeCar.get();
        DriverShift shift = driverShiftDao.insertShift(userId, car.getId());
        return Optional.of(shift);
      }
      return Optional.empty();
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public Optional<DriverShift> findActiveDriverShift(int driverId) throws ServiceException {
    try {
      return driverShiftDao.findActiveByDriverId(driverId);
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
  }
}
