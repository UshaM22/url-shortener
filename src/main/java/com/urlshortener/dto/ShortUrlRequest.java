package com.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class ShortUrlRequest {

    @URL @NotBlank
    private String longUrl;
}
