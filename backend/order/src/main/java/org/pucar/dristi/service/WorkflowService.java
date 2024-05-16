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
import org.pucar.dristi.web.models.CourtCase;
import org.pucar.dristi.web.models.Order;
import org.pucar.dristi.web.models.OrderRequest;
import org.pucar.dristi.web.models.RequestInfoWrapper;
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


    public void updateWorkflowStatus(OrderRequest orderRequest) {
            try {
                ProcessInstance processInstance = getProcessInstance(orderRequest.getOrder(), orderRequest.getRequestInfo());
                ProcessInstanceRequest workflowRequest = new ProcessInstanceRequest(orderRequest.getRequestInfo(), Collections.singletonList(processInstance));
                String applicationStatus=callWorkFlow(workflowRequest).getApplicationStatus();
                orderRequest.getOrder().setStatus(applicationStatus);
            } catch (Exception e) {
                log.error("Error updating workflow status: {}", e.getMessage());
                throw new CustomException();
            }
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

    private ProcessInstance getProcessInstance(Order order, RequestInfo requestInfo) {
        try {
            Workflow workflow = order.getWorkflow();
            ProcessInstance processInstance = new ProcessInstance();
            processInstance.setBusinessId(order.getFilingNumber());
            processInstance.setAction(workflow.getAction());
            processInstance.setModuleName("pucar"); // FIXME
            processInstance.setTenantId(order.getTenantId());
            processInstance.setBusinessService("order"); // FIXME
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
            log.error("Error getting process instance for CASE: {}", e.getMessage());
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
    private BusinessService getBusinessService(CourtCase courtCase, RequestInfo requestInfo) {
        try {
            String tenantId = courtCase.getTenantId();
            StringBuilder url = getSearchURLWithParams(tenantId, "CASE");
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
    public ProcessInstanceRequest getProcessInstanceRegistrationPayment(OrderRequest updateRequest) {
        try {
            Order application = updateRequest.getOrder();
            ProcessInstance process = ProcessInstance.builder()
                    .businessService("ORDER")
                    .businessId(application.getFilingNumber())
                    .comment("Payment for Order registration processed")
                    .moduleName("order-services")
                    .tenantId(application.getTenantId())
                    .action("PAY")
                    .build();
            return ProcessInstanceRequest.builder()
                    .requestInfo(updateRequest.getRequestInfo())
                    .processInstances(Arrays.asList(process))
                    .build();
        } catch (Exception e) {
            log.error("Error getting process instance for order registration payment: {}", e.getMessage());
            throw new CustomException();
        }
    }

    public Workflow getWorkflowFromProcessInstance(ProcessInstance processInstance) {
        if(processInstance == null) {
            return null;
        }
        return Workflow.builder().action(processInstance.getState().getState()).comments(processInstance.getComment()).build();
    }
}
 