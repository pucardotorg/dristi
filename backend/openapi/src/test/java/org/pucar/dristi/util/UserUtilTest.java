package org.pucar.dristi.util;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.request.User;
import org.egov.common.contract.user.UserDetailResponse;
import org.egov.common.contract.user.enums.UserType;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserUtilTest {

    @Mock
    private ObjectMapper mapper;

    @Mock
    private ServiceRequestRepository serviceRequestRepository;

    @Mock
    private Configuration configs;

    @InjectMocks
    private UserUtil userUtil;

    @Test
    public void testUserCallSearchEndpoint() {
        // Arrange
        Object userRequest = new Object();
        StringBuilder uri = new StringBuilder("http://example.com/user-search");

        when(configs.getUserSearchEndpoint()).thenReturn("/user-search");

        LinkedHashMap mockResponseMap = new LinkedHashMap();
        List<LinkedHashMap> users = new ArrayList<>();
        LinkedHashMap userMap = new LinkedHashMap();
        userMap.put("createdDate", "2023-12-12 10:00:00");
        userMap.put("lastModifiedDate", "2023-12-12 11:00:00");
        userMap.put("dob", "2001-01-01");
        users.add(userMap);
        mockResponseMap.put("user", users);

        when(serviceRequestRepository.fetchResult(eq(uri), eq(userRequest))).thenReturn(mockResponseMap);
        UserDetailResponse mockUserDetailResponse = new UserDetailResponse();
        when(mapper.convertValue(mockResponseMap, UserDetailResponse.class)).thenReturn(mockUserDetailResponse);

        // Act
        UserDetailResponse result = userUtil.userCall(userRequest, uri);

        // Assert
        assertNotNull(result);
        verify(serviceRequestRepository).fetchResult(eq(uri), eq(userRequest));
        verify(mapper).convertValue(mockResponseMap, UserDetailResponse.class);
    }

    @Test
    public void testUserCallCreateEndpoint() {
        // Arrange
        Object userRequest = new Object();
        StringBuilder uri = new StringBuilder("http://example.com/user-create");

        when(configs.getUserCreateEndpoint()).thenReturn("/user-create");

        LinkedHashMap mockResponseMap = new LinkedHashMap();
        List<LinkedHashMap> users = new ArrayList<>();
        LinkedHashMap userMap = new LinkedHashMap();
        userMap.put("createdDate", "12-12-2023 10:00:00");
        userMap.put("dob", "01/01/2000");
        users.add(userMap);
        mockResponseMap.put("user", users);

        when(serviceRequestRepository.fetchResult(eq(uri), eq(userRequest))).thenReturn(mockResponseMap);
        when(configs.getUserSearchEndpoint()).thenReturn("/user-search");
        when(configs.getUserUpdateEndpoint()).thenReturn("/user-update");
        UserDetailResponse mockUserDetailResponse = new UserDetailResponse();
        when(mapper.convertValue(mockResponseMap, UserDetailResponse.class)).thenReturn(mockUserDetailResponse);

        // Act
        UserDetailResponse result = userUtil.userCall(userRequest, uri);

        // Assert
        assertNotNull(result);
        verify(serviceRequestRepository).fetchResult(eq(uri), eq(userRequest));
        verify(mapper).convertValue(mockResponseMap, UserDetailResponse.class);
    }

    @Test
    public void testUserCallUpdateEndpoint() {
        // Arrange
        Object userRequest = new Object();
        StringBuilder uri = new StringBuilder("http://example.com/user-create");

        when(configs.getUserUpdateEndpoint()).thenReturn("/user-update");

        LinkedHashMap mockResponseMap = new LinkedHashMap();
        List<LinkedHashMap> users = new ArrayList<>();
        LinkedHashMap userMap = new LinkedHashMap();
        userMap.put("createdDate", "2023-12-12 10:00:00");
        userMap.put("lastModifiedDate", "2023-12-12 11:00:00");
        userMap.put("dob", "01/01/2000");
        userMap.put("pwdExpiryDate", "2024-12-12 10:00:00");
        users.add(userMap);
        mockResponseMap.put("user", users);

        when(serviceRequestRepository.fetchResult(eq(uri), eq(userRequest))).thenReturn(mockResponseMap);
        when(configs.getUserSearchEndpoint()).thenReturn("/user-search");
        when(configs.getUserCreateEndpoint()).thenReturn("/user-create");
        UserDetailResponse mockUserDetailResponse = new UserDetailResponse();
        when(mapper.convertValue(mockResponseMap, UserDetailResponse.class)).thenReturn(mockUserDetailResponse);
//        doNothing().when(userUtil).parseResponse(any(), anyString());
        // Act
        UserDetailResponse result = userUtil.userCall(userRequest, uri);

        // Assert
        assertNotNull(result);
        verify(serviceRequestRepository).fetchResult(eq(uri), eq(userRequest));
        verify(mapper).convertValue(mockResponseMap, UserDetailResponse.class);
    }

    @Test
    public void testParseResponse() {
        // Arrange
        LinkedHashMap responseMap = new LinkedHashMap();
        List<LinkedHashMap> users = new ArrayList<>();
        LinkedHashMap userMap1 = new LinkedHashMap();
        userMap1.put("createdDate", "12-12-2023 10:00:00");
        userMap1.put("lastModifiedDate", "12-12-2023 11:00:00");
        userMap1.put("dob", "01-01-2000");
        userMap1.put("pwdExpiryDate", "12-12-2024 10:00:00");
        users.add(userMap1);
        responseMap.put("user", users);

        // Act
        userUtil.parseResponse(responseMap, "dd-MM-yyyy");

        // Assert
        LinkedHashMap parsedUserMap = (LinkedHashMap) ((List) responseMap.get("user")).get(0);
        assertTrue(parsedUserMap.get("createdDate") instanceof Long);
        assertTrue(parsedUserMap.get("lastModifiedDate") instanceof Long);
        assertTrue(parsedUserMap.get("dob") instanceof Long);
        assertTrue(parsedUserMap.get("pwdExpiryDate") instanceof Long);
    }

    @Test
    public void testParseResponseNullUsers() {
        // Arrange
        LinkedHashMap responseMap = new LinkedHashMap();
        responseMap.put("user", null);

        // Act & Assert
        assertDoesNotThrow(() -> userUtil.parseResponse(responseMap, "dd-MM-yyyy"));
    }

    @Test
    public void testAddUserDefaultFields() {
        // Arrange
        String mobileNumber = "1234567890";
        String tenantId = "state.district";
        User userInfo = new User();
        UserType userType = UserType.CITIZEN;

        // Act
        userUtil.addUserDefaultFields(mobileNumber, tenantId, userInfo, userType);

        // Assert
        assertEquals(mobileNumber, userInfo.getUserName());
        assertEquals("state", userInfo.getTenantId());
        assertEquals(UserType.CITIZEN.toString(), userInfo.getType());

        List<Role> roles = userInfo.getRoles();
        assertNotNull(roles);
        assertEquals(1, roles.size());

        Role role = roles.get(0);
        assertEquals("CITIZEN", role.getCode());
        assertEquals("state", role.getTenantId());
    }

    @Test
    public void testGetStateLevelTenant() {
        // Arrange
        String tenantId = "state.district.subdiv";

        // Act
        String result = userUtil.getStateLevelTenant(tenantId);

        // Assert
        assertEquals("state", result);
    }

    @Test
    public void testUserCallWithInvalidResponse() {
        Object userRequest = new Object();
        StringBuilder uri = new StringBuilder("http://example.com/user-search");

        when(configs.getUserSearchEndpoint()).thenReturn("/user-search");
        when(serviceRequestRepository.fetchResult(eq(uri), eq(userRequest)))
                .thenThrow(new IllegalArgumentException("Invalid response"));

        assertThrows(CustomException.class, () ->
                userUtil.userCall(userRequest, uri));
    }
}