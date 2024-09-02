package org.pucar.dristi.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.models.AuditDetails;
import org.junit.jupiter.api.extension.ExtendWith;
import org.pucar.dristi.model.AdditionalFields;
import org.pucar.dristi.model.DeliveryStatus;
import org.pucar.dristi.model.EPostTracker;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EPostRowMapperTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private EPostRowMapper ePostRowMapper;

    @Mock
    private ResultSet resultSet;

    @Test
    void testMapRow() throws SQLException, JsonProcessingException {
        // Arrange
        String deliveryStatusStr = "DELIVERED";
        DeliveryStatus deliveryStatus = DeliveryStatus.valueOf(deliveryStatusStr);
        String additionalDetailsJson = "{\"someField\":\"someValue\"}";
        AdditionalFields additionalFields = new AdditionalFields();

        when(resultSet.getString("delivery_status")).thenReturn(deliveryStatusStr);
        when(resultSet.getString("additional_details")).thenReturn(additionalDetailsJson);
        when(objectMapper.readValue(additionalDetailsJson, AdditionalFields.class)).thenReturn(additionalFields);

        // Mock other columns
        when(resultSet.getString("process_number")).thenReturn("P12345");
        when(resultSet.getString("tenant_id")).thenReturn("tenant1");
        when(resultSet.getString("file_store_id")).thenReturn("fileStoreId1");
        when(resultSet.getString("task_number")).thenReturn("taskNumber1");
        when(resultSet.getString("tracking_number")).thenReturn("trackingNumber1");
        when(resultSet.getString("address")).thenReturn("123 Street");
        when(resultSet.getString("pincode")).thenReturn("123456");
        when(resultSet.getString("remarks")).thenReturn("Remarks");
        when(resultSet.getInt("row_version")).thenReturn(1);
        when(resultSet.getString("booking_date")).thenReturn("2023-01-01");
        when(resultSet.getString("received_date")).thenReturn("2023-01-02");
        when(resultSet.getString("createdBy")).thenReturn("creator");
        when(resultSet.getString("lastModifiedBy")).thenReturn("modifier");
        when(resultSet.getLong("createdTime")).thenReturn(1000L);
        when(resultSet.getLong("lastModifiedTime")).thenReturn(2000L);

        // Act
        EPostTracker result = ePostRowMapper.mapRow(resultSet, 1);

        // Assert
        assertNotNull(result);
        assertEquals("P12345", result.getProcessNumber());
        assertEquals("tenant1", result.getTenantId());
        assertEquals("fileStoreId1", result.getFileStoreId());
        assertEquals("taskNumber1", result.getTaskNumber());
        assertEquals("trackingNumber1", result.getTrackingNumber());
        assertEquals("123 Street", result.getAddress());
        assertEquals("123456", result.getPinCode());
        assertEquals(deliveryStatus, result.getDeliveryStatus());
        assertEquals("Remarks", result.getRemarks());
        assertEquals(additionalFields, result.getAdditionalDetails());
        assertEquals(1, result.getRowVersion());
        assertEquals("2023-01-01", result.getBookingDate());
        assertEquals("2023-01-02", result.getReceivedDate());

        AuditDetails auditDetails = result.getAuditDetails();
        assertNotNull(auditDetails);
        assertEquals("creator", auditDetails.getCreatedBy());
        assertEquals("modifier", auditDetails.getLastModifiedBy());
        assertEquals(1000L, auditDetails.getCreatedTime());
        assertEquals(2000L, auditDetails.getLastModifiedTime());

        verify(resultSet).getString("delivery_status");
        verify(resultSet).getString("additional_details");
        verify(objectMapper).readValue(additionalDetailsJson, AdditionalFields.class);
        // Verify other columns as necessary
    }

    @Test
    void testMapRow_ThrowsException() throws SQLException {
        // Arrange
        when(resultSet.getString("delivery_status")).thenReturn(null);
        when(resultSet.getString("additional_details")).thenThrow(new SQLException("Error reading additional details"));

        // Act & Assert
        assertThrows(SQLException.class, () -> ePostRowMapper.mapRow(resultSet, 1));
    }
}
