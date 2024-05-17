package org.pucar.dristi.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.OrderRegistrationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.OrderRepository;
import org.pucar.dristi.validators.OrderRegistrationValidator;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrderRegistrationService {

    @Autowired
    private OrderRegistrationValidator validator;

    @Autowired
    private OrderRegistrationEnrichment enrichmentUtil;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private Configuration config;
    @Autowired
    private Producer producer;

    public Order createOrder(OrderRequest body) {
        try {
            validator.validateOrderRegistration(body);
            enrichmentUtil.enrichOrderRegistration(body);
          //  workflowService.updateWorkflowStatus(body);
            producer.push(config.getSaveOrderKafkaTopic(), body);
            return body.getOrder();
        }catch (CustomException e) {
            log.error("Custom Exception occurred while creating order");
            throw new CustomException("ORDER_CREATE_EXCEPTION", e.getMessage());
        }
        catch (Exception e) {
            log.error("Error occurred while creating order");
            throw new CustomException("ORDER_CREATE_EXCEPTION", e.getMessage());
        }
    }

    public List<Order> searchOrder(String applicationNumber, String cnrNumber, String filingNumber, String tenantId, String id, String status, RequestInfo requestInfo) {

        try {
            // Fetch applications from database according to the given search criteria
            List<Order> orderList = orderRepository.getApplications(applicationNumber,cnrNumber,filingNumber, tenantId, id, status);

            // If no applications are found matching the given criteria, return an empty list
            if (CollectionUtils.isEmpty(orderList))
                return new ArrayList<>();
           orderList.forEach(order -> order.setWorkflow(workflowService.getWorkflowFromProcessInstance(workflowService.getCurrentWorkflow(requestInfo, tenantId, "order"))));
            return orderList;
        } catch (Exception e) {
            log.error("Error while fetching to search results");
            throw new CustomException("CASE_SEARCH_EXCEPTION", e.getMessage());
        }
    }

    public Order updateOrder(OrderRequest orderRequest) {

        try {

            // Validate whether the application that is being requested for update indeed exists
            Order existingApplication;
            try {
                existingApplication = validator.validateApplicationExistence(orderRequest);
            } catch (Exception e) {
                log.error("Error validating existing application");
                throw new CustomException("ORDER_UPDATE_EXCEPTION", "Error validating existing application: " + e.getMessage());
            }
            existingApplication.setWorkflow(orderRequest.getOrder().getWorkflow());

            // Enrich application upon update
            enrichmentUtil.enrichOrderRegistrationUponUpdate(orderRequest);

            workflowService.updateWorkflowStatus(orderRequest);

            producer.push(config.getUpdateOrderKafkaTopic(), orderRequest);

            return orderRequest.getOrder();

        } catch (CustomException e) {
            log.error("Custom Exception occurred while updating order");
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while updating order");
            throw new CustomException("ORDER_UPDATE_EXCEPTION", "Error occurred while updating order: " + e.getMessage());
        }

    }

    public OrderExists existsOrder(OrderExistsRequest orderExistsRequest) {
        try {
            OrderExists orderExists = orderExistsRequest.getOrder();

            // Fetch applications from database according to the given search criteria
            List<Order> orderList = orderRepository.getApplications(orderExists.getApplicationNumber(), orderExists.getFilingNumber(),
                    orderExists.getCnrNumber(), orderExistsRequest.getRequestInfo().getUserInfo().getTenantId(), null, null);

            boolean notExists = orderList.stream().filter(c->c.getFilingNumber().equalsIgnoreCase(orderExists.getFilingNumber())
                      && orderExists.getCnrNumber().equalsIgnoreCase(orderExists.getCnrNumber())).toList().isEmpty();

            return new OrderExists(orderExists.getApplicationNumber(),orderExists.getOrderNumber(),orderExists.getCnrNumber(), orderExists.getFilingNumber(), !notExists);
        }
        catch (CustomException e){
            log.error("Custom Exception occurred while searching");
            throw e;
        }
        catch (Exception e){
            log.error("Error while fetching to search order results");
            throw new CustomException("ORDER_EXIST_EXCEPTION",e.getMessage());
        }
    }
}