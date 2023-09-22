package org.example.ormframework.repository;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public interface Repository<T,ID> {
    void save(T entity) throws SQLException, IllegalAccessException;
    void update(T entity) throws SQLException, IllegalAccessException;
    void delete(ID id) throws SQLException, IllegalAccessException;
    T findById(ID id) throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException;
    List<T> findAll() throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException;
}
