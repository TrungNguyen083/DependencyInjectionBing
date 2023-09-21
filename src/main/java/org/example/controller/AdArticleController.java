package org.example.controller;

import org.example.DILibrary.annotation.MyAutowired;
import org.example.models.AdArticle;
import org.example.services.AdArticleService;

import java.util.List;

public class AdArticleController {
    private final AdArticleService adArticleService;

    @MyAutowired
    public AdArticleController(AdArticleService adArticleService) {
        this.adArticleService = adArticleService;
    }

    public List<AdArticle> getAllAd() throws Exception {
        return adArticleService.getAll();
    }
}
