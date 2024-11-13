package org.egov.eTreasury.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.models.Document;
import org.egov.eTreasury.config.PaymentConfiguration;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.egov.eTreasury.config.ServiceConstants.FILESTORE_UTILITY_EXCEPTION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileStorageUtilTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private PaymentConfiguration paymentConfiguration;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private FileStorageUtil fileStorageUtil;

    @Test
    void saveDocumentToFileStore_ShouldReturnDocument() {
        // Arrange
        byte[] payInSlipBytes = "test content".getBytes();
        String fileStoreId = "testFileStoreId";
        String uri = "http://localhost/api/files?tenantId=tenantId&module=treasury";
        ResponseEntity<Object> responseEntity = new ResponseEntity<>("{\"files\":[{\"fileStoreId\":\"" + fileStoreId + "\"}]}", HttpStatus.OK);

        // Mock PaymentConfiguration
        when(paymentConfiguration.getFileStoreHost()).thenReturn("http://localhost");
        when(paymentConfiguration.getFileStoreEndPoint()).thenReturn("/api/files");
        when(paymentConfiguration.getEgovStateTenantId()).thenReturn("tenantId");
        when(paymentConfiguration.getTreasuryFileStoreModule()).thenReturn("treasury");

        // Mock RestTemplate
        when(restTemplate.postForEntity(eq(uri), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(responseEntity);

        // Mock ObjectMapper
        JsonNode rootNode = mock(JsonNode.class);
        JsonNode filesArray = mock(JsonNode.class);
        JsonNode fileNode = mock(JsonNode.class);
        when(objectMapper.convertValue(responseEntity.getBody(), JsonNode.class)).thenReturn(rootNode);
        when(rootNode.has("files")).thenReturn(true);
        when(rootNode.get("files")).thenReturn(filesArray);
        when(filesArray.isArray()).thenReturn(true);
        when(filesArray.get(0)).thenReturn(fileNode);
        when(fileNode.isObject()).thenReturn(true);
        when(fileNode.get("fileStoreId")).thenReturn(mock(JsonNode.class));
        when(fileNode.get("fileStoreId").asText()).thenReturn(fileStoreId);
        // Act
        Document document = fileStorageUtil.saveDocumentToFileStore(payInSlipBytes);

        // Assert
        assertEquals(fileStoreId, document.getFileStore());
        assertEquals("application/pdf", document.getDocumentType());
    }

    @Test
    void saveDocumentToFileStore_ShouldThrowExceptionOnFailure() {
        // Arrange
        byte[] payInSlipBytes = "test content".getBytes();
        String uri = "http://localhost/api/files?tenantId=tenantId&module=treasury";

        when(paymentConfiguration.getFileStoreHost()).thenReturn("http://localhost");
        when(paymentConfiguration.getFileStoreEndPoint()).thenReturn("/api/files");
        when(paymentConfiguration.getEgovStateTenantId()).thenReturn("tenantId");
        when(paymentConfiguration.getTreasuryFileStoreModule()).thenReturn("treasury");
        when(restTemplate.postForEntity(eq(uri), any(HttpEntity.class), eq(Object.class)))
                .thenThrow(new RuntimeException("Test Exception"));

        // Act & Assert
        CustomException thrown = assertThrows(CustomException.class, () ->
                fileStorageUtil.saveDocumentToFileStore(payInSlipBytes));
        assertEquals(FILESTORE_UTILITY_EXCEPTION, thrown.getCode());
        assertEquals("Error occurred when getting saving document in File Store", thrown.getMessage());
    }

    @Test
    void saveDocumentToFileStore_Exception_1() {
        // Arrange
        byte[] payInSlipBytes = "test content".getBytes();
        String fileStoreId = "testFileStoreId";
        String uri = "http://localhost/api/files?tenantId=tenantId&module=treasury";
        ResponseEntity<Object> responseEntity = new ResponseEntity<>("{\"files\":[{\"fileStoreId\":\"" + fileStoreId + "\"}]}", HttpStatus.OK);

        // Mock PaymentConfiguration
        when(paymentConfiguration.getFileStoreHost()).thenReturn("http://localhost");
        when(paymentConfiguration.getFileStoreEndPoint()).thenReturn("/api/files");
        when(paymentConfiguration.getEgovStateTenantId()).thenReturn("tenantId");
        when(paymentConfiguration.getTreasuryFileStoreModule()).thenReturn("treasury");

        // Mock RestTemplate
        when(restTemplate.postForEntity(eq(uri), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(responseEntity);

        // Mock ObjectMapper
        JsonNode rootNode = mock(JsonNode.class);
        when(objectMapper.convertValue(responseEntity.getBody(), JsonNode.class)).thenReturn(rootNode);
        when(rootNode.has("files")).thenReturn(false);
        // Act
        CustomException thrown = assertThrows(CustomException.class, () ->
                fileStorageUtil.saveDocumentToFileStore(payInSlipBytes));

        // Assert
        assertEquals(FILESTORE_UTILITY_EXCEPTION, thrown.getCode());
        assertEquals("Error occurred when getting saving document in File Store", thrown.getMessage());
    }
}
