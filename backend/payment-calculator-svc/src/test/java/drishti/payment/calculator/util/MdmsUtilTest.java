package drishti.payment.calculator.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import drishti.payment.calculator.config.Configuration;
import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.MdmsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class MdmsUtilTest {

    @InjectMocks
    private MdmsUtil mdmsUtil;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private Configuration configs;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFetchMdmsData_Success() {
        RequestInfo requestInfo = new RequestInfo();
        String tenantId = "tenantId";
        String moduleName = "moduleName";
        List<String> masterNameList = Arrays.asList("master1", "master2");

        String mdmsHost = "http://localhost";
        String mdmsEndPoint = "/mdms";
        String uri = mdmsHost + mdmsEndPoint;

        MdmsCriteriaReq mdmsCriteriaReq = new MdmsCriteriaReq();
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
    public void testFetchMdmsData_Exception() {
        RequestInfo requestInfo = new RequestInfo();
        String tenantId = "tenantId";
        String moduleName = "moduleName";
        List<String> masterNameList = Arrays.asList("master1", "master2");

        String mdmsHost = "http://localhost";
        String mdmsEndPoint = "/mdms";
        String uri = mdmsHost + mdmsEndPoint;

        MdmsCriteriaReq mdmsCriteriaReq = new MdmsCriteriaReq();
        MdmsResponse mdmsResponse = new MdmsResponse();
        mdmsResponse.setMdmsRes(new HashMap<>());

        when(configs.getMdmsHost()).thenReturn(mdmsHost);
        when(configs.getMdmsEndPoint()).thenReturn(mdmsEndPoint);
        when(restTemplate.postForObject(eq(uri), any(MdmsCriteriaReq.class), eq(Map.class)))
                .thenThrow(new RuntimeException("Exception"));
        when(mapper.convertValue(any(), eq(MdmsResponse.class))).thenReturn(mdmsResponse);

        Map<String, Map<String, JSONArray>> result = mdmsUtil.fetchMdmsData(requestInfo, tenantId, moduleName, masterNameList);

        verify(configs, times(1)).getMdmsHost();
        verify(configs, times(1)).getMdmsEndPoint();
        verify(restTemplate, times(1)).postForObject(eq(uri), any(MdmsCriteriaReq.class), eq(Map.class));
    }

}
