package org.pucar.dristi.validators;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.json.JSONArray;
import org.json.JSONException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.OrderRepository;
import org.pucar.dristi.util.CaseUtil;
import org.pucar.dristi.util.MdmsUtil;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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

        String mdmsData = mdmsUtil.fetchMdmsData(requestInfo, orderRequest.getOrder().getTenantId(),
                configuration.getOrderModule(), createMasterDetails());

        Object jsonObjectOrderType = JsonPath.read(mdmsData, configuration.getOrderTypePath());
        Object jsonObjectOrderCategory = JsonPath.read(mdmsData, configuration.getOrderCategoryPath());

        //MDMS Order Type list
        JSONArray mdmsArrayOrderType = new JSONArray(jsonObjectOrderType.toString());
        List<OrderType> orderTypeList = convertToOrderType(mdmsArrayOrderType);

        //MDMS Order category list
        JSONArray mdmsArrayOrderCategory = new JSONArray(jsonObjectOrderCategory.toString());
        List<OrderCategory> orderCategoryList = convertToOrderCategory(mdmsArrayOrderCategory);

        String orderType = orderRequest.getOrder().getOrderType();
        String orderCategory = orderRequest.getOrder().getOrderCategory();

        if(orderTypeList.stream().filter(o -> o.getCode().equalsIgnoreCase(orderType)&& o.getIsActive()).toList().isEmpty())
            throw new CustomException(MDMS_DATA_NOT_FOUND, "Invalid OrderType");

        if(orderCategoryList.stream().filter(o -> o.getCategory().equalsIgnoreCase(orderCategory)&& o.getIsActive()).toList().isEmpty())
            throw new CustomException(MDMS_DATA_NOT_FOUND, "Invalid Order Category");

        if (!caseUtil.fetchCaseDetails(requestInfo, orderRequest.getOrder().getCnrNumber(), orderRequest.getOrder().getFilingNumber()))
            throw new CustomException("INVALID_CASE_DETAILS", "Invalid Case");
    }


    private List<OrderType> convertToOrderType(JSONArray orderTypeArray) throws JSONException {
        List<OrderType> orderTypeList = new ArrayList<>();

        for (int i = 0; i < orderTypeArray.length(); i++) {
            OrderType orderType = new OrderType();

            if(orderTypeArray.getJSONObject(i).has("code")){
                orderType.setCode(orderTypeArray.getJSONObject(i).getString("code"));
            } else {
                orderType.setCode(null);
            }
            if(orderTypeArray.getJSONObject(i).has("isactive")){
                orderType.setIsActive(orderTypeArray.getJSONObject(i).getBoolean("isactive"));
            } else {
                orderType.setIsActive(null);
            }
            orderTypeList.add(orderType);
        }

        return orderTypeList;
    }

    private List<OrderCategory> convertToOrderCategory(JSONArray orderCategoryArray) throws JSONException {
        List<OrderCategory> orderCategoryList = new ArrayList<>();

        for (int i = 0; i < orderCategoryArray.length(); i++) {
            OrderCategory orderCategory = new OrderCategory();
            if(orderCategoryArray.getJSONObject(i).has("category")){
                orderCategory.setCategory(orderCategoryArray.getJSONObject(i).getString("category"));
            } else {
                orderCategory.setCategory(null);
            }
            if(orderCategoryArray.getJSONObject(i).has("isactive")){
                orderCategory.setIsActive(orderCategoryArray.getJSONObject(i).getBoolean("isactive"));
            } else {
                orderCategory.setIsActive(null);
            }
            orderCategoryList.add(orderCategory);
        }

        return orderCategoryList;
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