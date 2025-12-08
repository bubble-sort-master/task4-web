package com.innowise.taxi.command.impl;

import com.innowise.taxi.command.Command;
import com.innowise.taxi.command.Router;
import com.innowise.taxi.constant.AttributeName;
import com.innowise.taxi.constant.PagePath;
import com.innowise.taxi.constant.ParameterName;
import com.innowise.taxi.entity.User;
import com.innowise.taxi.exception.ServiceException;
import com.innowise.taxi.service.UserService;
import com.innowise.taxi.service.impl.UserServiceImpl;
import com.innowise.taxi.validator.CustomValidator;
import com.innowise.taxi.validator.impl.UserValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegisterCommand implements Command {
  private static final Logger logger = LogManager.getLogger();

  @Override
  public Router execute(HttpServletRequest request) {
    String username = request.getParameter(ParameterName.USERNAME);
    String password = request.getParameter(ParameterName.PASSWORD);
    String firstName = request.getParameter(ParameterName.FIRST_NAME);
    String lastName = request.getParameter(ParameterName.LAST_NAME);

    UserService userService = UserServiceImpl.getInstance();
    String page;
    try {
      User user = new User(username, password, firstName, lastName);
      CustomValidator<User> validator = new UserValidator();
      if (!validator.isValid(user)) {
        logger.warn("Registration failed: invalid input for user {}", username);
        request.setAttribute(AttributeName.REGISTER_ERROR, "Invalid input data");
        return new Router(PagePath.REGISTER, Router.TransitionType.REDIRECT);
      }

      boolean registered = userService.register(user);
      if (registered) {
        logger.info("User {} registered successfully", username);
        request.setAttribute(AttributeName.REGISTER_SUCCESS, "Registration successful, please login");
        page = PagePath.INDEX;
      } else {
        logger.warn("Registration failed for user {}", username);
        request.setAttribute(AttributeName.REGISTER_ERROR, "username already exists");
        page = PagePath.REGISTER;
      }
    } catch (ServiceException e) {
      logger.error("Registration failed due to service error for user {}", username, e);
      request.setAttribute(AttributeName.REGISTER_ERROR, "internal error, please try later");
      page = PagePath.REGISTER;
    }
    return new Router(page, Router.TransitionType.REDIRECT);
  }
}
