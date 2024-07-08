package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.models.RequestInfoWrapper;
import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.workflow.*;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.pucar.dristi.config.ServiceConstants.BUSINESS_SERVICE_NOT_FOUND;

@ExtendWith(MockitoExtension.class)
 class WorkflowUtilTest {

    @Mock
    private ServiceRequestRepository repository;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private Configuration configs;

    @InjectMocks
    private WorkflowUtil workflowUtil;

    private RequestInfo requestInfo;
    private String tenantId;
    private String businessServiceCode;
    private Workflow workflow;

    @BeforeEach
    public void setUp() {
        requestInfo = new RequestInfo();
        tenantId = "tenantId";
        businessServiceCode = "businessServiceCode";
        workflow = Workflow.builder().action("APPROVE").build();
    }

    @Test
     void testGetBusinessService() {
        // Arrange
        when(configs.getWfHost()).thenReturn("http://localhost:8080");
        when(configs.getWfBusinessServiceSearchPath()).thenReturn("/businessService/_search");
        BusinessServiceResponse response = new BusinessServiceResponse();
        BusinessService businessService = new BusinessService();
        response.setBusinessServices(Collections.singletonList(businessService));
        when(repository.fetchResult(any(), any(RequestInfoWrapper.class))).thenReturn(new HashMap<>());
        when(mapper.convertValue(any(), eq(BusinessServiceResponse.class))).thenReturn(response);

        // Act
        BusinessService result = workflowUtil.getBusinessService(requestInfo, tenantId, businessServiceCode);

        // Assert
        assertNotNull(result);
        assertEquals(businessService, result);
        verify(repository, times(1)).fetchResult(any(), any(RequestInfoWrapper.class));
    }

    @Test
     void testGetBusinessService_ThrowsExceptionWhenEmptyResponse() {
        // Arrange
        when(configs.getWfHost()).thenReturn("http://localhost:8080");
        when(configs.getWfBusinessServiceSearchPath()).thenReturn("/businessService/_search");
        when(repository.fetchResult(any(), any(RequestInfoWrapper.class))).thenReturn(new HashMap<>());
        when(mapper.convertValue(any(), eq(BusinessServiceResponse.class))).thenReturn(new BusinessServiceResponse());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            workflowUtil.getBusinessService(requestInfo, tenantId, businessServiceCode);
        });

        assertEquals(BUSINESS_SERVICE_NOT_FOUND, exception.getCode());
        verify(repository, times(1)).fetchResult(any(), any(RequestInfoWrapper.class));
    }

    @Test
     void testUpdateWorkflowStatus() {
        // Arrange
        State state = new State();
        state.setApplicationStatus("UpdatedStatus");
        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setState(state);
        BusinessServiceResponse businessServiceResponse = new BusinessServiceResponse();
        businessServiceResponse.setBusinessServices(List.of(new BusinessService()));

        ProcessInstanceResponse response = new ProcessInstanceResponse();
        response.setProcessInstances(Collections.singletonList(processInstance));

        when(configs.getWfHost()).thenReturn("http://localhost:8080");
        when(configs.getWfTransitionPath()).thenReturn("/workflow/_transition");

        when(repository.fetchResult(any(StringBuilder.class), any())).thenReturn(businessServiceResponse);
        when(mapper.convertValue(any(), eq(BusinessServiceResponse.class))).thenReturn(businessServiceResponse);

        when(mapper.convertValue(any(), eq(ProcessInstanceResponse.class))).thenReturn(response);

        // Act
        String result = workflowUtil.updateWorkflowStatus(requestInfo, tenantId, "businessId", businessServiceCode, workflow, "wfModuleName");

        // Assert
        assertEquals("UpdatedStatus", result);
    }

    @Test
     void testGetWorkflow() {
        // Arrange
        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setBusinessId("businessId");
        processInstance.setAction("action");
        List<ProcessInstance> processInstances = Collections.singletonList(processInstance);

        // Act
        Map<String, Workflow> workflowMap = workflowUtil.getWorkflow(processInstances);

        // Assert
        assertNotNull(workflowMap);
        assertTrue(workflowMap.containsKey("businessId"));
        assertEquals("action", workflowMap.get("businessId").getAction());
    }
}