package org.example.ORM.repository.factory;


import org.example.ORM.repository.CrudRepository;

public interface CrudRepositoryFactory {
    CrudRepository getRepository(Class<?> clazz);
}
