package com.innowise.taxi.command.impl;

import com.innowise.taxi.command.Command;
import com.innowise.taxi.command.Router;
import com.innowise.taxi.constant.ParameterName;
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
  private static final String SEARCH = "search";
  private static final String ACCEPT = "accept";

  private static final Logger logger = LogManager.getLogger();

  @Override
  public Router execute(HttpServletRequest request) {
    HttpSession session = request.getSession();
    OrderService orderService = OrderServiceImpl.getInstance();

    String action = request.getParameter(ParameterName.ACTION);
    if (action == null) {
      action = SEARCH;
    }
    try {
      switch (action) {
        case SEARCH: {
          int driverShiftId = (int) session.getAttribute(AttributeName.DRIVER_SHIFT_ID);
          List<Order> orders = orderService.findOrdersForDriver(driverShiftId);

          String ordersJson = orders.stream()
                  .map(Order::toJson)
                  .collect(Collectors.joining(",", "[", "]"));
          return new Router(ordersJson, Router.TransitionType.DATA);
        }
        case ACCEPT: {
          String orderIdStr = request.getParameter("orderId");
          int orderId = Integer.parseInt(orderIdStr);

          boolean updated = orderService.acceptOrder(orderId);
          logger.info("Driver accepted order id={}, updated={}", orderId, updated);

          String resultJson = "{\"accepted\":" + updated + "}";
          return new Router(resultJson, Router.TransitionType.DATA);
        }
        default: {
          return new Router("[]", Router.TransitionType.DATA);
        }
      }
    } catch (ServiceException e) {
      logger.error("Failed in driver_order command", e);
      return new Router("{\"error\":\"service\"}", Router.TransitionType.DATA);
    } catch (NumberFormatException e) {
      logger.error("Invalid orderId parameter", e);
      return new Router("{\"error\":\"invalid_id\"}", Router.TransitionType.DATA);
    }
  }
}


