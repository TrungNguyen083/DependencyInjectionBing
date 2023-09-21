package org.example.repositories;

import org.example.CrudRepository;
import org.example.MyAutowired;

public class NewsRepository {
    private final CrudRepository crudRepository;
    @MyAutowired
    public NewsRepository(CrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    public void saveNews() {
        crudRepository.save();
    }
}
