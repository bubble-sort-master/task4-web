package com.innowise.taxi.controller.listener;

import jakarta.servlet.http.HttpSessionAttributeListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionBindingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebListener
public class SessionAttributeListener implements HttpSessionAttributeListener {

  private static final Logger logger = LogManager.getLogger();

  @Override
  public void attributeAdded(HttpSessionBindingEvent sbe) {
    logger.info("Session attribute added: name={}, value={}", sbe.getName(), sbe.getValue());
  }

  @Override
  public void attributeRemoved(HttpSessionBindingEvent sbe) {
    logger.info("Session attribute removed: name={}, value={}", sbe.getName(), sbe.getValue());
  }

  @Override
  public void attributeReplaced(HttpSessionBindingEvent sbe) {
    var newValue = sbe.getSession().getAttribute(sbe.getName());
    logger.info("Session attribute replaced: name={}, oldValue={}, newValue={}",
            sbe.getName(), sbe.getValue(), newValue);
  }
}
