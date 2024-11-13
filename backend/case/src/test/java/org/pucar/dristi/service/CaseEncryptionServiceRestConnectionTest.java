package org.pucar.dristi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.encryption.config.EncProperties;
import org.egov.encryption.web.contract.EncReqObject;
import org.egov.encryption.web.contract.EncryptionRequest;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.ServiceConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CaseEncryptionServiceRestConnectionTest {

    @Mock
    private EncProperties encProperties;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private CaseEncryptionServiceRestConnection caseEncryptionServiceRestConnection;

    private final String encryptUrl = "http://localhost/encrypt";
    private final String decryptUrl = "http://localhost/decrypt";
    private final String tenantId = "tenant1";
    private final String type = "type1";
    private final Object value = "someValue";
    private final Object ciphertext = "ciphertext";

    @BeforeEach
    void setUp() {

    }

    @Test
    void testCallEncryptSuccess() throws IOException {
        // Arrange
        EncryptionRequest encryptionRequest = new EncryptionRequest();
        EncReqObject encReqObject = new EncReqObject(tenantId, type, value);
        encryptionRequest.setEncryptionRequests(new ArrayList<>(Collections.singleton(encReqObject)));
        String responseBody = "[{\"encryptedValue\":\"encryptedData\"}]";
        JsonNode jsonNodeResponse = mock(JsonNode.class);

        when(encProperties.getEgovEncHost()).thenReturn("http://localhost");
        when(encProperties.getEgovEncEncryptPath()).thenReturn("/encrypt");

        when(restTemplate.postForEntity(eq(encryptUrl), any(EncryptionRequest.class), eq(String.class),any(Object.class)))
                .thenReturn(ResponseEntity.ok(responseBody));
        when(objectMapper.readTree(responseBody)).thenReturn(jsonNodeResponse);
        when(jsonNodeResponse.get(0)).thenReturn(jsonNodeResponse);

        // Act
        JsonNode result = (JsonNode) caseEncryptionServiceRestConnection.callEncrypt(tenantId, type, value);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(jsonNodeResponse, result);
    }

    @Test
    void testCallEncryptFailure() {
        // Arrange
        when(restTemplate.postForEntity(eq(encryptUrl), any(EncryptionRequest.class), eq(String.class)))
                .thenThrow(new RuntimeException("Service unavailable"));

        // Act & Assert
        CustomException thrown = assertThrows(CustomException.class, () ->
                caseEncryptionServiceRestConnection.callEncrypt(tenantId, type, value)
        );

        assertEquals(ServiceConstants.ENCRYPTION_SERVICE_ERROR, thrown.getMessage());
    }

    @Test
    void testCallDecryptSuccess() {
        // Arrange
        JsonNode responseJsonNode = mock(JsonNode.class);
        when(encProperties.getEgovEncHost()).thenReturn("http://localhost");
        when(encProperties.getEgovEncDecryptPath()).thenReturn("/decrypt");
        when(restTemplate.postForEntity(eq(decryptUrl), any(), eq(JsonNode.class),any(Object.class)))
                .thenReturn(ResponseEntity.ok(responseJsonNode));

        // Act
        JsonNode result = caseEncryptionServiceRestConnection.callDecrypt(ciphertext);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(responseJsonNode, result);
    }

    @Test
    void testCallDecryptFailure() {
        // Arrange
        when(restTemplate.postForEntity(eq(decryptUrl), any(), eq(JsonNode.class)))
                .thenThrow(new RuntimeException("Service unavailable"));

        // Act & Assert
        CustomException thrown = assertThrows(CustomException.class, () ->
                caseEncryptionServiceRestConnection.callDecrypt(ciphertext)
        );

        assertEquals(ServiceConstants.ENCRYPTION_SERVICE_ERROR, thrown.getMessage());
    }
}