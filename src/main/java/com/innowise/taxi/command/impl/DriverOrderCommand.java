package com.innowise.taxi.command.impl;

import com.google.gson.Gson;
import com.innowise.taxi.command.Command;
import com.innowise.taxi.command.Router;
import com.innowise.taxi.constant.PagePath;
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

public class DriverOrderCommand implements Command {
  private static final String SEARCH = "search";
  private static final String ACCEPT = "accept";
  private static final String COMPLETE = "complete";

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

            String ordersJson = new Gson().toJson(orders);
            return new Router(ordersJson, Router.TransitionType.DATA);
        }
        case ACCEPT: {
          int orderId = Integer.parseInt(request.getParameter(ParameterName.ORDER_ID));
          boolean updated = orderService.acceptOrder(orderId);

          if (updated) {
            session.setAttribute(AttributeName.ORDER_ID, orderId);
          }

          String resultJson = new Gson().toJson(
                  java.util.Collections.singletonMap("accepted", updated)
          );

          return new Router(resultJson, Router.TransitionType.DATA);
        }

        case COMPLETE: {
          int orderId = Integer.parseInt(request.getParameter(ParameterName.ORDER_ID));
          boolean success = orderService.complete(orderId);

          if (success) {
            session.removeAttribute(AttributeName.ORDER_ID);
          } else {
            session.setAttribute(AttributeName.ORDER_COMPLETE_ERROR, "Failed to complete order " + orderId);
          }

          return new Router(PagePath.DRIVER_MAIN, Router.TransitionType.REDIRECT);
        }

        default: {
          return new Router(PagePath.DRIVER_MAIN, Router.TransitionType.REDIRECT);
        }
      }
    } catch (ServiceException e) {
      logger.error("Failed in driver_order command", e);
      session.setAttribute(AttributeName.DRIVER_ERROR, "Internal error, please try later");
      return new Router(PagePath.DRIVER_MAIN, Router.TransitionType.REDIRECT);
    }
  }
}


