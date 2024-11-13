package org.pucar.dristi.validators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.OrderRepository;
import org.pucar.dristi.util.CaseUtil;
import org.pucar.dristi.util.FileStoreUtil;
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
    private FileStoreUtil fileStoreUtil;

    private Configuration configuration;
    private MdmsUtil mdmsUtil;

    public ObjectMapper objectMapper;


    @Autowired
    public OrderRegistrationValidator(OrderRepository repository, CaseUtil caseUtil, FileStoreUtil fileStoreUtil, Configuration configuration,MdmsUtil mdmsUtil,ObjectMapper objectMapper) {
            this.repository = repository;
            this.caseUtil = caseUtil;
            this.fileStoreUtil = fileStoreUtil;
            this.configuration = configuration;
            this.mdmsUtil = mdmsUtil;
            this.objectMapper = objectMapper;
        }

    public void validateOrderRegistration(OrderRequest orderRequest) throws CustomException {
        RequestInfo requestInfo = orderRequest.getRequestInfo();

        if (ObjectUtils.isEmpty(orderRequest.getOrder().getStatuteSection()))
            throw new CustomException(CREATE_ORDER_ERR, "statute and section is mandatory for creating order");

        if(!ADMINISTRATIVE.equalsIgnoreCase(orderRequest.getOrder().getOrderCategory()) && !caseUtil.fetchCaseDetails(requestInfo, orderRequest.getOrder().getCnrNumber(), orderRequest.getOrder().getFilingNumber())){
                throw new CustomException("INVALID_CASE_DETAILS", "Invalid Case");
        }

        //validate documents
        validateDocuments(orderRequest.getOrder());

        validateMDMSDocumentTypes(orderRequest);
    }

    public boolean validateApplicationExistence(OrderRequest orderRequest) {
        //validate documents
        validateDocuments(orderRequest.getOrder());

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

    private void validateDocuments(Order order){
        if (order.getDocuments() != null && !order.getDocuments().isEmpty()) {
            order.getDocuments().forEach(document -> {
                if (document.getFileStore() != null) {
                    if (!fileStoreUtil.doesFileExist(order.getTenantId(), document.getFileStore()))
                        throw new CustomException(INVALID_FILESTORE_ID, INVALID_DOCUMENT_DETAILS);
                } else
                    throw new CustomException(INVALID_FILESTORE_ID, INVALID_DOCUMENT_DETAILS);

            });
        }
    }

    private void validateMDMSDocumentTypes(OrderRequest orderRequest){
        String mdmsData = mdmsUtil.fetchMdmsData(orderRequest.getRequestInfo(), orderRequest.getOrder().getTenantId(), configuration.getOrderModule(), createMasterDetails());

        Object orderDetails = orderRequest.getOrder().getOrderDetails();

        if (orderDetails != null) {
            // Extract 'documentType' object from 'orderDetails'
            Map<String, Object> orderDetailsMap = objectMapper.convertValue(orderDetails, Map.class);

            if (orderDetailsMap != null) {

                // Extract 'documentType' from 'orderDetails'
                Map<String, Object> documentType = (Map<String, Object>) orderDetailsMap.get("documentType");

                if (documentType != null) {
                    // Extract 'value' from 'documentType'
                    String documentTypeValue = (String) documentType.get("value");

                    List<Map<String, Object>> orderTypeResults = JsonPath.read(mdmsData, configuration.getDocumentTypePath().replace("{}",documentTypeValue));
                    if (orderTypeResults.isEmpty()) {
                        throw new CustomException(MDMS_DATA_NOT_FOUND, "Invalid DocumentType");
                    }
                }
            }
        }
    }

    private List<String> createMasterDetails() {
        List<String> masterList = new ArrayList<>();
        masterList.add("DocumentType");
        return masterList;
    }
}