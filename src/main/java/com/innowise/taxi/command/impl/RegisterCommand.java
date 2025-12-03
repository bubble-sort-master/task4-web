package com.innowise.taxi.command.impl;

import com.innowise.taxi.command.Command;
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
    String username = request.getParameter("login");
    String password = request.getParameter("pass");
    String firstName = request.getParameter("firstName");
    String lastName = request.getParameter("lastName");
    logger.debug("accepted user input {}, {}, {}, {}", username, password, firstName, lastName);

    UserService userService = UserServiceImpl.getInstance();
    String page;
    try {
      User user = new User(username, password, firstName, lastName);
      boolean registered = userService.register(user);

      if (registered) {
        request.setAttribute("user", username);
        page = "pages/main.jsp";
      } else {
        logger.warn("Registration failed: user {} not saved", username);
        request.setAttribute("register_err", "registration failed");
        page = "pages/register.jsp";
      }
    } catch (ServiceException e) {
      //TODO: correct exception handling here and in related places
      logger.error("Registration failed for user {}", username, e);
      request.setAttribute("register_err", "internal error, please try later");
      page = "pages/register.jsp";
    }
    return page;
  }

}
