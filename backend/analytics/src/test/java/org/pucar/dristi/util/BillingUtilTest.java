package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.json.JSONObject;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.util.BillingUtil;
import org.pucar.dristi.util.CaseUtil;
import org.pucar.dristi.util.IndexerUtils;
import org.pucar.dristi.util.MdmsUtil;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BillingUtilTest {

    @Mock
    private Configuration config;
    @Mock
    private IndexerUtils indexerUtil;
    @Mock
    private ServiceRequestRepository requestRepository;
    @Mock
    private CaseUtil caseUtil;
    @Mock
    private MdmsUtil mdmsUtil;
    @Mock
    private ObjectMapper objectMapper;


    private BillingUtil billingUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        billingUtil = new BillingUtil(config, indexerUtil, requestRepository, caseUtil,objectMapper,mdmsUtil);
    }

    @Test
    void testBuildString() throws JSONException {
        JSONObject jsonObject = new JSONObject().put("key", "value");
        when(indexerUtil.buildString(any(JSONObject.class))).thenReturn("mocked-string");

        String result = billingUtil.buildString(jsonObject);

        assertEquals("mocked-string", result);
        verify(indexerUtil).buildString(jsonObject);
    }

    @Test
    void testGetDemand() throws JSONException {
        String tenantId = "test-tenant";
        String demandId = "demand-001";
        JSONObject requestInfo = new JSONObject().put("userInfo", new JSONObject().put("name", "Test User"));

        when(config.getDemandHost()).thenReturn("http://demand-host");
        when(config.getDemandEndPoint()).thenReturn("/demand");
        when(requestRepository.fetchResult(any(StringBuilder.class), any(JSONObject.class)))
                .thenReturn("mocked-demand-response");

        String result = billingUtil.getDemand(tenantId, demandId, requestInfo);

        assertEquals("mocked-demand-response", result);
        verify(requestRepository).fetchResult(
                argThat(sb -> sb.toString().equals("http://demand-host/demand?tenantId=test-tenant&demandId=demand-001")),
                eq(requestInfo)
        );
    }
}