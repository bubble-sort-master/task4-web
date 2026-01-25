package com.innowise.taxi.command.impl;

import com.google.gson.Gson;
import com.innowise.taxi.command.Command;
import com.innowise.taxi.command.Router;
import com.innowise.taxi.util.constant.ParameterName;
import com.innowise.taxi.exception.ServiceException;
import com.innowise.taxi.service.UserService;
import com.innowise.taxi.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Map;

public class UserStatusCommand implements Command {
  private static final String BAN_USER = "ban";
  private static final String UNBAN_USER = "unban";
  private static final Logger logger = LogManager.getLogger();

  @Override
  public Router execute(HttpServletRequest request) {
    UserService userService = UserServiceImpl.getInstance();

    String action = request.getParameter(ParameterName.ACTION);
    String userIdStr = request.getParameter(ParameterName.USER_ID);

    int userId;
    try {
      userId = Integer.parseInt(userIdStr);
    } catch (NumberFormatException e) {
      return jsonResponse(false, "Invalid userId");
    }

    boolean banned;
    if (action.equalsIgnoreCase(BAN_USER)) {
      banned = true;
    } else if (action.equalsIgnoreCase(UNBAN_USER)) {
      banned = false;
    } else {
      return jsonResponse(false, "Unknown action");
    }

    try {
      boolean updated = userService.setBanned(userId, banned);
      return jsonResponse(updated, null);
    } catch (ServiceException e) {
      logger.error("Failed to update user status", e);
      return jsonResponse(false, "Internal error");
    }
  }

  private Router jsonResponse(boolean success, String message) {
    Map<String, Object> map = (message == null)
            ? Map.of("success", success)
            : Map.of("success", success, "message", message);

    return new Router(new Gson().toJson(map), Router.TransitionType.DATA);
  }
}