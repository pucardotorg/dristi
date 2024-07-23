package org.pucar.dristi.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.config.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class FileStoreUtilTest {

    @InjectMocks
    private FileStoreUtil fileStoreUtil;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Configuration configs;

    private final String FILE_STORE_HOST = "http://localhost:8080";
    private final String FILE_STORE_PATH = "/filestore/";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void testFileStoreSuccess() {
//        String tenantId = "tenantId";
//        String fileStoreId = "fileStoreId";
//
//        ResponseEntity<String> responseEntity = new ResponseEntity<>("Success", HttpStatus.OK);
//        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);
//        when(configs.getFileStoreHost()).thenReturn(FILE_STORE_HOST);
//        when(configs.getFileStorePath()).thenReturn(FILE_STORE_PATH);
//
//        Boolean result = fileStoreUtil.fileStore(tenantId, fileStoreId);
//
//        assertTrue(result);
//        verify(restTemplate, times(1)).getForEntity(anyString(), eq(String.class));
//    }

    @Test
    public void testFileStoreFailure() {
        String tenantId = "tenantId";
        String fileStoreId = "fileStoreId";

        ResponseEntity<String> responseEntity = new ResponseEntity<>("Failure", HttpStatus.INTERNAL_SERVER_ERROR);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);

        Boolean result = fileStoreUtil.fileStore(tenantId, fileStoreId);

        assertFalse(result);
    }
}
