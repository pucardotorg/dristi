package org.pucar.dristi.validators;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.OrderRepository;
import org.pucar.dristi.util.CaseUtil;
import org.pucar.dristi.util.MdmsUtil;
import org.pucar.dristi.web.models.Order;
import org.pucar.dristi.web.models.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.pucar.dristi.config.ServiceConstants.*;

@Component
@Slf4j
public class OrderRegistrationValidator {
    @Autowired
    private OrderRepository repository;

    @Autowired
    private MdmsUtil mdmsUtil;

    @Autowired
    private CaseUtil caseUtil;

    public void validateOrderRegistration(OrderRequest orderRequest) throws CustomException {
        RequestInfo requestInfo = orderRequest.getRequestInfo();

        if (ObjectUtils.isEmpty(orderRequest.getOrder().getTenantId()))
            throw new CustomException(CREATE_ORDER_ERR, "tenantId is mandatory for creating order");
        if (ObjectUtils.isEmpty(orderRequest.getOrder().getStatuteSection()))
            throw new CustomException(CREATE_ORDER_ERR, "statute and section is mandatory for creating order");

        Map<String, Map<String, JSONArray>> mdmsData = mdmsUtil.fetchMdmsData(requestInfo, orderRequest.getOrder().getTenantId(), "Order", createMasterDetails());

        if (mdmsData.get("Order") == null)
            throw new CustomException(MDMS_DATA_NOT_FOUND, "MDMS data does not exist");

        if (!caseUtil.fetchCaseDetails(requestInfo, orderRequest.getOrder().getCnrNumber(), orderRequest.getOrder().getFilingNumber()))
            throw new CustomException("INVALID_CASE_DETAILS", "Invalid Case");
    }

    public Order validateApplicationExistence(OrderRequest orderRequest) {
        Order order = orderRequest.getOrder();
        RequestInfo requestInfo = orderRequest.getRequestInfo();
        List<Order> existingApplications = repository.getOrders("", "",order.getCnrNumber(), order.getFilingNumber(), order.getTenantId(),
                String.valueOf(order.getId()), order.getStatus());
        log.info("Existing application :: {}", existingApplications.size());
        if (existingApplications.isEmpty())
            throw new CustomException(VALIDATION_EXCEPTION, "Order does not exist");

        Map<String, Map<String, JSONArray>> mdmsData = mdmsUtil.fetchMdmsData(requestInfo, order.getTenantId(), "Order", createMasterDetails());

        if (mdmsData.get("Order") == null)
            throw new CustomException(MDMS_DATA_NOT_FOUND, "MDMS data does not exist");

        return existingApplications.get(0);
    }

    private List<String> createMasterDetails() {
        List<String> masterList = new ArrayList<>();
        masterList.add("ADRChannel");
        masterList.add("BailType");
        masterList.add("JudgementHolding");
        masterList.add("OrderCategory");
        masterList.add("OrderType");
        return masterList;
    }
}