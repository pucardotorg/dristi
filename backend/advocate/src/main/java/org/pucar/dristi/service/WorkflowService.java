package org.pucar.dristi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.common.contract.workflow.ProcessInstance;
import org.egov.common.contract.workflow.ProcessInstanceRequest;
import org.egov.common.contract.workflow.ProcessInstanceResponse;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.pucar.dristi.config.ServiceConstants.WORKFLOW_SERVICE_EXCEPTION;

@Component
@Slf4j
public class WorkflowService {

    private final ObjectMapper mapper;
    private final ServiceRequestRepository repository;
    private final Configuration config;

    @Autowired
    public WorkflowService(ObjectMapper mapper, ServiceRequestRepository repository, Configuration config) {
        this.mapper = mapper;
        this.repository = repository;
        this.config = config;
    }


    /** For updating workflow status of advocate by calling workflow
     * @param advocateRequest
     */
    public void updateWorkflowStatus(AdvocateRequest advocateRequest) {
        try {
            Advocate advocate =  advocateRequest.getAdvocate();
            ProcessInstance processInstance = getProcessInstanceForADV(advocate);
            ProcessInstanceRequest workflowRequest = new ProcessInstanceRequest(advocateRequest.getRequestInfo(), Collections.singletonList(processInstance));
            log.info("ProcessInstance Request :: {}", workflowRequest);
            ProcessInstance processInstanceResponse=callWorkFlow(workflowRequest);
            advocate.setCurrentProcessInstance(processInstanceResponse);
            String applicationStatus = processInstanceResponse.getState().getApplicationStatus();
            log.info("Application Status :: {}", applicationStatus);
            advocate.setStatus(applicationStatus);
        } catch(CustomException e){
            throw e;
        }
        catch (Exception e) {
            log.error("Error updating workflow status :: {}", e.toString());
            throw new CustomException(WORKFLOW_SERVICE_EXCEPTION,"Error updating workflow status: "+ e);
        }
    }
    public ProcessInstance callWorkFlow(ProcessInstanceRequest workflowReq) {
        try {
            StringBuilder url = new StringBuilder(config.getWfHost().concat(config.getWfTransitionPath()));
            Object optional = repository.fetchResult(url, workflowReq);
            log.info("Workflow Response :: {}", optional);
            ProcessInstanceResponse response = mapper.convertValue(optional, ProcessInstanceResponse.class);
            return response.getProcessInstances().get(0);
        } catch(CustomException e){
            throw e;
        } catch (Exception e) {
            log.error("Error calling workflow :: {}", e.toString());
            throw new CustomException(WORKFLOW_SERVICE_EXCEPTION,e.toString());
        }
    }

    /** For updating workflow status of advocate clerk by calling workflow
     * @param advocateClerkRequest
     */
    public void updateWorkflowStatus(AdvocateClerkRequest advocateClerkRequest) {
        AdvocateClerk advocateClerk = advocateClerkRequest.getClerk();
        try {
            ProcessInstance processInstance = getProcessInstanceForADVClerk(advocateClerk);
            ProcessInstanceRequest workflowRequest = new ProcessInstanceRequest(advocateClerkRequest.getRequestInfo(), Collections.singletonList(processInstance));
            log.info("ProcessInstance Request :: {}", workflowRequest);
            ProcessInstance processInstanceResponse=callWorkFlow(workflowRequest);
            advocateClerk.setCurrentProcessInstance(processInstanceResponse);
            String applicationStatus = processInstanceResponse.getState().getApplicationStatus();
            log.info("Application Status :: {}", applicationStatus);
            advocateClerk.setStatus(applicationStatus);
        } catch(CustomException e){
            throw e;
        } catch (Exception e) {
            log.error("Error updating workflow status :: {}", e.toString());
            throw new CustomException(WORKFLOW_SERVICE_EXCEPTION,e.toString());
        }
    }

    /** for advocate clerk application process instance
     * @param advocateClerk
     * @return payload for workflow service call
     */
    public ProcessInstance getProcessInstanceForADVClerk(AdvocateClerk advocateClerk) {
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
        } catch(CustomException e){
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
        } catch(CustomException e){
            throw e;
        } catch (Exception e) {
            log.error("Error getting process instance for ADVOCATE :: {}", e.toString());
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
        } catch(CustomException e){
            throw e;
        } catch (Exception e) {
            log.error("Error getting current workflow for tenant ID {} and business ID {}: {}", tenantId, businessId, e.toString());
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
 