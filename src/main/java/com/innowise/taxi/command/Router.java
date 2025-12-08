package com.innowise.taxi.command;

public class Router {
  private final String page;
  private final TransitionType type;

  public Router(String page, TransitionType type) {
    this.page = page;
    this.type = type;
  }

  public String getPage() { return page; }
  public TransitionType getType() { return type; }

  public enum TransitionType {
    FORWARD, REDIRECT
  }
}
