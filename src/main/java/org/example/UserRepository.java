package org.example;

public class UserRepository {
    @MyAutowired
    CrudRepository crudRepository;
    public void saveUser() {
        crudRepository.save();
    }
}
