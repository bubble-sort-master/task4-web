package com.innowise.taxi.command.impl;

import com.innowise.taxi.command.Command;
import com.innowise.taxi.command.constants.AttributeName;
import com.innowise.taxi.command.constants.PagePath;
import com.innowise.taxi.command.constants.ParameterName;
import com.innowise.taxi.entity.User;
import com.innowise.taxi.exception.ServiceException;
import com.innowise.taxi.service.UserService;
import com.innowise.taxi.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegisterCommand implements Command {
  private static final Logger logger = LogManager.getLogger(RegisterCommand.class);

  @Override
  public String execute(HttpServletRequest request) {
    String username = request.getParameter(ParameterName.LOGIN);
    String password = request.getParameter(ParameterName.PASSWORD);
    String firstName = request.getParameter(ParameterName.FIRST_NAME);
    String lastName = request.getParameter(ParameterName.LAST_NAME);
    logger.debug("accepted user input {}, {}, {}, {}", username, password, firstName, lastName);

    UserService userService = UserServiceImpl.getInstance();
    String page;
    try {
      User user = new User(username, password, firstName, lastName);
      boolean registered = userService.register(user);

      if (registered) {
        request.setAttribute(AttributeName.USER, username);
        page = PagePath.MAIN;
      } else {
        logger.warn("Registration failed: user {} not saved", username);
        request.setAttribute(AttributeName.REGISTER_ERROR, "registration failed");
        page = PagePath.REGISTER;
      }
    } catch (ServiceException e) {
      //TODO: correct exception handling here and in related places
      logger.error("Registration failed for user {}", username, e);
      request.setAttribute(AttributeName.REGISTER_ERROR, "internal error, please try later");
      page = PagePath.REGISTER;
    }
    return page;
  }

}
