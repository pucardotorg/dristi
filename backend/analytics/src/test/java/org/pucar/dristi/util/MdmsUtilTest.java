package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.*;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.MdmsUtil;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.pucar.dristi.config.ServiceConstants.ERROR_WHILE_FETCHING_FROM_MDMS;

@ExtendWith(MockitoExtension.class)
class MdmsUtilTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private Configuration configs;

    @InjectMocks
    private MdmsUtil mdmsUtil;

    private RequestInfo requestInfo;
    private String tenantId;
    private String moduleName;
    private List<String> masterNameList;

    @BeforeEach
    void setUp() {
        requestInfo = new RequestInfo();
        tenantId = "tenant1";
        moduleName = "module1";
        masterNameList = Arrays.asList("master1", "master2");

        when(configs.getMdmsHost()).thenReturn("http://localhost");
        when(configs.getMdmsEndPoint()).thenReturn("/mdms");
    }

    @Test
    void testFetchMdmsData_Success() {

        Map<String, Object> mockResponse = new HashMap<>();
        Map<String, Map<String, JSONArray>> mockMdmsRes = new HashMap<>();
        Map<String, JSONArray> mockModuleData = new HashMap<>();
        mockModuleData.put("master1", new JSONArray());
        mockModuleData.put("master2", new JSONArray());
        mockMdmsRes.put("module1", mockModuleData);
        mockResponse.put("MdmsRes", mockMdmsRes);

        MdmsResponse mdmsResponse = new MdmsResponse();
        mdmsResponse.setMdmsRes(mockMdmsRes);

        when(restTemplate.postForObject(any(String.class), any(MdmsCriteriaReq.class), eq(Map.class)))
                .thenReturn(mockResponse);
        when(mapper.convertValue(mockResponse, MdmsResponse.class)).thenReturn(mdmsResponse);

        Map<String, Map<String, JSONArray>> result = mdmsUtil.fetchMdmsData(requestInfo, tenantId, moduleName, masterNameList);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.containsKey(moduleName));
        assertTrue(result.get(moduleName).containsKey("master1"));
        assertTrue(result.get(moduleName).containsKey("master2"));
    }

    @Test
    void testFetchMdmsData_Exception() {

        when(restTemplate.postForObject(any(String.class), any(MdmsCriteriaReq.class), eq(Map.class)))
                .thenThrow(new RuntimeException("Fetch error"));

        CustomException exception = assertThrows(CustomException.class, () -> mdmsUtil.fetchMdmsData(requestInfo, tenantId, moduleName, masterNameList));

        assertEquals("Failed to fetch MDMS data: Fetch error", exception.getCode());
        assertEquals(ERROR_WHILE_FETCHING_FROM_MDMS, exception.getMessage());
    }
}
