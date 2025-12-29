package com.innowise.taxi.service.impl;

import com.innowise.taxi.dao.OrderDao;
import com.innowise.taxi.dao.impl.OrderDaoImpl;
import com.innowise.taxi.entity.Order;
import com.innowise.taxi.exception.DaoException;
import com.innowise.taxi.exception.ServiceException;
import com.innowise.taxi.service.OrderService;

import java.util.List;
import java.util.Optional;

public class OrderServiceImpl implements OrderService {
  private static final OrderServiceImpl instance = new OrderServiceImpl();
  private final OrderDao orderDao = new OrderDaoImpl();

  private OrderServiceImpl() {}

  public static OrderServiceImpl getInstance() {return instance;}

  @Override
  public Optional<Order> findById(int id) throws ServiceException {
    try {
      return orderDao.findById(id);
    } catch (DaoException e) {
      throw new ServiceException("Failed to find order by id=" + id, e);
    }
  }

  @Override
  public boolean create(Order order) throws ServiceException {
    try {
      Order inserted = orderDao.insert(order);
      return inserted.getId() > 0;
    } catch (DaoException e) {
      throw new ServiceException("Failed to create order", e);
    }
  }

  @Override
  public List<Order> findAll() throws ServiceException {
    try {
      throw new UnsupportedOperationException("findAll not implemented yet");
    } catch (Exception e) {
      throw new ServiceException("Failed to find all orders", e);
    }
  }
}
