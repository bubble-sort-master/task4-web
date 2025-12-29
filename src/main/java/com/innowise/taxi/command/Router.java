package com.innowise.taxi.command;

public class Router {
  private final String content;
  private final TransitionType type;

  public Router(String page, TransitionType type) {
    this.content = page;
    this.type = type;
  }

  public String getContent() { return content; }
  public TransitionType getType() { return type; }

  public enum TransitionType {
    FORWARD, REDIRECT, DATA
  }
}
