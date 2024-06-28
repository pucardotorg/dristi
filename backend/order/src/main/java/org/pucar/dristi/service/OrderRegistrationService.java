package org.pucar.dristi.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.OrderRegistrationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.OrderRepository;
import org.pucar.dristi.util.WorkflowUtil;
import org.pucar.dristi.validators.OrderRegistrationValidator;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static org.pucar.dristi.config.ServiceConstants.*;

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
    private WorkflowUtil workflowUtil;

    @Autowired
    private Configuration config;

    @Autowired
    private Producer producer;

    public Order createOrder(OrderRequest body) {
        try {
            validator.validateOrderRegistration(body);

            enrichmentUtil.enrichOrderRegistration(body);

            workflowUpdate(body);

            producer.push(config.getSaveOrderKafkaTopic(), body);

            return body.getOrder();
        }catch (CustomException e) {
            log.error("Custom Exception occurred while creating order");
            throw e;
        }
        catch (Exception e) {
            log.error("Error occurred while creating order :: {}",e.toString());
            throw new CustomException(ORDER_CREATE_EXCEPTION, e.getMessage());
        }
    }

    public List<Order> searchOrder(String orderNumber,String applicationNumber, String cnrNumber, String filingNumber, String tenantId, String id, String status,RequestInfo requestInfo) {
        try {
            // Fetch applications from database according to the given search criteria
            List<Order> orderList = orderRepository.getOrders(orderNumber,applicationNumber, cnrNumber, filingNumber, tenantId, id, status);

            // If no applications are found matching the given criteria, return an empty list
            if (CollectionUtils.isEmpty(orderList))
                return new ArrayList<>();
            return orderList;

        } catch (Exception e) {
            log.error("Error while fetching to search results :: {}",e.toString());
            throw new CustomException(ORDER_SEARCH_EXCEPTION, e.getMessage());
        }
    }

    public Order updateOrder(OrderRequest body) {

        try {

            // Validate whether the application that is being requested for update indeed exists
            Order existingApplication;
            try {
                existingApplication = validator.validateApplicationExistence(body);
            } catch (Exception e) {
                log.error("Error validating existing application :: {}",e.toString());
                throw new CustomException(ORDER_UPDATE_EXCEPTION, "Error validating existing application: " + e.getMessage());
            }
            existingApplication.setWorkflow(body.getOrder().getWorkflow());

            // Enrich application upon update
            enrichmentUtil.enrichOrderRegistrationUponUpdate(body);

            workflowUpdate(body);

            producer.push(config.getUpdateOrderKafkaTopic(), body);

            return body.getOrder();

        } catch (CustomException e) {
            log.error("Custom Exception occurred while updating order :: {}",e.toString());
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while updating order");
            throw new CustomException(ORDER_UPDATE_EXCEPTION, "Error occurred while updating order: " + e.getMessage());
        }

    }

    public List<OrderExists> existsOrder(OrderExistsRequest orderExistsRequest) {
        try {
            return orderRepository.checkOrderExists(orderExistsRequest.getOrder());
        }
        catch (CustomException e){
            log.error("Custom Exception occurred while searching :: {}",e.toString());
            throw e;
        }
        catch (Exception e){
            log.error("Error while fetching to search order results :: {}",e.toString());
            throw new CustomException(ORDER_EXISTS_EXCEPTION,e.getMessage());
        }
    }

    private void workflowUpdate(OrderRequest orderRequest){
        Order order = orderRequest.getOrder();
        RequestInfo requestInfo = orderRequest.getRequestInfo();

        String orderType = order.getOrderType();
        String tenantId = order.getTenantId();
        String orderNumber = order.getOrderNumber();
        Workflow workflow = order.getWorkflow();

        String status ;
        if (orderType.equalsIgnoreCase(JUDGEMENT)) {
            status = workflowUtil.updateWorkflowStatus(requestInfo, tenantId, orderNumber,
                    config.getOrderJudgementBusinessServiceName(), workflow, config.getOrderBusinessName());
        } else {
            status = workflowUtil.updateWorkflowStatus(requestInfo, tenantId, orderNumber, config.getOrderBusinessServiceName(),
                    workflow, config.getOrderBusinessName());
        }
        order.setStatus(status);
    }
}