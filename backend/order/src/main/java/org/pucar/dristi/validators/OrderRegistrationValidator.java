package org.pucar.dristi.validators;

import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.OrderRepository;
import org.pucar.dristi.util.CaseUtil;
import org.pucar.dristi.util.MdmsUtil;
import org.pucar.dristi.web.models.CaseCriteria;
import org.pucar.dristi.web.models.Order;
import org.pucar.dristi.web.models.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.pucar.dristi.config.ServiceConstants.CREATE_ORDER_ERR;
import static org.pucar.dristi.config.ServiceConstants.MDMS_DATA_NOT_FOUND;

@Component
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
        if (ObjectUtils.isEmpty(orderRequest.getOrder().getHearingNumber()))
            throw new CustomException(CREATE_ORDER_ERR, "Hearing Number is mandatory for creating order");
        if (ObjectUtils.isEmpty(orderRequest.getOrder().getCnrNumber()))
            throw new CustomException(CREATE_ORDER_ERR, "CNR Number is mandatory for creating order");
        if (ObjectUtils.isEmpty(orderRequest.getOrder().getStatuteSection()))
            throw new CustomException(CREATE_ORDER_ERR, "statute and section is mandatory for creating order");

        Map<String, Map<String, JSONArray>> mdmsData = mdmsUtil.fetchMdmsData(requestInfo, orderRequest.getOrder().getTenantId(), "case", createMasterDetails());

        if (mdmsData.get("order") == null)
            throw new CustomException(MDMS_DATA_NOT_FOUND, "MDMS data does not exist");

        if (!caseUtil.fetchOrderDetails(requestInfo, orderRequest.getOrder().getCnrNumber()))
            throw new CustomException("INVALID_CNR_NUMBER", "Invalid CNR number");
    }

    public Order validateApplicationExistence(OrderRequest orderRequest) {
        Order order = orderRequest.getOrder();
        RequestInfo requestInfo = orderRequest.getRequestInfo();
        List<Order> existingApplications = repository.getApplications(order.getApplicationNumber().get(0), order.getCnrNumber(), order.getFilingNumber(), order.getTenantId(),
                String.valueOf(order.getId()), order.getStatus());
        if (existingApplications.isEmpty())
            throw new CustomException("VALIDATION EXCEPTION", "Case Application does not exist");

        Map<String, Map<String, JSONArray>> mdmsData = mdmsUtil.fetchMdmsData(requestInfo, order.getTenantId(), "case", createMasterDetails());

        if (mdmsData.get("order") == null)
            throw new CustomException(MDMS_DATA_NOT_FOUND, "MDMS data does not exist");

        if (!caseUtil.fetchOrderDetails(requestInfo, order.getCnrNumber()))
            throw new CustomException("INVALID_CNR_NUMBER", "Invalid CNR number");

        return existingApplications.get(0);
    }

    private List<String> createMasterDetails() {
        List<String> masterList = new ArrayList<>();
        masterList.add("ComplainantType");
        masterList.add("CaseCategory");
        masterList.add("PaymentMode");
        masterList.add("ResolutionMechanism");
        return masterList;
    }


}