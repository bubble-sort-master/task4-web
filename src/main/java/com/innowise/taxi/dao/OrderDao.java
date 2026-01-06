package com.innowise.taxi.dao;

import com.innowise.taxi.entity.Order;
import com.innowise.taxi.entity.OrderStatus;
import com.innowise.taxi.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
  String ID = "id";
  String CLIENT_ID = "client_id";
  String DRIVER_SHIFT_ID = "driver_shift_id";
  String STATUS = "status";
  String PICKUP_LAT = "pickup_lat";
  String PICKUP_LON = "pickup_lon";
  String DROPOFF_LAT = "dropoff_lat";
  String DROPOFF_LON = "dropoff_lon";
  String PRICE = "price";
  String IS_PAID = "is_paid";
  String CREATED_AT = "created_at";

  Order insert(Order order) throws DaoException;
  Optional<Order> findById(int id) throws DaoException;
  List<Order> findByDriverShiftId(int driverShiftId) throws DaoException;
  boolean updateStatus(int id, OrderStatus status) throws DaoException;
  boolean setDriverShift(int id, int driverShiftId) throws DaoException;
  boolean updatePayment(int orderId) throws DaoException;
}
