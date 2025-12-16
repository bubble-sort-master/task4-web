package com.innowise.taxi.service;

import com.innowise.taxi.entity.DriverShift;
import com.innowise.taxi.exception.ServiceException;

import java.util.Optional;

public interface DriverShiftService {
  boolean closeDriverShift(int shiftId) throws ServiceException;
  Optional<DriverShift> startDriverShift(int userId) throws ServiceException;
  Optional<DriverShift> findActiveDriverShift(int driverID) throws ServiceException;

}
