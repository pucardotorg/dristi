package org.pucar.dristi.service;

import static org.pucar.dristi.config.ServiceConstants.WORKFLOW_SERVICE_EXCEPTION;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.common.contract.workflow.ProcessInstance;
import org.egov.common.contract.workflow.ProcessInstanceRequest;
import org.egov.common.contract.workflow.ProcessInstanceResponse;
import org.egov.common.contract.workflow.State;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.web.models.CaseCriteria;
import org.pucar.dristi.web.models.CaseRequest;
import org.pucar.dristi.web.models.CaseSearchRequest;
import org.pucar.dristi.web.models.CourtCase;
import org.pucar.dristi.web.models.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WorkflowService {

    private ObjectMapper mapper;

    private ServiceRequestRepository repository;

    private Configuration config;

    @Autowired
    public WorkflowService(ObjectMapper mapper, ServiceRequestRepository repository, Configuration config) {
        this.mapper = mapper;
        this.repository = repository;
        this.config = config;
    }

    public void updateWorkflowStatus(CaseRequest caseRequest) {
            try {
                ProcessInstance processInstance = getProcessInstance(caseRequest.getCases());
                ProcessInstanceRequest workflowRequest = new ProcessInstanceRequest(caseRequest.getRequestInfo(), Collections.singletonList(processInstance));
                log.info("ProcessInstance Request :: {}", workflowRequest);
                String state=callWorkFlow(workflowRequest).getState();
                log.info("Workflow State for filing number :: {} and state :: {}",caseRequest.getCases().getFilingNumber(), state);
                caseRequest.getCases().setStatus(state);
            } catch(CustomException e){
                throw e;
            } catch (Exception e) {
                log.error("Error updating workflow status :: {}", e.toString());
                throw new CustomException(WORKFLOW_SERVICE_EXCEPTION,"Error updating workflow status: "+e.getMessage());
            }
    }
    public State callWorkFlow(ProcessInstanceRequest workflowReq) {
        try {
            StringBuilder url = new StringBuilder(config.getWfHost().concat(config.getWfTransitionPath()));
            Object optional = repository.fetchResult(url, workflowReq);
            log.info("Workflow Response :: {}", optional);
            ProcessInstanceResponse response = mapper.convertValue(optional, ProcessInstanceResponse.class);
            return response.getProcessInstances().get(0).getState();
        } catch(CustomException e){
            throw e;
        } catch (Exception e) {
            log.error("Error calling workflow :: {}", e.toString());
            throw new CustomException(WORKFLOW_SERVICE_EXCEPTION,e.getMessage());
        }
    }

    public ProcessInstance getProcessInstance(CourtCase courtCase) {
        try {
            Workflow workflow = courtCase.getWorkflow();
            ProcessInstance processInstance = new ProcessInstance();
            processInstance.setBusinessId(courtCase.getFilingNumber());
            processInstance.setAction(workflow.getAction());
            processInstance.setModuleName(config.getCaseBusinessName());
            processInstance.setTenantId(courtCase.getTenantId());
            processInstance.setBusinessService(config.getCaseBusinessServiceName());
            processInstance.setDocuments(workflow.getDocuments());
            processInstance.setComment(workflow.getComments());
            if (!CollectionUtils.isEmpty(workflow.getAssignes())) {
                List<User> users = new ArrayList<>();
                workflow.getAssignes().forEach(uuid -> {
                    User user = new User();
                    user.setUuid(uuid);
                    users.add(user);
                });
                processInstance.setAssignes(users);
            }
            return processInstance;
        } catch (Exception e) {
            log.error("Error getting process instance for CASE :: {}", e.toString());
            throw new CustomException(WORKFLOW_SERVICE_EXCEPTION,e.getMessage());
        }
    }
    public ProcessInstance getCurrentWorkflow(RequestInfo requestInfo, String tenantId, String businessId) {
        try {
            RequestInfoWrapper requestInfoWrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
            StringBuilder url = getSearchURLForProcessInstanceWithParams(tenantId, businessId);
            Object res = repository.fetchResult(url, requestInfoWrapper);
            ProcessInstanceResponse response = mapper.convertValue(res, ProcessInstanceResponse.class);
            if (response != null && !CollectionUtils.isEmpty(response.getProcessInstances()) && response.getProcessInstances().get(0) != null)
                return response.getProcessInstances().get(0);
            return null;
        } catch (Exception e) {
            log.error("Error getting current workflow :: {}", e.toString());
            throw new CustomException(WORKFLOW_SERVICE_EXCEPTION, e.getMessage());
        }
    }

    private StringBuilder getSearchURLForProcessInstanceWithParams(String tenantId, String businessService) {
        StringBuilder url = new StringBuilder(config.getWfHost());
        url.append(config.getWfProcessInstanceSearchPath());
        url.append("?tenantId=").append(tenantId);
        url.append("&businessIds=").append(businessService);
        return url;
    }
    public ProcessInstanceRequest getProcessInstanceRegistrationPayment(CaseRequest updateRequest) {
        try {
            CourtCase application = updateRequest.getCases();
            ProcessInstance process = ProcessInstance.builder()
                    .businessService("ADV")
                    .businessId(application.getFilingNumber())
                    .comment("Payment for Case registration processed")
                    .moduleName("cases-services")
                    .tenantId(application.getTenantId())
                    .action("PAY")
                    .build();
            return ProcessInstanceRequest.builder()
                    .requestInfo(updateRequest.getRequestInfo())
                    .processInstances(Arrays.asList(process))
                    .build();
        } catch(CustomException e){
            throw e;
        } catch (Exception e) {
            log.error("Error getting process instance for case registration payment :: {}", e.toString());
            throw new CustomException(WORKFLOW_SERVICE_EXCEPTION, e.getMessage());
        }
    }

    public ProcessInstanceRequest getProcessInstanceForCasePayment(CaseSearchRequest updateRequest, String tenantId) {

        CaseCriteria caseCriteria = updateRequest.getCriteria().get(0);

        ProcessInstance process = ProcessInstance.builder()
                .businessService(config.getCaseBusinessServiceName())
                .businessId(caseCriteria.getFilingNumber())
                .comment("Payment for Case processed")
                .moduleName(config.getCaseBusinessName())
                .tenantId(tenantId)
                .action("MAKE_PAYMENT")
                .build();

        return ProcessInstanceRequest.builder()
                .requestInfo(updateRequest.getRequestInfo())
                .processInstances(Arrays.asList(process))
                .build();

    }

    public Workflow getWorkflowFromProcessInstance(ProcessInstance processInstance) {
        if(processInstance == null) {
            return null;
        }
        return Workflow.builder().action(processInstance.getState().getState()).comments(processInstance.getComment()).build();
    }
}
 