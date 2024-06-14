package org.pucar.dristi.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConfigurationTest {

    private Configuration configuration;

    @BeforeEach
    void setUp() {
        configuration = new Configuration();
        configuration.setUserHost("testUserHost");
        configuration.setUserContextPath("testUserContextPath");
        configuration.setUserCreateEndpoint("testUserCreateEndpoint");
        configuration.setUserSearchEndpoint("testUserSearchEndpoint");
        configuration.setUserUpdateEndpoint("testUserUpdateEndpoint");
        configuration.setIdGenHost("testIdGenHost");
        configuration.setIdGenPath("testIdGenPath");
        configuration.setWfHost("testWfHost");
        configuration.setWfTransitionPath("testWfTransitionPath");
        configuration.setWfBusinessServiceSearchPath("testWfBusinessServiceSearchPath");
        configuration.setWfProcessInstanceSearchPath("testWfProcessInstanceSearchPath");
        configuration.setMdmsHost("testMdmsHost");
        configuration.setMdmsEndPoint("testMdmsEndPoint");
        configuration.setHrmsHost("testHrmsHost");
        configuration.setHrmsEndPoint("testHrmsEndPoint");
        configuration.setUrlShortnerHost("testUrlShortnerHost");
        configuration.setUrlShortnerEndpoint("testUrlShortnerEndpoint");
        configuration.setSmsNotificationTopic("testSmsNotificationTopic");
        configuration.setEvidenceCreateTopic("testEvidenceCreateTopic");
        configuration.setUpdateEvidenceKafkaTopic("testUpdateEvidenceKafkaTopic");
        configuration.setBusinessServiceModule("testBusinessServiceModule");
        configuration.setBusinessServiceName("testBusinessServiceName");
    }

    @Test
    void testUserConfig() {
        assertNotNull(configuration.getUserHost());
        assertNotNull(configuration.getUserContextPath());
        assertNotNull(configuration.getUserCreateEndpoint());
        assertNotNull(configuration.getUserSearchEndpoint());
        assertNotNull(configuration.getUserUpdateEndpoint());
    }

    @Test
    void testIdGenConfig() {
        assertNotNull(configuration.getIdGenHost());
        assertNotNull(configuration.getIdGenPath());
    }

    @Test
    void testWorkflowConfig() {
        assertNotNull(configuration.getWfHost());
        assertNotNull(configuration.getWfTransitionPath());
        assertNotNull(configuration.getWfBusinessServiceSearchPath());
        assertNotNull(configuration.getWfProcessInstanceSearchPath());
    }

    @Test
    void testMdmsConfig() {
        assertNotNull(configuration.getMdmsHost());
        assertNotNull(configuration.getMdmsEndPoint());
    }

    @Test
    void testHrmsConfig() {
        assertNotNull(configuration.getHrmsHost());
        assertNotNull(configuration.getHrmsEndPoint());
    }

    @Test
    void testUrlShortnerConfig() {
        assertNotNull(configuration.getUrlShortnerHost());
        assertNotNull(configuration.getUrlShortnerEndpoint());
    }

    @Test
    void testSmsNotificationConfig() {
        assertNotNull(configuration.getSmsNotificationTopic());
    }

    @Test
    void testEvidenceConfig() {
        assertNotNull(configuration.getEvidenceCreateTopic());
        assertNotNull(configuration.getUpdateEvidenceKafkaTopic());
    }

    @Test
    void testBusinessServiceConfig() {
        assertNotNull(configuration.getBusinessServiceModule());
        assertNotNull(configuration.getBusinessServiceName());
    }
}
