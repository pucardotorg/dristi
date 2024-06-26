package org.pucar.dristi.validators;

import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.repository.OrderRepository;
import org.pucar.dristi.util.CaseUtil;
import org.pucar.dristi.util.MdmsUtil;
import org.pucar.dristi.web.models.Order;
import org.pucar.dristi.web.models.OrderRequest;
import org.pucar.dristi.web.models.StatuteSection;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderRegistrationValidatorTest {

    @InjectMocks
    private OrderRegistrationValidator validator;

    @Mock
    private OrderRepository repository;

    @Mock
    private MdmsUtil mdmsUtil;

    @Mock
    private CaseUtil caseUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testValidateOrderRegistration_ValidOrderRequest_Success() {
        // Create a valid order request
        OrderRequest orderRequest = new OrderRequest();
        Order order = new Order();
        order.setTenantId("tenantId");
        order.setCnrNumber("CNR123");
        order.setStatuteSection(new StatuteSection());
        orderRequest.setOrder(order);
        RequestInfo requestInfo = new RequestInfo();
        orderRequest.setRequestInfo(requestInfo);

        // Mock dependencies
        Map<String, Map<String, JSONArray>> mdmsData = new HashMap<>();
        mdmsData.put("Order", new HashMap<>());
        // Mocking MDMS data
        when(mdmsUtil.fetchMdmsData(any(RequestInfo.class), eq("tenantId"), eq("Order"), anyList())).thenReturn(mdmsData);

        // Perform validation
        CustomException exception = assertThrows(CustomException.class, () -> validator.validateOrderRegistration(orderRequest));
        assertEquals("Invalid Case", exception.getMessage());
    }

    @Test
    public void testValidateOrderRegistration_MissingTenantId_ExceptionThrown() {
        // Create an order request with missing tenantId
        OrderRequest orderRequest = new OrderRequest();
        Order order = new Order();
        order.setCnrNumber("CNR123");
        orderRequest.setOrder(order);

        // Perform validation
        CustomException exception = assertThrows(CustomException.class, () -> validator.validateOrderRegistration(orderRequest));
        assertEquals("tenantId is mandatory for creating order", exception.getMessage());
    }

    @Test
    public void testValidateApplicationExistence_ValidOrderRequest_Success() {
        // Create a valid order request
        OrderRequest orderRequest = new OrderRequest();
        Order order = new Order();
        order.setTenantId("tenantId");
        order.setCnrNumber("CNR123");
        order.setFilingNumber("Filing123");
        order.setStatus("status");
        order.setOrderNumber("order");
        order.setApplicationNumber(Collections.singletonList(""));
        order.setId(UUID.fromString("3244d158-c5cb-4769-801f-a0f94f383679"));
        order.setStatuteSection(new StatuteSection());
        orderRequest.setOrder(order);
        RequestInfo requestInfo = new RequestInfo();
        orderRequest.setRequestInfo(requestInfo);

        // Mock repository response
        List<Order> existingApplications = new ArrayList<>();
        existingApplications.add(order);
        when(repository.getOrders(anyString(),anyString(), anyString(), anyString(), anyString(), anyString(),anyString())).thenReturn(existingApplications);

        // Mock MDMS data
        Map<String, Map<String, JSONArray>> mdmsData = new HashMap<>();
        mdmsData.put("Order", new HashMap<>());
        when(mdmsUtil.fetchMdmsData(any(RequestInfo.class), eq("tenantId"), eq("Order"), anyList())).thenReturn(mdmsData);

        // Perform validation
        assertDoesNotThrow(() -> validator.validateApplicationExistence(orderRequest));
        assertEquals("tenantId", existingApplications.get(0).getTenantId());
    }

}
