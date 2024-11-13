package org.pucar.dristi.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.config.MdmsDataConfig;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.web.models.CaseOutcome;
import org.pucar.dristi.web.models.CaseOutcomeType;
import org.pucar.dristi.web.models.CaseOverallStatusType;
import org.pucar.dristi.web.models.CaseStageSubStage;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CaseOverallStatusUtilTest {

    @Mock
    private Configuration config;

    @Mock
    private HearingUtil hearingUtil;

    @Mock
    private OrderUtil orderUtil;

    @Mock
    private Producer producer;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private MdmsDataConfig mdmsDataConfig;

    @InjectMocks
    private CaseOverallStatusUtil caseOverallStatusUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckCaseOverAllStatusForCase() throws JsonProcessingException {
        // Prepare test data
        String entityType = "case";
        String referenceId = "123";
        String status = "pending approval";
        String action = "submit_case";
        String tenantId = "tenant1";

        User user = new User();
        user.setUuid("uuid-test-123");

        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setUserInfo(user);

        JSONObject requestInfoJson = new JSONObject();

        Map<String, List<CaseOverallStatusType>> caseOverallStatusTypeMap = new HashMap<>();
        caseOverallStatusTypeMap.put("case",List.of(CaseOverallStatusType.builder().action(action).state(status).build()));

        // Mock configuration
        when(config.getCaseBusinessServiceList()).thenReturn(List.of("case"));
        when(mapper.readValue(anyString(), eq(RequestInfo.class))).thenReturn(requestInfo);
        when(config.getCaseOverallStatusTopic()).thenReturn("topic");
        when(mdmsDataConfig.getCaseOverallStatusTypeMap()).thenReturn(caseOverallStatusTypeMap);
        // Call the method
        Object result = caseOverallStatusUtil.checkCaseOverAllStatus(entityType, referenceId, status, action, tenantId, requestInfoJson);

        // Assertions
        assertNull(result); // processCaseOverallStatus returns null

        // Verify publishToCaseOverallStatus method is called with correct arguments
        verify(producer, times(1)).push(anyString(), any(CaseStageSubStage.class));
    }

    @Test
    void testCheckCaseOverAllStatusForHearing() throws Exception {
        // Prepare test data
        String entityType = "hearing";
        String referenceId = "456";
        String status = "status";
        String action = "create";
        String tenantId = "tenant2";

        User user = new User();
        user.setUuid("uuid-test-123");
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setUserInfo(user);
        JSONObject requestInfoJson = new JSONObject();

        JSONObject hearingObject = new JSONObject();
        JSONArray filingList = new JSONArray();
        filingList.put("filingNumber");
        hearingObject.put("filingNumber",filingList);
        hearingObject.put("hearingType","evidence");

        Map<String, List<CaseOverallStatusType>> caseOverallStatusTypeMap = new HashMap<>();
        caseOverallStatusTypeMap.put("hearing",List.of(CaseOverallStatusType.builder().action(action).typeIdentifier("evidence").state(status).build()));

        // Mock configuration
        when(config.getHearingBusinessServiceList()).thenReturn(List.of("hearing"));
        when(hearingUtil.getHearing(any(), any(), any(), eq(referenceId), any())).thenReturn(hearingObject);
        when(mapper.readValue(anyString(), eq(RequestInfo.class))).thenReturn(requestInfo);
        when(config.getCaseOverallStatusTopic()).thenReturn("topic");
        when(mdmsDataConfig.getCaseOverallStatusTypeMap()).thenReturn(caseOverallStatusTypeMap);


        // Call the method
        Object result = caseOverallStatusUtil.checkCaseOverAllStatus(entityType, referenceId, status, action, tenantId, requestInfoJson);

        // Assertions
        assertNotNull(result); // processHearingCaseOverallStatus returns the hearingObject

        // Verify publishToCaseOverallStatus method is called with correct arguments
        verify(producer, times(1)).push(anyString(), any(CaseStageSubStage.class));
    }

    @Test
    void testCheckCaseOverAllStatusForOrder() throws Exception {
        // Prepare test data
        String entityType = "order";
        String referenceId = "789";
        String status = "published";
        String action = "action";
        String tenantId = "tenant3";

        User user = new User();
        user.setUuid("uuid-test-123");
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setUserInfo(user);
        JSONObject requestInfoJson = new JSONObject();

        JSONObject orderObject = new JSONObject();
        orderObject.put("filingNumber","filingNumber");
        orderObject.put("orderType","WITHDRAWAL");

        Map<String, List<CaseOverallStatusType>> caseOverallStatusTypeMap = new HashMap<>();
        caseOverallStatusTypeMap.put("order",List.of(CaseOverallStatusType.builder().action(action).typeIdentifier("WITHDRAWAL").state(status).build()));
        Map<String, CaseOutcomeType> caseOutcomeTypeMap = new HashMap<>();
        caseOutcomeTypeMap.put("WITHDRAWAL", CaseOutcomeType.builder().isJudgement(false).build());
        // Mock configuration
        when(config.getOrderBusinessServiceList()).thenReturn(List.of("order"));
        when(orderUtil.getOrder(any(), eq(referenceId), any())).thenReturn(orderObject);
        when(mapper.readValue(anyString(), eq(RequestInfo.class))).thenReturn(requestInfo);
        when(config.getCaseOutcomeTopic()).thenReturn("topic");
        when(mdmsDataConfig.getCaseOverallStatusTypeMap()).thenReturn(caseOverallStatusTypeMap);
        when(mdmsDataConfig.getCaseOutcomeTypeMap()).thenReturn(caseOutcomeTypeMap);

        // Call the method
        Object result = caseOverallStatusUtil.checkCaseOverAllStatus(entityType, referenceId, status, action, tenantId, requestInfoJson);

        // Assertions
        assertNotNull(result); // processOrderOverallStatus returns the orderObject

        // Verify publishToCaseOverallStatus method is called with correct arguments
        verify(producer, times(1)).push(anyString(), any(CaseOutcome.class));
    }

    @Test
    void testCheckCaseOverAllStatusUnsupportedEntityType() throws JSONException {
        // Prepare test data
        String entityType = "unsupported";
        String referenceId = "123";
        String status = null;
        String action = null;
        String tenantId = "tenant1";
        JSONObject requestInfo = new JSONObject().put("RequestInfo", new JSONObject());

        // Mock configuration
        when(config.getCaseBusinessServiceList()).thenReturn(List.of("case"));
        when(config.getHearingBusinessServiceList()).thenReturn(List.of("hearing"));
        when(config.getOrderBusinessServiceList()).thenReturn(List.of("order"));
        when(mdmsDataConfig.getCaseOverallStatusTypeMap()).thenReturn(new HashMap<>());

        // Call the method
        Object result = caseOverallStatusUtil.checkCaseOverAllStatus(entityType, referenceId, status, action, tenantId, requestInfo);

        // Assertions
        assertNull(result);
    }
}
