package org.example.ormframework.repository.factory;


import org.example.ormframework.repository.CrudRepository;

public interface CrudRepositoryFactory {
    CrudRepository getRepository(Class<?> clazz);
}
