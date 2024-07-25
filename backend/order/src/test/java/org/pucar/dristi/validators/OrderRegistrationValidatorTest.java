package org.pucar.dristi.validators;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.repository.OrderRepository;
import org.pucar.dristi.web.models.Order;
import org.pucar.dristi.web.models.OrderExists;
import org.pucar.dristi.web.models.OrderRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class OrderRegistrationValidatorTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderRegistrationValidator orderRegistrationValidator;

    private OrderRequest orderRequest;
    private RequestInfo requestInfo;
    private Order order;

    @BeforeEach
    void setUp() {
        requestInfo = new RequestInfo();
        order = new Order();
        order.setCnrNumber("123");
        order.setFilingNumber("111");
        orderRequest = new OrderRequest();
        orderRequest.setRequestInfo(requestInfo);
        orderRequest.setOrder(order);
    }

    @Test
    void validateOrderRegistration_shouldThrowExceptionWhenStatuteSectionIsEmpty() {
        order.setStatuteSection(null);
        CustomException exception = assertThrows(CustomException.class, () ->
                orderRegistrationValidator.validateOrderRegistration(orderRequest));
        assertEquals("statute and section is mandatory for creating order", exception.getMessage());
    }

    @Test
    void validateApplicationExistence_shouldReturnTrue_whenOrderExists() {
        order.setFilingNumber("filingNumber");
        order.setCnrNumber("cnrNumber");

        OrderExists orderExists = new OrderExists();
        orderExists.setFilingNumber(order.getFilingNumber());
        orderExists.setCnrNumber(order.getCnrNumber());
        orderExists.setOrderId(order.getId());

        List<OrderExists> orderExistsList = new ArrayList<>();
        orderExistsList.add(orderExists);
        orderExistsList.get(0).setExists(true);

        when(orderRepository.checkOrderExists(anyList())).thenReturn(orderExistsList);

        boolean result = orderRegistrationValidator.validateApplicationExistence(orderRequest);
        assertTrue(result);
    }

    @Test
    void validateApplicationExistence_shouldReturnFalse_whenOrderDoesNotExist() {
        order.setFilingNumber("filingNumber");
        when(orderRepository.checkOrderExists(anyList())).thenReturn(Collections.emptyList());

        boolean result = orderRegistrationValidator.validateApplicationExistence(orderRequest);
        assertFalse(result);
    }
}
