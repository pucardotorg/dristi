package com.pucar.drishti.util;

import com.pucar.drishti.config.Configuration;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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
        when(configs.getFilestoreEndPoint()).thenReturn("/endpoint");
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
        when(configs.getFilestoreEndPoint()).thenReturn("/endpoint");
        when(restTemplate.getForObject(anyString(), eq(Resource.class)))
                .thenThrow(new RuntimeException("Test Exception"));

        CustomException thrown = assertThrows(CustomException.class, () ->
                fileStoreUtil.fetchFileStoreObjectById(fileStoreId, tenantId)
        );

        assertEquals("invalid transaction id for e-sign", thrown.getCode());
        assertEquals("Test Exception", thrown.getMessage());
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
}
