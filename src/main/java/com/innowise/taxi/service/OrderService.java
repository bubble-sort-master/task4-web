package com.innowise.taxi.service;

import com.innowise.taxi.entity.Order;
import com.innowise.taxi.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface OrderService {
  Optional<Order> findById(int id) throws ServiceException;
  boolean create(Order order) throws ServiceException;
  List<Order> findAll() throws ServiceException;
}
