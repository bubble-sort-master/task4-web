package com.innowise.taxi.command.impl;

import com.innowise.taxi.command.Command;
import com.innowise.taxi.command.Router;
import com.innowise.taxi.entity.Order;
import com.innowise.taxi.exception.ServiceException;
import com.innowise.taxi.service.OrderService;
import com.innowise.taxi.service.impl.OrderServiceImpl;
import com. innowise.taxi.constant.AttributeName;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class DriverOrderCommand implements Command {
  private static final Logger logger = LogManager.getLogger();

  @Override
  public Router execute(HttpServletRequest request) {
    HttpSession session = request.getSession();
    OrderService orderService = OrderServiceImpl.getInstance();
    try {
      int driverShiftId = (int) session.getAttribute(AttributeName.DRIVER_SHIFT_ID);
      List<Order> orders = orderService.findOrdersForDriver(driverShiftId);

      String ordersJson = orders.stream()
              .map(Order::toJson)
              .collect(Collectors.joining(",", "[", "]"));
      return new Router(ordersJson, Router.TransitionType.DATA);
    } catch (ServiceException e) {
      logger.error("Failed to load driver orders", e);
      return new Router("[]", Router.TransitionType.DATA);
    }
  }
}
