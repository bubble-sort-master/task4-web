package com.innowise.taxi.dao;

import com.innowise.taxi.entity.AbstractEntity;
import com.innowise.taxi.exception.DaoException;

import java.util.List;
import java.util.Optional;

public abstract class AbstractDao<T extends AbstractEntity> {
  public abstract boolean insert(T entity) throws DaoException;
  public abstract boolean delete(T entity) throws DaoException;
  public abstract List<T> findAll() throws DaoException;
  public abstract Optional<T> findById(Long id) throws DaoException;
  public abstract T update(T entity) throws DaoException;
}


