package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.util.UserUtil;

import java.util.*;

class UserUtilTest {

    @InjectMocks
    private UserUtil userUtil;

    @Mock
    private ServiceRequestRepository serviceRequestRepository;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private Configuration configs;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testUserCall_UserSearchEndpoint() {
        // Setup
        StringBuilder uri = new StringBuilder("/search");
        Mockito.when(configs.getUserSearchEndpoint()).thenReturn("/search");
        // Test
        // Write test logic here
    }
    @Test
    void testParseResponse_WithUsers() {
        // Setup
        LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();
        List<LinkedHashMap<String, Object>> users = new ArrayList<>();
        LinkedHashMap<String, Object> user = new LinkedHashMap<>();
        user.put("createdDate", "2024-05-28 12:00:00");
        users.add(user);
        responseMap.put("user", users);
        // Test
        userUtil.parseResponse(responseMap, "dd-MM-yyyy HH:mm:ss");
        Assertions.assertEquals(1, users.size());
        Assertions.assertTrue(users.get(0).containsKey("createdDate"));
        Assertions.assertTrue(users.get(0).get("createdDate") instanceof Long);
    }
}
