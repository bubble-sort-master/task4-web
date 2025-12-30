package com.innowise.taxi.service.impl;

import com.innowise.taxi.dao.DriverShiftDao;
import com.innowise.taxi.dao.impl.DriverShiftDaoImpl;
import com.innowise.taxi.entity.DriverOption;
import com.innowise.taxi.entity.DriverShift;
import com.innowise.taxi.exception.ServiceException;
import com.innowise.taxi.service.DriverSearchService;
import com.innowise.taxi.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

import java.util.List;

public class DriverSearchServiceImpl implements DriverSearchService {
  private static final DriverSearchServiceImpl instance = new DriverSearchServiceImpl();
  private final DriverShiftDao driverShiftDao = new DriverShiftDaoImpl();
  private static final Logger logger = LogManager.getLogger();

  private DriverSearchServiceImpl() {}

  public static DriverSearchServiceImpl getInstance() {
    return instance;
  }

  @Override
  public List<DriverOption> findNearestDrivers(int pickupLat, int pickupLon,
                                               int dropoffLat, int dropoffLon) throws ServiceException {
    try {
      List<DriverShift> activeShifts = driverShiftDao.findActiveShifts();

      for (int radius = 1; radius <= 7; radius++) {
        int minLat = Math.max(0, pickupLat - radius);
        int maxLat = Math.min(7, pickupLat + radius);
        int minLon = Math.max(0, pickupLon - radius);
        int maxLon = Math.min(7, pickupLon + radius);

        List<DriverOption> nearest = activeShifts.stream()
                .filter(shift -> shift.getCurrentLat() >= minLat && shift.getCurrentLat() <= maxLat
                        && shift.getCurrentLon() >= minLon && shift.getCurrentLon() <= maxLon)
                .map(shift -> {
                  int tripDistance = Math.abs(pickupLat - dropoffLat) + Math.abs(pickupLon - dropoffLon);
                  double price = tripDistance * CAR_TARIFF;
                  return new DriverOption(shift.getId(), shift.getCurrentLat(), shift.getCurrentLon(), price);
                })
                .collect(Collectors.toList());

        if (!nearest.isEmpty()) {
          logger.info("Found {} nearest drivers within radius {} around ({}, {})",
                  nearest.size(), radius, pickupLat, pickupLon);
          return nearest;
        }
      }

      logger.info("No drivers found near ({}, {}) for dropoff ({}, {})",
              pickupLat, pickupLon, dropoffLat, dropoffLon);
      return List.of();
    } catch (DaoException e) {
      throw new ServiceException("Failed to find nearest drivers", e);
    }
  }

}
