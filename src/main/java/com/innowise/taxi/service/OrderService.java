package com.innowise.taxi.service;

import com.innowise.taxi.entity.Order;
import com.innowise.taxi.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface OrderService {
  Order create(Order order) throws ServiceException;
  Optional<Order> findById(int id) throws ServiceException;
  List<Order> findOrdersForDriver(int driverShiftId) throws ServiceException;
  boolean acceptOrder(int orderId) throws ServiceException;
  boolean isOrderInProgress(int orderId) throws ServiceException;
}
