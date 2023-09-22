package org.example.services;

import org.example.ormframework.repository.factory.RepositoryFactory;
import org.example.models.AdArticle;
import org.example.repositories.AdArticleRepository;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class AdArticleService {
    private final AdArticleRepository adArticleRepository;

    public AdArticleService() throws SQLException, IOException, ClassNotFoundException {
        Class<?> interfaceToImplement = AdArticleRepository.class;
        this.adArticleRepository = (AdArticleRepository) RepositoryFactory.createRepoImpl(interfaceToImplement);
    }

    public List<AdArticle> getAll() throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        return adArticleRepository.findAll();
    }
}
