package com.innowise.taxi.controller.listener;

import com.innowise.taxi.pool.ConnectionPool;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebListener
public class ServletContextListenerImpl implements ServletContextListener {

  private static final Logger logger = LogManager.getLogger(ServletContextListenerImpl.class);

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    ConnectionPool pool = ConnectionPool.getInstance();
    logger.info("Connection pool initialized on context startup");
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    ConnectionPool pool = ConnectionPool.getInstance();
    pool.shutdown();
    logger.info("Connection pool destroyed on context shutdown");
  }
}
