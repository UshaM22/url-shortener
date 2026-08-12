package com.urlshortener.service;

import com.urlshortener.exception.ShortCodeNotFoundException;
import com.urlshortener.model.UrlDetail;
import com.urlshortener.repository.UrlDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;



@Service
public class UrlLookupService {

    @Autowired
    private UrlDetailRepository urlDetailRepository;

    @Cacheable(value = "urls", key = "#shortCode")
    public UrlDetail getByShortCode(String shortCode){
        UrlDetail urlDetail = urlDetailRepository.findByShortCode(shortCode).orElseThrow(() -> new ShortCodeNotFoundException("This short link does not exist or has expired"));
        return urlDetail;
    }


}
