package com.innowise.taxi.service.impl;

import com.innowise.taxi.dao.DriverShiftDao;
import com.innowise.taxi.dao.impl.DriverShiftDaoImpl;
import com.innowise.taxi.entity.DriverShift;
import com.innowise.taxi.exception.DaoErrorCode;
import com.innowise.taxi.exception.DaoException;
import com.innowise.taxi.exception.ServiceException;
import com.innowise.taxi.service.DriverShiftService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Optional;

public class DriverShiftServiceImpl implements DriverShiftService {
  private static final DriverShiftServiceImpl instance = new DriverShiftServiceImpl();
  private final DriverShiftDao driverShiftDao = new DriverShiftDaoImpl();
  private static final int MAX_RETRIES = 5;
  private static final Logger logger = LogManager.getLogger();

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
    int attempt = 0;
    while (attempt < MAX_RETRIES) {
      try {
        Optional<Integer> shiftIdOptional = driverShiftDao.insertShift(userId);
        if (shiftIdOptional.isEmpty()) {
          return Optional.empty();
        }
        return driverShiftDao.findById(shiftIdOptional.get());
      } catch (DaoException e) {
        if (e.getErrorCode() == DaoErrorCode.DEADLOCK) {
          attempt++;
          logger.warn("Deadlock detected for driver {}. Retrying {}/{}", userId, attempt, MAX_RETRIES);
          continue;
        }
        throw new ServiceException(e);
      }
    }
    return Optional.empty();
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
