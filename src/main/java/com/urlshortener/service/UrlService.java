package com.urlshortener.service;

import com.urlshortener.dto.UrlDetailResponse;
import com.urlshortener.model.UrlDetail;
import com.urlshortener.model.User;
import com.urlshortener.repository.UrlDetailRepository;
import com.urlshortener.repository.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class UrlService {

    @Autowired
    private UrlDetailRepository urlDetailRepository;
    @Autowired
    private UserDetailRepository userDetailRepository;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${app.default-expiry-days}")
    private int expiryDays;


    public String createShortCode(String longUrl) {
        int leftLimit = 48; // numeral 0
        int rightLimit = 122; // letter z
        int targetStringLength = 6;
        String generateShortCode;
        boolean codeExists;
        Random random = new Random();

        do {
            generateShortCode = random.ints(leftLimit, rightLimit + 1)
                    .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
            codeExists = urlDetailRepository.existsByShortCode(generateShortCode);

        } while (codeExists);

        UrlDetail urlDetail = new UrlDetail();
        String shortCode = generateShortCode;
        urlDetail.setShortCode(shortCode);
        LocalDateTime createdAt = LocalDateTime.now();
        urlDetail.setCreatedAt(createdAt);
        urlDetail.setExpiresAt(createdAt.plusDays(expiryDays));
        urlDetail.setLongUrl(longUrl);
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailRepository.findByUserName(currentUsername).orElseThrow();
        urlDetail.setUser(user);
        UrlDetail saved = urlDetailRepository.save(urlDetail);

        return baseUrl + "/" + shortCode;


    }

    public String redirectToLongUrl(String shortCode){
        UrlDetail urlDetail = urlDetailRepository.findByShortCode(shortCode).orElseThrow();
        if(LocalDateTime.now().isAfter(urlDetail.getExpiresAt()))
        {
            throw new RuntimeException("The Url is expired");
        }
        urlDetail.setClickCount(urlDetail.getClickCount()+1);
        urlDetailRepository.save(urlDetail);
        return urlDetail.getLongUrl();
    }

    public List<UrlDetailResponse> urlDetailResponse (){
        ArrayList<UrlDetailResponse> resultList = new ArrayList<>();
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailRepository.findByUserName(currentUsername).orElseThrow();
        List<UrlDetail> userUrls= urlDetailRepository.findByUser(user);
        int i = 0;
        while(i< userUrls.size()){

            UrlDetailResponse response = new UrlDetailResponse();
            response.setLongUrl(userUrls.get(i).getLongUrl());
            response.setCreatedAt(userUrls.get(i).getCreatedAt());
            response.setExpiresAt(userUrls.get(i).getExpiresAt());
            response.setClickCount(userUrls.get(i).getClickCount());
            String shortcode = userUrls.get(i).getShortCode();
            String shortUrl = baseUrl + "/" + shortcode;
            response.setShortUrl(shortUrl);
            resultList.add(response);
            i++;

        }

        return resultList;
    }
}
