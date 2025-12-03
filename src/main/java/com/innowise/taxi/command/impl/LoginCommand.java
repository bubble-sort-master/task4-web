package com.innowise.taxi.command.impl;

import com.innowise.taxi.command.Command;
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
    String login = request.getParameter("login");
    String password = request.getParameter("pass");
    UserService userService = UserServiceImpl.getInstance();
    String page;
    try {
      if (userService.authenticate(login, password)) {
        request.setAttribute("user", login);
        page = "pages/main.jsp";
      } else {
        request.setAttribute("login_err", "authentication failed");
        page = "index.jsp";
      }
    } catch (ServiceException e) {
      logger.error("Login failed due to service error for user {}", login, e);
      request.setAttribute("login_err", "internal error, please try later");
      page = "index.jsp";
    }
    return page;
  }
}

