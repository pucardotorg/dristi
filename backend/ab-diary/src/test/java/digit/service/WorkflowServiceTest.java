package digit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.config.Configuration;
import digit.repository.ServiceRequestRepository;
import digit.web.models.CaseDiary;
import digit.web.models.CaseDiaryRequest;
import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.workflow.ProcessInstance;
import org.egov.common.contract.workflow.ProcessInstanceRequest;
import org.egov.common.contract.workflow.ProcessInstanceResponse;
import org.egov.common.contract.workflow.State;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static digit.config.ServiceConstants.GENERATE_ACTION;
import static digit.config.ServiceConstants.WORKFLOW_SERVICE_EXCEPTION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkflowServiceTest {

    @Mock
    private ObjectMapper mapper;

    @Mock
    private ServiceRequestRepository repository;

    @Mock
    private Configuration config;

    @InjectMocks
    private WorkflowService workflowService;

    private CaseDiaryRequest caseDiaryRequest;
    private CaseDiary caseDiary;

    @BeforeEach
    void setUp() {
        caseDiary = new CaseDiary();
        caseDiary.setDiaryType("ADiary");
        caseDiary.setTenantId("tenant1");
        caseDiary.setJudgeId("judge1");
        caseDiary.setDiaryDate(System.currentTimeMillis());

        Workflow workflow = Workflow.builder()
                .action("GENERATE")
                .build();
        caseDiary.setWorkflow(workflow);

        caseDiaryRequest = new CaseDiaryRequest();
        caseDiaryRequest.setDiary(caseDiary);
        caseDiaryRequest.setRequestInfo(new RequestInfo());
    }

    @Test
    void updateWorkflowStatusSuccess() {
        // Mock configuration and workflow calls
        when(config.getWfHost()).thenReturn("http://workflow-host");
        when(config.getWfTransitionPath()).thenReturn("/transition");
        when(config.getCaseDiaryBusinessName()).thenReturn("CASE_DIARY");
        when(config.getCaseDiaryBusinessServiceName()).thenReturn("CASE_DIARY_SERVICE");

        // Mock repository and mapper
        ProcessInstanceResponse mockResponse = new ProcessInstanceResponse();
        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setState(State.builder().state(GENERATE_ACTION).build());
        mockResponse.setProcessInstances(Collections.singletonList(processInstance));
        when(repository.fetchResult(any(), any())).thenReturn(mockResponse);
        when(mapper.convertValue(any(), eq(ProcessInstanceResponse.class))).thenReturn(mockResponse);

        // Call method
        workflowService.updateWorkflowStatus(caseDiaryRequest);

        // Verify
        assertEquals("GENERATE", caseDiary.getStatus());
        verify(repository).fetchResult(any(), any());
    }

    @Test
    void updateWorkflowStatusBDiaryWithCaseNumber() {
        caseDiary.setDiaryType("BDiary");
        caseDiary.setCaseNumber("124");

        // Mock configuration and workflow calls
        when(config.getWfHost()).thenReturn("http://workflow-host");
        when(config.getWfTransitionPath()).thenReturn("/transition");
        when(config.getCaseDiaryBusinessName()).thenReturn("CASE_DIARY");
        when(config.getCaseDiaryBusinessServiceName()).thenReturn("CASE_DIARY_SERVICE");

        // Mock repository and mapper
        ProcessInstanceResponse mockResponse = new ProcessInstanceResponse();
        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setState(State.builder().state(GENERATE_ACTION).build());
        mockResponse.setProcessInstances(Collections.singletonList(processInstance));
        when(repository.fetchResult(any(), any())).thenReturn(mockResponse);
        when(mapper.convertValue(any(), eq(ProcessInstanceResponse.class))).thenReturn(mockResponse);

        // Call method
        workflowService.updateWorkflowStatus(caseDiaryRequest);

        // Verify
        assertEquals("GENERATE", caseDiary.getStatus());
    }

    @Test
    void getProcessInstanceWithAssignees() {
        Workflow workflow = Workflow.builder()
                .action("GENERATE")
                .assignes(Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString()))
                .build();
        caseDiary.setWorkflow(workflow);

        when(config.getCaseDiaryBusinessName()).thenReturn("CASE_DIARY");
        when(config.getCaseDiaryBusinessServiceName()).thenReturn("CASE_DIARY_SERVICE");

        ProcessInstance processInstance = workflowService.getProcessInstance(caseDiary);

        assertNotNull(processInstance);
        assertEquals(2, processInstance.getAssignes().size());
        assertNotNull(processInstance.getBusinessId());
    }

    @Test
    void callWorkflowSuccess() {
        ProcessInstanceRequest workflowReq = mock(ProcessInstanceRequest.class);

        // Mock repository and mapper
        ProcessInstanceResponse mockResponse = new ProcessInstanceResponse();
        ProcessInstance processInstance = new ProcessInstance();
        State state = new State();
        state.setUuid("state-uuid");
        processInstance.setState(state);
        mockResponse.setProcessInstances(Collections.singletonList(processInstance));

        when(config.getWfHost()).thenReturn("http://workflow-host");
        when(config.getWfTransitionPath()).thenReturn("/transition");
        when(repository.fetchResult(any(), eq(workflowReq))).thenReturn(mockResponse);
        when(mapper.convertValue(any(), eq(ProcessInstanceResponse.class))).thenReturn(mockResponse);

        State returnedState = workflowService.callWorkFlow(workflowReq);

        assertNotNull(returnedState);
        verify(repository).fetchResult(any(), eq(workflowReq));
    }

    @Test
    void updateWorkflowStatusRepositoryException() {
        when(config.getWfHost()).thenReturn("http://workflow-host");
        when(config.getWfTransitionPath()).thenReturn("/transition");
        when(repository.fetchResult(any(), any())).thenThrow(new RuntimeException("Repository Error"));

        CustomException exception = assertThrows(CustomException.class, () -> {
            workflowService.updateWorkflowStatus(caseDiaryRequest);
        });

        assertEquals(WORKFLOW_SERVICE_EXCEPTION, exception.getCode());
    }

    @Test
    void getProcessInstanceException() {
        caseDiary.setWorkflow(null);

        CustomException exception = assertThrows(CustomException.class, () -> {
            workflowService.getProcessInstance(caseDiary);
        });

        assertEquals(WORKFLOW_SERVICE_EXCEPTION, exception.getCode());
    }
}