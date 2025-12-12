package com.innowise.taxi.command.impl;

import com.innowise.taxi.command.Command;
import com.innowise.taxi.command.Router;
import com.innowise.taxi.entity.Car;
import com.innowise.taxi.exception.ServiceException;
import com.innowise.taxi.service.CarService;
import com.innowise.taxi.service.impl.CarServiceImpl;
import com.innowise.taxi.constant.AttributeName;
import com.innowise.taxi.constant.PagePath;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ShowCarsCommand implements Command {
  private static final Logger logger = LogManager.getLogger();

  @Override
  public Router execute(HttpServletRequest request) {
    CarService carService = CarServiceImpl.getInstance();
    String page;
    try {
      List<Car> cars = carService.findAllCars();
      request.setAttribute(AttributeName.CARS, cars);
      page = PagePath.ADMIN_CAR_LIST;
      logger.info("Admin requested car list, {} cars found", cars.size());
    } catch (ServiceException e) {
      logger.error("Failed to load car list for admin", e);
      request.setAttribute(AttributeName.ADMIN_ERROR, "internal error, please try later");
      page = PagePath.ADMIN_MAIN;
    }
    return new Router(page, Router.TransitionType.FORWARD);
  }

}
