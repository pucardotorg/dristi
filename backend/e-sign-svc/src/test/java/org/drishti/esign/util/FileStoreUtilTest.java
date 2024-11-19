package org.drishti.esign.util;

import org.drishti.esign.config.Configuration;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import static org.drishti.esign.config.ServiceConstants.FILE_STORE_SERVICE_EXCEPTION_CODE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class FileStoreUtilTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Configuration configs;

    @InjectMocks
    private FileStoreUtil fileStoreUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFetchFileStoreObjectById_Success() {
        String fileStoreId = "fileStoreId";
        String tenantId = "tenantId";
        Resource resource = mock(Resource.class);

        when(configs.getFilestoreHost()).thenReturn("http://localhost");
        when(configs.getFilestoreSearchEndPoint()).thenReturn("/filestore/v1/files/id");

        when(restTemplate.getForObject(anyString(), eq(Resource.class))).thenReturn(resource);

        Resource result = fileStoreUtil.fetchFileStoreObjectById(fileStoreId, tenantId);

        assertNotNull(result);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Resource.class));
    }

    @Test
    public void testFetchFileStoreObjectById_Exception() {
        String fileStoreId = "fileStoreId";
        String tenantId = "tenantId";

        when(configs.getFilestoreHost()).thenReturn("http://localhost");
        when(configs.getFilestoreSearchEndPoint()).thenReturn("/filestore/v1/files/id");

        when(restTemplate.getForObject(anyString(), eq(Resource.class))).thenThrow(new RuntimeException());

        CustomException exception = assertThrows(CustomException.class, () -> {
            fileStoreUtil.fetchFileStoreObjectById(fileStoreId, tenantId);
        });

        assertEquals(FILE_STORE_SERVICE_EXCEPTION_CODE, exception.getCode());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Resource.class));
    }

    @Test
    public void testAppendQueryParams() {
        StringBuilder uri = new StringBuilder("http://localhost");
        String paramName1 = "param1";
        String paramValue1 = "value1";
        String paramName2 = "param2";
        String paramValue2 = "value2";

        StringBuilder result = fileStoreUtil.appendQueryParams(uri, paramName1, paramValue1, paramName2, paramValue2);

        assertNotNull(result);
        assertTrue(result.toString().contains(paramName1 + "=" + paramValue1));
        assertTrue(result.toString().contains(paramName2 + "=" + paramValue2));
    }

    @Test
    public void StoreFileInFileStore_Success() {
        MultipartFile file = mock(MultipartFile.class);
        String tenantId = "tenantId";
        String fileStoreId = "fileStoreId";
        String responseJson = "{\"files\":[{\"fileStoreId\":\"" + fileStoreId + "\"}]}";

        when(configs.getFilestoreHost()).thenReturn("http://localhost");
        when(configs.getFilestoreCreateEndPoint()).thenReturn("/filestore/v1/files");

        when(file.getResource()).thenReturn(mock(Resource.class));
        when(file.getContentType()).thenReturn("application/pdf");

        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseJson, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);

        String result = fileStoreUtil.storeFileInFileStore(file, tenantId);

        assertNotNull(result);
        assertEquals(fileStoreId, result);
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class));
    }
}
