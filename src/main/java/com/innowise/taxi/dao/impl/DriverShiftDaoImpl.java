package com.innowise.taxi.dao.impl;

import com.innowise.taxi.dao.DriverShiftDao;
import com.innowise.taxi.entity.DriverShift;
import com.innowise.taxi.entity.DriverShiftStatus;
import com.innowise.taxi.exception.DaoException;
import com.innowise.taxi.pool.ConnectionPool;
import com.innowise.taxi.util.LocationGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Optional;

public class DriverShiftDaoImpl implements DriverShiftDao {
  private static final Logger logger = LogManager.getLogger();
  private static final String SELECT_ACTIVE_BY_DRIVER = """
        SELECT id, driver_id, car_id, start_time, end_time, status, current_lat, current_lon
        FROM driver_shifts
        WHERE driver_id = ? AND status = 'ACTIVE'
        LIMIT 1
    """;
  private static final String UPDATE_STATUS_TO_CLOSED = """
        UPDATE driver_shifts
        SET status = 'CLOSED', end_time = NOW()
        WHERE id = ?
    """;
  private static final String SQL_INSERT_SHIFT = """
    INSERT INTO driver_shifts (driver_id, car_id, start_time, current_lat, current_lon)
    SELECT ?, c.id, NOW(), ?, ?
    FROM cars c
    WHERE c.id NOT IN (
        SELECT car_id FROM driver_shifts WHERE status = 'ACTIVE'
    )
    LIMIT 1
    """;
  private static final String SQL_SELECT_SHIFT_BY_ID = """
    SELECT id, driver_id, car_id, start_time, end_time, status, current_lat, current_lon
    FROM driver_shifts
    WHERE id = ?
    """;

  @Override
  public Optional<DriverShift> findActiveByDriverId(int driverId) throws DaoException {
    Connection connection = null;
    try {
      connection = ConnectionPool.getInstance().getConnection();
      try (PreparedStatement statement = connection.prepareStatement(SELECT_ACTIVE_BY_DRIVER)) {
        statement.setInt(1, driverId);
        try (ResultSet rs = statement.executeQuery()) {
          if (rs.next()) {
            DriverShift shift = mapRow(rs);
            return Optional.of(shift);
          }
        }
      }
    } catch (SQLException e) {
      logger.error("Error finding active shift for driver {}", driverId, e);
      throw new DaoException(e);
    } finally {
      ConnectionPool.getInstance().releaseConnection(connection);
    }
    return Optional.empty();
  }

  @Override
  public boolean updateStatusToClosed(int shiftId) throws DaoException {
    Connection connection = null;
    try {
      connection = ConnectionPool.getInstance().getConnection();
      try (PreparedStatement statement = connection.prepareStatement(UPDATE_STATUS_TO_CLOSED)) {
        statement.setInt(1, shiftId);
        int rows = statement.executeUpdate();
        return rows > 0;
      }
    } catch (SQLException e) {
      logger.error("Error closing shift {}", shiftId, e);
      throw new DaoException(e);
    } finally {
      ConnectionPool.getInstance().releaseConnection(connection);
    }
  }

  @Override
  public Optional<DriverShift> findById(int shiftId) throws DaoException {
    Connection connection = null;
    try {
      connection = ConnectionPool.getInstance().getConnection();
      try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_SHIFT_BY_ID)) {
        statement.setInt(1, shiftId);
        try (ResultSet rs = statement.executeQuery()) {
          if (rs.next()) {
            return Optional.of(mapRow(rs));
          }
        }
      }
    } catch (SQLException e) {
      throw new DaoException(e);
    } finally {
      ConnectionPool.getInstance().releaseConnection(connection);
    }
    return Optional.empty();
  }

  @Override
  public Optional<Integer> insertShift(int driverId) throws DaoException {
    Connection connection = null;
    try {
      connection = ConnectionPool.getInstance().getConnection();
      try (PreparedStatement ps = connection.prepareStatement(SQL_INSERT_SHIFT, Statement.RETURN_GENERATED_KEYS)) {
        int latitude = LocationGenerator.nextLatitude();
        int longitude = LocationGenerator.nextLongitude();

        ps.setInt(1, driverId);
        ps.setInt(2, latitude);
        ps.setInt(3, longitude);

        int affected = ps.executeUpdate();
        if (affected == 0) {
          return Optional.empty();
        }
        try (ResultSet keys = ps.getGeneratedKeys()) {
          if (keys.next()) {
            return Optional.of(keys.getInt(1));
          }
        }
      }
    } catch (SQLIntegrityConstraintViolationException e) {
      return Optional.empty();
    } catch (SQLException e) {
      throw new DaoException(e);
    } finally {
      ConnectionPool.getInstance().releaseConnection(connection);
    }
    return Optional.empty();
  }


  private DriverShift mapRow(ResultSet rs) throws SQLException {
    return new DriverShift(
            rs.getInt(ID),
            rs.getInt(DRIVER_ID),
            rs.getInt(CAR_ID),
            rs.getTimestamp(START_TIME).toLocalDateTime(),
            rs.getTimestamp(END_TIME) != null
                    ? rs.getTimestamp(END_TIME).toLocalDateTime()
                    : null,
            DriverShiftStatus.valueOf(rs.getString(STATUS)),
            rs.getInt(CURRENT_LAT),
            rs.getInt(CURRENT_LON)
    );
  }
}
