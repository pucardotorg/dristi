package org.pucar.dristi.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfigurationTest {

    private Configuration configuration;

    @BeforeEach
    void setUp() {
        configuration = new Configuration();

        ReflectionTestUtils.setField(configuration, "hearingBusinessServices", "hearingService1,hearingService2");
        ReflectionTestUtils.setField(configuration, "caseBusinessServices", "caseService1,caseService2");
        ReflectionTestUtils.setField(configuration, "evidenceBusinessServices", "evidenceService1,evidenceService2");
        ReflectionTestUtils.setField(configuration, "taskBusinessServices", "taskService1,taskService2");
        ReflectionTestUtils.setField(configuration, "applicationBusinessServices", "applicationService1,applicationService2");
        ReflectionTestUtils.setField(configuration, "orderBusinessServices", "orderService1,orderService2");
    }

    @Test
    void testInit() {
        configuration.init();

        List<String> expectedHearingList = Arrays.asList("hearingService1", "hearingService2");
        List<String> expectedCaseList = Arrays.asList("caseService1", "caseService2");
        List<String> expectedEvidenceList = Arrays.asList("evidenceService1", "evidenceService2");
        List<String> expectedTaskList = Arrays.asList("taskService1", "taskService2");
        List<String> expectedApplicationList = Arrays.asList("applicationService1", "applicationService2");
        List<String> expectedOrderList = Arrays.asList("orderService1", "orderService2");

        assertEquals(expectedHearingList, configuration.getHearingBusinessServiceList());
        assertEquals(expectedCaseList, configuration.getCaseBusinessServiceList());
        assertEquals(expectedEvidenceList, configuration.getEvidenceBusinessServiceList());
        assertEquals(expectedTaskList, configuration.getTaskBusinessServiceList());
        assertEquals(expectedApplicationList, configuration.getApplicationBusinessServiceList());
        assertEquals(expectedOrderList, configuration.getOrderBusinessServiceList());
    }

    @Test
    void testGettersAndSetters() {
        // Test all the getters and setters for properties
        configuration.setEsHostUrl("http://localhost:9200");
        configuration.setPollInterval("30");
        configuration.setIndex("indexName");
        configuration.setCaseOverallStatusTopic("topicName");
        configuration.setBulkPath("/path/to/bulk");
        configuration.setEsUsername("username");
        configuration.setEsPassword("password");
        configuration.setTimezone("UTC");
        configuration.setStateLevelTenantId("tenantId");
        configuration.setHearingHost("http://hearing-host");
        configuration.setHearingSearchPath("/hearing/search");
        configuration.setCaseHost("http://case-host");
        configuration.setCaseSearchPath("/case/search");
        configuration.setEvidenceHost("http://evidence-host");
        configuration.setEvidenceSearchPath("/evidence/search");
        configuration.setTaskHost("http://task-host");
        configuration.setTaskSearchPath("/task/search");
        configuration.setApplicationHost("http://application-host");
        configuration.setApplicationSearchPath("/application/search");
        configuration.setOrderHost("http://order-host");
        configuration.setOrderSearchPath("/order/search");
        configuration.setApiCallDelayInSeconds(10);
        configuration.setMdmsHost("http://mdms-host");
        configuration.setMdmsEndPoint("/mdms/search");
        configuration.setMdmsPendingTaskModuleName("moduleName");
        configuration.setMdmsPendingTaskMasterName("masterName");

        assertEquals("http://localhost:9200", configuration.getEsHostUrl());
        assertEquals("30", configuration.getPollInterval());
        assertEquals("indexName", configuration.getIndex());
        assertEquals("topicName", configuration.getCaseOverallStatusTopic());
        assertEquals("/path/to/bulk", configuration.getBulkPath());
        assertEquals("username", configuration.getEsUsername());
        assertEquals("password", configuration.getEsPassword());
        assertEquals("UTC", configuration.getTimezone());
        assertEquals("tenantId", configuration.getStateLevelTenantId());
        assertEquals("http://hearing-host", configuration.getHearingHost());
        assertEquals("/hearing/search", configuration.getHearingSearchPath());
        assertEquals("http://case-host", configuration.getCaseHost());
        assertEquals("/case/search", configuration.getCaseSearchPath());
        assertEquals("http://evidence-host", configuration.getEvidenceHost());
        assertEquals("/evidence/search", configuration.getEvidenceSearchPath());
        assertEquals("http://task-host", configuration.getTaskHost());
        assertEquals("/task/search", configuration.getTaskSearchPath());
        assertEquals("http://application-host", configuration.getApplicationHost());
        assertEquals("/application/search", configuration.getApplicationSearchPath());
        assertEquals("http://order-host", configuration.getOrderHost());
        assertEquals("/order/search", configuration.getOrderSearchPath());
        assertEquals(10, configuration.getApiCallDelayInSeconds());
        assertEquals("http://mdms-host", configuration.getMdmsHost());
        assertEquals("/mdms/search", configuration.getMdmsEndPoint());
        assertEquals("moduleName", configuration.getMdmsPendingTaskModuleName());
        assertEquals("masterName", configuration.getMdmsPendingTaskMasterName());
    }
}
