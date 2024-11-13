package digit.repository.rowmapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import digit.web.models.AdditionalFields;
import digit.web.models.ChannelName;
import digit.web.models.DeliveryStatus;
import digit.web.models.SummonsDelivery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SummonsDeliveryRowMapperTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private SummonsDeliveryRowMapper rowMapper;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMapRowSuccess() throws Exception {
        String summonsId = "summons123";
        String taskNumber = "task123";
        String caseId = "case123";
        String tenantId = "tenant123";
        String docType = "docType123";
        String docSubType = "docSubType123";
        String partyType = "partyType123";
        String channelName = "EMAIL";
        String paymentFees = "100";
        String paymentTransactionId = "transaction123";
        String paymentStatus = "SUCCESS";
        boolean isAcceptedByChannel = true;
        String channelAcknowledgementId = "ack123";
        String deliveryRequestDate = "2021-01-01";
        DeliveryStatus deliveryStatus = DeliveryStatus.DELIVERED;
        String additionalFieldsJson = "{\"field\":\"value\"}";
        String createdBy = "user1";
        long createdTime = 1609459200000L; // 2021-01-01
        String lastModifiedBy = "user2";
        long lastModifiedTime = 1609545600000L; // 2021-01-02
        int rowVersion = 1;

        when(resultSet.getString("summons_delivery_id")).thenReturn(summonsId);
        when(resultSet.getString("task_number")).thenReturn(taskNumber);
        when(resultSet.getString("case_id")).thenReturn(caseId);
        when(resultSet.getString("tenant_id")).thenReturn(tenantId);
        when(resultSet.getString("doc_type")).thenReturn(docType);
        when(resultSet.getString("doc_sub_type")).thenReturn(docSubType);
        when(resultSet.getString("party_type")).thenReturn(partyType);
        when(resultSet.getString("channel_name")).thenReturn(channelName);
        when(resultSet.getString("payment_fees")).thenReturn(paymentFees);
        when(resultSet.getString("payment_transaction_id")).thenReturn(paymentTransactionId);
        when(resultSet.getString("payment_status")).thenReturn(paymentStatus);
        when(resultSet.getBoolean("is_accepted_by_channel")).thenReturn(isAcceptedByChannel);
        when(resultSet.getString("channel_acknowledgement_id")).thenReturn(channelAcknowledgementId);
        when(resultSet.getString("delivery_request_date")).thenReturn(deliveryRequestDate);
        when(resultSet.getString("delivery_status")).thenReturn(deliveryStatus.toString());
        when(resultSet.getString("additional_fields")).thenReturn(additionalFieldsJson);
        when(resultSet.getString("created_by")).thenReturn(createdBy);
        when(resultSet.getLong("created_time")).thenReturn(createdTime);
        when(resultSet.getString("last_modified_by")).thenReturn(lastModifiedBy);
        when(resultSet.getLong("last_modified_time")).thenReturn(lastModifiedTime);
        when(resultSet.getInt("row_version")).thenReturn(rowVersion);

        AdditionalFields additionalFields = new AdditionalFields();

        when(objectMapper.readValue(additionalFieldsJson, AdditionalFields.class)).thenReturn(additionalFields);

        SummonsDelivery summonsDelivery = rowMapper.mapRow(resultSet, 1);

        assert summonsDelivery != null;
        assertEquals(summonsId, summonsDelivery.getSummonDeliveryId());
        assertEquals(taskNumber, summonsDelivery.getTaskNumber());
        assertEquals(caseId, summonsDelivery.getCaseId());
        assertEquals(tenantId, summonsDelivery.getTenantId());
        assertEquals(docType, summonsDelivery.getDocType());
        assertEquals(docSubType, summonsDelivery.getDocSubType());
        assertEquals(partyType, summonsDelivery.getPartyType());
        assertEquals(ChannelName.EMAIL, summonsDelivery.getChannelName());
        assertEquals(paymentFees, summonsDelivery.getPaymentFees());
        assertEquals(paymentTransactionId, summonsDelivery.getPaymentTransactionId());
        assertEquals(paymentStatus, summonsDelivery.getPaymentStatus());
        assertEquals(isAcceptedByChannel, summonsDelivery.getIsAcceptedByChannel());
        assertEquals(channelAcknowledgementId, summonsDelivery.getChannelAcknowledgementId());
        assertEquals(deliveryRequestDate, summonsDelivery.getDeliveryRequestDate());
        assertEquals(deliveryStatus, summonsDelivery.getDeliveryStatus());
        assertEquals(additionalFields, summonsDelivery.getAdditionalFields());
        assertEquals(createdBy, summonsDelivery.getAuditDetails().getCreatedBy());
        assertEquals(createdTime, summonsDelivery.getAuditDetails().getCreatedTime());
        assertEquals(lastModifiedBy, summonsDelivery.getAuditDetails().getLastModifiedBy());
        assertEquals(lastModifiedTime, summonsDelivery.getAuditDetails().getLastModifiedTime());
        assertEquals(rowVersion, summonsDelivery.getRowVersion());

        verify(resultSet, times(1)).getString("summons_delivery_id");
        verify(resultSet, times(1)).getString("task_number");
        verify(resultSet, times(1)).getString("case_id");
        verify(resultSet, times(1)).getString("tenant_id");
        verify(resultSet, times(1)).getString("doc_type");
        verify(resultSet, times(1)).getString("doc_sub_type");
        verify(resultSet, times(1)).getString("party_type");
        verify(resultSet, times(1)).getString("channel_name");
        verify(resultSet, times(1)).getString("payment_fees");
        verify(resultSet, times(1)).getString("payment_transaction_id");
        verify(resultSet, times(1)).getString("payment_status");
        verify(resultSet, times(1)).getBoolean("is_accepted_by_channel");
        verify(resultSet, times(1)).getString("channel_acknowledgement_id");
        verify(resultSet, times(1)).getString("delivery_request_date");
        verify(resultSet, times(1)).getString("delivery_status");
        verify(resultSet, times(1)).getString("additional_fields");
        verify(resultSet, times(1)).getString("created_by");
        verify(resultSet, times(1)).getLong("created_time");
        verify(resultSet, times(1)).getString("last_modified_by");
        verify(resultSet, times(1)).getLong("last_modified_time");
        verify(resultSet, times(1)).getInt("row_version");
        verify(objectMapper, times(1)).readValue(additionalFieldsJson, AdditionalFields.class);
    }

    @Test
    void testMapRowJsonProcessingException() throws Exception {
        String additionalFieldsJson = "{\"field\":\"value\"}";

        when(resultSet.getString("additional_fields")).thenReturn(additionalFieldsJson);
        when(objectMapper.readValue(additionalFieldsJson, AdditionalFields.class)).thenThrow(new JsonProcessingException("Error") {});
        when(resultSet.getString("channel_name")).thenReturn("EMAIL");
        SQLException exception = assertThrows(SQLException.class, () -> {
            rowMapper.mapRow(resultSet, 1);
        });

        assertNotNull(exception);
        verify(resultSet, times(1)).getString("additional_fields");
        verify(objectMapper, times(1)).readValue(additionalFieldsJson, AdditionalFields.class);
    }
}
