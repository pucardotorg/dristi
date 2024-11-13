package com.egov.icops_integrationkerala.repository;

import com.egov.icops_integrationkerala.model.AdditionalFields;
import com.egov.icops_integrationkerala.model.DeliveryStatus;
import com.egov.icops_integrationkerala.model.IcopsTracker;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IcopsRowMapperTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private IcopsRowMapper icopsRowMapper;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMapRowSuccess() throws Exception {
        String processNumber = "process123";
        String tenantId = "tenant123";
        String taskNumber = "task123";
        String taskType = "taskType123";
        String fileStoreId = "fileStore123";
        String taskDetails = "{\"detail\":\"value\"}";
        String remarks = "remarks123";
        String additionalDetails = "{\"field\":\"value\"}";
        int rowVersion = 1;
        String bookingDate = "2021-01-01";
        String receivedDate = "2021-01-02";
        String acknowledgementId = "ack123";

        when(resultSet.getString("process_number")).thenReturn(processNumber);
        when(resultSet.getString("tenant_id")).thenReturn(tenantId);
        when(resultSet.getString("task_number")).thenReturn(taskNumber);
        when(resultSet.getString("task_type")).thenReturn(taskType);
        when(resultSet.getString("file_store_id")).thenReturn(fileStoreId);
        when(resultSet.getString("task_details")).thenReturn(taskDetails);
        when(resultSet.getString("delivery_status")).thenReturn(DeliveryStatus.DELIVERED.name());
        when(resultSet.getString("remarks")).thenReturn(remarks);
        when(resultSet.getString("additional_details")).thenReturn(additionalDetails);
        when(resultSet.getInt("row_version")).thenReturn(rowVersion);
        when(resultSet.getString("booking_date")).thenReturn(bookingDate);
        when(resultSet.getString("received_date")).thenReturn(receivedDate);
        when(resultSet.getString("acknowledgement_id")).thenReturn(acknowledgementId);

        AdditionalFields additionalFields = new AdditionalFields();

        when(objectMapper.readValue(additionalDetails, AdditionalFields.class)).thenReturn(additionalFields);
        when(objectMapper.readValue(taskDetails, Object.class)).thenReturn(new Object());

        IcopsTracker icopsTracker = icopsRowMapper.mapRow(resultSet, 1);

        assert icopsTracker != null;
        assertEquals(processNumber, icopsTracker.getProcessNumber());
        assertEquals(tenantId, icopsTracker.getTenantId());
        assertEquals(taskNumber, icopsTracker.getTaskNumber());
        assertEquals(taskType, icopsTracker.getTaskType());
        assertEquals(fileStoreId, icopsTracker.getFileStoreId());
        assertNotNull(icopsTracker.getTaskDetails());
        assertEquals(remarks, icopsTracker.getRemarks());
        assertEquals(additionalFields, icopsTracker.getAdditionalDetails());
        assertEquals(rowVersion, icopsTracker.getRowVersion());
        assertEquals(bookingDate, icopsTracker.getBookingDate());
        assertEquals(receivedDate, icopsTracker.getReceivedDate());
        assertEquals(acknowledgementId, icopsTracker.getAcknowledgementId());

        verify(resultSet, times(1)).getString("process_number");
        verify(resultSet, times(1)).getString("tenant_id");
        verify(resultSet, times(1)).getString("task_number");
        verify(resultSet, times(1)).getString("task_type");
        verify(resultSet, times(1)).getString("file_store_id");
        verify(resultSet, times(1)).getString("task_details");
        verify(resultSet, times(1)).getString("delivery_status");
        verify(resultSet, times(1)).getString("additional_details");
        verify(resultSet, times(1)).getString("booking_date");
        verify(resultSet, times(1)).getString("received_date");
        verify(resultSet, times(1)).getString("acknowledgement_id");
    }

    @Test
    void testMapRowJsonProcessingException() throws Exception {
        String additionalDetails = "{\"field\":\"value\"}";

        when(resultSet.getString("additional_details")).thenReturn(additionalDetails);
        when(objectMapper.readValue(additionalDetails, AdditionalFields.class)).thenThrow(new JsonProcessingException("Error") {});

        SQLException exception = assertThrows(SQLException.class, () -> {
            icopsRowMapper.mapRow(resultSet, 1);
        });

        assertNotNull(exception);
        verify(resultSet, times(1)).getString("additional_details");
        verify(objectMapper, times(1)).readValue(additionalDetails, AdditionalFields.class);
    }
}
