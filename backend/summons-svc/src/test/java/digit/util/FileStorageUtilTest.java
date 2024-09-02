package digit.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import digit.config.Configuration;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class FileStorageUtilTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Configuration config;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private FileStorageUtil fileStorageUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveDocumentToFileStore_Success() {
        // Arrange
        ByteArrayResource byteArrayResource = new ByteArrayResource("testFileContent".getBytes());
        String fileStoreId = "testFileStoreId";

        when(config.getFileStoreHost()).thenReturn("http://filestore");
        when(config.getFileStoreEndPoint()).thenReturn("/save");
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
        String result = fileStorageUtil.saveDocumentToFileStore(byteArrayResource);

        // Assert
        assertEquals(fileStoreId, result);
    }

    @Test
    void saveDocumentToFileStore_InvalidResponse() {
        // Arrange

        when(config.getFileStoreHost()).thenReturn("http://filestore");
        when(config.getFileStoreEndPoint()).thenReturn("/save");
        when(config.getEgovStateTenantId()).thenReturn("testTenantId");
        when(config.getSummonsFileStoreModule()).thenReturn("testModule");

        JsonNode mockResponse = mock(JsonNode.class);
        when(mockResponse.has("files")).thenReturn(false);

        when(restTemplate.postForEntity(anyString(), any(), eq(Object.class)))
                .thenReturn(new ResponseEntity<>(new Object(), HttpStatus.OK));
        when(mapper.convertValue(any(), eq(JsonNode.class))).thenReturn(mockResponse);

        // Act & Assert
        assertThrows(CustomException.class, () -> fileStorageUtil.saveDocumentToFileStore(new ByteArrayResource("testFileContent".getBytes())));
    }
}
