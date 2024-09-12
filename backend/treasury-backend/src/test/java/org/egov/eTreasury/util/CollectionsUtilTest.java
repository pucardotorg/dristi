package org.egov.eTreasury.util;


import org.egov.eTreasury.model.PaymentRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CollectionsUtilTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CollectionsUtil collectionsUtil;

    @Test
    void testCallService() {
        // Arrange
        PaymentRequest paymentRequest = new PaymentRequest();
        String collectionsUrl = "http://example.com";
        String paymentCreateEndPoint = "/create";
        String fullUrl = collectionsUrl + paymentCreateEndPoint;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PaymentRequest> requestEntity = new HttpEntity<>(paymentRequest, headers);

        ResponseEntity<String> responseEntity = new ResponseEntity<>("Success", HttpStatus.OK);

        when(restTemplate.postForEntity(fullUrl, requestEntity, String.class)).thenReturn(responseEntity);

        // Act
        ResponseEntity<String> result = collectionsUtil.callService(paymentRequest, collectionsUrl, paymentCreateEndPoint);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Success", result.getBody());
    }
}

