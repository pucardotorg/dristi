package com.egov.icops_integrationkerala.util;

import com.egov.icops_integrationkerala.config.IcopsConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class MdmsUtilTest {

    @InjectMocks
    private MdmsUtil mdmsUtil;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private IcopsConfiguration configs;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFetchMdmsData_Success() {
        RequestInfo requestInfo = new RequestInfo();
        String tenantId = "tenantId";
        String moduleName = "moduleName";
        List<String> masterNameList = Arrays.asList("master1", "master2");

        String mdmsHost = "http://localhost";
        String mdmsEndPoint = "/mdms";
        String uri = mdmsHost + mdmsEndPoint;

        MdmsResponse mdmsResponse = new MdmsResponse();
        Map<String, Map<String, JSONArray>> mdmsRes = new HashMap<>();
        mdmsResponse.setMdmsRes(mdmsRes);

        when(configs.getMdmsHost()).thenReturn(mdmsHost);
        when(configs.getMdmsEndPoint()).thenReturn(mdmsEndPoint);
        when(restTemplate.postForObject(eq(uri), any(MdmsCriteriaReq.class), eq(Map.class)))
                .thenReturn(new HashMap<>());
        when(mapper.convertValue(any(), eq(MdmsResponse.class))).thenReturn(mdmsResponse);

        Map<String, Map<String, JSONArray>> result = mdmsUtil.fetchMdmsData(requestInfo, tenantId, moduleName, masterNameList);

        assertNotNull(result);
        assertEquals(mdmsRes, result);
        verify(configs, times(1)).getMdmsHost();
        verify(configs, times(1)).getMdmsEndPoint();
        verify(restTemplate, times(1)).postForObject(eq(uri), any(MdmsCriteriaReq.class), eq(Map.class));
        verify(mapper, times(1)).convertValue(any(), eq(MdmsResponse.class));
    }

    @Test
    void testFetchMdmsData_Exception() {
        RequestInfo requestInfo = new RequestInfo();
        String tenantId = "tenantId";
        String moduleName = "moduleName";
        List<String> masterNameList = Arrays.asList("master1", "master2");

        String mdmsHost = "http://localhost";
        String mdmsEndPoint = "/mdms";
        String uri = mdmsHost + mdmsEndPoint;

        MdmsResponse mdmsResponse = new MdmsResponse();
        mdmsResponse.setMdmsRes(new HashMap<>());

        when(configs.getMdmsHost()).thenReturn(mdmsHost);
        when(configs.getMdmsEndPoint()).thenReturn(mdmsEndPoint);
        when(restTemplate.postForObject(eq(uri), any(MdmsCriteriaReq.class), eq(Map.class)))
                .thenThrow(new RuntimeException("Exception"));
        when(mapper.convertValue(any(), eq(MdmsResponse.class))).thenReturn(mdmsResponse);

        mdmsUtil.fetchMdmsData(requestInfo, tenantId, moduleName, masterNameList);

        verify(configs, times(1)).getMdmsHost();
        verify(configs, times(1)).getMdmsEndPoint();
        verify(restTemplate, times(1)).postForObject(eq(uri), any(MdmsCriteriaReq.class), eq(Map.class));
    }

}
