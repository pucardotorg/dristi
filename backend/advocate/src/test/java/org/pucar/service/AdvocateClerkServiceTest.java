package org.pucar.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.config.Configuration;
import org.pucar.enrichment.AdvocateClerkRegistrationEnrichment;
import org.pucar.kafka.Producer;
import org.pucar.repository.AdvocateClerkRepository;
import org.pucar.validators.AdvocateClerkRegistrationValidator;
import org.pucar.web.models.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.pucar.config.ServiceConstants.TEST_EXCEPTION;

@ExtendWith(MockitoExtension.class)
public class AdvocateClerkServiceTest {

    @Mock
    private AdvocateClerkRegistrationValidator validator;

    @Mock
    private AdvocateClerkRegistrationEnrichment enrichmentUtil;

    @Mock
    private WorkflowService workflowService;

    @Mock
    private AdvocateClerkRepository advocateClerkRepository;

    @Mock
    private Producer producer;

    @InjectMocks
    private AdvocateClerkService service;
    @Mock
    private Configuration config;

    @Test
    public void testRegisterAdvocateClerkRequest_Success() {
        // Prepare request
        AdvocateClerkRequest request = new AdvocateClerkRequest();
        List<AdvocateClerk> clerks = new ArrayList<>();
        AdvocateClerk clerk = new AdvocateClerk();
        clerks.add(clerk);
        request.setClerks(clerks);

        // Call the method
        List<AdvocateClerk> response = service.registerAdvocateClerkRequest(request);

        // Verify results
        assertEquals(request.getClerks(), response);
        verify(validator, times(1)).validateAdvocateClerkRegistration(request);
        verify(enrichmentUtil, times(1)).enrichAdvocateClerkRegistration(request);
        verify(workflowService, times(1)).updateWorkflowStatus(request);
        verify(producer, times(1)).push(config.getAdvClerkcreateTopic(), request);
    }

    @Test()
    public void testRegisterAdvocateClerkRequest_CustomException() {
        // Prepare request
        AdvocateClerkRequest request = new AdvocateClerkRequest();
        List<AdvocateClerk> clerks = new ArrayList<>();
        AdvocateClerk clerk = new AdvocateClerk();
        clerks.add(clerk);
        request.setClerks(clerks);

        // Mock exception during validation
        doThrow(new CustomException(TEST_EXCEPTION,"Mock test")).when(validator).validateAdvocateClerkRegistration(request);

        // Call the method
        try {
            service.registerAdvocateClerkRequest(request);
        }
        catch (Exception e){
            assertTrue(e instanceof CustomException);
            assertEquals("Mock test", e.getMessage());
        }
    }
    @Test()
    public void testRegisterAdvocateClerkRequest_GenericException() {
        // Prepare request
        AdvocateClerkRequest request = new AdvocateClerkRequest();
        List<AdvocateClerk> clerks = new ArrayList<>();
        AdvocateClerk clerk = new AdvocateClerk();
        clerks.add(clerk);
        request.setClerks(clerks);

        // Mock successful validation, exception during enrichment
        doThrow(new CustomException(TEST_EXCEPTION,"Mock test")).when(enrichmentUtil).enrichAdvocateClerkRegistration(request);

        // Call the method
        try {
            service.registerAdvocateClerkRequest(request);
        }
        catch (Exception e){
            assertTrue(e instanceof CustomException);
            assertEquals("Mock test", e.getMessage());
            verify(validator, times(1)).validateAdvocateClerkRegistration(request);
        }
    }
    @Test
    public void testSearchAdvocateClerkApplications_Success() {
        // Prepare search criteria
        RequestInfo requestInfo = new RequestInfo();
        User user = new User();
        user.setType("CITIZEN");
        requestInfo.setUserInfo(user);
        List<AdvocateClerkSearchCriteria> searchCriteria = new ArrayList<>();
        List<String> statusList = Arrays.asList("APPROVED", "PENDING");
        String applicationNumber = "";

        // Mock successful repository call
        List<AdvocateClerk> applications = new ArrayList<>();
        when(advocateClerkRepository.getApplications(eq(searchCriteria), eq(statusList), eq(applicationNumber), any(), any(), any(), any())).thenReturn(applications);

        // Call the method
        List<AdvocateClerk> response = service.searchAdvocateClerkApplications(requestInfo, searchCriteria,statusList,applicationNumber, null, null, new Pagination());

        // Verify results
        assertEquals(applications, response);
        verify(advocateClerkRepository, times(1)).getApplications(eq(searchCriteria), eq(statusList), eq(applicationNumber), any(), any(), any(), any());
        verify(workflowService, times(0)).getWorkflowFromProcessInstance(any()); // No workflows to fetch for empty applications
    }

    @Test
    public void testSearchAdvocateClerkApplications_EmptyResults() {
        // Prepare search criteria
        RequestInfo requestInfo = new RequestInfo();
        User user = new User();
        user.setType("CITIZEN");
        requestInfo.setUserInfo(user);
        List<AdvocateClerkSearchCriteria> searchCriteria = new ArrayList<>();
        AdvocateClerkSearchCriteria criteria = new AdvocateClerkSearchCriteria(null, "APP123", null, null);
        searchCriteria.add(criteria);
        List<String> statusList = Arrays.asList("APPROVED", "PENDING");
        String applicationNumber = "";

        // Mock successful repository call with empty results
        List<AdvocateClerk> applications = Collections.emptyList();
        when(advocateClerkRepository.getApplications(any(), any(), any(), any(), any(), any(),any())).thenReturn(Collections.emptyList());

        // Call the method
        List<AdvocateClerk> response = service.searchAdvocateClerkApplications(requestInfo, searchCriteria, statusList, applicationNumber, null, null, new Pagination());

        // Verify results
        assertEquals(applications, response);
        verify(advocateClerkRepository, times(1)).getApplications(eq(searchCriteria), eq(statusList), eq(applicationNumber), any(), any(), any(), any());
        verify(workflowService, times(0)).getWorkflowFromProcessInstance(any()); // No workflows to fetch for empty applications
    }

    @Test
    public void testUpdateAdvocateClerk_Success() {
        // Prepare request
        AdvocateClerkRequest request = new AdvocateClerkRequest();
        List<AdvocateClerk> clerks = new ArrayList<>();
        AdvocateClerk clerk = new AdvocateClerk();
        clerk.setTenantId("tenantId");
        clerks.add(clerk);
        request.setClerks(clerks);

        // Mock successful behavior
        when(validator.validateApplicationExistence(clerk)).thenReturn(clerk); // Existing application found

        // Call the method
        List<AdvocateClerk> response = service.updateAdvocateClerk(request);

        // Verify results
        assertEquals(request.getClerks(), response);
        verify(validator, times(1)).validateApplicationExistence(clerk); // Verify for each clerk
        verify(enrichmentUtil, times(1)).enrichAdvocateClerkApplicationUponUpdate(request);
        verify(workflowService, times(1)).updateWorkflowStatus(request);
        verify(producer, times(1)).push(config.getAdvClerkUpdateTopic(), request);
    }

    @Test()
    public void testUpdateAdvocateClerk_ValidateApplicationExistenceException() {
        // Prepare request
        AdvocateClerkRequest request = new AdvocateClerkRequest();
        List<AdvocateClerk> clerks = new ArrayList<>();
        AdvocateClerk clerk = new AdvocateClerk();
        clerks.add(clerk);
        request.setClerks(clerks);

        // Mock exception during validation
        when(validator.validateApplicationExistence(clerk)).thenThrow(new CustomException(TEST_EXCEPTION,"Mock test"));

        // Call the method
        try{
            service.updateAdvocateClerk(request);
        }
        catch (Exception e){
        assertTrue(e instanceof CustomException);
        assertEquals("Error validating existing application: Mock test", e.getMessage());
        }
    }
}
