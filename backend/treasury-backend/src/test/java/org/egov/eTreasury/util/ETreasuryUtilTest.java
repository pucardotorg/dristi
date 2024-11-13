package org.egov.eTreasury.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ETreasuryUtilTest {

    @Mock
    private RestTemplate restTemplate;

    private ETreasuryUtil eTreasuryUtil;

    @BeforeEach
    void setUp() {
        eTreasuryUtil = new ETreasuryUtil(restTemplate);
    }

    @Test
    void testCallConnectionService() {
        String url = "http://example.com/api";
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Success", HttpStatus.OK);

        when(restTemplate.postForEntity(eq(url), any(HttpEntity.class), eq(String.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<String> actualResponse = eTreasuryUtil.callConnectionService(url, String.class);

        assertEquals(expectedResponse, actualResponse);

        verify(restTemplate).postForEntity(eq(url), argThat(argument -> {
            if (argument instanceof HttpEntity) {
                HttpEntity<?> entity = (HttpEntity<?>) argument;
                HttpHeaders headers = entity.getHeaders();
                return headers.getAccept().contains(MediaType.APPLICATION_JSON);
            }
            return false;
        }), eq(String.class));
    }

    @Test
    void testCallAuthService() {
        String clientId = "testClient";
        String clientSecret = "testSecret";
        String payload = "{\"key\":\"value\"}";
        String url = "http://example.com/auth";
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(new Object(), HttpStatus.OK);

        when(restTemplate.postForEntity(eq(url), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<?> actualResponse = eTreasuryUtil.callAuthService(clientId, clientSecret, payload, url);

        assertEquals(expectedResponse, actualResponse);

        verify(restTemplate).postForEntity(eq(url), argThat(argument -> {
            if (argument instanceof HttpEntity) {
                HttpEntity<?> entity = (HttpEntity<?>) argument;
                HttpHeaders headers = entity.getHeaders();
                return headers.getContentType().equals(MediaType.APPLICATION_JSON) &&
                        headers.getAccept().contains(MediaType.ALL) &&
                        headers.get("clientId").contains(clientId) &&
                        headers.get("clientSecret").contains(clientSecret) &&
                        payload.equals(entity.getBody());
            }
            return false;
        }), eq(Object.class));
    }

    @Test
    void testCallService() {
        String inputHeaders = "header1:value1";
        String inputBody = "testBody";
        String url = "http://example.com/service";
        MediaType mediaType = MediaType.APPLICATION_JSON;
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Success", HttpStatus.OK);

        when(restTemplate.postForEntity(eq(url), any(HttpEntity.class), eq(String.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<String> actualResponse = eTreasuryUtil.callService(inputHeaders, inputBody, url, String.class, mediaType);

        assertEquals(expectedResponse, actualResponse);

        verify(restTemplate).postForEntity(eq(url), argThat(argument -> {
            if (argument instanceof HttpEntity) {
                HttpEntity<?> entity = (HttpEntity<?>) argument;
                HttpHeaders headers = entity.getHeaders();
                if (entity.getBody() instanceof MultiValueMap) {
                    MultiValueMap<String, String> body = (MultiValueMap<String, String>) entity.getBody();
                    return headers.getContentType().equals(MediaType.APPLICATION_FORM_URLENCODED) &&
                            headers.getAccept().contains(mediaType) &&
                            body.get("input_headers").contains(inputHeaders) &&
                            body.get("input_data").contains(inputBody);
                }
            }
            return false;
        }), eq(String.class));
    }
}
