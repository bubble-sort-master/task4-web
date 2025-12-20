package com.innowise.taxi;

import com.innowise.taxi.entity.DriverShift;
import com.innowise.taxi.pool.ConnectionPool;
import com.innowise.taxi.service.DriverShiftService;
import com.innowise.taxi.service.impl.DriverShiftServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class DriverShiftStartTest {

  private static final int DRIVER_START_ID = 100;
  private static final int DRIVER_END_ID = 114;
  private static final int DRIVER_COUNT = DRIVER_END_ID - DRIVER_START_ID + 1;
  private static final String USER_TEST_NAME = "testDriver";
  private static final String USER_TEST_PASSWORD = "password";

  private static final String SQL_INSERT_DRIVER =
          "INSERT INTO users (id, username, password, first_name, role) VALUES (?, ?, ?, ?, 'DRIVER')";
  private static final String SQL_DELETE_TEST_DRIVERS =
          "DELETE FROM users WHERE id BETWEEN ? AND ?";
  private static final String SQL_COUNT_ACTIVE_SHIFTS =
          "SELECT COUNT(*) FROM driver_shifts WHERE status='ACTIVE'";
  private static final String SQL_COUNT_CARS =
          "SELECT COUNT(*) FROM cars";

  private final DriverShiftService shiftService = DriverShiftServiceImpl.getInstance();

  @BeforeEach
  void setUpTestDrivers() throws Exception {
    try (Connection conn = ConnectionPool.getInstance().getConnection();
         PreparedStatement ps = conn.prepareStatement(SQL_INSERT_DRIVER)) {
      for (int id = DRIVER_START_ID; id <= DRIVER_END_ID; id++) {
        ps.setInt(1, id);
        ps.setString(2, USER_TEST_NAME + id);
        ps.setString(3, USER_TEST_PASSWORD + id);
        ps.setString(4, USER_TEST_NAME);
        ps.executeUpdate();
      }
    }
  }

  @AfterEach
  void cleanUpTestDrivers() throws Exception {
    try (Connection conn = ConnectionPool.getInstance().getConnection();
         PreparedStatement ps = conn.prepareStatement(SQL_DELETE_TEST_DRIVERS)) {
      ps.setInt(1, DRIVER_START_ID);
      ps.setInt(2, DRIVER_END_ID);
      ps.executeUpdate();
    }
  }

  @Test
  void testAllFreeCarsAreAssignedToShifts() throws Exception {
    // given
    ExecutorService executor = Executors.newFixedThreadPool(DRIVER_COUNT);
    List<Future<Optional<DriverShift>>> results = new ArrayList<>();
    for (int id = DRIVER_START_ID; id <= DRIVER_END_ID; id++) {
      final int driverId = id;
      results.add(executor.submit(() -> shiftService.startDriverShift(driverId)));
    }
    executor.shutdown();
    executor.awaitTermination(10, TimeUnit.SECONDS);

    // then
    try (Connection conn = ConnectionPool.getInstance().getConnection();
         PreparedStatement psActive = conn.prepareStatement(SQL_COUNT_ACTIVE_SHIFTS);
         PreparedStatement psCars = conn.prepareStatement(SQL_COUNT_CARS)) {

      ResultSet rsActive = psActive.executeQuery();
      rsActive.next();
      int activeShiftsInDb = rsActive.getInt(1);

      ResultSet rsCars = psCars.executeQuery();
      rsCars.next();
      int totalCars = rsCars.getInt(1);

      assertEquals(totalCars, activeShiftsInDb,
              "Not all free cars were assigned to shifts!");
    }
  }

}


