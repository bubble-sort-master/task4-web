package com.innowise.taxi.command.impl;

import com.innowise.taxi.command.Command;
import com.innowise.taxi.service.UserService;
import com.innowise.taxi.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;

public class LoginCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        String login = request.getParameter("login");
        String password = request.getParameter("pass");
        UserService userService = UserServiceImpl.getInstance();
        String page;
        if(userService.authentificate(login, password)){
            request.setAttribute("user", login);
            page = "pages/main.jsp";
        }
        else{
            request.setAttribute("login_err", "authentication failed");
            page = "index.jsp";
        }
        return page;
    }
}
