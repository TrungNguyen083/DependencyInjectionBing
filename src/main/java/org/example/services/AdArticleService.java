package org.example.services;

import org.example.ormframework.repository.factory.RepositoryFactory;
import org.example.models.AdArticle;
import org.example.repositories.AdArticleRepository;

import java.util.List;

public class AdArticleService {
    private final AdArticleRepository adArticleRepository;

    public AdArticleService() throws Exception {
        Class<?> interfaceToImplement = AdArticleRepository.class;
        this.adArticleRepository = (AdArticleRepository) RepositoryFactory.createRepoImpl(interfaceToImplement);
    }

    public List<AdArticle> getAll() throws Exception {
        return adArticleRepository.findAll();
    }
}
