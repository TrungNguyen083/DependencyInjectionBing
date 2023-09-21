package org.example.repositories;

import org.example.DILibrary.annotation.MyAutowired;

public class NewsRepository {
    private final CrudRepositoryCustom crudRepository;
    @MyAutowired
    public NewsRepository(CrudRepositoryCustom crudRepository) {
        this.crudRepository = crudRepository;
    }

    public void saveNews() {
        crudRepository.save();
    }
}
