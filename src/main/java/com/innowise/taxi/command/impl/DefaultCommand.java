package com.innowise.taxi.command.impl;

import com.innowise.taxi.command.Command;
import com.innowise.taxi.command.constants.PagePath;
import jakarta.servlet.http.HttpServletRequest;
import com.innowise.taxi.command.constants.AttributeName;

public class DefaultCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        request.setAttribute(AttributeName.DEFAULT_ERROR, "Unknown command");
        return PagePath.INDEX;
    }
}
