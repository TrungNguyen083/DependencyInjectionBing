package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dilibrary.annotation.Controller;
import org.example.dilibrary.annotation.Autowired;
import org.example.models.AdArticle;
import org.example.restapi.annotation.GetMapping;
import org.example.services.AdArticleService;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

@Controller
public class AdArticleController {
    private final AdArticleService adArticleService;
    ObjectMapper objectMapper;

    @Autowired
    public AdArticleController(AdArticleService adArticleService) {
        this.adArticleService = adArticleService;
        objectMapper = new ObjectMapper();
    }

    @GetMapping(value = "/adArticles")
    public String getAllAd() throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, JsonProcessingException {
        List<AdArticle> adArticleList = adArticleService.getAll();
        return objectMapper.writeValueAsString(adArticleList);
    }
}
