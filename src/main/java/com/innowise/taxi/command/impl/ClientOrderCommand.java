package com.innowise.taxi.command.impl;

import com.innowise.taxi.command.Command;
import com.innowise.taxi.command.Router;
import com.innowise.taxi.constant.AttributeName;
import com.innowise.taxi.constant.PagePath;
import com.innowise.taxi.constant.ParameterName;
import com.innowise.taxi.entity.DriverOption;
import com.innowise.taxi.entity.Order;
import com.innowise.taxi.entity.OrderStatus;
import com.innowise.taxi.exception.ServiceException;
import com.innowise.taxi.service.DriverSearchService;
import com.innowise.taxi.service.OrderService;
import com.innowise.taxi.service.impl.DriverSearchServiceImpl;
import com.innowise.taxi.service.impl.OrderServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ClientOrderCommand implements Command {
  private static final Logger logger = LogManager.getLogger();

  private static final String CREATE = "create";
  private static final String STATUS = "status";
  private static final String CANCEL = "cancel";
  private static final String SEARCH = "search";

  private final OrderService orderService = OrderServiceImpl.getInstance();
  private final DriverSearchService driverSearchService = DriverSearchServiceImpl.getInstance();

  @Override
  public Router execute(HttpServletRequest request) {
    String action = request.getParameter(ParameterName.ACTION);
    HttpSession session = request.getSession();
    String page;

    try {
      if (SEARCH.equals(action)) {
        int pickupLat = (int) session.getAttribute(AttributeName.CLIENT_LATITUDE);
        int pickupLon = (int) session.getAttribute(AttributeName.CLIENT_LONGITUDE);

        int dropoffLat = Integer.parseInt(request.getParameter(ParameterName.DROPOFF_LAT));
        int dropoffLon = Integer.parseInt(request.getParameter(ParameterName.DROPOFF_LON));

        List<DriverOption> nearestDrivers = driverSearchService.findNearestDrivers(
                pickupLat, pickupLon, dropoffLat, dropoffLon);

        double avgPrice = nearestDrivers.stream()
                .mapToDouble(DriverOption::getPrice)
                .average()
                .orElse(0.0);

        session.setAttribute(AttributeName.NEAREST_DRIVERS, nearestDrivers);
        session.setAttribute(AttributeName.AVG_PRICE, avgPrice);
        session.setAttribute(AttributeName.DROPOFF_LAT, dropoffLat);
        session.setAttribute(AttributeName.DROPOFF_LON, dropoffLon);

        page = PagePath.CLIENT_MAIN;

      } else if (CREATE.equals(action)) {
        int clientId = (int) session.getAttribute(AttributeName.USER_ID);
        int pickupLat = (int) session.getAttribute(AttributeName.CLIENT_LATITUDE);
        int pickupLon = (int) session.getAttribute(AttributeName.CLIENT_LONGITUDE);
        int dropoffLat = Integer.parseInt(request.getParameter(ParameterName.DROPOFF_LAT));
        int dropoffLon = Integer.parseInt(request.getParameter(ParameterName.DROPOFF_LON));
        double price = (double) session.getAttribute(AttributeName.AVG_PRICE);

        List<DriverOption> nearestDrivers = (List<DriverOption>) session.getAttribute(AttributeName.NEAREST_DRIVERS);
        if (nearestDrivers != null && !nearestDrivers.isEmpty()) {
          DriverOption chosen = nearestDrivers.get(0);
          Order order = new Order(clientId, pickupLat, pickupLon, dropoffLat, dropoffLon, price);
          order.setStatus(OrderStatus.NEW);
          order.setDriverShiftId(chosen.getDriverShiftId());
          orderService.create(order);
          session.setAttribute(AttributeName.ORDER_SUCCESS, "Order created successfully with driver " + chosen.getDriverShiftId());
        } else {
          session.setAttribute(AttributeName.ORDER_ERROR, "Order creation failed");
        }
        page = PagePath.CLIENT_MAIN;

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
