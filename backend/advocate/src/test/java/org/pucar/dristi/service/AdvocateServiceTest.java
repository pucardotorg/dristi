package org.pucar.dristi.service;
import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.AdvocateRegistrationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.AdvocateRepository;
import org.pucar.dristi.validators.AdvocateRegistrationValidator;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AdvocateServiceTest {

    @InjectMocks
    private AdvocateService advocateService;

    @Mock
    private AdvocateRegistrationValidator validator;

    @Mock
    private AdvocateRegistrationEnrichment enrichmentUtil;

    @Mock
    private WorkflowService workflowService;

    @Mock
    private IndividualService individualService;

    @Mock
    private AdvocateRepository advocateRepository;

    @Mock
    private Producer producer;

    @Mock
    private Configuration config;


    @Test
    public void testRegisterAdvocateClerkRequest_Success() {
        // Mock data
        AdvocateRequest request = new AdvocateRequest();
        // Setup mocks
        when(config.getAdvocateCreateTopic()).thenReturn("advCreateTopic");
        // Test method
        advocateService.createAdvocate(request);
        // Verify interactions
        verify(validator).validateAdvocateRegistration(request);
        verify(enrichmentUtil).enrichAdvocateRegistration(request);
        verify(workflowService).updateWorkflowStatus(request);
        verify(producer).push("advCreateTopic", request);
    }

    @Test
    public void registerAdvocateRequest_Exception() {
        // Arrange
        AdvocateRequest advocateRequest = new AdvocateRequest();

        doThrow(new RuntimeException("Internal error")).when(validator).validateAdvocateRegistration(any());

        // Act and Assert
        assertThrows(Exception.class, () -> {
            advocateService.createAdvocate(advocateRequest);
        });
    }

    @Test
    public void registerAdvocateRequest_CustomException() {
        // Arrange
        AdvocateRequest advocateRequest = new AdvocateRequest();

        doThrow(new CustomException()).when(validator).validateAdvocateRegistration(any());

        // Act and Assert
        assertThrows(CustomException.class, () -> {
            advocateService.createAdvocate(advocateRequest);
        });
    }

    @Test
    public void registerAdvocateRequest_Custom() {
        // Arrange
        AdvocateRequest advocateRequest = new AdvocateRequest();

        doThrow(new RuntimeException("Internal error")).when(validator).validateAdvocateRegistration(any());

        // Act and Assert
        assertThrows(CustomException.class, () -> {
            advocateService.createAdvocate(advocateRequest);
        });
    }

    @Test
    public void searchAdvocateClerkApplications() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setType("INDIVIDUAL");
        userInfo.setUuid(UUID.randomUUID().toString());
        requestInfo.setUserInfo(userInfo);

        List<AdvocateSearchCriteria> advocateSearchCriteria = new ArrayList<>();
        // Populate advocateClerkSearchCriteria with test data

        String tenantId = "testTenantId";
        Integer limit = 10;
        Integer offset = 0;
        advocateService.searchAdvocate(requestInfo, advocateSearchCriteria, tenantId, limit, offset);
        verify(advocateRepository, times(1)).getApplications(advocateSearchCriteria, "testTenantId",   10, 0);
    }

    @Test
    public void searchAdvocateClerkApplications_IndividualLoggedInUser() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setType("INDIVIDUAL");
        userInfo.setUuid(UUID.randomUUID().toString());
        requestInfo.setUserInfo(userInfo);

        List<AdvocateSearchCriteria> advocateSearchCriteria = new ArrayList<>();
        // Populate advocateClerkSearchCriteria with test data

        String tenantId = "testTenantId";
        Integer limit = 10;
        Integer offset = 0;

        AtomicReference<Boolean> isIndividualLoggedInUser = new AtomicReference<>(false);

        // Act
        advocateService.searchAdvocate(requestInfo, advocateSearchCriteria, tenantId, limit, offset);
        verify(advocateRepository, times(1)).getApplications(advocateSearchCriteria, "testTenantId",   10, 0);
    }

    @Test
    public void searchAdvocateApplications_IndividualLoggedInUserEmploye() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setType("EMPLOYEE");
        userInfo.setUuid(UUID.randomUUID().toString());
        requestInfo.setUserInfo(userInfo);

        List<AdvocateSearchCriteria> advocateSearchCriteria = new ArrayList<>();
        // Populate advocateClerkSearchCriteria with test data

        String tenantId = "testTenantId";
        Integer limit = null;
        Integer offset = null;

        // Act
        advocateService.searchAdvocate(requestInfo, advocateSearchCriteria, tenantId, limit, offset);
        verify(advocateRepository, times(1)).getApplications(advocateSearchCriteria, "testTenantId",   10, 0);
    }

    @Test
    public void searchAdvocate_CustomException() {
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setType("EMPLOYEE");
        userInfo.setUuid(UUID.randomUUID().toString());
        requestInfo.setUserInfo(userInfo);

        List<AdvocateSearchCriteria> advocateSearchCriteria = new ArrayList<>();
        String tenantId = "testTenantId";
        Integer limit = null;
        Integer offset = null;

        when(advocateRepository.getApplications(any(), any(), anyInt(), anyInt())).thenThrow(CustomException.class);

        // Assert
        assertThrows(CustomException.class, () -> advocateService.searchAdvocate(requestInfo, advocateSearchCriteria, tenantId, limit, offset));
    }

    @Test
    public void searchAdvocate_Exception() {
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setType("EMPLOYEE");
        userInfo.setUuid(UUID.randomUUID().toString());
        requestInfo.setUserInfo(userInfo);

        List<AdvocateSearchCriteria> advocateSearchCriteria = new ArrayList<>();
        String tenantId = "testTenantId";
        Integer limit = null;
        Integer offset = null;

        when(advocateRepository.getApplications(any(), any(), anyInt(), anyInt())).thenThrow(RuntimeException.class);

        // Assert
        assertThrows(Exception.class, () -> advocateService.searchAdvocate(requestInfo, advocateSearchCriteria, tenantId, limit, offset));
    }

    @Test
    public void testSearchAdvocateByStatus_Success() {
        // Setup
        String status = "testStatus";
        String tenantId = "testTenantId";
        Integer limit = 10;
        Integer offset = 0;
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setType("EMPLOYEE");
        userInfo.setUuid(UUID.randomUUID().toString());
        requestInfo.setUserInfo(userInfo);

        // Mock behavior
        when(advocateRepository.getListApplicationsByStatus(status, tenantId, limit, offset)).thenReturn(Collections.singletonList(new Advocate() {
        }));

        // Execution
        List<Advocate> result = advocateService.searchAdvocateByStatus(requestInfo, status, tenantId, limit, offset);

        // Verification
        assertNotNull(result);
        // Add more verification as needed
    }

    @Test
    public void testSearchAdvocateByStatus_EmptySuccess() {
        // Setup
        String status = "testStatus";
        String tenantId = "testTenantId";
        Integer limit = 10;
        Integer offset = 0;
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setType("EMPLOYEE");
        userInfo.setUuid(UUID.randomUUID().toString());
        requestInfo.setUserInfo(userInfo);

        // Mock behavior
        when(advocateRepository.getListApplicationsByStatus(status, tenantId, limit, offset)).thenReturn(new ArrayList<>());

        // Execution
        List<Advocate> result = advocateService.searchAdvocateByStatus(requestInfo, status, tenantId, limit, offset);

        // Verification
        assertNotNull(result);
        // Add more verification as needed
    }

    @Test
    public void searchAdvocateByStatus_Exception() {
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setType("EMPLOYEE");
        userInfo.setUuid(UUID.randomUUID().toString());
        requestInfo.setUserInfo(userInfo);

        List<AdvocateSearchCriteria> advocateSearchCriteria = new ArrayList<>();
        String status = "testStatus";
        String tenantId = "testTenantId";
        Integer limit = null;
        Integer offset = null;

        when(advocateRepository.getListApplicationsByStatus(anyString(), anyString(), anyInt(), anyInt())).thenThrow(RuntimeException.class);

        // Assert
        assertThrows(Exception.class, () -> advocateService.searchAdvocateByStatus(requestInfo, status, tenantId, limit, offset));
    }

    @Test
    public void searchAdvocateByStatus_CustomException() {
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setType("EMPLOYEE");
        userInfo.setUuid(UUID.randomUUID().toString());
        requestInfo.setUserInfo(userInfo);

        List<AdvocateSearchCriteria> advocateSearchCriteria = new ArrayList<>();
        String status = "testStatus";
        String tenantId = "testTenantId";
        Integer limit = null;
        Integer offset = null;

        when(advocateRepository.getListApplicationsByStatus(anyString(), anyString(), anyInt(), anyInt())).thenThrow(CustomException.class);

        // Assert
        assertThrows(CustomException.class, () -> advocateService.searchAdvocateByStatus(requestInfo, status, tenantId, limit, offset));
    }

    @Test
    public void testSearchAdvocateByApplicationNumber_Success() {
        // Setup
        String applicationNumber = "testApplicationNumber";
        String tenantId = "testTenantId";
        Integer limit = 10;
        Integer offset = 0;
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setType("EMPLOYEE");
        userInfo.setUuid(UUID.randomUUID().toString());
        requestInfo.setUserInfo(userInfo);

        // Mock behavior
        when(advocateRepository.getListApplicationsByApplicationNumber(applicationNumber, tenantId, limit, offset)).thenReturn(Collections.singletonList(new Advocate() {
        }));

        // Execution
        List<Advocate> result = advocateService.searchAdvocateByApplicationNumber(requestInfo, applicationNumber, tenantId, limit, offset);

        // Verification
        assertNotNull(result);
        // Add more verification as needed
    }

    @Test
    public void testSearchAdvocateByApplicationNumber_EmptySuccess() {
        // Setup
        String applicationNumber = "testApplicationNumber";
        String tenantId = "testTenantId";
        Integer limit = null;
        Integer offset = null;
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setType("EMPLOYEE");
        userInfo.setUuid(UUID.randomUUID().toString());
        requestInfo.setUserInfo(userInfo);

        // Execution
        List<Advocate> result = advocateService.searchAdvocateByApplicationNumber(requestInfo, applicationNumber, tenantId, limit, offset);

        // Verification
        assertNotNull(result);
        // Add more verification as needed
    }

    @Test
    public void testSearchAdvocateByApplicationNumber_CustomException() {
        // Setup
        String applicationNumber = "testApplicationNumber";
        String tenantId = "testTenantId";
        Integer limit = null;
        Integer offset = null;
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setType("EMPLOYEE");
        userInfo.setUuid(UUID.randomUUID().toString());
        requestInfo.setUserInfo(userInfo);

        // Execution
        List<Advocate> result = advocateService.searchAdvocateByApplicationNumber(requestInfo, applicationNumber, tenantId, limit, offset);

        when(advocateRepository.getListApplicationsByApplicationNumber(anyString(), anyString(), anyInt(), anyInt())).thenThrow(CustomException.class);

        // Assert
        assertThrows(CustomException.class, () -> advocateService.searchAdvocateByApplicationNumber(requestInfo, applicationNumber, tenantId, limit, offset));
    }

    @Test
    public void testSearchAdvocateByApplicationNumber_Exception() {
        // Setup
        String applicationNumber = "testApplicationNumber";
        String tenantId = "testTenantId";
        Integer limit = null;
        Integer offset = null;
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setType("EMPLOYEE");
        userInfo.setUuid(UUID.randomUUID().toString());
        requestInfo.setUserInfo(userInfo);

        when(advocateRepository.getListApplicationsByApplicationNumber(anyString(), anyString(), anyInt(), anyInt())).thenThrow(RuntimeException.class);

        // Assert
        assertThrows(Exception.class, () -> advocateService.searchAdvocateByApplicationNumber(requestInfo, applicationNumber, tenantId, limit, offset));
    }

    @Test
    public void updateAdvocate_EmptySuccess() {
        // Arrange
        AdvocateRequest advocateRequest = new AdvocateRequest();
        Advocate advocate = new Advocate();
        advocateRequest.setAdvocate(advocate);

        when(validator.validateApplicationExistence(any())).thenReturn(new Advocate());
        doNothing().when(enrichmentUtil).enrichAdvocateApplicationUponUpdate(any());
        doNothing().when(workflowService).updateWorkflowStatus((AdvocateRequest) any());
        when(config.getAdvocateUpdateTopic()).thenReturn("testTopic");
        doNothing().when(producer).push(anyString(), any());

        // Act
        Advocate result = advocateService.updateAdvocate(advocateRequest);

        // Assert
        assertNotNull(result);
        assertEquals(advocate, result);
    }

    @Test
    public void updateAdvocate_Success() {
        AdvocateRequest advocateRequest = new AdvocateRequest();
        Advocate advocate = new Advocate();
        advocate.setApplicationNumber("appNum1");
        advocate.setTenantId("tenantId");
        advocate.setStatus("ACTIVE");
        advocateRequest.setAdvocate(advocate);

        when(validator.validateApplicationExistence(any())).thenReturn(advocate);
//        when(config.getAdvClerkUpdateTopic()).thenReturn("testTopic");

        // Act
        Advocate result = advocateService.updateAdvocate(advocateRequest);

        // Assert
        assertNotNull(result);
        assertEquals(advocate, result);
    }

    @Test
    public void updateAdvocate_ValidationCustomException() {
        // Arrange
        AdvocateRequest advocateRequest = new AdvocateRequest();
        Advocate advocate = new Advocate();
        advocate.setApplicationNumber("appNum1");
        advocate.setTenantId("tenantId");
        advocateRequest.setAdvocate(advocate);

        when(validator.validateApplicationExistence(any())).thenThrow(CustomException.class);

        // Assert
        assertThrows(CustomException.class, () -> advocateService.updateAdvocate(advocateRequest));
    }

    @Test
    public void updateAdvocate_ValidationException() {
        // Arrange
        AdvocateRequest advocateRequest = new AdvocateRequest();
        Advocate advocate = new Advocate();
        advocate.setApplicationNumber("appNum1");
        advocate.setTenantId("tenantId");
        advocateRequest.setAdvocate(advocate);

        // Assert
        assertThrows(Exception.class, () -> advocateService.updateAdvocate(advocateRequest));
    }
}


