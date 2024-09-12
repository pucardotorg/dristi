package com.egov.icops_integrationkerala.util;

import com.egov.icops_integrationkerala.config.IcopsConfiguration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class FileStorageUtilTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private IcopsConfiguration config;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private FileStorageUtil fileStorageUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getFileFromFileStoreService_Success() {
        // Arrange
        String fileStoreId = "testFileStoreId";
        String tenantId = "testTenantId";
        String fileContent = "testFileContent";
        ByteArrayResource resource = new ByteArrayResource(fileContent.getBytes());

        when(config.getFileStoreHost()).thenReturn("http://filestore");
        when(config.getFileStoreSearchEndPoint()).thenReturn("/search");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ByteArrayResource.class)))
                .thenReturn(new ResponseEntity<>(resource, HttpStatus.OK));

        // Act
        String result = fileStorageUtil.getFileFromFileStoreService(fileStoreId, tenantId);

        // Assert
        assertNotNull(result);
        assertEquals(org.apache.commons.codec.binary.Base64.encodeBase64String(fileContent.getBytes()), result);
    }

    @Test
    void getFileFromFileStoreService_EmptyResponse() {
        // Arrange
        String fileStoreId = "testFileStoreId";
        String tenantId = "testTenantId";

        when(config.getFileStoreHost()).thenReturn("http://filestore");
        when(config.getFileStoreSearchEndPoint()).thenReturn("/search");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ByteArrayResource.class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        // Act & Assert
        assertThrows(CustomException.class, () -> fileStorageUtil.getFileFromFileStoreService(fileStoreId, tenantId));
    }

    @Test
    void getFileFromFileStoreService_RestClientException() {
        // Arrange
        String fileStoreId = "testFileStoreId";
        String tenantId = "testTenantId";

        when(config.getFileStoreHost()).thenReturn("http://filestore");
        when(config.getFileStoreSearchEndPoint()).thenReturn("/search");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ByteArrayResource.class)))
                .thenThrow(new RestClientException("Test exception"));

        // Act & Assert
        assertThrows(CustomException.class, () -> fileStorageUtil.getFileFromFileStoreService(fileStoreId, tenantId));
    }

    @Test
    void saveDocumentToFileStore_Success() {
        // Arrange
        String filePath = "test/file/path.txt";
        String fileStoreId = "testFileStoreId";

        when(config.getFileStoreHost()).thenReturn("http://filestore");
        when(config.getFileStoreSaveEndPoint()).thenReturn("/save");
        when(config.getEgovStateTenantId()).thenReturn("testTenantId");
        when(config.getSummonsFileStoreModule()).thenReturn("testModule");

        JsonNode mockResponse = mock(JsonNode.class);
        JsonNode filesNode = mock(JsonNode.class);
        JsonNode fileNode = mock(JsonNode.class);
        when(mockResponse.has("files")).thenReturn(true);
        when(mockResponse.get("files")).thenReturn(filesNode);
        when(filesNode.isArray()).thenReturn(true);
        when(filesNode.get(0)).thenReturn(fileNode);
        when(fileNode.isObject()).thenReturn(true);
        when(fileNode.get("fileStoreId")).thenReturn(mock(JsonNode.class));
        when(fileNode.get("fileStoreId").asText()).thenReturn(fileStoreId);

        when(restTemplate.postForEntity(anyString(), any(), eq(Object.class)))
                .thenReturn(new ResponseEntity<>(new Object(), HttpStatus.OK));
        when(mapper.convertValue(any(), eq(JsonNode.class))).thenReturn(mockResponse);

        // Act
        String result = fileStorageUtil.saveDocumentToFileStore(filePath);

        // Assert
        assertEquals(fileStoreId, result);
    }

    @Test
    void saveDocumentToFileStore_InvalidResponse() {
        // Arrange
        String filePath = "test/file/path.txt";

        when(config.getFileStoreHost()).thenReturn("http://filestore");
        when(config.getFileStoreSaveEndPoint()).thenReturn("/save");
        when(config.getEgovStateTenantId()).thenReturn("testTenantId");
        when(config.getSummonsFileStoreModule()).thenReturn("testModule");

        JsonNode mockResponse = mock(JsonNode.class);
        when(mockResponse.has("files")).thenReturn(false);

        when(restTemplate.postForEntity(anyString(), any(), eq(Object.class)))
                .thenReturn(new ResponseEntity<>(new Object(), HttpStatus.OK));
        when(mapper.convertValue(any(), eq(JsonNode.class))).thenReturn(mockResponse);

        // Act & Assert
        assertThrows(CustomException.class, () -> fileStorageUtil.saveDocumentToFileStore(filePath));
    }
}