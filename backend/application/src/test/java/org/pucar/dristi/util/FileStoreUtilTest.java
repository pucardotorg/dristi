package org.pucar.dristi.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    @Test
    public void fileStore_returnsTrueWhenHttpStatusIsOk() {
        String tenantId = "tenantId";
        String fileStoreId = "fileStoreId";

        ResponseEntity<String> responseEntity = new ResponseEntity<>("Success", HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);
        when(configs.getFileStoreHost()).thenReturn(FILE_STORE_HOST);
        when(configs.getFileStorePath()).thenReturn(FILE_STORE_PATH);

        Boolean result = fileStoreUtil.doesFileExist(tenantId, fileStoreId);

        assertTrue(result);
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(String.class));
    }

    @Test
    public void fileStore_returnsFalseWhenHttpStatusIsNotOk() {
        String tenantId = "tenantId";
        String fileStoreId = "fileStoreId";

        ResponseEntity<String> responseEntity = new ResponseEntity<>("Failure", HttpStatus.INTERNAL_SERVER_ERROR);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);
        when(configs.getFileStoreHost()).thenReturn(FILE_STORE_HOST);
        when(configs.getFileStorePath()).thenReturn(FILE_STORE_PATH);

        Boolean result = fileStoreUtil.doesFileExist(tenantId, fileStoreId);

        assertFalse(result);
    }

    @Test
    public void fileStore_returnsFalseWhenExceptionIsThrown() {
        String tenantId = "tenantId";
        String fileStoreId = "fileStoreId";

        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenThrow(new RuntimeException());
        when(configs.getFileStoreHost()).thenReturn(FILE_STORE_HOST);
        when(configs.getFileStorePath()).thenReturn(FILE_STORE_PATH);

        Boolean result = fileStoreUtil.doesFileExist(tenantId, fileStoreId);

        assertFalse(result);
    }
}
