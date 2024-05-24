package org.pucar.dristi.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")  // Use the application-test.properties
public class ConfigurationTest {

    @Autowired
    private Configuration configuration;

    @Test
    public void testConfigurationValues() {
        // Assert User Config
        assertEquals("http://localhost:8086", configuration.getUserHost());
        assertEquals("/user/users", configuration.getUserContextPath());
        assertEquals("/_createnovalidate", configuration.getUserCreateEndpoint());
        assertEquals("/user/_search", configuration.getUserSearchEndpoint());
        assertEquals("/_updatenovalidate", configuration.getUserUpdateEndpoint());

        // Assert Idgen Config
        assertEquals("http://localhost:8082/", configuration.getIdGenHost());
        assertEquals("egov-idgen/id/_generate", configuration.getIdGenPath());

        // Assert Case Config
        assertEquals("http://localhost:9090/", configuration.getCaseHost());
        assertEquals("case/case/v1/_search", configuration.getCasePath());

        // Assert Workflow Config
        assertEquals("http://localhost:8083", configuration.getWfHost());
        assertEquals("/egov-workflow-v2/egov-wf/process/_transition", configuration.getWfTransitionPath());
        assertEquals("/egov-workflow-v2/egov-wf/businessservice/_search", configuration.getWfBusinessServiceSearchPath());
        assertEquals("/egov-workflow-v2/egov-wf/process/_search", configuration.getWfProcessInstanceSearchPath());

        // Assert MDMS Config
        assertEquals("http://localhost:8084", configuration.getMdmsHost());
        assertEquals("/egov-mdms-service/v1/_search", configuration.getMdmsEndPoint());

        // Assert HRMS Config
        assertEquals("https://dev.digit.org", configuration.getHrmsHost());
        assertEquals("/egov-hrms/employees/_search", configuration.getHrmsEndPoint());

        // Assert URL Shortening Config
        assertEquals("https://dev.digit.org", configuration.getUrlShortnerHost());
        assertEquals("/egov-url-shortening/shortener", configuration.getUrlShortnerEndpoint());

        // Assert SMS Notification Config
        assertEquals("egov.core.notification.sms", configuration.getSmsNotificationTopic());

        // Assert Kafka Topics
        assertEquals("save-order-application", configuration.getSaveOrderKafkaTopic());
        assertEquals("update-order-application", configuration.getUpdateOrderKafkaTopic());

        // Assert Workflow Order Business Config
        assertEquals("order-services", configuration.getOrderBusinessName());
        assertEquals("order", configuration.getOrderBusinessServiceName());
    }
}
