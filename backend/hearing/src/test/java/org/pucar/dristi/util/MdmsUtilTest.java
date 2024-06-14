package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.*;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.pucar.dristi.config.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.pucar.dristi.config.ServiceConstants.ERROR_WHILE_FETCHING_FROM_MDMS;

class MdmsUtilTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private Configuration configs;

    @InjectMocks
    private MdmsUtil mdmsUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFetchMdmsData_Success() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        String tenantId = "tenant1";
        String moduleName = "module1";
        List<String> masterNameList = Collections.singletonList("master1");

        String host = "http://example.com";
        String endpoint = "/mdms";

        Map<String, Object> responseMap = new HashMap<>();
        Map<String, Map<String, JSONArray>> mdmsRes = new HashMap<>();
        Map<String, JSONArray> moduleMap = new HashMap<>();
        moduleMap.put("master1", new JSONArray());
        mdmsRes.put("module1", moduleMap);

        responseMap.put("MdmsRes", mdmsRes);
        MdmsResponse mdmsResponse = new MdmsResponse();
        mdmsResponse.setMdmsRes(mdmsRes);

        when(configs.getMdmsHost()).thenReturn(host);
        when(configs.getMdmsEndPoint()).thenReturn(endpoint);
        when(restTemplate.postForObject(anyString(), any(), eq(Map.class))).thenReturn(responseMap);
        when(mapper.convertValue(any(), eq(MdmsResponse.class))).thenReturn(mdmsResponse);

        // Act
        Map<String, Map<String, JSONArray>> result = mdmsUtil.fetchMdmsData(requestInfo, tenantId, moduleName, masterNameList);

        // Assert
        assertNotNull(result);
        assertEquals(mdmsRes, result);
    }

    @Test
    void testFetchMdmsData_Exception() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        String tenantId = "tenant1";
        String moduleName = "module1";
        List<String> masterNameList = Collections.singletonList("master1");

        String host = "http://example.com";
        String endpoint = "/mdms";

        when(configs.getMdmsHost()).thenReturn(host);
        when(configs.getMdmsEndPoint()).thenReturn(endpoint);
        when(restTemplate.postForObject(anyString(), any(), eq(Map.class))).thenThrow(new RuntimeException("Service unavailable"));

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            mdmsUtil.fetchMdmsData(requestInfo, tenantId, moduleName, masterNameList);
        });
        assertEquals("Service unavailable", exception.getCode());
        assertEquals(ERROR_WHILE_FETCHING_FROM_MDMS, exception.getMessage());
    }
}
