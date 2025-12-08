package com.innowise.taxi.command.impl;

import com.innowise.taxi.command.Command;
import com.innowise.taxi.command.Router;
import com.innowise.taxi.entity.User;
import com.innowise.taxi.exception.ServiceException;
import com.innowise.taxi.service.UserService;
import com.innowise.taxi.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.innowise.taxi.constant.AttributeName;
import com.innowise.taxi.constant.PagePath;

import java.util.List;

public class ShowUsersCommand implements Command {
  private static final Logger logger = LogManager.getLogger();

  @Override
  public Router execute(HttpServletRequest request) {
    UserService userService = UserServiceImpl.getInstance();
    String page;
    try {
      List<User> users = userService.findAllUsers();
      request.setAttribute(AttributeName.USERS, users);
      page = PagePath.ADMIN_USER_LIST;
      logger.info("Admin requested user list, {} users found", users.size());
    } catch (ServiceException e) {
      logger.error("Failed to load user list for admin", e);
      request.setAttribute(AttributeName.ADMIN_ERROR, "internal error, please try later");
      page = PagePath.ADMIN_MAIN;
    }
    return new Router(page, Router.TransitionType.FORWARD);
  }
}
