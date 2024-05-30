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
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.pucar.dristi.config.ServiceConstants.WORKFLOW_SERVICE_EXCEPTION;

@Component
@Slf4j
public class WorkflowService {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ServiceRequestRepository repository;

    @Autowired
    private Configuration config;


    public void updateWorkflowStatus(EvidenceRequest evidenceRequest) {
            try {
                ProcessInstance processInstance = getProcessInstanceForArtifact(evidenceRequest.getArtifact(), evidenceRequest.getRequestInfo());
                ProcessInstanceRequest workflowRequest = new ProcessInstanceRequest(evidenceRequest.getRequestInfo(), Collections.singletonList(processInstance));
                String applicationStatus=callWorkFlow(workflowRequest).getApplicationStatus();
                evidenceRequest.getArtifact().setStatus(applicationStatus);
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
            ProcessInstanceResponse response = mapper.convertValue(optional, ProcessInstanceResponse.class);
            return response.getProcessInstances().get(0).getState();
        } catch (CustomException e){
            throw e;
        } catch (Exception e) {
            log.error("Error calling workflow: {}", e.getMessage());
            throw new CustomException(WORKFLOW_SERVICE_EXCEPTION,e.getMessage());
        }
    }
    ProcessInstance getProcessInstanceForArtifact(Artifact artifact, RequestInfo requestInfo) {
        try {
            Workflow workflow = artifact.getWorkflow();
            ProcessInstance processInstance = new ProcessInstance();
            processInstance.setBusinessId(artifact.getArtifactNumber());
            processInstance.setAction(workflow.getAction());
            processInstance.setModuleName(config.getBusinessServiceModule());
            processInstance.setTenantId(artifact.getTenantId());
            processInstance.setBusinessService(config.getBusinessServiceName());
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
            log.error("Error getting process instance for Evidence: {}", e.getMessage());
            throw new CustomException(WORKFLOW_SERVICE_EXCEPTION, e.getMessage());
        }
    }
    public Workflow getWorkflowFromProcessInstance(ProcessInstance processInstance) {
        if(processInstance == null) {
            return null;
        }
        return Workflow.builder().action(processInstance.getState().getState()).comments(processInstance.getComment()).build();
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
        StringBuilder getSearchURLForProcessInstanceWithParams(String tenantId, String businessService) {
            StringBuilder url = new StringBuilder(config.getWfHost());
            url.append(config.getWfProcessInstanceSearchPath());
            url.append("?tenantId=").append(tenantId);
            url.append("&businessIds=").append(businessService);
            return url;
        }
}
