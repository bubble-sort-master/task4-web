package com.innowise.taxi.controller;

import java.io.*;

import com.innowise.taxi.command.Command;
import com.innowise.taxi.command.CommandType;
import com.innowise.taxi.command.Router;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "Servlet", value = "/controller")
public class Controller extends HttpServlet {
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

    if (router.getType() == Router.TransitionType.FORWARD) {
      request.getRequestDispatcher(router.getPage()).forward(request, response);
    } else {
      response.sendRedirect(request.getContextPath() + "/" + router.getPage());
    }
  }


  public void destroy() {
  }
}