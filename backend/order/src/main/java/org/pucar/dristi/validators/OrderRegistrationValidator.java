package org.pucar.dristi.validators;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.OrderRepository;
import org.pucar.dristi.util.CaseUtil;
import org.pucar.dristi.util.MdmsUtil;
import org.pucar.dristi.web.models.*;
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
    private OrderRepository repository;

    private CaseUtil caseUtil;

    private MdmsUtil mdmsUtil;

    private Configuration configuration;

    @Autowired
    public OrderRegistrationValidator(OrderRepository repository, CaseUtil caseUtil, MdmsUtil mdmsUtil,Configuration configuration) {
        this.repository = repository;
        this.caseUtil = caseUtil;
        this.mdmsUtil = mdmsUtil;
        this.configuration = configuration;
    }

    public void validateOrderRegistration(OrderRequest orderRequest) throws CustomException {
        RequestInfo requestInfo = orderRequest.getRequestInfo();

        if (ObjectUtils.isEmpty(orderRequest.getOrder().getStatuteSection()))
            throw new CustomException(CREATE_ORDER_ERR, "statute and section is mandatory for creating order");

        String orderType = orderRequest.getOrder().getOrderType();
        String mdmsData = mdmsUtil.fetchMdmsData(requestInfo, orderRequest.getOrder().getTenantId(), configuration.getOrderModule(), createMasterDetails());

        System.out.printf(configuration.getOrderTypePath().replace("{}",orderType));
        List<Map<String, Object>> orderTypeResults = JsonPath.read(mdmsData, configuration.getOrderTypePath().replace("{}",orderType));
        if (orderTypeResults.isEmpty()) {
            throw new CustomException(MDMS_DATA_NOT_FOUND, "Invalid OrderType");
        }

        String orderCategory = orderRequest.getOrder().getOrderCategory();
        List<Map<String, Object>> orderCategoryResults = JsonPath.read(mdmsData, configuration.getOrderCategoryPath().replace("{}",orderCategory));
        if (orderCategoryResults.isEmpty()) {
            throw new CustomException(MDMS_DATA_NOT_FOUND, "Invalid Order Category");
        }

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

    private List<String> createMasterDetails() {
        List<String> masterList = new ArrayList<>();
        masterList.add("OrderType");
        masterList.add("OrderCategory");

        return masterList;
    }
}