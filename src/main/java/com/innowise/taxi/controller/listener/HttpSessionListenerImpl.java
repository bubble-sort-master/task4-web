package com.innowise.taxi.controller.listener;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebListener
public class HttpSessionListenerImpl implements HttpSessionListener {

  private static final Logger logger = LogManager.getLogger();

  @Override
  public void sessionCreated(HttpSessionEvent se) {
    logger.info("Session created: id={}", se.getSession().getId());
  }

  @Override
  public void sessionDestroyed(HttpSessionEvent se) {
    logger.info("Session destroyed: id={}", se.getSession().getId());
  }
}
