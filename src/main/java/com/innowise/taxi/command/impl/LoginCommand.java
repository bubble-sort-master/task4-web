package com.innowise.taxi.command.impl;

import com.innowise.taxi.command.Command;
import com.innowise.taxi.constant.AttributeName;
import com.innowise.taxi.constant.PagePath;
import com.innowise.taxi.constant.ParameterName;
import com.innowise.taxi.exception.ServiceException;
import com.innowise.taxi.service.UserService;
import com.innowise.taxi.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginCommand implements Command {
  private static final Logger logger = LogManager.getLogger();

  @Override
  public String execute(HttpServletRequest request) {
    String username = request.getParameter(ParameterName.USERNAME);
    String password = request.getParameter(ParameterName.PASSWORD);
    UserService userService = UserServiceImpl.getInstance();
    String page;
    try {
      if (userService.authenticate(username, password)) {
        HttpSession session = request.getSession();
        session.setAttribute(AttributeName.USERNAME, username);
        page = PagePath.MAIN;
      } else {
        request.setAttribute(AttributeName.LOGIN_ERROR, "authentication failed");
        page = PagePath.INDEX;
      }
    } catch (ServiceException e) {
      logger.error("Login failed due to service error for user {}", username, e);
      request.setAttribute(AttributeName.LOGIN_ERROR, "internal error, please try later");
      page = PagePath.INDEX;
    }
    return page;
  }
}

