package org.example.repositories;

import org.example.CrudRepository;
import org.example.MyAutowired;

public class UserRepository {
    @MyAutowired
    CrudRepository crudRepository;
    public void saveUser() {
        crudRepository.save();
    }
}
