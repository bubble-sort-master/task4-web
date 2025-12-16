package com.innowise.taxi.command.impl;

import com.innowise.taxi.command.Command;
import com.innowise.taxi.command.Router;
import com.innowise.taxi.constant.PagePath;
import com.innowise.taxi.constant.AttributeName;
import com.innowise.taxi.entity.Car;
import com.innowise.taxi.entity.DriverShift;
import com.innowise.taxi.service.CarService;
import com.innowise.taxi.service.DriverShiftService;
import com.innowise.taxi.exception.ServiceException;
import com.innowise.taxi.service.impl.CarServiceImpl;
import com.innowise.taxi.service.impl.DriverShiftServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

public class DriverShiftCommand implements Command {

  @Override
  public Router execute(HttpServletRequest request) {
    HttpSession session = request.getSession(true);
    DriverShiftService shiftService = DriverShiftServiceImpl.getInstance();
    CarService carService = CarServiceImpl.getInstance();

    Boolean active = (Boolean) session.getAttribute(AttributeName.DRIVER_SHIFT_ACTIVE);
    int userId = (int) session.getAttribute(AttributeName.USER_ID);

    try {
      if (active == null || !active) {
        Optional<DriverShift> shiftOpt = shiftService.startDriverShift(userId);
        if (shiftOpt.isPresent()) {
          DriverShift shift = shiftOpt.get();
          Car car = carService.findById(shift.getCarId());
          session.setAttribute(AttributeName.DRIVER_SHIFT_ACTIVE, true);
          session.setAttribute(AttributeName.SHIFT_START_TIME, shift.getStartTime());
          session.setAttribute(AttributeName.CAR_MODEL, car.getModel());
          session.setAttribute(AttributeName.CAR_PLATE_NUMBER, car.getPlateNumber());
        } else {
          session.setAttribute(AttributeName.DRIVER_ERROR, "No available cars");
        }
      } else {
        Optional<DriverShift> shiftOpt = shiftService.findActiveDriverShift(userId);
        if (shiftOpt.isPresent()) {
          shiftService.closeDriverShift(shiftOpt.get().getId());
        }
        session.removeAttribute(AttributeName.DRIVER_SHIFT_ACTIVE);
        session.removeAttribute(AttributeName.SHIFT_START_TIME);
        session.removeAttribute(AttributeName.CAR_MODEL);
        session.removeAttribute(AttributeName.CAR_PLATE_NUMBER);
      }
    } catch (ServiceException e) {
      session.setAttribute(AttributeName.DRIVER_ERROR, "Internal error, please try later");
    }

    return new Router(PagePath.DRIVER_MAIN, Router.TransitionType.REDIRECT);
  }

}
