package org.pucar.dristi.enrichment;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.IdgenUtil;
import org.pucar.dristi.web.models.Order;
import org.pucar.dristi.web.models.OrderRequest;
import org.pucar.dristi.web.models.StatuteSection;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class OrderRegistrationEnrichmentTest {

    @Mock
    private IdgenUtil idgenUtil;

    @Mock
    private Configuration configuration;

    @InjectMocks
    private OrderRegistrationEnrichment orderRegistrationEnrichment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private OrderRequest createMockOrderRequest() {
        OrderRequest orderRequest = new OrderRequest();
        Order order = new Order();
        order.setStatuteSection(new StatuteSection());
        order.setFilingNumber("tenant-123");
        orderRequest.setOrder(order);
        orderRequest.setRequestInfo(new RequestInfo());
        orderRequest.getRequestInfo().setUserInfo(new User());
        orderRequest.getRequestInfo().getUserInfo().setUuid(UUID.randomUUID().toString());

        return orderRequest;
    }

    @Test
    void testEnrichOrderRegistration_Success() {
        // Given
        OrderRequest orderRequest = createMockOrderRequest();  // Ensure mock request is fully initialized
        String mockTenantId = "tenant123";
        String mockOrderId = "ORDER123";  // Mock ID to return
        String mockOrderNumber = "tenant-123" + "-" + mockOrderId;

        // Mock configuration and ID generation utility behavior
        when(configuration.getOrderConfig()).thenReturn("orderConfigValue");  // Mock return for getOrderConfig
        when(configuration.getOrderFormat()).thenReturn("orderFormatValue");  // Mock return for getOrderFormat
        when(idgenUtil.getIdList(any(),any(), any(),any(), eq(1), eq(false)))
                .thenReturn(Collections.singletonList(mockOrderId));  // Return list with one element

        // When
        orderRegistrationEnrichment.enrichOrderRegistration(orderRequest);

        // Then
        // Check that enrichment added correct fields
        assertNotNull(orderRequest.getOrder().getAuditDetails());
        assertNotNull(orderRequest.getOrder().getId());
        assertEquals(mockOrderNumber, orderRequest.getOrder().getOrderNumber());
        assertNotNull(orderRequest.getOrder().getStatuteSection().getId());

        // Verify that the ID generation utility was called
        verify(idgenUtil).getIdList(any(), eq(mockTenantId), any(), any(), eq(1), eq(false));
    }


    @Test
    void testEnrichOrderRegistration_WithoutUserInfo_ShouldDoNothing() {
        // Given
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setRequestInfo(new RequestInfo());
        // When
        orderRegistrationEnrichment.enrichOrderRegistration(orderRequest);

        // Then
        verify(idgenUtil, never()).getIdList(any(), any(), any(), any(), anyInt(), anyBoolean());
    }

    @Test
    void testEnrichOrderRegistration_ThrowsCustomException() {
        // Given
        OrderRequest orderRequest = createMockOrderRequest();

        when(configuration.getOrderConfig()).thenReturn("orderConfig");
        when(configuration.getOrderFormat()).thenReturn("orderFormat");
        when(idgenUtil.getIdList(any(), anyString(), anyString(), anyString(), eq(1), eq(false)))
                .thenThrow(new CustomException("IDGEN_ERROR", "Error generating ID"));

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            orderRegistrationEnrichment.enrichOrderRegistration(orderRequest);
        });
        assertEquals("IDGEN_ERROR", exception.getCode());
        assertEquals("Error generating ID", exception.getMessage());
    }

    @Test
    void testEnrichOrderRegistrationUponUpdate_Success() {
        // Given
        OrderRequest orderRequest = createMockOrderRequest();
        orderRequest.getOrder().setAuditDetails(new org.egov.common.contract.models.AuditDetails());

        // When
        orderRegistrationEnrichment.enrichOrderRegistrationUponUpdate(orderRequest);

        // Then
        assertNotNull(orderRequest.getOrder().getAuditDetails().getLastModifiedTime());
        assertNotNull(orderRequest.getOrder().getAuditDetails().getLastModifiedBy());
    }

    @Test
    void testEnrichOrderRegistrationUponUpdate_ThrowsCustomException() {
        // Given
        OrderRequest orderRequest = new OrderRequest();  // Missing required data

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            orderRegistrationEnrichment.enrichOrderRegistrationUponUpdate(orderRequest);
        });
        assertEquals("ENRICHMENT_EXCEPTION", exception.getCode());
    }
}
