package com.example.urlshortenerservice.dto;

import jakarta.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.URL;

public class ShortenUrlRequest {

    @NotBlank
    @URL
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
