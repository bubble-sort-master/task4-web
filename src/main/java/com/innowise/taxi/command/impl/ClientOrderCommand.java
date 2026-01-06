package com.innowise.taxi.command.impl;

import com.google.gson.Gson;
import com.innowise.taxi.command.Command;
import com.innowise.taxi.command.Router;
import com.innowise.taxi.constant.AttributeName;
import com.innowise.taxi.constant.PagePath;
import com.innowise.taxi.constant.ParameterName;
import com.innowise.taxi.entity.*;
import com.innowise.taxi.exception.ServiceException;;
import com.innowise.taxi.service.*;
import com.innowise.taxi.service.impl.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ClientOrderCommand implements Command {
  private static final String CREATE = "create";
  private static final String CHECK_STATUS = "check_status";
  private static final String SEARCH = "search";
  private static final String PAY = "pay";
  public static final String STATUS_WAITING_JSON = "{\"status\":\"waiting\"}";

  private final OrderService orderService = OrderServiceImpl.getInstance();
  private final DriverSearchService driverSearchService = DriverSearchServiceImpl.getInstance();
  private final DriverShiftService driverShiftService = DriverShiftServiceImpl.getInstance();

  private static final Logger logger = LogManager.getLogger();

  @Override
  public Router execute(HttpServletRequest request) {
    String action = request.getParameter(ParameterName.ACTION);
    HttpSession session = request.getSession();
    String page;

    try {
      switch (action) {
        case SEARCH: {
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
          break;
        }
        case CREATE: {
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
            Order createdOrder = orderService.create(order);
            session.setAttribute(AttributeName.ORDER_ID, createdOrder.getId());
          } else {
            session.setAttribute(AttributeName.ORDER_ERROR, "Order creation failed");
          }
          page = PagePath.CLIENT_MAIN;
          break;
        }
        case CHECK_STATUS: {
          UserService userService = UserServiceImpl.getInstance();
          CarService carService = CarServiceImpl.getInstance();

          int orderId = Integer.parseInt(request.getParameter(ParameterName.ORDER_ID));
          boolean inProgress = orderService.isOrderInProgress(orderId);
          if (inProgress) {
            Optional<Order> orderOpt = orderService.findById(orderId);
            if (orderOpt.isPresent()) {
              int driverShiftId = orderOpt.get().getDriverShiftId();
              Optional<DriverShift> shiftOpt = driverShiftService.findById(driverShiftId);
              if (shiftOpt.isPresent()) {
                DriverShift shift = shiftOpt.get();

                Optional<User> userOpt = userService.findById(shift.getDriverId());
                if (userOpt.isPresent()) {
                  User driver = userOpt.get();
                  Car car = carService.findById(shift.getCarId());

                  Map<String, Object> result = Map.of(
                          "driverName", driver.getFirstName(),
                          "carModel", car.getModel(),
                          "carPlate", car.getPlateNumber(),
                          "currentLat", shift.getCurrentLat(),
                          "currentLon", shift.getCurrentLon()
                  );

                  String json = new Gson().toJson(result);

                  return new Router(json, Router.TransitionType.DATA);
                }
              }
            }
          }
          return new Router(STATUS_WAITING_JSON, Router.TransitionType.DATA);
        }
        case PAY: {
          int orderId = Integer.parseInt(request.getParameter(ParameterName.ORDER_ID));
          boolean success = orderService.pay(orderId);

          if (success) {
            session.setAttribute(AttributeName.PAYMENT_SUCCESS, "Payment completed for orderId=" + orderId);
            session.removeAttribute(AttributeName.ORDER_ID);
            session.removeAttribute(AttributeName.NEAREST_DRIVERS);
            session.removeAttribute(AttributeName.AVG_PRICE);
            session.removeAttribute(AttributeName.DROPOFF_LAT);
            session.removeAttribute(AttributeName.DROPOFF_LON);
          } else {
            session.setAttribute(AttributeName.PAYMENT_ERROR, "Payment failed for orderId=" + orderId);
          }

          page = PagePath.CLIENT_MAIN;
          break;
        }

        default: {
          session.setAttribute(AttributeName.ORDER_ERROR, "Unknown action");
          page = PagePath.CLIENT_MAIN;
          break;
        }
      }
    } catch (ServiceException e) {
      logger.error("Order command failed", e);
      session.setAttribute(AttributeName.ORDER_ERROR, "Internal error, please try later");
      page = PagePath.CLIENT_MAIN;
    }

    return new Router(page, Router.TransitionType.REDIRECT);
  }
}
