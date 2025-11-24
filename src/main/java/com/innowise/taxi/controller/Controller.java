package com.innowise.taxi.controller;

import java.io.*;

import com.innowise.taxi.command.Command;
import com.innowise.taxi.command.CommandType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/controller")
public class Controller extends HttpServlet {

    public void init() {}

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");

        String commandStr = request.getParameter("command");
        Command command = CommandType.parse(commandStr);
        String page = command.execute(request);
        request.getRequestDispatcher(page).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    public void destroy() {
    }
}