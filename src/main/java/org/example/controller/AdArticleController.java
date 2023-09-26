package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dilibrary.annotation.Controller;
import org.example.dilibrary.annotation.Autowired;
import org.example.models.AdArticle;
import org.example.restapi.annotation.GetMapping;
import org.example.restapi.annotation.PathVariable;
import org.example.restapi.annotation.PostMapping;
import org.example.restapi.annotation.RequestBody;
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
        List<AdArticle> adArticleList = adArticleService.getAllAd();
        return objectMapper.writeValueAsString(adArticleList);
    }

    @GetMapping(value = "/adArticles/{guid}")
    public String getAdByID(@PathVariable("guid") String guid) throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, JsonProcessingException {
        AdArticle adArticleList = adArticleService.findAdByID(guid);
        return objectMapper.writeValueAsString(adArticleList);
    }

    @PostMapping(value = "/add-adArticle")
    public String addAdArticle(@RequestBody String requestBody) throws SQLException, IllegalAccessException, JsonProcessingException {
        AdArticle adArticle = objectMapper.readValue(requestBody, AdArticle.class);
        adArticleService.addAdArticle(adArticle);
        return "Add adArticle successfully";
    }
}
