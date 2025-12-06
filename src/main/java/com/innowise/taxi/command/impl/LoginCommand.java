package com.innowise.taxi.command.impl;

import com.innowise.taxi.auth.AuthResult;
import com.innowise.taxi.command.Command;
import com.innowise.taxi.constant.AttributeName;
import com.innowise.taxi.constant.PagePath;
import com.innowise.taxi.constant.ParameterName;
import com.innowise.taxi.entity.Role;
import com.innowise.taxi.entity.User;
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
      AuthResult result = userService.authenticate(username, password);

      switch (result.getType()) {
        case SUCCESS -> {
          User user = result.getUser();
          HttpSession session = request.getSession();
          session.setAttribute(AttributeName.USER_ID, user.getId());
          session.setAttribute(AttributeName.USERNAME, user.getUsername());
          session.setAttribute(AttributeName.FIRST_NAME, user.getFirstName());
          session.setAttribute(AttributeName.LAST_NAME, user.getLastName());
          Role role = Role.valueOf(user.getRole().toUpperCase());
          session.setAttribute(AttributeName.ROLE, role);

          switch (role) {
            case CLIENT -> page = PagePath.CLIENT_MAIN;
            case DRIVER -> page = PagePath.DRIVER_MAIN;
            case ADMIN -> page = PagePath.ADMIN_MAIN;
            default -> page = PagePath.INDEX;
          }
        }
        case WRONG_PASSWORD -> {
          request.setAttribute(AttributeName.LOGIN_ERROR, "Wrong password");
          page = PagePath.INDEX;
        }
        case USER_NOT_FOUND -> {
          request.setAttribute(AttributeName.LOGIN_ERROR, "User not found");
          page = PagePath.INDEX;
        }
        default -> page = PagePath.INDEX;
      }
    } catch (ServiceException e) {
      logger.error("Login failed due to service error for user {}", username, e);
      request.setAttribute(AttributeName.LOGIN_ERROR, "internal error, please try later");
      page = PagePath.INDEX;
    }

    return page;
  }

}

