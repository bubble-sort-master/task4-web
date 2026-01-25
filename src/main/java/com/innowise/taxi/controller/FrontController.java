package com.innowise.taxi.controller;

import java.io.*;

import com.innowise.taxi.command.Command;
import com.innowise.taxi.command.CommandType;
import com.innowise.taxi.command.Router;
import com.innowise.taxi.util.constant.ResponseContent;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "Servlet", value = "/controller")
public class FrontController extends HttpServlet {
  public static final String COMMAND = "command";

  public void init() {}

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    processRequest(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    processRequest(request, response);
  }

  private void processRequest(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    String commandStr = request.getParameter(COMMAND);
    Command command = CommandType.parse(commandStr);
    Router router = command.execute(request);

    switch (router.getType()) {
      case FORWARD:
        request.getRequestDispatcher(router.getContent()).forward(request, response);
        break;
      case REDIRECT:
        response.sendRedirect(request.getContextPath() + "/" + router.getContent());
        break;
      case DATA:
        response.setContentType(ResponseContent.APPLICATION_JSON);
        response.setCharacterEncoding(ResponseContent.UTF8);
        response.getWriter().write(router.getContent());
        break;
    }
  }


  public void destroy() {
  }
}