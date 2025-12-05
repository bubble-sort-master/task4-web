package com.innowise.taxi.controller.filter;

import com.innowise.taxi.command.CommandType;
import com.innowise.taxi.constant.AttributeName;
import com.innowise.taxi.constant.PagePath;
import com.innowise.taxi.constant.ParameterName;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@WebFilter(filterName = "AuthenticationFilter",  urlPatterns = {"/*"})
public class AuthenticationFilter implements Filter {
  private static final Logger logger = LogManager.getLogger();

  public void init(FilterConfig config) throws ServletException {
  }

  public void destroy() {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
          throws ServletException, IOException {

    HttpServletRequest httpReq = (HttpServletRequest) request;
    HttpServletResponse httpResp = (HttpServletResponse) response;
    HttpSession session = httpReq.getSession(false);

    String uri = httpReq.getRequestURI();
    String command = httpReq.getParameter(ParameterName.COMMAND);
    boolean isLoginCommand = CommandType.LOGIN.name().equalsIgnoreCase(command);
    boolean isRegisterCommand = CommandType.REGISTER.name().equalsIgnoreCase(command);
    boolean isLogoutCommand = CommandType.LOGOUT.name().equalsIgnoreCase(command);
    boolean isLoginPage = uri.endsWith(PagePath.INDEX);
    boolean isRegisterPage = uri.contains(PagePath.REGISTER);

    if (session == null || session.getAttribute(AttributeName.USERNAME) == null) {
      if (isLoginPage || isRegisterPage || isLoginCommand || isRegisterCommand || isLogoutCommand) {
        chain.doFilter(request, response);
      } else {
        logger.info("Unauthorized access attempt to {}", uri);
        httpResp.sendRedirect(httpReq.getContextPath() + "/" + PagePath.INDEX);
      }
    } else {
      chain.doFilter(request, response);
    }
  }
}

