package com.innowise.taxi.command.impl;

import com.innowise.taxi.command.Command;
import com.innowise.taxi.command.Router;
import com.innowise.taxi.constant.AttributeName;
import com.innowise.taxi.constant.PagePath;
import com.innowise.taxi.constant.ParameterName;
import com.innowise.taxi.entity.UserRole;
import com.innowise.taxi.entity.User;
import com.innowise.taxi.exception.ServiceException;
import com.innowise.taxi.service.UserService;
import com.innowise.taxi.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class LoginCommand implements Command {
  private static final Logger logger = LogManager.getLogger();

  @Override
  public Router execute(HttpServletRequest request) {
    String username = request.getParameter(ParameterName.USERNAME);
    String password = request.getParameter(ParameterName.PASSWORD);
    UserService userService = UserServiceImpl.getInstance();
    String page;

    try {
      Optional<User> userOptional = userService.authenticate(username, password);
      HttpSession session = request.getSession();

      if (userOptional.isPresent()) {
        User user = userOptional.get();
        session.setAttribute(AttributeName.USER_ID, user.getId());
        session.setAttribute(AttributeName.USERNAME, user.getUsername());
        session.setAttribute(AttributeName.FIRST_NAME, user.getFirstName());
        session.setAttribute(AttributeName.LAST_NAME, user.getLastName());
        session.setAttribute(AttributeName.BONUS_POINTS, user.getBonusPoints());
        session.setAttribute(AttributeName.BANNED, user.isBanned());
        UserRole role = user.getRole();
        session.setAttribute(AttributeName.ROLE, role);

        switch (role) {
          case CLIENT -> page = PagePath.CLIENT_MAIN;
          case DRIVER -> page = PagePath.DRIVER_MAIN;
          case ADMIN -> page = PagePath.ADMIN_MAIN;
          default -> page = PagePath.INDEX;
        }
      } else {
        session.setAttribute(AttributeName.LOGIN_ERROR, "Invalid username or password");
        page = PagePath.INDEX;
      }
    } catch (ServiceException e) {
      logger.error("Login failed due to service error for user {}", username, e);
      request.getSession().setAttribute(AttributeName.LOGIN_ERROR, "Internal error, please try later");
      page = PagePath.INDEX;
    }

    return new Router(page, Router.TransitionType.REDIRECT);
  }

}

