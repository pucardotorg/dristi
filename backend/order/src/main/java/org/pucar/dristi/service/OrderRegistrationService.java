package org.pucar.dristi.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.OrderRegistrationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.OrderRepository;
import org.pucar.dristi.validators.OrderRegistrationValidator;
import org.pucar.dristi.web.models.Order;
import org.pucar.dristi.web.models.OrderListResponse;
import org.pucar.dristi.web.models.OrderRequest;
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
            workflowService.updateWorkflowStatus(body);

            producer.push(config.getSaveOrderKafkaTopic(), body);
            return body.getOrder();
        } catch (Exception e) {
            log.error("Error occurred while creating order");
            throw new CustomException("ORDER_CREATE_EXCEPTION", e.getMessage());
        }
    }

    public List<Order> searchCases(String applicationNumber, String cnrNumber, String filingNumber, String tenantId, String id, String status) {

        try {
            // Fetch applications from database according to the given search criteria
            List<Order> orderList = orderRepository.getApplications(applicationNumber,cnrNumber,filingNumber, tenantId, id, status);

            // If no applications are found matching the given criteria, return an empty list
            if (CollectionUtils.isEmpty(orderList))
                return new ArrayList<>();
          //  orderList.forEach(order -> order.setWorkflow(workflowService.getWorkflowFromProcessInstance(workflowService.getCurrentWorkflow())));
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
//
//    public List<CaseExists> existCases(CaseSearchRequest caseSearchRequests) {
//        try {
//            // Fetch applications from database according to the given search criteria
//            List<CourtCase> courtCases = caseRepository.getApplications(caseSearchRequests.getCriteria());
//
//            List<CaseExists> caseExistsList = new ArrayList<>();
//
//            for(CaseCriteria caseCriteria: caseSearchRequests.getCriteria()){
//              boolean notExists = courtCases.stream().filter(c->c.getFilingNumber().equalsIgnoreCase(caseCriteria.getFilingNumber())
//                      && c.getCaseNumber().equalsIgnoreCase(caseCriteria.getCnrNumber())).toList().isEmpty();
//              CaseExists caseExists = new CaseExists(caseCriteria.getCourtCaseNumber(),caseCriteria.getCnrNumber(), caseCriteria.getFilingNumber(), !notExists);
//              caseExistsList.add(caseExists);
//            }
//
//            // If no applications are found matching the given criteria, return an empty list
//            if(CollectionUtils.isEmpty(courtCases))
//                return new ArrayList<>();
//            courtCases.forEach(cases -> cases.setWorkflow(workflowService.getWorkflowFromProcessInstance(workflowService.getCurrentWorkflow(caseSearchRequests.getRequestInfo(), cases.getTenantId(), cases.getCaseNumber()))));
//            return caseExistsList;
//        }
//        catch (CustomException e){
//            log.error("Custom Exception occurred while searching");
//            throw e;
//        }
//        catch (Exception e){
//            log.error("Error while fetching to search results");
//            throw new CustomException("CASE_EXIST_EXCEPTION",e.getMessage());
//        }
//    }
}