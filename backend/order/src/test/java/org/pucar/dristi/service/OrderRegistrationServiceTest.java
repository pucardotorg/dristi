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
import org.pucar.dristi.util.WorkflowUtil;
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
    private WorkflowUtil workflowUtil;

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
        order.setOrderType("other");
        orderRequest.setOrder(order);

        doNothing().when(validator).validateOrderRegistration(any(OrderRequest.class));
        doNothing().when(enrichmentUtil).enrichOrderRegistration(any(OrderRequest.class));
        when(workflowUtil.updateWorkflowStatus(any(RequestInfo.class),anyString(),anyString(),anyString(),any(Workflow.class),anyString())).thenReturn("APPROVED");
        doNothing().when(producer).push(anyString(), any(OrderRequest.class));

        Order result = orderRegistrationService.createOrder(orderRequest);

        assertNotNull(result);
        verify(validator, times(1)).validateOrderRegistration(orderRequest);
        verify(enrichmentUtil, times(1)).enrichOrderRegistration(orderRequest);
    }

    @Test
    public void testCreateOrder_customException() {
        OrderRequest orderRequest = new OrderRequest();

        doThrow(new CustomException("TEST_EXCEPTION", "Test exception"))
                .when(validator).validateOrderRegistration(any(OrderRequest.class));

        CustomException exception = assertThrows(CustomException.class, () ->
                orderRegistrationService.createOrder(orderRequest));

        assertTrue(exception.getMessage().contains("Test exception"));
        verify(validator, times(1)).validateOrderRegistration(orderRequest);
    }

    @Test
    public void testSearchOrder_success() {
        List<Order> mockOrderList = new ArrayList<>();
        Order order = new Order();
        order.setTenantId("tenantId");
        order.setCnrNumber("CNR123");
        order.setFilingNumber("Filing123");
        order.setStatus("status");
        order.setOrderNumber("order");
        order.setApplicationNumber(Collections.singletonList(""));
        order.setId(UUID.fromString("3244d158-c5cb-4769-801f-a0f94f383679"));
        order.setStatuteSection(new StatuteSection());
        mockOrderList.add(order);

        when(orderRepository.getOrders(anyString(), anyString(),anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockOrderList);

        List<Order> result = orderRegistrationService.searchOrder("order-no","appNum", "cnrNum", "filingNum", "tenant", "id", "status", new RequestInfo());

        assertNotNull(result);
        verify(orderRepository, times(1)).getOrders("order-no","appNum","cnrNum", "filingNum", "tenant", "id", "status");
    }

    @Test
    public void testUpdateOrder_success() {
        OrderRequest orderRequest = new OrderRequest();
        Order order = new Order();
        order.setWorkflow(new Workflow());
        order.setOrderType("other");
        orderRequest.setOrder(order);

        Order existingOrder = new Order();
        existingOrder.setId(UUID.randomUUID());

        when(validator.validateApplicationExistence(any(OrderRequest.class)))
                .thenReturn(existingOrder);
        doNothing().when(enrichmentUtil).enrichOrderRegistrationUponUpdate(any(OrderRequest.class));
        doNothing().when(producer).push(anyString(), any(OrderRequest.class));

        Order result = orderRegistrationService.updateOrder(orderRequest);

        assertNotNull(result);
        verify(validator, times(1)).validateApplicationExistence(orderRequest);
        verify(enrichmentUtil, times(1)).enrichOrderRegistrationUponUpdate(orderRequest);
    }

    @Test
    public void testExistsOrder_success() {
        OrderExistsRequest orderExistsRequest = new OrderExistsRequest();
        OrderExists orderExists = new OrderExists();
        orderExists.setApplicationNumber("appNum");
        orderExists.setFilingNumber("filingNum");
        orderExists.setCnrNumber("cnrNum");
        orderExistsRequest.setOrder(List.of(orderExists));
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

        when(orderRepository.getOrders(anyString(),anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockOrderList);

        List<OrderExists> result = orderRegistrationService.existsOrder(orderExistsRequest);

        assertNotNull(result);
    }
}
