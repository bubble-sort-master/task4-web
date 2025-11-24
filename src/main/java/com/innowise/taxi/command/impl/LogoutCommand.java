package com.innowise.taxi.command.impl;

import com.innowise.taxi.command.Command;
import jakarta.servlet.http.HttpServletRequest;

public class LogoutCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        return "/index.jsp"; //TODO: //invalidate session
    }
}
