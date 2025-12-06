package com.innowise.taxi.controller.filter;

import com.innowise.taxi.constant.AttributeName;
import com.innowise.taxi.constant.PagePath;
import com.innowise.taxi.entity.Role;
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

  private static final Map<Role, Set<String>> rolePages = new HashMap<>();
  private static final Set<String> publicPages = Set.of(PagePath.INDEX, PagePath.REGISTER);

  static {
    rolePages.put(Role.CLIENT, Set.of(PagePath.CLIENT_MAIN));
    rolePages.put(Role.DRIVER, Set.of(PagePath.DRIVER_MAIN));
    rolePages.put(Role.ADMIN, Set.of(PagePath.ADMIN_MAIN));
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
          throws IOException, ServletException {

    HttpServletRequest httpReq = (HttpServletRequest) request;
    HttpServletResponse httpResp = (HttpServletResponse) response;
    HttpSession session = httpReq.getSession(false);

    String command = httpReq.getParameter("command");
    boolean isLogoutCommand = "LOGOUT".equalsIgnoreCase(command);

    if (session != null && session.getAttribute(AttributeName.USERNAME) != null) {
      Role role = (Role) session.getAttribute(AttributeName.ROLE);
      String uri = httpReq.getRequestURI();
      Set<String> allowedPages = rolePages.getOrDefault(role, Set.of());

      if (uri.endsWith(PagePath.INDEX)) {
        if (!allowedPages.isEmpty()) {
          String mainPage = allowedPages.iterator().next();
          httpResp.sendRedirect(httpReq.getContextPath() + "/" + mainPage);
          return;
        }
      }

      boolean allowed = allowedPages.stream().anyMatch(uri::endsWith)
              || publicPages.stream().anyMatch(uri::endsWith)
              || isLogoutCommand;

      if (!allowed) {
        logger.warn("Access denied: role {} tried to access {}", role, uri);
        if (!allowedPages.isEmpty()) {
          String mainPage = allowedPages.iterator().next();
          httpResp.sendRedirect(httpReq.getContextPath() + "/" + mainPage);
        } else {
          httpResp.sendRedirect(httpReq.getContextPath() + "/" + PagePath.INDEX);
        }
        return;
      }
    }

    chain.doFilter(request, response);
  }

}


