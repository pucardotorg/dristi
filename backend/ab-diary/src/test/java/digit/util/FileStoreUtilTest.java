package digit.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import digit.config.Configuration;
import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static digit.config.ServiceConstants.FILE_STORE_UTILITY_EXCEPTION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileStoreUtilTest {

    @Mock
    private Configuration config;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private FileStoreUtil fileStoreUtil;

    private static final String TENANT_ID = "default";
    private static final String FILE_STORE_ID = "fs-123";
    private static final String FILE_STORE_HOST = "http://filestore";

    @Test
    void getFileSuccess() {
        when(config.getFileStoreHost()).thenReturn(FILE_STORE_HOST);
        // Setup mock data
        byte[] expectedContent = "test content".getBytes();
        Resource mockResource = new ByteArrayResource(expectedContent);
        ResponseEntity<Resource> mockResponse = new ResponseEntity<>(mockResource, HttpStatus.OK);

        // Mock REST call
        when(restTemplate.getForEntity(any(String.class), eq(Resource.class)))
                .thenReturn(mockResponse);

        // Execute test
        byte[] result = fileStoreUtil.getFile(TENANT_ID, FILE_STORE_ID);

        // Verify
        assertNotNull(result);
        assertArrayEquals(expectedContent, result);
    }

    @Test
    void getFileException() {
        when(config.getFileStoreHost()).thenReturn(FILE_STORE_HOST);
        // Mock REST call throwing exception
        when(restTemplate.getForEntity(any(String.class), eq(Resource.class)))
                .thenThrow(new RuntimeException("Network error"));

        // Execute test
        byte[] result = fileStoreUtil.getFile(TENANT_ID, FILE_STORE_ID);

        // Verify
        assertNull(result);
        verify(restTemplate).getForEntity(any(String.class), eq(Resource.class));
    }

    @Test
    void saveDocumentToFileStoreSuccess() {
        // Setup mock data
        ByteArrayResource byteArrayResource = new ByteArrayResource("test content".getBytes());
        ObjectNode fileNode = JsonNodeFactory.instance.objectNode();
        fileNode.put("fileStoreId", FILE_STORE_ID);

        ArrayNode filesArray = JsonNodeFactory.instance.arrayNode();
        filesArray.add(fileNode);

        ObjectNode responseNode = JsonNodeFactory.instance.objectNode();
        responseNode.set("files", filesArray);

        ResponseEntity<Object> mockResponse = new ResponseEntity<>(new Object(), HttpStatus.OK);

        // Mock REST call and JSON processing
        when(restTemplate.postForEntity(
                any(String.class),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenReturn(mockResponse);

        when(mapper.convertValue(any(), eq(JsonNode.class))).thenReturn(responseNode);

        // Execute test
        Document result = fileStoreUtil.saveDocumentToFileStore(byteArrayResource, TENANT_ID);

        // Verify
        assertNotNull(result);
        assertEquals(FILE_STORE_ID, result.getFileStore());
        assertEquals("application/pdf", result.getDocumentType());
    }

    @Test
    void saveDocumentToFileStoreInvalidResponse() {
        // Setup mock data
        ByteArrayResource byteArrayResource = new ByteArrayResource("test content".getBytes());
        ObjectNode invalidResponse = JsonNodeFactory.instance.objectNode();
        ResponseEntity<Object> mockResponse = new ResponseEntity<>(new Object(), HttpStatus.OK);

        // Mock REST call and JSON processing
        when(restTemplate.postForEntity(
                any(String.class),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenReturn(mockResponse);

        when(mapper.convertValue(any(), eq(JsonNode.class))).thenReturn(invalidResponse);

        // Execute test and verify exception
        CustomException exception = assertThrows(CustomException.class, () -> {
            fileStoreUtil.saveDocumentToFileStore(byteArrayResource, TENANT_ID);
        });

        assertEquals("FILE_STORE_UTILITY_EXCEPTION", exception.getCode());
    }

    @Test
    void saveDocumentToFileStoreRestException() {
        // Setup mock data
        ByteArrayResource byteArrayResource = new ByteArrayResource("test content".getBytes());

        // Mock REST call throwing exception
        when(restTemplate.postForEntity(
                any(String.class),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenThrow(new RuntimeException("Network error"));

        // Execute test and verify exception
        CustomException exception = assertThrows(CustomException.class, () -> {
            fileStoreUtil.saveDocumentToFileStore(byteArrayResource, TENANT_ID);
        });

        assertEquals(FILE_STORE_UTILITY_EXCEPTION, exception.getCode());
    }

    @Test
    void extractDocumentFromResponseInvalidStructure() {
        // Setup mock response with invalid structure
        ResponseEntity<Object> mockResponse = new ResponseEntity<>(new Object(), HttpStatus.OK);
        ObjectNode invalidResponse = JsonNodeFactory.instance.objectNode();
        invalidResponse.put("someOtherKey", "value");

        when(mapper.convertValue(any(), eq(JsonNode.class))).thenReturn(invalidResponse);

        // Execute test and verify exception
        CustomException exception = assertThrows(CustomException.class, () -> {
            fileStoreUtil.extractDocumentFromResponse(mockResponse);
        });

        assertEquals("INVALID_FILE_STORE_RESPONSE", exception.getCode());
    }
}