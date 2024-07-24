package org.pucar.dristi.validators;

import com.jayway.jsonpath.JsonPath;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.OrderRepository;
import org.pucar.dristi.util.CaseUtil;
import org.pucar.dristi.util.MdmsUtil;
import org.pucar.dristi.web.models.Order;
import org.pucar.dristi.web.models.OrderExists;
import org.pucar.dristi.web.models.OrderRequest;
import org.pucar.dristi.web.models.StatuteSection;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.pucar.dristi.config.ServiceConstants.CREATE_ORDER_ERR;
import static org.pucar.dristi.config.ServiceConstants.MDMS_DATA_NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class OrderRegistrationValidatorTest {

    @Mock
    private OrderRepository repository;

    @Mock
    private CaseUtil caseUtil;

    @Mock
    private MdmsUtil mdmsUtil;

    @Mock
    private Configuration configuration;

    @InjectMocks
    private OrderRegistrationValidator validator;

    private OrderRequest orderRequest;
    private Order order;
    private RequestInfo requestInfo;

    @BeforeEach
    void setUp() {
        orderRequest = new OrderRequest();
        order = new Order();
        requestInfo = new RequestInfo();
        orderRequest.setOrder(order);
        orderRequest.setRequestInfo(requestInfo);
    }

    @Test
    void validateOrderRegistration_shouldThrowException_whenStatuteSectionIsEmpty() {
        order.setStatuteSection(null);
        CustomException exception = assertThrows(CustomException.class, () -> {
            validator.validateOrderRegistration(orderRequest);
        });
        assertEquals(CREATE_ORDER_ERR, exception.getCode());
        assertEquals("statute and section is mandatory for creating order", exception.getMessage());
    }

//    @Test
//    void validateOrderRegistration_shouldThrowException_whenJsonPathIsInvalid() {
//        String mdmsData = "{\"OrderType\": [{\"code\": \"TYPE1\", \"isactive\": true}], \"OrderCategory\": [{\"category\": \"CATEGORY1\", \"isactive\": true}]}";
//        when(mdmsUtil.fetchMdmsData(any(), any(), any(), any())).thenReturn(mdmsData);
//        when(configuration.getOrderTypePath()).thenReturn("$.InvalidOrderTypePath");
//        when(configuration.getOrderCategoryPath()).thenReturn("$.InvalidOrderCategoryPath");
//
//        order.setStatuteSection(new StatuteSection());
//        order.setOrderType("TYPE1");
//        order.setOrderCategory("CATEGORY1");
//
//        Exception exception = assertThrows(Exception.class, () -> validator.validateOrderRegistration(orderRequest));
//        assertEquals("No result", exception.getMessage());
//    }

//    @Test
//    void validateOrderRegistration_shouldThrowException_whenOrderTypeIsInvalid() {
//        String mdmsData = "{\"OrderType\": [{\"code\": \"TYPE1\", \"isactive\": true}], \"OrderCategory\": [{\"category\": \"CATEGORY1\", \"isactive\": true}]}";
//        when(mdmsUtil.fetchMdmsData(any(), any(), any(), any())).thenReturn(mdmsData);
//        when(configuration.getOrderTypePath()).thenReturn("$.OrderType");
//        when(configuration.getOrderCategoryPath()).thenReturn("$.OrderCategory");
//        when(JsonPath.read(mdmsData, configuration.getOrderTypePath())).thenReturn("[{\"code\": \"TYPE1\", \"isactive\": true}]");
//        when(JsonPath.read(mdmsData, configuration.getOrderCategoryPath())).thenReturn("[{\"category\": \"CATEGORY1\", \"isactive\": true}]");
//
//        order.setStatuteSection(new StatuteSection());
//        order.setOrderType("INVALID_TYPE");
//
//        CustomException exception = assertThrows(CustomException.class, () -> validator.validateOrderRegistration(orderRequest));
//        assertEquals(MDMS_DATA_NOT_FOUND, exception.getCode());
//        assertEquals("Invalid OrderType", exception.getMessage());
//    }
//
//    @Test
//    void validateOrderRegistration_shouldThrowException_whenOrderCategoryIsInvalid()  {
//        String mdmsData = "{\"OrderType\": [{\"code\": \"TYPE1\", \"isactive\": true}], \"OrderCategory\": [{\"category\": \"CATEGORY1\", \"isactive\": true}]}";
//        when(mdmsUtil.fetchMdmsData(any(), any(), any(), any())).thenReturn(mdmsData);
//        when(configuration.getOrderTypePath()).thenReturn("$.OrderType");
//        when(configuration.getOrderCategoryPath()).thenReturn("$.OrderCategory");
//        when(JsonPath.read(mdmsData, configuration.getOrderTypePath())).thenReturn("[{\"code\": \"TYPE1\", \"isactive\": true}]");
//        when(JsonPath.read(mdmsData, configuration.getOrderCategoryPath())).thenReturn("[{\"category\": \"CATEGORY1\", \"isactive\": true}]");
//
//        order.setStatuteSection(new StatuteSection());
//        order.setOrderType("TYPE1");
//        order.setOrderCategory("INVALID_CATEGORY");
//
//        CustomException exception = assertThrows(CustomException.class, () -> validator.validateOrderRegistration(orderRequest));
//        assertEquals(MDMS_DATA_NOT_FOUND, exception.getCode());
//        assertEquals("Invalid Order Category", exception.getMessage());
//    }
//
//    @Test
//    void validateOrderRegistration_shouldThrowException_whenCaseDetailsAreInvalid() {
//        String mdmsData = "{\"OrderType\": [{\"code\": \"TYPE1\", \"isactive\": true}], \"OrderCategory\": [{\"category\": \"CATEGORY1\", \"isactive\": true}]}";
//        when(mdmsUtil.fetchMdmsData(any(), any(), any(), any())).thenReturn(mdmsData);
//        when(configuration.getOrderTypePath()).thenReturn("$.OrderType");
//        when(configuration.getOrderCategoryPath()).thenReturn("$.OrderCategory");
//        when(JsonPath.read(mdmsData, configuration.getOrderTypePath())).thenReturn("[{\"code\": \"TYPE1\", \"isactive\": true}]");
//        when(JsonPath.read(mdmsData, configuration.getOrderCategoryPath())).thenReturn("[{\"category\": \"CATEGORY1\", \"isactive\": true}]");
//        when(caseUtil.fetchCaseDetails(any(), any(), any())).thenReturn(false);
//
//        order.setStatuteSection(new StatuteSection());
//        order.setOrderType("TYPE1");
//        order.setOrderCategory("CATEGORY1");
//
//        CustomException exception = assertThrows(CustomException.class, () -> validator.validateOrderRegistration(orderRequest));
//        assertEquals("INVALID_CASE_DETAILS", exception.getCode());
//        assertEquals("Invalid Case", exception.getMessage());
//    }
//
//    @Test
//    void validateOrderRegistration_shouldPass_whenAllValidationsPass(){
//        String mdmsData = "{\"OrderType\": [{\"code\": \"TYPE1\", \"isactive\": true}], \"OrderCategory\": [{\"category\": \"CATEGORY1\", \"isactive\": true}]}";
//        when(mdmsUtil.fetchMdmsData(any(), any(), any(), any())).thenReturn(mdmsData);
//        when(configuration.getOrderTypePath()).thenReturn("$.OrderType");
//        when(configuration.getOrderCategoryPath()).thenReturn("$.OrderCategory");
//        when(JsonPath.read(mdmsData, configuration.getOrderTypePath())).thenReturn("[{\"code\": \"TYPE1\", \"isactive\": true}]");
//        when(JsonPath.read(mdmsData, configuration.getOrderCategoryPath())).thenReturn("[{\"category\": \"CATEGORY1\", \"isactive\": true}]");
//        when(caseUtil.fetchCaseDetails(any(), any(), any())).thenReturn(true);
//
//        order.setStatuteSection(new StatuteSection());
//        order.setOrderType("TYPE1");
//        order.setOrderCategory("CATEGORY1");
//
//        assertDoesNotThrow(() -> validator.validateOrderRegistration(orderRequest));
//    }

    @Test
    void validateApplicationExistence_shouldReturnTrue_whenOrderExists() {
        Order order = new Order();
        order.setFilingNumber("FilingNumber");
        order.setCnrNumber("CnrNumber");
        orderRequest.setOrder(order);

        OrderExists orderExists = new OrderExists();
        orderExists.setFilingNumber("FilingNumber");
        orderExists.setCnrNumber("CnrNumber");
        orderExists.setExists(true);

        List<OrderExists> orderExistsList = new ArrayList<>();
        orderExistsList.add(orderExists);

        when(repository.checkOrderExists(any())).thenReturn(orderExistsList);

        boolean result = validator.validateApplicationExistence(orderRequest);
        assertTrue(result);
    }

    @Test
    void validateApplicationExistence_shouldReturnFalse_whenOrderDoesNotExist() {
        Order order = new Order();
        order.setFilingNumber("FilingNumber");
        order.setCnrNumber("CnrNumber");
        orderRequest.setOrder(order);

        List<OrderExists> orderExistsList = new ArrayList<>();

        when(repository.checkOrderExists(any())).thenReturn(orderExistsList);

        boolean result = validator.validateApplicationExistence(orderRequest);
        assertFalse(result);
    }
}
