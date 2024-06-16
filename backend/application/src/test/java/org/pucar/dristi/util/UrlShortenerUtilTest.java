package org.pucar.dristi.util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UrlShortenerUtilTest {

    @InjectMocks
    private UrlShortenerUtil urlShortenerUtil;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Configuration configs;

    @BeforeEach
    public void setUp() {
        // Mock configuration
        when(configs.getUrlShortnerHost()).thenReturn("http://example.com/");
        when(configs.getUrlShortnerEndpoint()).thenReturn("shorten");
    }

    @Test
    public void testGetShortenedUrl_Success() {
        // Arrange
        String originalUrl = "http://example.com/original-url";
        String shortenedUrl = "http://example.com/shortened";

        when(restTemplate.postForObject(anyString(), any(), any())).thenReturn(shortenedUrl);

        // Act
        String result = urlShortenerUtil.getShortenedUrl(originalUrl);

        // Assert
        assertEquals(shortenedUrl, result);
    }

    @Test
    public void testGetShortenedUrl_Failure() {
        // Arrange
        String originalUrl = "http://example.com/original-url";

        when(restTemplate.postForObject(anyString(), any(), any())).thenReturn(null);

        // Act & Assert
        String result = urlShortenerUtil.getShortenedUrl(originalUrl);
        assertEquals(originalUrl, result);
    }

    @Test
    public void testGetShortenedUrl_EmptyUrl() {
        // Arrange
        String originalUrl = "";

        // Act & Assert
        String result = urlShortenerUtil.getShortenedUrl(originalUrl);
        assertEquals(originalUrl, result);
    }
}

