package org.example.controller;

import org.example.dilibrary.annotation.Controller;
import org.example.dilibrary.annotation.MyAutowired;
import org.example.models.AdArticle;
import org.example.services.AdArticleService;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

@Controller
public class AdArticleController {
    private final AdArticleService adArticleService;

    @MyAutowired
    public AdArticleController(AdArticleService adArticleService) {
        this.adArticleService = adArticleService;
    }

    public List<AdArticle> getAllAd() throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        return adArticleService.getAll();
    }
}
