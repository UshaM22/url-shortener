package com.urlshortener;

import com.urlshortener.model.UrlDetail;
import com.urlshortener.repository.UrlDetailRepository;
import com.urlshortener.service.UrlLookupService;
import com.urlshortener.service.UrlService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UrlServiceTest {

    @Mock
    private UrlLookupService urlLookupService;

    @Mock
    private UrlDetailRepository urlDetailRepository;

    @InjectMocks
    private UrlService urlService;

    @Test
    void redirectToLongUrl_validCode_returnsLongUrl(){
        String shortCode = "abc123";
        UrlDetail urlDetail = new UrlDetail();
        urlDetail.setLongUrl("https://google.com");
        urlDetail.setExpiresAt(LocalDateTime.now().plusDays(1));

        when(urlLookupService.getByShortCode("abc123")).thenReturn(urlDetail);
        String result = urlService.redirectToLongUrl("abc123");
        verify(urlDetailRepository).incrementClickCount("abc123");
        assertEquals("https://google.com", result);
    }

    @Test
    void redirectToLongUrl_validCode_expired(){
        String shortCode = "abc123";
        UrlDetail urlDetail = new UrlDetail();
        urlDetail.setLongUrl("https://google.com");
        urlDetail.setExpiresAt(LocalDateTime.now().minusDays(1));

        when(urlLookupService.getByShortCode("abc123")).thenReturn(urlDetail);
        assertThrows(RuntimeException.class, () -> urlService.redirectToLongUrl(shortCode));


    }


}
