package com.innowise.taxi.command.impl;

import com.innowise.taxi.command.Command;
import com.innowise.taxi.command.Router;
import com.innowise.taxi.constant.AttributeName;
import com.innowise.taxi.constant.PagePath;
import com.innowise.taxi.constant.ParameterName;
import com.innowise.taxi.entity.Order;
import com.innowise.taxi.entity.OrderStatus;
import com.innowise.taxi.exception.ServiceException;
import com.innowise.taxi.service.OrderService;
import com.innowise.taxi.service.impl.OrderServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientOrderCommand implements Command {
  private static final Logger logger = LogManager.getLogger();

  private static final String CREATE = "create";
  private static final String STATUS = "status";
  private static final String CANCEL = "cancel";

  private final OrderService orderService = OrderServiceImpl.getInstance();

  @Override
  public Router execute(HttpServletRequest request) {
    String action = request.getParameter(ParameterName.ACTION);
    HttpSession session = request.getSession();
    String page;
    try {
      if (CREATE.equals(action)) {
        int clientId = (int) session.getAttribute(AttributeName.USER_ID);
        int pickupLat = Integer.parseInt(request.getParameter(ParameterName.PICKUP_LAT));
        int pickupLon = Integer.parseInt(request.getParameter(ParameterName.PICKUP_LON));
        int dropoffLat = Integer.parseInt(request.getParameter(ParameterName.DROPOFF_LAT));
        int dropoffLon = Integer.parseInt(request.getParameter(ParameterName.DROPOFF_LON));
        double price = 10;
        Order order = new Order(clientId, pickupLat, pickupLon, dropoffLat, dropoffLon, price);
        order.setStatus(OrderStatus.NEW);
        boolean created = orderService.create(order);
        if (created) {
          logger.info("Order created successfully for client {}", clientId);
          session.setAttribute(AttributeName.ORDER_SUCCESS, "Order created successfully");
          page = PagePath.CLIENT_MAIN;
        } else {
          logger.warn("Order creation failed for client {}", clientId);
          session.setAttribute(AttributeName.ORDER_ERROR, "Order creation failed");
          page = PagePath.CLIENT_MAIN;
        }
      } else if (STATUS.equals(action)) {
        int orderId = Integer.parseInt(request.getParameter(ParameterName.ORDER_ID));
        orderService.findById(orderId).ifPresentOrElse(
                o -> session.setAttribute(AttributeName.ORDER_SUCCESS, "Order status: " + o.getStatus()),
                () -> session.setAttribute(AttributeName.ORDER_ERROR, "Order not found")
        );
        page = PagePath.CLIENT_MAIN;
      } else if (CANCEL.equals(action)) {
        session.setAttribute(AttributeName.ORDER_SUCCESS, "Order cancelled");
        page = PagePath.CLIENT_MAIN;
      } else {
        session.setAttribute(AttributeName.ORDER_ERROR, "Unknown action");
        page = PagePath.CLIENT_MAIN;
      }
    } catch (ServiceException e) {
      logger.error("Order command failed", e);
      session.setAttribute(AttributeName.ORDER_ERROR, "Internal error, please try later");
      page = PagePath.CLIENT_MAIN;
    }

    return new Router(page, Router.TransitionType.REDIRECT);
  }
}
