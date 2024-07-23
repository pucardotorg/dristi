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
import org.pucar.dristi.web.models.Hearing;
import org.pucar.dristi.web.models.HearingRequest;
import org.pucar.dristi.web.models.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.pucar.dristi.config.ServiceConstants.APPLICATION_ACTIVE_STATUS;
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


    /**
     * For updating workflow status of hearing by calling workflow
     *
     * @param hearingRequest
     */
    public void updateWorkflowStatus(HearingRequest hearingRequest) {
        try {
            Hearing hearing = hearingRequest.getHearing();
            ProcessInstance processInstance = getProcessInstanceForHearing(hearing, hearingRequest.getRequestInfo());
            ProcessInstanceRequest workflowRequest = new ProcessInstanceRequest(hearingRequest.getRequestInfo(), Collections.singletonList(processInstance));
            log.info("ProcessInstance Request :: {}", workflowRequest);
            State workflowState = callWorkFlow(workflowRequest);
            String state = workflowState.getState();
            log.info("Application state :: {}", state);
            hearing.setStatus(state);
            if (APPLICATION_ACTIVE_STATUS.equalsIgnoreCase(workflowState.getApplicationStatus())) {
                //setting true once application approved
                hearing.setIsActive(true);
            }
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating workflow status: {}", e.toString());
            throw new CustomException(WORKFLOW_SERVICE_EXCEPTION, "Error updating workflow status: " + e);
        }
    }

    public State callWorkFlow(ProcessInstanceRequest workflowReq) {
        try {
            StringBuilder url = new StringBuilder(config.getWfHost().concat(config.getWfTransitionPath()));
            Object optional = repository.fetchResult(url, workflowReq);
            log.info("Workflow Response :: {}", optional);
            ProcessInstanceResponse response = mapper.convertValue(optional, ProcessInstanceResponse.class);
            return response.getProcessInstances().get(0).getState();
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error calling workflow: {}", e.toString());
            throw new CustomException(WORKFLOW_SERVICE_EXCEPTION, e.toString());
        }
    }


    /**
     * for hearing application process instance
     *
     * @param hearing
     * @param requestInfo
     * @return payload for workflow service call
     */
    ProcessInstance getProcessInstanceForHearing(Hearing hearing, RequestInfo requestInfo) {
        try {
            Workflow workflow = hearing.getWorkflow();
            ProcessInstance processInstance = new ProcessInstance();
            processInstance.setBusinessId(hearing.getHearingId());
            processInstance.setAction(workflow.getAction());
            processInstance.setModuleName(config.getHearingBusinessName());
            processInstance.setTenantId(hearing.getTenantId());
            processInstance.setBusinessService(config.getHearingBusinessServiceName());
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
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting process instance for HEARING: {}", e.toString());
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
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting current workflow: {}", e.toString());
            throw new CustomException(WORKFLOW_SERVICE_EXCEPTION, e.toString());
        }
    }

    BusinessService getBusinessService(Hearing hearing, RequestInfo requestInfo) {
        try {
            String tenantId = hearing.getTenantId();
            StringBuilder url = getSearchURLWithParams(tenantId, "ADV");
            RequestInfoWrapper requestInfoWrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
            Object result = repository.fetchResult(url, requestInfoWrapper);
            BusinessServiceResponse response = mapper.convertValue(result, BusinessServiceResponse.class);
            if (CollectionUtils.isEmpty(response.getBusinessServices()))
                throw new CustomException();
            return response.getBusinessServices().get(0);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting business service: {}", e.toString());
            throw new CustomException(WORKFLOW_SERVICE_EXCEPTION, e.toString());
        }
    }

    StringBuilder getSearchURLWithParams(String tenantId, String businessService) {
        StringBuilder url = new StringBuilder(config.getWfHost());
        url.append(config.getWfBusinessServiceSearchPath());
        url.append("?tenantId=").append(tenantId);
        url.append("&businessServices=").append(businessService);
        return url;
    }

    StringBuilder getSearchURLForProcessInstanceWithParams(String tenantId, String businessService) {
        StringBuilder url = new StringBuilder(config.getWfHost());
        url.append(config.getWfProcessInstanceSearchPath());
        url.append("?tenantId=").append(tenantId);
        url.append("&businessIds=").append(businessService);
        return url;
    }

    public ProcessInstanceRequest getProcessInstanceForHearingRegistrationPayment(HearingRequest updateRequest) {
        try {
            Hearing application = updateRequest.getHearing();
            ProcessInstance process = ProcessInstance.builder()
                    .businessService("ADV")
                    .businessId(application.getId().toString())
                    .comment("Payment for hearing registration processed")
                    .moduleName("hearing-services")
                    .tenantId(application.getTenantId())
                    .action("PAY")
                    .build();
            return ProcessInstanceRequest.builder()
                    .requestInfo(updateRequest.getRequestInfo())
                    .processInstances(Arrays.asList(process))
                    .build();
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting process instance for hearing registration payment: {}", e.toString());
            throw new CustomException(WORKFLOW_SERVICE_EXCEPTION, e.toString());
        }
    }

    public Workflow getWorkflowFromProcessInstance(ProcessInstance processInstance) {
        if (processInstance == null) {
            return null;
        }
        return Workflow.builder().action(processInstance.getState().getState()).comments(processInstance.getComment()).documents(processInstance.getDocuments()).build();
    }
}
 