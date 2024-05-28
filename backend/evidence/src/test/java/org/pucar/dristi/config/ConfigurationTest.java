package org.pucar.dristi.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
class ConfigurationTest {

    @Autowired
    private Configuration configuration;

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
