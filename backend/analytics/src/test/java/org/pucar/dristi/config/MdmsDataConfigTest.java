package org.pucar.dristi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.config.MdmsDataConfig;
import org.pucar.dristi.util.MdmsUtil;
import org.pucar.dristi.web.models.CaseOutcomeType;
import org.pucar.dristi.web.models.CaseOverallStatusType;
import org.pucar.dristi.web.models.PendingTaskType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

class MdmsDataConfigTest {

    @Mock
    private MdmsUtil mdmsUtil;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Configuration configuration;

    @InjectMocks
    private MdmsDataConfig mdmsDataConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock configuration properties
        when(configuration.getStateLevelTenantId()).thenReturn("tenantId");
        when(configuration.getMdmsPendingTaskModuleName()).thenReturn("mdmsPendingTaskModuleName");
        when(configuration.getMdmsPendingTaskMasterName()).thenReturn("mdmsPendingTaskMasterName");
        when(configuration.getMdmsCaseOverallStatusModuleName()).thenReturn("mdmsCaseOverallStatusModuleName");
        when(configuration.getMdmsCaseOverallStatusMasterName()).thenReturn("mdmsCaseOverallStatusMasterName");
        when(configuration.getMdmsCaseOutcomeModuleName()).thenReturn("mdmsCaseOutcomeModuleName");
        when(configuration.getMdmsCaseOutcomeMasterName()).thenReturn("mdmsCaseOutcomeMasterName");
    }

    @Test
    void testLoadConfigData() {
        // Prepare mock MDMS data for pendingTaskTypeMap
        JSONArray pendingTaskTypeList = new JSONArray();
        JSONObject pendingTaskTypeJson1 = new JSONObject();
        pendingTaskTypeJson1.put("workflowModule", "module1");
        pendingTaskTypeList.add(pendingTaskTypeJson1);

        JSONObject pendingTaskTypeJson2 = new JSONObject();
        pendingTaskTypeJson2.put("workflowModule", "module2");
        pendingTaskTypeList.add(pendingTaskTypeJson2);

        // Prepare mock MDMS data for caseOverallStatusTypeMap
        JSONArray caseOverallStatusTypeList = new JSONArray();
        JSONObject caseOverallStatusTypeJson1 = new JSONObject();
        caseOverallStatusTypeJson1.put("workflowModule", "module1");
        caseOverallStatusTypeJson1.put("stage", "Pre-Trial");
        caseOverallStatusTypeJson1.put("substage", "Filing");
        caseOverallStatusTypeList.add(caseOverallStatusTypeJson1);

        JSONObject caseOverallStatusTypeJson2 = new JSONObject();
        caseOverallStatusTypeJson2.put("workflowModule", "module2");
        caseOverallStatusTypeJson2.put("stage", "Trial");
        caseOverallStatusTypeJson2.put("substage", "Evidence");
        caseOverallStatusTypeList.add(caseOverallStatusTypeJson2);

        // Prepare mock MDMS data for caseOutcomeTypeMap
        JSONArray caseOutcomeTypeList = new JSONArray();
        JSONObject caseOutcomeTypeJson1 = new JSONObject();
        caseOutcomeTypeJson1.put("orderType", "order1");
        caseOutcomeTypeList.add(caseOutcomeTypeJson1);

        JSONObject caseOutcomeTypeJson2 = new JSONObject();
        caseOutcomeTypeJson2.put("orderType", "order2");
        caseOutcomeTypeList.add(caseOutcomeTypeJson2);

        Map<String, JSONArray> pendingTaskMasterData = new HashMap<>();
        pendingTaskMasterData.put("mdmsPendingTaskMasterName", pendingTaskTypeList);

        Map<String, JSONArray> caseOverallStatusMasterData = new HashMap<>();
        caseOverallStatusMasterData.put("mdmsCaseOverallStatusMasterName", caseOverallStatusTypeList);

        Map<String, JSONArray> caseOutcomeMasterData = new HashMap<>();
        caseOutcomeMasterData.put("mdmsCaseOutcomeMasterName", caseOutcomeTypeList);

        Map<String, Map<String, JSONArray>> mdmsModuleData = new HashMap<>();
        mdmsModuleData.put("mdmsPendingTaskModuleName", pendingTaskMasterData);
        mdmsModuleData.put("mdmsCaseOverallStatusModuleName", caseOverallStatusMasterData);
        mdmsModuleData.put("mdmsCaseOutcomeModuleName", caseOutcomeMasterData);

        when(mdmsUtil.fetchMdmsData(any(RequestInfo.class), any(String.class), any(String.class), anyList())).thenReturn(mdmsModuleData);

        // Mock objectMapper behavior for pendingTaskTypeMap
        PendingTaskType pendingTaskType1 = new PendingTaskType();
        pendingTaskType1.setWorkflowModule("module1");
        when(objectMapper.convertValue(pendingTaskTypeJson1, PendingTaskType.class)).thenReturn(pendingTaskType1);

        PendingTaskType pendingTaskType2 = new PendingTaskType();
        pendingTaskType2.setWorkflowModule("module2");
        when(objectMapper.convertValue(pendingTaskTypeJson2, PendingTaskType.class)).thenReturn(pendingTaskType2);

        // Mock objectMapper behavior for caseOverallStatusTypeMap
        CaseOverallStatusType caseOverallStatusType1 = new CaseOverallStatusType();
        caseOverallStatusType1.setWorkflowModule("module1");
        caseOverallStatusType1.setStage("Pre-Trial");
        caseOverallStatusType1.setSubstage("Filing");
        when(objectMapper.convertValue(caseOverallStatusTypeJson1, CaseOverallStatusType.class)).thenReturn(caseOverallStatusType1);

        CaseOverallStatusType caseOverallStatusType2 = new CaseOverallStatusType();
        caseOverallStatusType2.setWorkflowModule("module2");
        caseOverallStatusType2.setStage("Trial");
        caseOverallStatusType2.setSubstage("Evidence");
        when(objectMapper.convertValue(caseOverallStatusTypeJson2, CaseOverallStatusType.class)).thenReturn(caseOverallStatusType2);

        // Mock objectMapper behavior for caseOutcomeTypeMap
        CaseOutcomeType caseOutcomeType1 = new CaseOutcomeType();
        caseOutcomeType1.setOrderType("order1");
        when(objectMapper.convertValue(caseOutcomeTypeJson1, CaseOutcomeType.class)).thenReturn(caseOutcomeType1);

        CaseOutcomeType caseOutcomeType2 = new CaseOutcomeType();
        caseOutcomeType2.setOrderType("order2");
        when(objectMapper.convertValue(caseOutcomeTypeJson2, CaseOutcomeType.class)).thenReturn(caseOutcomeType2);

        // Execute the method
        mdmsDataConfig.loadConfigData();

        // Verify the results for pendingTaskTypeMap
        assertNotNull(mdmsDataConfig.getPendingTaskTypeMap());
        assertEquals(2, mdmsDataConfig.getPendingTaskTypeMap().size());

        List<PendingTaskType> module1Tasks = mdmsDataConfig.getPendingTaskTypeMap().get("module1");
        assertNotNull(module1Tasks);
        assertEquals(1, module1Tasks.size());
        assertEquals("module1", module1Tasks.get(0).getWorkflowModule());

        List<PendingTaskType> module2Tasks = mdmsDataConfig.getPendingTaskTypeMap().get("module2");
        assertNotNull(module2Tasks);
        assertEquals(1, module2Tasks.size());
        assertEquals("module2", module2Tasks.get(0).getWorkflowModule());

        // Verify the results for caseOverallStatusTypeMap
        assertNotNull(mdmsDataConfig.getCaseOverallStatusTypeMap());
        assertEquals(2, mdmsDataConfig.getCaseOverallStatusTypeMap().size());

        List<CaseOverallStatusType> module1StatusTypes = mdmsDataConfig.getCaseOverallStatusTypeMap().get("module1");
        assertNotNull(module1StatusTypes);
        assertEquals(1, module1StatusTypes.size());
        assertEquals("module1", module1StatusTypes.get(0).getWorkflowModule());
        assertEquals("Pre-Trial", module1StatusTypes.get(0).getStage());
        assertEquals("Filing", module1StatusTypes.get(0).getSubstage());

        List<CaseOverallStatusType> module2StatusTypes = mdmsDataConfig.getCaseOverallStatusTypeMap().get("module2");
        assertNotNull(module2StatusTypes);
        assertEquals(1, module2StatusTypes.size());
        assertEquals("module2", module2StatusTypes.get(0).getWorkflowModule());
        assertEquals("Trial", module2StatusTypes.get(0).getStage());
        assertEquals("Evidence", module2StatusTypes.get(0).getSubstage());

        // Verify the results for caseOutcomeTypeMap
        assertNotNull(mdmsDataConfig.getCaseOutcomeTypeMap());
        assertEquals(2, mdmsDataConfig.getCaseOutcomeTypeMap().size());

        CaseOutcomeType outcomeType1 = mdmsDataConfig.getCaseOutcomeTypeMap().get("order1");
        assertNotNull(outcomeType1);
        assertEquals("order1", outcomeType1.getOrderType());

        CaseOutcomeType outcomeType2 = mdmsDataConfig.getCaseOutcomeTypeMap().get("order2");
        assertNotNull(outcomeType2);
        assertEquals("order2", outcomeType2.getOrderType());
    }
}
