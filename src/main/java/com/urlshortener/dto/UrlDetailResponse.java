package com.urlshortener.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UrlDetailResponse {

    private String longUrl;

    private String shortUrl;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private int clickCount;

}
