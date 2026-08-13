package com.urlshortener;

import com.urlshortener.repository.UrlDetailRepository;
import com.urlshortener.repository.UserDetailRepository;
import com.urlshortener.service.UrlService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UrlServiceTest {

    @Mock
    private UrlDetailRepository urlDetailRepository;

    @Mock
    private UserDetailRepository userDetailRepository;

    @InjectMocks
    private UrlService urlService;
}
