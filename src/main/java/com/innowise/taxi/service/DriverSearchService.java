package com.innowise.taxi.service;

import com.innowise.taxi.entity.DriverOption;
import com.innowise.taxi.exception.ServiceException;

import java.util.List;

public interface DriverSearchService {
  double CAR_TARIFF = 10.00;
  List<DriverOption> findNearestDrivers(int pickupLat, int pickupLon, int dropoffLat, int dropoffLon) throws ServiceException;
}
