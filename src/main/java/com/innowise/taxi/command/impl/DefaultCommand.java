package com.innowise.taxi.command.impl;

import com.innowise.taxi.command.Command;
import com.innowise.taxi.command.Router;
import com.innowise.taxi.util.constant.PagePath;
import jakarta.servlet.http.HttpServletRequest;
import com.innowise.taxi.util.constant.AttributeName;

public class DefaultCommand implements Command {
    @Override
    public Router execute(HttpServletRequest request) {
        request.setAttribute(AttributeName.DEFAULT_ERROR, "Unknown command");
        return new Router(PagePath.INDEX, Router.TransitionType.REDIRECT);
    }
}
