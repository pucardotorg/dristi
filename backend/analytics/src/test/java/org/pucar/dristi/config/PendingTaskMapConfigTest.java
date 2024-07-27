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
import org.pucar.dristi.util.MdmsUtil;
import org.pucar.dristi.web.models.PendingTaskType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

class PendingTaskMapConfigTest {

    @Mock
    private MdmsUtil mdmsUtil;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Configuration configuration;

    @InjectMocks
    private PendingTaskMapConfig pendingTaskMapConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock configuration properties
        when(configuration.getStateLevelTenantId()).thenReturn("tenantId");
        when(configuration.getMdmsModuleName()).thenReturn("mdmsModuleName");
        when(configuration.getMdmsMasterName()).thenReturn("mdmsMasterName");
    }

    @Test
    void testLoadConfigData() {
        // Prepare mock MDMS data
        JSONArray pendingTaskTypeList = new JSONArray();
        JSONObject pendingTaskTypeJson1 = new JSONObject();
        pendingTaskTypeJson1.put("workflowModule", "module1");
        pendingTaskTypeList.add(pendingTaskTypeJson1);

        JSONObject pendingTaskTypeJson2 = new JSONObject();
        pendingTaskTypeJson2.put("workflowModule", "module2");
        pendingTaskTypeList.add(pendingTaskTypeJson2);

        Map<String, JSONArray> mdmsMasterData = new HashMap<>();
        mdmsMasterData.put("mdmsMasterName", pendingTaskTypeList);

        Map<String, Map<String, JSONArray>> mdmsModuleData = new HashMap<>();
        mdmsModuleData.put("mdmsModuleName", mdmsMasterData);

        when(mdmsUtil.fetchMdmsData(any(RequestInfo.class), any(String.class), any(String.class), anyList())).thenReturn(mdmsModuleData);

        // Mock objectMapper behavior
        PendingTaskType pendingTaskType1 = new PendingTaskType();
        pendingTaskType1.setWorkflowModule("module1");
        when(objectMapper.convertValue(pendingTaskTypeJson1, PendingTaskType.class)).thenReturn(pendingTaskType1);

        PendingTaskType pendingTaskType2 = new PendingTaskType();
        pendingTaskType2.setWorkflowModule("module2");
        when(objectMapper.convertValue(pendingTaskTypeJson2, PendingTaskType.class)).thenReturn(pendingTaskType2);

        // Execute the method
        pendingTaskMapConfig.loadConfigData();

        // Verify the results
        assertNotNull(pendingTaskMapConfig.getPendingTaskTypeMap());
        assertEquals(2, pendingTaskMapConfig.getPendingTaskTypeMap().size());

        List<PendingTaskType> module1Tasks = pendingTaskMapConfig.getPendingTaskTypeMap().get("module1");
        assertNotNull(module1Tasks);
        assertEquals(1, module1Tasks.size());
        assertEquals("module1", module1Tasks.get(0).getWorkflowModule());

        List<PendingTaskType> module2Tasks = pendingTaskMapConfig.getPendingTaskTypeMap().get("module2");
        assertNotNull(module2Tasks);
        assertEquals(1, module2Tasks.size());
        assertEquals("module2", module2Tasks.get(0).getWorkflowModule());
    }

    @Test
    void testLoadConfigDataWithException() {
        // Prepare mock MDMS data
        JSONArray pendingTaskTypeList = new JSONArray();
        JSONObject pendingTaskTypeJson = new JSONObject();
        pendingTaskTypeJson.put("workflowModule", "module1");
        pendingTaskTypeList.add(pendingTaskTypeJson);

        Map<String, JSONArray> mdmsMasterData = new HashMap<>();
        mdmsMasterData.put("mdmsMasterName", pendingTaskTypeList);

        Map<String, Map<String, JSONArray>> mdmsModuleData = new HashMap<>();
        mdmsModuleData.put("mdmsModuleName", mdmsMasterData);

        when(mdmsUtil.fetchMdmsData(any(RequestInfo.class), any(String.class), any(String.class), anyList())).thenReturn(mdmsModuleData);

        // Mock objectMapper to throw an exception
        when(objectMapper.convertValue(pendingTaskTypeJson, PendingTaskType.class)).thenThrow(new RuntimeException("Conversion error"));

        // Execute the method
        pendingTaskMapConfig.loadConfigData();

        // Verify the map is empty due to exception
        assertNotNull(pendingTaskMapConfig.getPendingTaskTypeMap());
        assertEquals(0, pendingTaskMapConfig.getPendingTaskTypeMap().size());
    }
}
