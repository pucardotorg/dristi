package org.pucar.dristi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.common.contract.workflow.*;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.web.models.Application;
import org.pucar.dristi.web.models.ApplicationRequest;
import org.pucar.dristi.web.models.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.pucar.dristi.config.ServiceConstants.*;

@Component
@Slf4j
public class WorkflowService {
    private final ObjectMapper mapper;
    private final ServiceRequestRepository repository;
    private final Configuration config;

    @Autowired
    public WorkflowService(
            ObjectMapper mapper,
            ServiceRequestRepository repository,
            Configuration config) {
        this.mapper = mapper;
        this.repository = repository;
        this.config = config;
    }


    public void updateWorkflowStatus(ApplicationRequest applicationRequest) {
        Application application = applicationRequest.getApplication();
        try {
                ProcessInstance processInstance = getProcessInstance(application);
                ProcessInstanceRequest workflowRequest = new ProcessInstanceRequest(applicationRequest.getRequestInfo(), Collections.singletonList(processInstance));
                log.info("ProcessInstance Request :: {}", workflowRequest);
                String state=callWorkFlow(workflowRequest).getState();
                log.info("Application Status :: {}", state);
                application.setStatus(state);
            } catch (CustomException e){
                throw e;
            } catch (Exception e) {
                log.error("Error updating workflow status: {}", e.getMessage());
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
        } catch (CustomException e){
            throw e;
        } catch (Exception e) {
            log.error("Error calling workflow: {}", e.getMessage());
            throw new CustomException(WORKFLOW_SERVICE_EXCEPTION,e.getMessage());
        }
    }

    private ProcessInstance getProcessInstance(Application application) {
        try {
            Workflow workflow = application.getWorkflow();
            ProcessInstance processInstance = new ProcessInstance();
            processInstance.setBusinessId(application.getApplicationNumber());
            processInstance.setAction(workflow.getAction());
            processInstance.setModuleName("pucar");
            processInstance.setTenantId(application.getTenantId());
            processInstance.setBusinessService(getBusinessServiceFromAppplication(application));
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
        } catch (CustomException e){
            throw e;
        } catch (Exception e) {
            log.error("Error getting process instance for Application: {}", e.getMessage());
            throw new CustomException(WORKFLOW_SERVICE_EXCEPTION,e.getMessage());
        }
    }
    String getBusinessServiceFromAppplication(Application application) {
        if(application.getReferenceId() == null){
            return ASYNC_VOLUNTARY_SUBMISSION_SERVICES;
        }
        else if(application.isResponseRequired()){
            return ASYNSUBMISSIONWITHRESPONSE;
        }
        else {
            return ASYNCSUBMISSIONWITHOUTRESPONSE;
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
        } catch (CustomException e){
            throw e;
        } catch (Exception e) {
            log.error("Error getting current workflow: {}", e.getMessage());
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

    public Workflow getWorkflowFromProcessInstance(ProcessInstance processInstance) {
        if(processInstance == null) {
            return null;
        }
        return Workflow.builder().action(processInstance.getState().getState()).comments(processInstance.getComment()).build();
    }
}
