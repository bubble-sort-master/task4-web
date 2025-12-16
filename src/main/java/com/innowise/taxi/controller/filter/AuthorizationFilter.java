package com.innowise.taxi.controller.filter;

import com.innowise.taxi.constant.AttributeName;
import com.innowise.taxi.constant.PagePath;
import com.innowise.taxi.constant.ParameterName;
import com.innowise.taxi.entity.UserRole;
import com.innowise.taxi.command.CommandType;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@WebFilter(filterName = "AuthorizationFilter", urlPatterns = {"/*"})
public class AuthorizationFilter implements Filter {
  private static final Logger logger = LogManager.getLogger();

  private static final Map<UserRole, String> roleMainPage = new HashMap<>();

  private static final Map<UserRole, Set<String>> rolePages = new HashMap<>();
  private static final Map<UserRole, Set<String>> roleCommands = new HashMap<>();

  private static final Set<String> publicPages = Set.of(PagePath.INDEX);
  private static final Set<String> publicCommands = Set.of(
          CommandType.LOGOUT.name()
  );

  static {
    roleMainPage.put(UserRole.CLIENT, PagePath.CLIENT_MAIN);
    roleMainPage.put(UserRole.DRIVER, PagePath.DRIVER_MAIN);
    roleMainPage.put(UserRole.ADMIN, PagePath.ADMIN_MAIN);

    rolePages.put(UserRole.CLIENT, Set.of(PagePath.CLIENT_MAIN));
    rolePages.put(UserRole.DRIVER, Set.of(PagePath.DRIVER_MAIN));
    rolePages.put(UserRole.ADMIN, Set.of(PagePath.ADMIN_MAIN, PagePath.ADMIN_USER_LIST, PagePath.ADMIN_CAR_LIST));

    roleCommands.put(UserRole.ADMIN, Set.of(
            CommandType.SHOW_USERS.name(),
            CommandType.SHOW_CARS.name()
    ));
    roleCommands.put(UserRole.DRIVER, Set.of(
            CommandType.DRIVER_SHIFT.name()
    ));
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
          throws IOException, ServletException {

    HttpServletRequest httpReq = (HttpServletRequest) request;
    HttpServletResponse httpResp = (HttpServletResponse) response;
    HttpSession session = httpReq.getSession(false);

    String command = httpReq.getParameter(ParameterName.COMMAND);
    String uri = httpReq.getRequestURI();

    if (session != null && session.getAttribute(AttributeName.USERNAME) != null) {
      UserRole role = (UserRole) session.getAttribute(AttributeName.ROLE);
      Set<String> allowedPages = rolePages.getOrDefault(role, Set.of());
      Set<String> allowedCommands = roleCommands.getOrDefault(role, Set.of());
      String mainPage = roleMainPage.getOrDefault(role, PagePath.INDEX);

      if (uri.endsWith(PagePath.INDEX)
              || uri.endsWith(PagePath.REGISTER)
              || (command != null && (command.equalsIgnoreCase(CommandType.LOGIN.name())
              || command.equalsIgnoreCase(CommandType.REGISTER.name())))) {
        httpResp.sendRedirect(httpReq.getContextPath() + "/" + mainPage);
        return;
      }


      boolean allowed = allowedPages.stream().anyMatch(uri::endsWith)
              || publicPages.stream().anyMatch(uri::endsWith)
              || (command != null && allowedCommands.contains(command.toUpperCase()))
              || (command != null && publicCommands.contains(command.toUpperCase()));

      if (!allowed) {
        logger.warn("Access denied: role {} tried to access {}", role, uri);
        httpResp.sendRedirect(httpReq.getContextPath() + "/" + mainPage);
        return;
      }
    }

    chain.doFilter(request, response);
  }
}
