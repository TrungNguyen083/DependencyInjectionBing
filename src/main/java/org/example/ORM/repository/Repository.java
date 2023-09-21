package org.example.ORM.repository;

import java.util.List;

public interface Repository<T,ID> {
    void save(T entity) throws Exception;
    void update(T entity) throws Exception;
    void delete(ID id) throws Exception;
    T findById(ID id) throws Exception;
    List<T> findAll() throws Exception;
}
