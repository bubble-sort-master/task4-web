package com.innowise.taxi.dao;

import com.innowise.taxi.entity.DriverShift;
import com.innowise.taxi.exception.DaoException;

import java.util.Optional;

public interface DriverShiftDao {
  String ID = "id";
  String DRIVER_ID = "driver_id";
  String CAR_ID = "car_id";
  String START_TIME = "start_time";
  String END_TIME = "end_time";
  String STATUS = "status";
  String CURRENT_LAT = "current_lat";
  String CURRENT_LON = "current_lon";

  Optional<DriverShift> findActiveByDriverId(int driverId) throws DaoException;
  boolean updateStatusToClosed(int shiftId) throws DaoException;
  DriverShift insertShift(int driverId, int carId)   throws DaoException;
}
