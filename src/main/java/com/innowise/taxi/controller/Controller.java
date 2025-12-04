package com.innowise.taxi.controller;

import java.io.*;

import com.innowise.taxi.command.Command;
import com.innowise.taxi.command.CommandType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "Servlet", value = "/controller")
public class Controller extends HttpServlet {
  public static final String COMMAND = "command";

  public void init() {}

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");
    String commandStr = request.getParameter(COMMAND);
    Command command = CommandType.parse(commandStr);
    String page = command.execute(request);
    request.getRequestDispatcher(page).forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");
    String commandStr = request.getParameter(COMMAND);
    Command command = CommandType.parse(commandStr);
    String page = command.execute(request);
    request.getRequestDispatcher(page).forward(request, response);
  }

  public void destroy() {
  }
}