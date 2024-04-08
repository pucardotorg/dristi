package org.pucar.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.common.contract.workflow.*;
import org.egov.tracer.model.CustomException;
import org.pucar.config.Configuration;
import org.pucar.repository.ServiceRequestRepository;
import org.pucar.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class WorkflowService {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private ServiceRequestRepository repository;
    @Autowired
    private Configuration config;
    public void updateWorkflowStatus(AdvocateClerkRequest advocateClerkRequest) {
        advocateClerkRequest.getClerks().forEach(advocateClerk -> {
            try {
                ProcessInstance processInstance = getProcessInstanceForADVClerk(advocateClerk, advocateClerkRequest.getRequestInfo());
                ProcessInstanceRequest workflowRequest = new ProcessInstanceRequest(advocateClerkRequest.getRequestInfo(), Collections.singletonList(processInstance));
                callWorkFlow(workflowRequest);
            } catch (Exception e) {
                log.error("Error updating workflow status: {}", e.getMessage());
                throw new CustomException();
            }
        });
    }
    public State callWorkFlow(ProcessInstanceRequest workflowReq) {
        try {
            StringBuilder url = new StringBuilder(config.getWfHost().concat(config.getWfTransitionPath()));
            Object optional = repository.fetchResult(url, workflowReq);
            ProcessInstanceResponse response = mapper.convertValue(optional, ProcessInstanceResponse.class);
            return response.getProcessInstances().get(0).getState();
        } catch (Exception e) {
            log.error("Error calling workflow: {}", e.getMessage());
            throw new CustomException();
        }
    }
    private ProcessInstance getProcessInstanceForADVClerk(AdvocateClerk advocateClerk, RequestInfo requestInfo) {
        try {
            Workflow workflow = advocateClerk.getWorkflow();
            ProcessInstance processInstance = new ProcessInstance();
            processInstance.setBusinessId(advocateClerk.getApplicationNumber());
            processInstance.setAction(workflow.getAction());
            processInstance.setModuleName("advocate-clerk-services");
            processInstance.setTenantId(advocateClerk.getTenantId());
            processInstance.setBusinessService("ADVClerk");
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
            log.error("Error getting process instance for BTR: {}", e.getMessage());
            throw new CustomException();
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
            log.error("Error getting current workflow: {}", e.getMessage());
            throw new CustomException();
        }
    }

    private BusinessService getBusinessService(AdvocateClerk advocateClerk, RequestInfo requestInfo) {
        try {
            String tenantId = advocateClerk.getTenantId();
            StringBuilder url = getSearchURLWithParams(tenantId, "ADVClerk");
            RequestInfoWrapper requestInfoWrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
            Object result = repository.fetchResult(url, requestInfoWrapper);
            BusinessServiceResponse response = mapper.convertValue(result, BusinessServiceResponse.class);
            if (CollectionUtils.isEmpty(response.getBusinessServices()))
                throw new CustomException();
            return response.getBusinessServices().get(0);
        } catch (Exception e) {
            log.error("Error getting business service: {}", e.getMessage());
            throw new CustomException();
        }
    }
    private StringBuilder getSearchURLWithParams(String tenantId, String businessService) {
        StringBuilder url = new StringBuilder(config.getWfHost());
        url.append(config.getWfBusinessServiceSearchPath());
        url.append("?tenantId=").append(tenantId);
        url.append("&businessServices=").append(businessService);
        return url;
    }
    private StringBuilder getSearchURLForProcessInstanceWithParams(String tenantId, String businessService) {
        StringBuilder url = new StringBuilder(config.getWfHost());
        url.append(config.getWfProcessInstanceSearchPath());
        url.append("?tenantId=").append(tenantId);
        url.append("&businessIds=").append(businessService);
        return url;
    }
    public ProcessInstanceRequest getProcessInstanceForAdvocateClerkRegistrationPayment(AdvocateClerkRequest updateRequest) {
        try {
            AdvocateClerk application = updateRequest.getClerks().get(0);
            ProcessInstance process = ProcessInstance.builder()
                    .businessService("ADVClerk")
                    .businessId(application.getApplicationNumber())
                    .comment("Payment for advocate clerk registration processed")
                    .moduleName("advocate-clerk-services")
                    .tenantId(application.getTenantId())
                    .action("PAY")
                    .build();
            return ProcessInstanceRequest.builder()
                    .requestInfo(updateRequest.getRequestInfo())
                    .processInstances(Arrays.asList(process))
                    .build();
        } catch (Exception e) {
            log.error("Error getting process instance for advocate clerk registration payment: {}", e.getMessage());
            throw new CustomException();
        }
    }
}
 