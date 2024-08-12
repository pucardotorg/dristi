package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pucar.dristi.web.models.Order;
import org.postgresql.util.PGobject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderRowMapperTest {

    private OrderRowMapper orderRowMapper;

    private ResultSet resultSet;


    @BeforeEach
    void setUp() {
        orderRowMapper = new OrderRowMapper(new ObjectMapper());
        resultSet = mock(ResultSet.class);
    }

    @Test
    void testExtractData() throws Exception {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("id")).thenReturn("123e4567-e89b-12d3-a456-556642440000");
        when(resultSet.getString("tenantid")).thenReturn("tenant-123");
        when(resultSet.getString("ordernumber")).thenReturn("ORDER-123");
        when(resultSet.getString("hearingnumber")).thenReturn("123e4567-e89b-12d3-a456-556642440001");
        when(resultSet.getString("filingnumber")).thenReturn("FILING-123");
        when(resultSet.getString("status")).thenReturn("Active");
        when(resultSet.getString("createdby")).thenReturn("user-123");
        when(resultSet.getLong("createdtime")).thenReturn(1617187200000L);
        when(resultSet.getString("lastmodifiedby")).thenReturn("user-123");
        when(resultSet.getLong("lastmodifiedtime")).thenReturn(1617187200000L);
        when(resultSet.wasNull()).thenReturn(false);

        PGobject pgObject = new PGobject();
        pgObject.setType("json");
        pgObject.setValue("{\"key\":\"value\"}");
        when(resultSet.getObject("additionaldetails")).thenReturn(pgObject);

        List<Order> orders = orderRowMapper.extractData(resultSet);

        assertNotNull(orders);
        assertEquals(1, orders.size());
        Order order = orders.get(0);
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-556642440000"), order.getId());
        assertEquals("tenant-123", order.getTenantId());
        assertEquals("FILING-123", order.getFilingNumber());
        assertEquals("Active", order.getStatus());
        assertNotNull(order.getAuditDetails());
        assertEquals("user-123", order.getAuditDetails().getCreatedBy());
        assertEquals(1617187200000L, order.getAuditDetails().getCreatedTime());
        assertEquals("user-123", order.getAuditDetails().getLastModifiedBy());
        assertEquals(1617187200000L, order.getAuditDetails().getLastModifiedTime());
        assertNotNull(order.getAdditionalDetails());
    }

    @Test
    void testExtractDataWithException() throws Exception {
        when(resultSet.next()).thenThrow(new SQLException("Test exception"));

        CustomException thrown = assertThrows(CustomException.class, () -> {
            orderRowMapper.extractData(resultSet);
        });

        assertEquals("ROW_MAPPER_EXCEPTION", thrown.getCode());
        assertTrue(thrown.getMessage().contains("Test exception"));
    }
}
