package org.example.services;

import org.example.DILibrary.annotation.MyAutowired;
import org.example.repositories.NewsRepository;

public class NewsService {
    private final NewsRepository newsRepository;

    @MyAutowired
    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public void performAction() {
        newsRepository.saveNews();
    }
}
