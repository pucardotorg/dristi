package digit.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.config.Configuration;
import digit.repository.ServiceRequestRepository;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.request.User;
import org.egov.common.contract.user.UserDetailResponse;
import org.egov.common.contract.user.enums.UserType;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserUtilTest {

    @Mock
    private ObjectMapper mapper;

    @Mock
    private ServiceRequestRepository serviceRequestRepository;

    @Mock
    private Configuration configs;

    @InjectMocks
    private UserUtil userUtil;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUserCallForSearchEndpoint() {
        // Setup
        StringBuilder uri = new StringBuilder("/user/search");
        Object userRequest = new Object();
        LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();
        List<LinkedHashMap<String, Object>> users = new ArrayList<>();
        LinkedHashMap<String, Object> user = new LinkedHashMap<>();
        user.put("createdDate", "2024-02-10 10:00:00");
        user.put("lastModifiedDate", "2024-02-10 11:00:00");
        user.put("dob", "1990-01-01");
        user.put("pwdExpiryDate", "2025-02-10 10:00:00");
        users.add(user);
        responseMap.put("user", users);

        when(configs.getUserSearchEndpoint()).thenReturn("/user/search");
        when(serviceRequestRepository.fetchResult(any(), any())).thenReturn(responseMap);
        when(mapper.convertValue(any(), any(Class.class))).thenReturn(new UserDetailResponse());

        // Test
        UserDetailResponse response = userUtil.userCall(userRequest, uri);

        // Verify
        assertNotNull(response);
    }

    @Test
    public void testUserCallForCreateEndpoint() {
        // Setup
        StringBuilder uri = new StringBuilder("/user/create");
        Object userRequest = new Object();
        LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();
        List<LinkedHashMap<String, Object>> users = new ArrayList<>();
        LinkedHashMap<String, Object> user = new LinkedHashMap<>();
        user.put("createdDate", "10-02-2024 10:00:00");
        users.add(user);
        responseMap.put("user", users);

        when(configs.getUserCreateEndpoint()).thenReturn("/user/create");
        when(configs.getUserSearchEndpoint()).thenReturn("/user/search");
        when(configs.getUserUpdateEndpoint()).thenReturn("/user/update");
        when(serviceRequestRepository.fetchResult(any(), any())).thenReturn(responseMap);
        when(mapper.convertValue(any(), any(Class.class))).thenReturn(new UserDetailResponse());

        // Test
        UserDetailResponse response = userUtil.userCall(userRequest, uri);

        // Verify
        assertNotNull(response);
    }

    @Test
    public void testUserCallWithInvalidDateFormat() {
        // Setup
        StringBuilder uri = new StringBuilder("/user/search");
        Object userRequest = new Object();
        LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();
        List<LinkedHashMap<String, Object>> users = new ArrayList<>();
        LinkedHashMap<String, Object> user = new LinkedHashMap<>();
        user.put("createdDate", "invalid-date-format");
        users.add(user);
        responseMap.put("user", users);

        when(configs.getUserSearchEndpoint()).thenReturn("/user/search");
        when(serviceRequestRepository.fetchResult(any(), any())).thenReturn(responseMap);

        // Test & Verify
        assertThrows(CustomException.class, () -> userUtil.userCall(userRequest, uri));
    }

    @Test
    public void testUserCallWithObjectMapperException() {
        // Setup
        StringBuilder uri = new StringBuilder("/user/search");
        Object userRequest = new Object();
        when(configs.getUserSearchEndpoint()).thenReturn("/user/search");
        when(serviceRequestRepository.fetchResult(any(), any())).thenReturn(new LinkedHashMap<>());
        when(mapper.convertValue(any(), any(Class.class))).thenThrow(IllegalArgumentException.class);

        // Test & Verify
        assertThrows(CustomException.class, () -> userUtil.userCall(userRequest, uri));
    }

    @Test
    public void testAddUserDefaultFields() {
        // Setup
        String mobileNumber = "1234567890";
        String tenantId = "state.city";
        User userInfo = new User();
        UserType userType = UserType.CITIZEN;

        // Test
        userUtil.addUserDefaultFields(mobileNumber, tenantId, userInfo, userType);

        // Verify
        assertEquals(mobileNumber, userInfo.getUserName());
        assertEquals("state", userInfo.getTenantId());
        assertEquals(UserType.CITIZEN.toString(), userInfo.getType());
        assertEquals(1, userInfo.getRoles().size());
        Role role = userInfo.getRoles().get(0);
        assertEquals("CITIZEN", role.getCode());
        assertEquals("Citizen", role.getName());
        assertEquals("state", role.getTenantId());
    }

    @Test
    public void testGetStateLevelTenant() {
        // Test
        String result = userUtil.getStateLevelTenant("state.city");

        // Verify
        assertEquals("state", result);
    }

    @Test
    public void testParseResponseWithNullUsers() {
        // Setup
        LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("user", null);

        // Test
        userUtil.parseResponse(responseMap, "dd-MM-yyyy");

        // Verify - no exception should be thrown
        assertNull(responseMap.get("user"));
    }

}