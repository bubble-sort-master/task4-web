package com.innowise.taxi.command;

import jakarta.servlet.http.HttpServletRequest;

import java.net.http.HttpRequest;

public interface Command {
    String execute(HttpServletRequest request);
}
