package org.pucar.dristi.validators;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.OrderRepository;
import org.pucar.dristi.util.CaseUtil;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

import static org.pucar.dristi.config.ServiceConstants.*;

@Component
@Slf4j
public class OrderRegistrationValidator {
    private OrderRepository repository;

    private CaseUtil caseUtil;


    @Autowired
    public OrderRegistrationValidator(OrderRepository repository, CaseUtil caseUtil) {
        this.repository = repository;
        this.caseUtil = caseUtil;
    }

    public void validateOrderRegistration(OrderRequest orderRequest) throws CustomException {
        RequestInfo requestInfo = orderRequest.getRequestInfo();

        if (ObjectUtils.isEmpty(orderRequest.getOrder().getStatuteSection()))
            throw new CustomException(CREATE_ORDER_ERR, "statute and section is mandatory for creating order");

        if (!caseUtil.fetchCaseDetails(requestInfo, orderRequest.getOrder().getCnrNumber(), orderRequest.getOrder().getFilingNumber()))
            throw new CustomException("INVALID_CASE_DETAILS", "Invalid Case");
    }

    public boolean validateApplicationExistence(OrderRequest orderRequest) {
        Order order = orderRequest.getOrder();

        OrderExists orderExists = new OrderExists();
        orderExists.setFilingNumber(order.getFilingNumber());
        orderExists.setCnrNumber(order.getCnrNumber());
        orderExists.setOrderId(order.getId());
        List<OrderExists> criteriaList = new ArrayList<>();
        criteriaList.add(orderExists);
        List<OrderExists> orderExistsList = repository.checkOrderExists(criteriaList);

        return !orderExistsList.isEmpty() && orderExistsList.get(0).getExists();
    }
}