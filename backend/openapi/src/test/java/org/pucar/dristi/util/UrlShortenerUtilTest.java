package org.pucar.dristi.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import org.pucar.dristi.config.Configuration;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlShortenerUtilTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Configuration configs;

    @InjectMocks
    private UrlShortenerUtil urlShortenerUtil;

    @Test
    void testGetShortenedUrl_Success() {
        String originalUrl = "http://example.com";
        String shortenedUrl = "http://short.url/abc123";

        // Mock configuration values
        when(configs.getUrlShortnerHost()).thenReturn("http://shortener.com");
        when(configs.getUrlShortnerEndpoint()).thenReturn("/shorten");

        // Mock RestTemplate response
        when(restTemplate.postForObject(eq("http://shortener.com/shorten"), any(HashMap.class), eq(String.class)))
                .thenReturn(shortenedUrl);

        // Test the method
        String result = urlShortenerUtil.getShortenedUrl(originalUrl);

        // Verify results
        assertEquals(shortenedUrl, result);
        verify(restTemplate, times(1)).postForObject(eq("http://shortener.com/shorten"), any(HashMap.class), eq(String.class));
    }

    @Test
    void testGetShortenedUrl_Failure_EmptyResponse() {
        String originalUrl = "http://example.com";

        // Mock configuration values
        when(configs.getUrlShortnerHost()).thenReturn("http://shortener.com");
        when(configs.getUrlShortnerEndpoint()).thenReturn("/shorten");

        // Mock RestTemplate response
        when(restTemplate.postForObject(eq("http://shortener.com/shorten"), any(HashMap.class), eq(String.class)))
                .thenReturn(null);

        // Test the method
        String result = urlShortenerUtil.getShortenedUrl(originalUrl);

        // Verify results
        assertEquals(originalUrl, result);
        verify(restTemplate, times(1)).postForObject(eq("http://shortener.com/shorten"), any(HashMap.class), eq(String.class));
    }

}
