package org.pucar.dristi.validators;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.repository.OrderRepository;
import org.pucar.dristi.util.CaseUtil;
import org.pucar.dristi.web.models.Order;
import org.pucar.dristi.web.models.OrderExists;
import org.pucar.dristi.web.models.OrderRequest;
import org.pucar.dristi.web.models.StatuteSection;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderRegistrationValidatorTest {

    @Mock
    private OrderRepository repository;

    @Mock
    private CaseUtil caseUtil;

    @InjectMocks
    private OrderRegistrationValidator orderRegistrationValidator;

    private OrderRequest orderRequest;
    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setTenantId("tenantId");
        order.setStatuteSection(new StatuteSection());
        order.setCnrNumber("cnrNumber");
        order.setFilingNumber("filingNumber");

        orderRequest = new OrderRequest();
        orderRequest.setOrder(order);
        orderRequest.setRequestInfo(new RequestInfo());
    }

    @Test
    void validateOrderRegistration_shouldThrowExceptionWhenTenantIdIsEmpty() {
        order.setTenantId(null);
        CustomException exception = assertThrows(CustomException.class, () ->
                orderRegistrationValidator.validateOrderRegistration(orderRequest));
        assertEquals("tenantId is mandatory for creating order", exception.getMessage());
    }

    @Test
    void validateOrderRegistration_shouldThrowExceptionWhenStatuteSectionIsEmpty() {
        order.setStatuteSection(null);
        CustomException exception = assertThrows(CustomException.class, () ->
                orderRegistrationValidator.validateOrderRegistration(orderRequest));
        assertEquals("statute and section is mandatory for creating order", exception.getMessage());
    }

    @Test
    void validateOrderRegistration_shouldThrowExceptionWhenCaseDetailsAreInvalid() {
        when(caseUtil.fetchCaseDetails(any(RequestInfo.class), anyString(), anyString())).thenReturn(false);
        CustomException exception = assertThrows(CustomException.class, () ->
                orderRegistrationValidator.validateOrderRegistration(orderRequest));
        assertEquals("Invalid Case", exception.getMessage());
    }

    @Test
    void validateOrderRegistration_shouldPassWhenAllValidationsArePassed() {
        when(caseUtil.fetchCaseDetails(any(RequestInfo.class), anyString(), anyString())).thenReturn(true);
        assertDoesNotThrow(() -> orderRegistrationValidator.validateOrderRegistration(orderRequest));
    }

    @Test
    void validateApplicationExistence_shouldReturnTrueWhenOrderExists() {
        List<OrderExists> orderExistsList = new ArrayList<>();
        OrderExists orderExists = new OrderExists();
        orderExists.setExists(true);
        orderExistsList.add(orderExists);

        when(repository.checkOrderExists(anyList())).thenReturn(orderExistsList);
        boolean result = orderRegistrationValidator.validateApplicationExistence(orderRequest);
        assertTrue(result);
    }

    @Test
    void validateApplicationExistence_shouldReturnFalseWhenOrderDoesNotExist() {
        List<OrderExists> orderExistsList = new ArrayList<>();
        OrderExists orderExists = new OrderExists();
        orderExists.setExists(false);
        orderExistsList.add(orderExists);

        when(repository.checkOrderExists(anyList())).thenReturn(orderExistsList);
        boolean result = orderRegistrationValidator.validateApplicationExistence(orderRequest);
        assertFalse(result);
    }

    @Test
    void validateApplicationExistence_shouldReturnFalseWhenOrderExistsListIsEmpty() {
        when(repository.checkOrderExists(anyList())).thenReturn(new ArrayList<>());
        boolean result = orderRegistrationValidator.validateApplicationExistence(orderRequest);
        assertFalse(result);
    }
}
