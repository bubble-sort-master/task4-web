package com.innowise.taxi.service.impl;

import com.innowise.taxi.dao.OrderDao;
import com.innowise.taxi.dao.impl.OrderDaoImpl;
import com.innowise.taxi.entity.Order;
import com.innowise.taxi.entity.OrderStatus;
import com.innowise.taxi.exception.DaoException;
import com.innowise.taxi.exception.ServiceException;
import com.innowise.taxi.service.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class OrderServiceImpl implements OrderService {
  private static final OrderServiceImpl instance = new OrderServiceImpl();
  private final OrderDao orderDao = new OrderDaoImpl();
  private static final Logger logger = LogManager.getLogger();

  private OrderServiceImpl() {}

  public static OrderServiceImpl getInstance() {
    return instance;
  }

  @Override
  public Order create(Order order) throws ServiceException {
    try {
      Order inserted = orderDao.insert(order);
      if (inserted.getId() > 0) {
        logger.info("Order creation succeeded for orderId={}", inserted.getId());
        return inserted;
      } else {
        logger.warn("Order creation failed, no id assigned");
        throw new ServiceException("Order creation failed");
      }
    } catch (DaoException e) {
      logger.error("Failed to create order: {}", order, e);
      throw new ServiceException("Failed to create order", e);
    }
  }


  @Override
  public Optional<Order> findById(int id) throws ServiceException {
    try {
      Optional<Order> result = orderDao.findById(id);
      if (result.isPresent()) {
        logger.info("Order found: {}", result.get());
      } else {
        logger.warn("Order not found for id={}", id);
      }
      return result;
    } catch (DaoException e) {
      logger.error("Failed to find order by id={}", id, e);
      throw new ServiceException("Failed to find order by id=" + id, e);
    }
  }

  @Override
  public List<Order> findOrdersForDriver(int driverShiftId) throws ServiceException {
    try {
      List<Order> orders = orderDao.findByDriverShiftId(driverShiftId);
      logger.info("Found {} orders for driverShiftId={}", orders.size(), driverShiftId);
      return orders;
    } catch (DaoException e) {
      logger.error("Failed to find orders for driverShiftId={}", driverShiftId, e);
      throw new ServiceException("Failed to find orders for driver", e);
    }
  }

  @Override
  public boolean acceptOrder(int orderId) throws ServiceException {
    try {
      return orderDao.updateStatus(orderId, OrderStatus.IN_PROGRESS);
    } catch (DaoException e) {
      logger.error("Failed to accept order with id={}", orderId, e);
      throw new ServiceException("Error while accepting order", e);
    }
  }

  @Override
  public boolean isOrderInProgress(int orderId) throws ServiceException {
    try {
      Optional<Order> orderOpt = orderDao.findById(orderId);
      return orderOpt.isPresent() && orderOpt.get().getStatus() == OrderStatus.IN_PROGRESS;
    } catch (DaoException e) {
      logger.error("Error checking order status for id={}", orderId, e);
      throw new ServiceException("Failed to check order status", e);
    }
  }

}
