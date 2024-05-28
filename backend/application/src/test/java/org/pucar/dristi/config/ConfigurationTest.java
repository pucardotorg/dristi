package org.pucar.dristi.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigurationTest {

    @InjectMocks
    private Configuration configuration;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Manually setting the values for testing
        ReflectionTestUtils.setField(configuration, "userHost", "http://userhost.com");
        ReflectionTestUtils.setField(configuration, "userContextPath", "/user/context");
        ReflectionTestUtils.setField(configuration, "userCreateEndpoint", "/user/create");
        ReflectionTestUtils.setField(configuration, "userSearchEndpoint", "/user/search");
        ReflectionTestUtils.setField(configuration, "userUpdateEndpoint", "/user/update");
        ReflectionTestUtils.setField(configuration, "idGenHost", "http://idgenhost.com");
        ReflectionTestUtils.setField(configuration, "idGenPath", "/idgen/path");
        ReflectionTestUtils.setField(configuration, "wfHost", "http://wfhost.com");
        ReflectionTestUtils.setField(configuration, "wfTransitionPath", "/wf/transition");
        ReflectionTestUtils.setField(configuration, "wfBusinessServiceSearchPath", "/wf/businessservice/search");
        ReflectionTestUtils.setField(configuration, "wfProcessInstanceSearchPath", "/wf/processinstance/search");
        ReflectionTestUtils.setField(configuration, "mdmsHost", "http://mdmshost.com");
        ReflectionTestUtils.setField(configuration, "mdmsEndPoint", "/mdms/search");
        ReflectionTestUtils.setField(configuration, "hrmsHost", "http://hrmshost.com");
        ReflectionTestUtils.setField(configuration, "hrmsEndPoint", "/hrms/search");
        ReflectionTestUtils.setField(configuration, "urlShortnerHost", "http://urlshortnerhost.com");
        ReflectionTestUtils.setField(configuration, "urlShortnerEndpoint", "/urlshortner/endpoint");
        ReflectionTestUtils.setField(configuration, "smsNotificationTopic", "smsNotificationTopic");
        ReflectionTestUtils.setField(configuration, "applicationCreateTopic", "applicationCreateTopic");
        ReflectionTestUtils.setField(configuration, "applicationUpdateTopic", "applicationUpdateTopic");
    }

    @Test
    public void testUserHost() {
        assertEquals("http://userhost.com", configuration.getUserHost());
    }

    @Test
    public void testUserContextPath() {
        assertEquals("/user/context", configuration.getUserContextPath());
    }

    @Test
    public void testUserCreateEndpoint() {
        assertEquals("/user/create", configuration.getUserCreateEndpoint());
    }

    @Test
    public void testUserSearchEndpoint() {
        assertEquals("/user/search", configuration.getUserSearchEndpoint());
    }

    @Test
    public void testUserUpdateEndpoint() {
        assertEquals("/user/update", configuration.getUserUpdateEndpoint());
    }

    @Test
    public void testIdGenHost() {
        assertEquals("http://idgenhost.com", configuration.getIdGenHost());
    }

    @Test
    public void testIdGenPath() {
        assertEquals("/idgen/path", configuration.getIdGenPath());
    }

    @Test
    public void testWfHost() {
        assertEquals("http://wfhost.com", configuration.getWfHost());
    }

    @Test
    public void testWfTransitionPath() {
        assertEquals("/wf/transition", configuration.getWfTransitionPath());
    }

    @Test
    public void testWfBusinessServiceSearchPath() {
        assertEquals("/wf/businessservice/search", configuration.getWfBusinessServiceSearchPath());
    }

    @Test
    public void testWfProcessInstanceSearchPath() {
        assertEquals("/wf/processinstance/search", configuration.getWfProcessInstanceSearchPath());
    }

    @Test
    public void testMdmsHost() {
        assertEquals("http://mdmshost.com", configuration.getMdmsHost());
    }

    @Test
    public void testMdmsEndPoint() {
        assertEquals("/mdms/search", configuration.getMdmsEndPoint());
    }

    @Test
    public void testHrmsHost() {
        assertEquals("http://hrmshost.com", configuration.getHrmsHost());
    }

    @Test
    public void testHrmsEndPoint() {
        assertEquals("/hrms/search", configuration.getHrmsEndPoint());
    }

    @Test
    public void testUrlShortnerHost() {
        assertEquals("http://urlshortnerhost.com", configuration.getUrlShortnerHost());
    }

    @Test
    public void testUrlShortnerEndpoint() {
        assertEquals("/urlshortner/endpoint", configuration.getUrlShortnerEndpoint());
    }

    @Test
    public void testSmsNotificationTopic() {
        assertEquals("smsNotificationTopic", configuration.getSmsNotificationTopic());
    }

    @Test
    public void testApplicationCreateTopic() {
        assertEquals("applicationCreateTopic", configuration.getApplicationCreateTopic());
    }

    @Test
    public void testApplicationUpdateTopic() {
        assertEquals("applicationUpdateTopic", configuration.getApplicationUpdateTopic());
    }
}
