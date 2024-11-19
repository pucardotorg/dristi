package org.pucar.dristi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.models.individual.Individual;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.OrderRegistrationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.OrderRepository;
import org.pucar.dristi.util.AdvocateUtil;
import org.pucar.dristi.util.CaseUtil;
import org.pucar.dristi.util.WorkflowUtil;
import org.pucar.dristi.validators.OrderRegistrationValidator;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.pucar.dristi.config.ServiceConstants.*;

@Service
@Slf4j
public class OrderRegistrationService {

    private OrderRegistrationValidator validator;

    private OrderRegistrationEnrichment enrichmentUtil;

    private OrderRepository orderRepository;

    private WorkflowUtil workflowUtil;

    private Configuration config;

    private Producer producer;
    private ObjectMapper objectMapper;

    private CaseUtil caseUtil;

    private SmsNotificationService notificationService;

    private IndividualService individualService;

    private AdvocateUtil advocateUtil;

    @Autowired
    public OrderRegistrationService(OrderRegistrationValidator validator, Producer producer, Configuration config, WorkflowUtil workflowUtil, OrderRepository orderRepository, OrderRegistrationEnrichment enrichmentUtil, ObjectMapper objectMapper, CaseUtil caseUtil, SmsNotificationService notificationService, IndividualService individualService, AdvocateUtil advocateUtil) {
        this.validator = validator;
        this.producer = producer;
        this.config = config;
        this.workflowUtil = workflowUtil;
        this.orderRepository = orderRepository;
        this.enrichmentUtil = enrichmentUtil;
        this.objectMapper = objectMapper;
        this.caseUtil = caseUtil;
        this.notificationService = notificationService;
        this.individualService = individualService;
        this.advocateUtil = advocateUtil;
    }

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

    public List<Order> searchOrder(OrderSearchRequest request) {
        try {
            // Fetch applications from database according to the given search criteria
            List<Order> orderList = orderRepository.getOrders(request.getCriteria(), request.getPagination());

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
             if(!validator.validateApplicationExistence(body))
                throw new CustomException(ORDER_UPDATE_EXCEPTION, "Order don't exist");

            // Enrich application upon update
            enrichmentUtil.enrichOrderRegistrationUponUpdate(body);

            workflowUpdate(body);
            String updatedState = body.getOrder().getStatus();
            String orderType = body.getOrder().getOrderType();
            producer.push(config.getUpdateOrderKafkaTopic(), body);

            String messageCode = updatedState != null ? getMessageCode(orderType, updatedState) : null;
            if(messageCode != null){
                callNotificationService(body, messageCode);
            }

            return body.getOrder();

        } catch (CustomException e) {
            log.error("Custom Exception occurred while updating order :: {}",e.toString());
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while updating order");
            throw new CustomException(ORDER_UPDATE_EXCEPTION, "Error occurred while updating order: " + e.getMessage());
        }

    }

    public CaseSearchRequest createCaseSearchRequest(RequestInfo requestInfo, Order order) {
        CaseSearchRequest caseSearchRequest = new CaseSearchRequest();
        caseSearchRequest.setRequestInfo(requestInfo);
        CaseCriteria caseCriteria = CaseCriteria.builder().filingNumber(order.getFilingNumber()).defaultFields(false).build();
        caseSearchRequest.addCriteriaItem(caseCriteria);
        return caseSearchRequest;
    }
    private String getMessageCode(String orderType, String updatedStatus) {

         if(orderType.equalsIgnoreCase(SCHEDULE_OF_HEARING_DATE) && updatedStatus.equalsIgnoreCase(PUBLISHED)){
            return ADMISSION_HEARING_SCHEDULED;
        }
         if (updatedStatus.equalsIgnoreCase(PUBLISHED)){
             return ORDER_ISSUED;
         }
        return null;
    }

    private void callNotificationService(OrderRequest orderRequest, String messageCode) {

        try {
            CaseSearchRequest caseSearchRequest = createCaseSearchRequest(orderRequest.getRequestInfo(), orderRequest.getOrder());
            JsonNode caseDetails = caseUtil.searchCaseDetails(caseSearchRequest);
            JsonNode litigants = caseUtil.getLitigants(caseDetails);
            Set<String> individualIds = caseUtil.getIndividualIds(litigants);
            JsonNode representatives = caseUtil.getRepresentatives(caseDetails);
            Set<String> representativeIds = caseUtil.getAdvocateIds(representatives);

            if(!representativeIds.isEmpty()){
                representativeIds = advocateUtil.getAdvocate(orderRequest.getRequestInfo(),representativeIds.stream().toList());
            }
            individualIds.addAll(representativeIds);

            Object additionalDetailsObject = orderRequest.getOrder().getAdditionalDetails();
            String jsonData = objectMapper.writeValueAsString(additionalDetailsObject);
            JsonNode orderData = objectMapper.readTree(jsonData);
            String hearingDate = orderData.path("formdata").path("hearingDate").asText();

            Set<String> phonenumbers = callIndividualService(orderRequest.getRequestInfo(), individualIds);

            SmsTemplateData smsTemplateData = SmsTemplateData.builder()
                    .courtCaseNumber(caseDetails.has("courtCaseNumber") ? caseDetails.get("courtCaseNumber").asText() : "")
                    .cmpNumber(caseDetails.has("cmpNumber") ? caseDetails.get("cmpNumber").asText() : "")
                    .hearingDate(hearingDate)
                    .tenantId(orderRequest.getOrder().getTenantId()).build();

            for (String number : phonenumbers) {
                notificationService.sendNotification(orderRequest.getRequestInfo(), smsTemplateData, messageCode, number);
            }
        }
        catch (Exception e) {
            // Log the exception and continue the execution without throwing
            log.error("Error occurred while sending notification: {}", e.toString());
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

        String tenantId = order.getTenantId();
        String orderNumber = order.getOrderNumber();
        Workflow workflow = order.getWorkflow();

        String status = workflowUtil.updateWorkflowStatus(requestInfo, tenantId, orderNumber, config.getOrderBusinessServiceName(),
                    workflow, config.getOrderBusinessName());
        order.setStatus(status);
        if (PUBLISHED.equalsIgnoreCase(status))
            order.setCreatedDate(System.currentTimeMillis());
    }

    private Set<String> callIndividualService(RequestInfo requestInfo, Set<String> individualIds) {

        Set<String> mobileNumber = new HashSet<>();
        for(String id : individualIds){
            List<Individual> individuals = individualService.getIndividualsByIndividualId(requestInfo, id);
            if(individuals.get(0).getMobileNumber() != null){
                mobileNumber.add(individuals.get(0).getMobileNumber());
            }
        }
        return mobileNumber;
    }

}