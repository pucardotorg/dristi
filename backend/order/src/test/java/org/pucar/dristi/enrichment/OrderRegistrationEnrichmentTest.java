package org.pucar.dristi.enrichment;

import org.egov.common.contract.models.AuditDetails;
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

//    @Test
//    void testEnrichOrderRegistration() {
//        User user = User.builder().uuid(UUID.randomUUID().toString()).build();
//        RequestInfo requestInfo = RequestInfo.builder().userInfo(user).build();
//        Order order = Order.builder().tenantId("tenant-123").statuteSection(new StatuteSection()).build();
//        OrderRequest orderRequest = OrderRequest.builder().requestInfo(requestInfo).order(order).build();
//
//        when(idgenUtil.getIdList(any(), anyString(), anyString(), any(), anyInt(),any()))
//                .thenReturn(Collections.singletonList("ORD-2024-0001"));
//
//        orderRegistrationEnrichment.enrichOrderRegistration(orderRequest);
//
//        assertNotNull(orderRequest.getOrder().getId());
//        assertNotNull(orderRequest.getOrder().getStatuteSection().getId());
//        assertNotNull(orderRequest.getOrder().getAuditDetails());
//        assertEquals("ORD-2024-0001", orderRequest.getOrder().getOrderNumber());
//        assertNotNull(orderRequest.getOrder().getAuditDetails().getCreatedBy());
//        assertNotNull(orderRequest.getOrder().getAuditDetails().getCreatedTime());
//        assertNotNull(orderRequest.getOrder().getAuditDetails().getLastModifiedBy());
//        assertNotNull(orderRequest.getOrder().getAuditDetails().getLastModifiedTime());
//    }

    @Test
    void testEnrichOrderRegistrationUponUpdate() {
        User user = User.builder().uuid(UUID.randomUUID().toString()).build();
        RequestInfo requestInfo = RequestInfo.builder().userInfo(user).build();
        AuditDetails auditDetails = AuditDetails.builder().createdBy(user.getUuid()).createdTime(System.currentTimeMillis()).build();
        Order order = Order.builder().tenantId("tenant-123").auditDetails(auditDetails).build();
        OrderRequest orderRequest = OrderRequest.builder().requestInfo(requestInfo).order(order).build();

        orderRegistrationEnrichment.enrichOrderRegistrationUponUpdate(orderRequest);

        assertNotNull(orderRequest.getOrder().getAuditDetails().getLastModifiedBy());
        assertNotNull(orderRequest.getOrder().getAuditDetails().getLastModifiedTime());
        assertEquals(user.getUuid(), orderRequest.getOrder().getAuditDetails().getLastModifiedBy());
    }

//    @Test
//    void testEnrichOrderRegistrationCustomException() {
//        User user = User.builder().uuid(UUID.randomUUID().toString()).build();
//        RequestInfo requestInfo = RequestInfo.builder().userInfo(user).build();
//        Order order = Order.builder().tenantId("tenant-123").statuteSection(new StatuteSection()).build();
//        OrderRequest orderRequest = OrderRequest.builder().requestInfo(requestInfo).order(order).build();
//
//        when(idgenUtil.getIdList(any(), anyString(), anyString(), any(), anyInt(),any()))
//                .thenThrow(new CustomException("IDGEN_ERROR", "Error generating ID"));
//
//        Exception exception = assertThrows(CustomException.class, () -> {
//            orderRegistrationEnrichment.enrichOrderRegistration(orderRequest);
//        });
//
//        assertEquals("Error generating ID", exception.getMessage());
//    }

    @Test
    void testEnrichOrderRegistrationUponUpdateException() {
        User user = User.builder().uuid(UUID.randomUUID().toString()).build();
        RequestInfo requestInfo = RequestInfo.builder().userInfo(user).build();
        Order order = Order.builder().tenantId("tenant-123").build(); // Missing auditDetails
        OrderRequest orderRequest = OrderRequest.builder().requestInfo(requestInfo).order(order).build();

        CustomException exception = assertThrows(CustomException.class, () -> {
            orderRegistrationEnrichment.enrichOrderRegistrationUponUpdate(orderRequest);
        });

        assertEquals("Error in order enrichment service during order update process: Cannot invoke \"org.egov.common.contract.models.AuditDetails.setLastModifiedTime(java.lang.Long)\" because the return value of \"org.pucar.dristi.web.models.Order.getAuditDetails()\" is null", exception.getMessage());
    }
}
