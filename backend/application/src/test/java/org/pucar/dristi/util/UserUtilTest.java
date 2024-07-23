package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.request.User;
import org.egov.common.contract.user.UserDetailResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserUtilTest {

    @InjectMocks
    private UserUtil userUtil;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private ServiceRequestRepository serviceRequestRepository;

    @Mock
    private Configuration configs;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(userUtil, "configs", configs);
    }


    @Test
    void testParseResponse() {
        LinkedHashMap responseMap = new LinkedHashMap();
        LinkedHashMap userMap = new LinkedHashMap();
        userMap.put("createdDate", "01-01-2020 12:00:00");
        responseMap.put("User", List.of(userMap));

        userUtil.parseResponse(responseMap, "dd-MM-yyyy HH:mm:ss");

        assertNotNull(responseMap);
    }

    @Test
    void testDateToLong() {
        String date = "01-01-2020 12:00:00";
        Long result = ReflectionTestUtils.invokeMethod(userUtil, "dateTolong", date, "dd-MM-yyyy HH:mm:ss");

        assertNotNull(result);
    }


    @Test
    void testGetCitizenRole() {
        String tenantId = "tenant";
        Role role = ReflectionTestUtils.invokeMethod(userUtil, "getCitizenRole", tenantId);

        assertNotNull(role);
        assertEquals("CITIZEN", role.getCode());
    }

    @Test
    void testGetStateLevelTenant() {
        String tenantId = "tenant.subtenant";
        String stateLevelTenant = userUtil.getStateLevelTenant(tenantId);

        assertEquals("tenant", stateLevelTenant);
    }
}
