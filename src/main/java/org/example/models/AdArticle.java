package org.example.models;


import org.example.ormframework.annotation.Column;
import org.example.ormframework.annotation.Entity;
import org.example.ormframework.annotation.Primary;

@Entity(name = "adarticle")
public class AdArticle {
    @Primary
    @Column(name = "guid")
    private String guid;

    @Column(name = "adImage")
    private String adImage;

    @Column(name = "adTitle")
    private String adTitle;

    @Column(name = "adSource")
    private String adSource;

    @Column(name = "adLink")
    private String adLink;

    public AdArticle() {
    }

    public AdArticle(String adImage, String adTitle, String adSource, String adLink) {
        this.adImage = adImage;
        this.adTitle = adTitle;
        this.adSource = adSource;
        this.adLink = adLink;
    }

    public AdArticle(String guid, String adImage, String adTitle, String adSource, String adLink) {
        this.guid = guid;
        this.adImage = adImage;
        this.adTitle = adTitle;
        this.adSource = adSource;
        this.adLink = adLink;
    }

    public String getGuid() {
        return guid;
    }

    public String getAdImage() {
        return adImage;
    }

    public String getAdTitle() {
        return adTitle;
    }

    public String getAdSource() {
        return adSource;
    }

    public String getAdLink() {
        return adLink;
    }
}
