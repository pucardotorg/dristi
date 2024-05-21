package org.pucar.dristi.service;

import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.OrderRegistrationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.OrderRepository;
import org.pucar.dristi.validators.OrderRegistrationValidator;
import org.pucar.dristi.web.models.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class OrderRegistrationServiceTest {

    @InjectMocks
    private OrderRegistrationService orderRegistrationService;

    @Mock
    private OrderRegistrationValidator validator;

    @Mock
    private OrderRegistrationEnrichment enrichmentUtil;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private WorkflowService workflowService;

    @Mock
    private Configuration config;

    @Mock
    private Producer producer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOrder_success() {
        OrderRequest orderRequest = new OrderRequest();
        Order order = new Order();
        orderRequest.setOrder(order);

        doNothing().when(validator).validateOrderRegistration(any(OrderRequest.class));
        doNothing().when(enrichmentUtil).enrichOrderRegistration(any(OrderRequest.class));
        doNothing().when(workflowService).updateWorkflowStatus(any(OrderRequest.class));
        doNothing().when(producer).push(anyString(), any(OrderRequest.class));

        Order result = orderRegistrationService.createOrder(orderRequest);

        assertNotNull(result);
        verify(validator, times(1)).validateOrderRegistration(orderRequest);
        verify(enrichmentUtil, times(1)).enrichOrderRegistration(orderRequest);
        verify(workflowService, times(1)).updateWorkflowStatus(orderRequest);
    }

    @Test
    public void testCreateOrder_customException() {
        OrderRequest orderRequest = new OrderRequest();

        doThrow(new CustomException("TEST_EXCEPTION", "Test exception"))
                .when(validator).validateOrderRegistration(any(OrderRequest.class));

        CustomException exception = assertThrows(CustomException.class, () ->
                orderRegistrationService.createOrder(orderRequest));

        assertEquals("ORDER_CREATE_EXCEPTION", exception.getCode());
        assertTrue(exception.getMessage().contains("Test exception"));
        verify(validator, times(1)).validateOrderRegistration(orderRequest);
    }

    @Test
    public void testSearchOrder_success() {
        List<Order> mockOrderList = new ArrayList<>();
        Order mockOrder = new Order();
        mockOrder.setId(UUID.randomUUID());
        mockOrderList.add(mockOrder);

        when(orderRepository.getApplications(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockOrderList);
        when(workflowService.getWorkflowFromProcessInstance(any())).thenReturn(new Workflow());

        List<Order> result = orderRegistrationService.searchOrder("appNum", "cnrNum", "filingNum", "tenant", "id", "status", new RequestInfo());

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).getApplications("appNum", "cnrNum", "filingNum", "tenant", "id", "status");
        verify(workflowService, times(1)).getWorkflowFromProcessInstance(any());
    }

    @Test
    public void testUpdateOrder_success() {
        OrderRequest orderRequest = new OrderRequest();
        Order order = new Order();
        order.setWorkflow(new Workflow());
        orderRequest.setOrder(order);

        Order existingOrder = new Order();
        existingOrder.setId(UUID.randomUUID());

        when(validator.validateApplicationExistence(any(OrderRequest.class)))
                .thenReturn(existingOrder);
        doNothing().when(enrichmentUtil).enrichOrderRegistrationUponUpdate(any(OrderRequest.class));
        doNothing().when(workflowService).updateWorkflowStatus(any(OrderRequest.class));
        doNothing().when(producer).push(anyString(), any(OrderRequest.class));

        Order result = orderRegistrationService.updateOrder(orderRequest);

        assertNotNull(result);
        verify(validator, times(1)).validateApplicationExistence(orderRequest);
        verify(enrichmentUtil, times(1)).enrichOrderRegistrationUponUpdate(orderRequest);
        verify(workflowService, times(1)).updateWorkflowStatus(orderRequest);
    }

    @Test
    public void testExistsOrder_success() {
        OrderExistsRequest orderExistsRequest = new OrderExistsRequest();
        OrderExists orderExists = new OrderExists();
        orderExists.setApplicationNumber("appNum");
        orderExists.setFilingNumber("filingNum");
        orderExists.setCnrNumber("cnrNum");
        orderExistsRequest.setOrder(orderExists);
        User user = User.builder().uuid(UUID.randomUUID().toString()).build();
        RequestInfo requestInfo = RequestInfo.builder().userInfo(user).build();
        orderExistsRequest.setRequestInfo(requestInfo);

        List<Order> mockOrderList = new ArrayList<>();
        Order mockOrder = new Order();
        mockOrder.setFilingNumber("filingNum");
        mockOrder.setCnrNumber("cnrNum");
        mockOrder.setApplicationNumber(List.of("cnrNum"));
        mockOrder.setTenantId("pg");
        mockOrderList.add(mockOrder);

        when(orderRepository.getApplications(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockOrderList);

        OrderExists result = orderRegistrationService.existsOrder(orderExistsRequest);

        assertNotNull(result);
    }
}
