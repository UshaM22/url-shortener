package com.urlshortener.controller;

import com.urlshortener.dto.ShortUrlRequest;
import com.urlshortener.dto.UrlDetailResponse;
import com.urlshortener.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping
public class UrlController {

    @Autowired
    private UrlService urlService;

    @PostMapping("/api/shorten")
    public ResponseEntity<String> shortenUrl(@Valid @RequestBody ShortUrlRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(urlService.createShortCode(request.getLongUrl()));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectLongUrl(@PathVariable String shortCode){

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(urlService.redirectToLongUrl(shortCode)));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);

    }

    @GetMapping("/api/mylinks")
    public ResponseEntity<List<UrlDetailResponse>> usersLinks(){
        return ResponseEntity.ok(urlService.urlDetailResponse());
    }



}
