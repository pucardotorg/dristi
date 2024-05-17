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


    /** For updating workflow status of advocate by calling workflow
     * @param advocateRequest
     */
    public void updateWorkflowStatus(AdvocateRequest advocateRequest) {
        advocateRequest.getAdvocates().forEach(advocate -> {
            try {
                ProcessInstance processInstance = getProcessInstanceForADV(advocate);
                ProcessInstanceRequest workflowRequest = new ProcessInstanceRequest(advocateRequest.getRequestInfo(), Collections.singletonList(processInstance));
                log.info("ProcessInstance Request :: {}", workflowRequest);
                String applicationStatus=callWorkFlow(workflowRequest).getApplicationStatus();
                log.info("Application Status :: {}", applicationStatus);
                advocate.setStatus(applicationStatus);
            } catch (CustomException e){
                throw e;
            } catch (Exception e) {
                log.error("Error updating workflow status: {}", e.toString());
                throw new CustomException(WORKFLOW_SERVICE_EXCEPTION,"Error updating workflow status: "+ e);
            }
        });
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
            log.error("Error calling workflow: {}", e.toString());
            throw new CustomException(WORKFLOW_SERVICE_EXCEPTION,e.toString());
        }
    }

    /** For updating workflow status of advocate clerk by calling workflow
     * @param advocateClerkRequest
     */
    public void updateWorkflowStatus(AdvocateClerkRequest advocateClerkRequest) {
        advocateClerkRequest.getClerks().forEach(advocateClerk -> {
            try {
                ProcessInstance processInstance = getProcessInstanceForADVClerk(advocateClerk);
                ProcessInstanceRequest workflowRequest = new ProcessInstanceRequest(advocateClerkRequest.getRequestInfo(), Collections.singletonList(processInstance));
                log.info("ProcessInstance Request :: {}", workflowRequest);
                String applicationStatus=callWorkFlow(workflowRequest).getApplicationStatus();
                log.info("Application Status :: {}", applicationStatus);
                advocateClerk.setStatus(applicationStatus);
            } catch (CustomException e){
                throw e;
            } catch (Exception e) {
                log.error("Error updating workflow status: {}", e.toString());
                throw new CustomException(WORKFLOW_SERVICE_EXCEPTION,e.toString());
            }
        });
    }

    /** for advocate clerk application process instance
     * @param advocateClerk
     * @return payload for workflow service call
     */
    private ProcessInstance getProcessInstanceForADVClerk(AdvocateClerk advocateClerk) {
        try {
            Workflow workflow = advocateClerk.getWorkflow();
            ProcessInstance processInstance = new ProcessInstance();
            processInstance.setBusinessId(advocateClerk.getApplicationNumber());
            processInstance.setAction(workflow.getAction());
            processInstance.setModuleName(config.getAdvocateClerkBusinessName());
            processInstance.setTenantId(advocateClerk.getTenantId());
            processInstance.setBusinessService(config.getAdvocateClerkBusinessServiceName());
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
            log.error("Error getting process instance for ADVOCATE: {}", e.toString());
            throw new CustomException(WORKFLOW_SERVICE_EXCEPTION,e.toString());
        }
    }

    /** for advocate application process instance
     * @param advocate
     * @return payload for workflow service call
     */
    private ProcessInstance getProcessInstanceForADV(Advocate advocate) {
        try {
            Workflow workflow = advocate.getWorkflow();
            ProcessInstance processInstance = new ProcessInstance();
            processInstance.setBusinessId(advocate.getApplicationNumber());
            processInstance.setAction(workflow.getAction());
            processInstance.setModuleName(config.getAdvocateBusinessName());
            processInstance.setTenantId(advocate.getTenantId());
            processInstance.setBusinessService(config.getAdvocateBusinessServiceName());
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
            log.error("Error getting process instance for ADVOCATE: {}", e.toString());
            throw new CustomException(WORKFLOW_SERVICE_EXCEPTION, e.toString());
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
            log.error("Error getting current workflow: {}", e.toString());
            throw new CustomException(WORKFLOW_SERVICE_EXCEPTION, e.toString());
        }
    }
    private StringBuilder getSearchURLForProcessInstanceWithParams(String tenantId, String businessService) {
        StringBuilder url = new StringBuilder(config.getWfHost());
        url.append(config.getWfProcessInstanceSearchPath());
        url.append("?tenantId=").append(tenantId);
        url.append("&businessIds=").append(businessService);
        return url;
    }
    public ProcessInstanceRequest getProcessInstanceForAdvocateRegistrationPayment(AdvocateRequest updateRequest) {
        try {
            Advocate application = updateRequest.getAdvocates().get(0);
            ProcessInstance process = ProcessInstance.builder()
                    .businessService("ADV")
                    .businessId(application.getApplicationNumber())
                    .comment("Payment for advocate registration processed")
                    .moduleName("advocate-services")
                    .tenantId(application.getTenantId())
                    .action("PAY")
                    .build();
            return ProcessInstanceRequest.builder()
                    .requestInfo(updateRequest.getRequestInfo())
                    .processInstances(Arrays.asList(process))
                    .build();
        } catch (CustomException e){
            throw e;
        } catch (Exception e) {
            log.error("Error getting process instance for advocate registration payment: {}", e.toString());
            throw new CustomException(WORKFLOW_SERVICE_EXCEPTION, e.toString());
        }
    }

    public Workflow getWorkflowFromProcessInstance(ProcessInstance processInstance) {
        if(processInstance == null) {
            return null;
        }
        return Workflow.builder().action(processInstance.getState().getState()).comments(processInstance.getComment()).build();
    }
}
 