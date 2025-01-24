package digit.util;

import digit.config.Configuration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UrlShortenerUtilTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Configuration configs;

    @InjectMocks
    private UrlShortenerUtil urlShortenerUtil;

    private final String testUrl = "http://example.com";

    @BeforeEach
    void setUp() {
        // Mocking configuration
        when(configs.getUrlShortnerHost()).thenReturn("http://shortenerhost.com");
        when(configs.getUrlShortnerEndpoint()).thenReturn("/shorten");
    }

    @Test
    void getShortenedUrl_Successful() {
        // Mocking successful response from the URL shortening service
        String testShortenedUrl = "http://short.url";
        when(restTemplate.postForObject(anyString(), any(), eq(String.class))).thenReturn(testShortenedUrl);

        String shortenedUrl = urlShortenerUtil.getShortenedUrl(testUrl);

        assertEquals(testShortenedUrl, shortenedUrl);
        verify(restTemplate, times(1)).postForObject(anyString(), any(), eq(String.class));
    }

    @Test
    void getShortenedUrl_Failed() {
        // Mocking empty response from the URL shortening service
        when(restTemplate.postForObject(anyString(), any(), eq(String.class))).thenReturn(null);

        String shortenedUrl = urlShortenerUtil.getShortenedUrl(testUrl);

        assertEquals(testUrl, shortenedUrl);
        verify(restTemplate, times(1)).postForObject(anyString(), any(), eq(String.class));
    }

    @Test
    void getShortenedUrl_EmptyResponse() {
        // Mocking empty response from the URL shortening service
        when(restTemplate.postForObject(anyString(), any(), eq(String.class))).thenReturn("");

        String shortenedUrl = urlShortenerUtil.getShortenedUrl(testUrl);

        assertEquals(testUrl, shortenedUrl);
        verify(restTemplate, times(1)).postForObject(anyString(), any(), eq(String.class));
    }
}