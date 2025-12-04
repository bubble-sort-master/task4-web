package com.innowise.taxi.command.impl;

import com.innowise.taxi.command.Command;
import com.innowise.taxi.command.constants.AttributeName;
import com.innowise.taxi.command.constants.PagePath;
import com.innowise.taxi.command.constants.ParameterName;
import com.innowise.taxi.exception.ServiceException;
import com.innowise.taxi.service.UserService;
import com.innowise.taxi.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginCommand implements Command {
  private static final Logger logger = LogManager.getLogger(LoginCommand.class);

  @Override
  public String execute(HttpServletRequest request) {
    String login = request.getParameter(ParameterName.LOGIN);
    String password = request.getParameter(ParameterName.PASSWORD);
    UserService userService = UserServiceImpl.getInstance();
    String page;
    try {
      if (userService.authenticate(login, password)) {
        request.setAttribute(AttributeName.USER, login);
        page = PagePath.MAIN;
      } else {
        request.setAttribute(AttributeName.LOGIN_ERROR, "authentication failed");
        page = PagePath.INDEX;
      }
    } catch (ServiceException e) {
      logger.error("Login failed due to service error for user {}", login, e);
      request.setAttribute(AttributeName.LOGIN_ERROR, "internal error, please try later");
      page = PagePath.INDEX;
    }
    return page;
  }
}

