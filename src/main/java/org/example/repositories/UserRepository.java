package org.example.repositories;

import org.example.DILibrary.annotation.MyAutowired;

public class UserRepository {
    @MyAutowired
    CrudRepositoryCustom crudRepository;
    public void saveUser() {
        crudRepository.save();
    }
}
