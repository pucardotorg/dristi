package org.pucar.dristi.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.AdvocateClerkRegistrationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.AdvocateClerkRepository;
import org.pucar.dristi.validators.AdvocateClerkRegistrationValidator;
import org.pucar.dristi.web.models.AdvocateClerk;
import org.pucar.dristi.web.models.AdvocateClerkRequest;
import org.pucar.dristi.web.models.AdvocateClerkSearchCriteria;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
 class AdvocateClerkServiceTest {

    @InjectMocks
    private AdvocateClerkService advocateClerkService;

    @Mock
    private AdvocateClerkRepository advocateClerkRepository;

    @Mock
    private AdvocateClerkRegistrationValidator validator;

    @Mock
    private AdvocateClerkRegistrationEnrichment enrichmentUtil;

    @Mock
    private WorkflowService workflowService;

    @Mock
    private IndividualService individualService;

    @Mock
    private Producer producer;

    @Mock
    private Configuration config;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
     void testRegisterAdvocateClerkRequest_Success() {
        // Mock data
        AdvocateClerkRequest request = new AdvocateClerkRequest();
        // Setup mocks
        when(config.getAdvClerkcreateTopic()).thenReturn("advClerkCreateTopic");
        // Test method
        advocateClerkService.registerAdvocateClerkRequest(request);
        // Verify interactions
        verify(validator).validateAdvocateClerkRegistration(request);
        verify(enrichmentUtil).enrichAdvocateClerkRegistration(request);
        verify(workflowService).updateWorkflowStatus(request);
        verify(producer).push("advClerkCreateTopic", request);
    }

    @Test
    void registerAdvocateClerkRequest_Exception() {
        // Arrange
        AdvocateClerkRequest advocateClerkRequest = new AdvocateClerkRequest();

        doThrow(new RuntimeException("Internal error")).when(validator).validateAdvocateClerkRegistration(any());

        // Act and Assert
        assertThrows(Exception.class, () -> {
            advocateClerkService.registerAdvocateClerkRequest(advocateClerkRequest);
        });
    }

    @Test
    void registerAdvocateClerkRequest_CustomException() {
        // Arrange
        AdvocateClerkRequest advocateClerkRequest = new AdvocateClerkRequest();

        doThrow(new CustomException()).when(validator).validateAdvocateClerkRegistration(any());

        // Act and Assert
        assertThrows(CustomException.class, () -> {
            advocateClerkService.registerAdvocateClerkRequest(advocateClerkRequest);
        });
    }

    @Test
    void searchAdvocateClerkApplications_IndividualLoggedInUser() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setType("INDIVIDUAL");
        userInfo.setUuid(UUID.randomUUID().toString());
        requestInfo.setUserInfo(userInfo);

        List<AdvocateClerkSearchCriteria> advocateClerkSearchCriteria = new ArrayList<>();
        // Populate advocateClerkSearchCriteria with test data

        String tenantId = "testTenantId";
        Integer limit = 10;
        Integer offset = 0;

        Map<String, String> individualUserUUID = new HashMap<>();
        individualUserUUID.put("userUuid", userInfo.getUuid());


        // Act
        advocateClerkService.searchAdvocateClerkApplications(requestInfo, advocateClerkSearchCriteria, tenantId, limit, offset);
        verify(advocateClerkRepository, times(1)).getClerks(advocateClerkSearchCriteria, "testTenantId",   10, 0);
    }

    @Test
    void searchAdvocateClerkApplications_Exception() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setType("INDIVIDUAL");
        userInfo.setUuid(UUID.randomUUID().toString());
        requestInfo.setUserInfo(userInfo);

        List<AdvocateClerkSearchCriteria> advocateClerkSearchCriteria = new ArrayList<>();
        // Populate advocateClerkSearchCriteria with test data

        String tenantId = "testTenantId";
        Integer limit = 10;
        Integer offset = 0;

        when(advocateClerkRepository.getClerks(any(), any(), any(), any())).thenThrow(new RuntimeException());

        assertThrows(Exception.class, () -> {
            advocateClerkService.searchAdvocateClerkApplications(requestInfo, advocateClerkSearchCriteria, tenantId, limit, offset);
        });
    }

    @Test
    void searchAdvocateClerkApplications_CustomException() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setType("INDIVIDUAL");
        userInfo.setUuid(UUID.randomUUID().toString());
        requestInfo.setUserInfo(userInfo);

        List<AdvocateClerkSearchCriteria> advocateClerkSearchCriteria = new ArrayList<>();
        // Populate advocateClerkSearchCriteria with test data

        String tenantId = "testTenantId";
        Integer limit = 10;
        Integer offset = 0;

        when(advocateClerkRepository.getClerks(any(), any(), any(), any())).thenThrow(new CustomException());

        assertThrows(CustomException.class, () -> {
            advocateClerkService.searchAdvocateClerkApplications(requestInfo, advocateClerkSearchCriteria, tenantId, limit, offset);
        });
    }

    @Test
    void searchAdvocateClerkApplications_IndividualLoggedInUserEmploye() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setType("EMPLOYEE");
        userInfo.setUuid(UUID.randomUUID().toString());
        requestInfo.setUserInfo(userInfo);

        List<AdvocateClerkSearchCriteria> advocateClerkSearchCriteria = new ArrayList<>();
        // Populate advocateClerkSearchCriteria with test data

        String tenantId = "testTenantId";
        Integer limit = null;
        Integer offset = null;

        Map<String, String> individualUserUUID = new HashMap<>();
        individualUserUUID.put("userUuid", userInfo.getUuid());

        // Act
        advocateClerkService.searchAdvocateClerkApplications(requestInfo, advocateClerkSearchCriteria, tenantId, limit, offset);

        verify(advocateClerkRepository, times(1)).getClerks(advocateClerkSearchCriteria, "testTenantId",   10, 0);

    }
    @Test
    void searchAdvocateClerkApplicationsByAppNumber_Success() {
        // Arrange
        String applicationNumber = "testAppNumber";
        String tenantId = "testTenantId";
        Integer limit = 10;
        Integer offset = 0;

        List<AdvocateClerk> applications = new ArrayList<>();
        AdvocateClerk clerk = new AdvocateClerk();
        clerk.setApplicationNumber("appNum1");
        clerk.setTenantId("tenantId");
        applications.add(clerk);

        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setType("EMPLOYEE");
        userInfo.setUuid(UUID.randomUUID().toString());
        requestInfo.setUserInfo(userInfo);

        when(advocateClerkRepository.getApplicationsByAppNumber(anyString(), anyString(), anyInt(), anyInt())).thenReturn(applications);

        // Act
        List<AdvocateClerk> result = advocateClerkService.searchAdvocateClerkApplicationsByAppNumber(requestInfo, applicationNumber, tenantId, limit, offset);

        // Assert
        assertNotNull(result);
        assertEquals(applications, result);
    }

    @Test
    void searchAdvocateClerkApplicationsByAppNumber_Exception() {
        // Arrange
        String applicationNumber = "testAppNumber";
        String tenantId = "testTenantId";
        Integer limit = null;
        Integer offset = null;
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setType("EMPLOYEE");
        userInfo.setUuid(UUID.randomUUID().toString());
        requestInfo.setUserInfo(userInfo);

        when(advocateClerkRepository.getApplicationsByAppNumber(anyString(), anyString(), anyInt(), anyInt())).thenThrow(RuntimeException.class);
        // Act and Assert
        assertThrows(Exception.class, () -> {
            advocateClerkService.searchAdvocateClerkApplicationsByAppNumber(requestInfo, applicationNumber, tenantId, limit, offset);
        });
    }

    @Test
    void searchAdvocateClerkApplicationsByAppNumber_CustomException() {
        // Arrange
        String applicationNumber = "testAppNumber";
        String tenantId = "testTenantId";
        Integer limit = null;
        Integer offset = null;
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setType("EMPLOYEE");
        userInfo.setUuid(UUID.randomUUID().toString());
        requestInfo.setUserInfo(userInfo);

        when(advocateClerkRepository.getApplicationsByAppNumber(anyString(), anyString(), anyInt(), anyInt())).thenThrow(new CustomException());
        // Act and Assert
        assertThrows(CustomException.class, () -> {
            advocateClerkService.searchAdvocateClerkApplicationsByAppNumber(requestInfo, applicationNumber, tenantId, limit, offset);
        });
    }

    @Test
    void searchAdvocateClerkApplicationsByAppNumber_EmptySuccess() {
        // Arrange
        String applicationNumber = "testAppNumber";
        String tenantId = "testTenantId";
        Integer limit = null;
        Integer offset = null;
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setType("EMPLOYEE");
        userInfo.setUuid(UUID.randomUUID().toString());
        requestInfo.setUserInfo(userInfo);

        List<AdvocateClerk> applications = new ArrayList<>();
        // Populate applications with test data

        when(advocateClerkRepository.getApplicationsByAppNumber(anyString(), anyString(), anyInt(), anyInt())).thenReturn(applications);

        // Act
        List<AdvocateClerk> result = advocateClerkService.searchAdvocateClerkApplicationsByAppNumber(requestInfo, applicationNumber, tenantId, limit, offset);

        // Assert
        assertNotNull(result);
        assertEquals(applications, result);
    }

    @Test
    void searchAdvocateClerkApplicationsByStatus_Success() {
        // Arrange
        String status = "testStatus";
        String tenantId = "testTenantId";
        Integer limit = 10;
        Integer offset = 0;

        List<AdvocateClerk> applications = new ArrayList<>();
        AdvocateClerk clerk = new AdvocateClerk();
        clerk.setApplicationNumber("appNum1");
        clerk.setTenantId("tenantId");
        applications.add(clerk);

        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setType("EMPLOYEE");
        userInfo.setUuid(UUID.randomUUID().toString());
        requestInfo.setUserInfo(userInfo);

        when(advocateClerkRepository.getApplicationsByStatus(anyString(), anyString(), anyInt(), anyInt())).thenReturn(applications);

        // Act
        List<AdvocateClerk> result = advocateClerkService.searchAdvocateClerkApplicationsByStatus(requestInfo, status, tenantId, limit, offset);

        // Assert
        assertNotNull(result);
        assertEquals(applications, result);
    }

    @Test
    void searchAdvocateClerkApplicationsByStatus_EmptySuccess() {
        // Arrange
        String status = "testStatus";
        String tenantId = "testTenantId";
        Integer limit = null;
        Integer offset = null;

        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setType("EMPLOYEE");
        userInfo.setUuid(UUID.randomUUID().toString());
        requestInfo.setUserInfo(userInfo);

        List<AdvocateClerk> applications = new ArrayList<>();
        // Populate applications with test data

        when(advocateClerkRepository.getApplicationsByStatus(anyString(), anyString(), anyInt(), anyInt())).thenReturn(applications);

        // Act
        List<AdvocateClerk> result = advocateClerkService.searchAdvocateClerkApplicationsByStatus(requestInfo, status, tenantId, limit, offset);

        // Assert
        assertNotNull(result);
        assertEquals(applications, result);
    }

    @Test
    void searchAdvocateClerkApplicationsByStatus_Exception() {
        // Arrange
        String status = "testStatus";
        String tenantId = "testTenantId";
        Integer limit = null;
        Integer offset = null;

        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setType("EMPLOYEE");
        userInfo.setUuid(UUID.randomUUID().toString());
        requestInfo.setUserInfo(userInfo);

        when(advocateClerkRepository.getApplicationsByStatus(anyString(), anyString(), anyInt(), anyInt())).thenThrow(new RuntimeException());

        // Assert
        assertThrows(Exception.class, () -> advocateClerkService.searchAdvocateClerkApplicationsByStatus(requestInfo, status, tenantId, limit, offset));
    }

    @Test
    void searchAdvocateClerkApplicationsByStatus_CustomException() {
        // Arrange
        String status = "testStatus";
        String tenantId = "testTenantId";
        Integer limit = null;
        Integer offset = null;

        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setType("EMPLOYEE");
        userInfo.setUuid(UUID.randomUUID().toString());
        requestInfo.setUserInfo(userInfo);

        when(advocateClerkRepository.getApplicationsByStatus(anyString(), anyString(), anyInt(), anyInt())).thenThrow(new CustomException());

        // Assert
        assertThrows(CustomException.class, () -> advocateClerkService.searchAdvocateClerkApplicationsByStatus(requestInfo, status, tenantId, limit, offset));
    }

    @Test
    void updateAdvocateClerk_EmptySuccess() {
        // Arrange
        AdvocateClerkRequest advocateClerkRequest = new AdvocateClerkRequest();
        AdvocateClerk clerk = new AdvocateClerk();
        // Populate clerks with test data
        advocateClerkRequest.setClerk(clerk);

        when(validator.validateApplicationExistence(any())).thenReturn(new AdvocateClerk());
        doNothing().when(enrichmentUtil).enrichAdvocateClerkApplicationUponUpdate(any());
        doNothing().when(workflowService).updateWorkflowStatus((AdvocateClerkRequest) any());
        when(config.getAdvClerkUpdateTopic()).thenReturn("testTopic");
        doNothing().when(producer).push(anyString(), any());

        // Act
        AdvocateClerk result = advocateClerkService.updateAdvocateClerk(advocateClerkRequest);

        // Assert
        assertNotNull(result);
        assertEquals(clerk, result);
        // Add more assertions as needed
    }

    @Test
    void updateAdvocateClerk_Success() {
        // Arrange
        AdvocateClerkRequest advocateClerkRequest = new AdvocateClerkRequest();
        AdvocateClerk clerk = new AdvocateClerk();
        clerk.setApplicationNumber("appNum1");
        clerk.setTenantId("tenantId");
        advocateClerkRequest.setClerk(clerk);

        when(validator.validateApplicationExistence(any())).thenReturn(clerk);
        doNothing().when(enrichmentUtil).enrichAdvocateClerkApplicationUponUpdate(any());
        doNothing().when(workflowService).updateWorkflowStatus((AdvocateClerkRequest) any());
        when(config.getAdvClerkUpdateTopic()).thenReturn("testTopic");
        doNothing().when(producer).push(anyString(), any());

        // Act
        AdvocateClerk result = advocateClerkService.updateAdvocateClerk(advocateClerkRequest);

        // Assert
        assertNotNull(result);
        assertEquals(clerk, result);
        // Add more assertions as needed
    }

    @Test
    void updateAdvocate_ValidationCustomException() {
        // Arrange
        AdvocateClerkRequest advocateClerkRequest = new AdvocateClerkRequest();
        AdvocateClerk clerk = new AdvocateClerk();
        clerk.setApplicationNumber("appNum1");
        clerk.setTenantId("tenantId");
        advocateClerkRequest.setClerk(clerk);

        when(validator.validateApplicationExistence(any())).thenThrow(new CustomException());

        // Assert
        assertThrows(CustomException.class, () -> advocateClerkService.updateAdvocateClerk(advocateClerkRequest));
    }

    @Test
    void updateAdvocateClerk_ValidationException() {
        // Arrange
        AdvocateClerkRequest advocateClerkRequest = new AdvocateClerkRequest();
        AdvocateClerk clerk = new AdvocateClerk();
        clerk.setApplicationNumber("appNum1");
        clerk.setTenantId("tenantId");
        advocateClerkRequest.setClerk(clerk);

        when(validator.validateApplicationExistence(any())).thenThrow(new RuntimeException());

        // Assert
        assertThrows(Exception.class, () -> advocateClerkService.updateAdvocateClerk(advocateClerkRequest));
    }

}

