package digit.repository.rowmapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import digit.web.models.AdditionalFields;
import digit.web.models.ChannelName;
import digit.web.models.DeliveryStatus;
import digit.web.models.SummonsDelivery;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Slf4j
public class SummonsDeliveryRowMapper implements RowMapper<SummonsDelivery> {

    private final ObjectMapper objectMapper;

    @Autowired
    public SummonsDeliveryRowMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    public SummonsDelivery mapRow(ResultSet rs, int rowNum) throws SQLException {
        SummonsDelivery summonsDelivery = new SummonsDelivery();

        String deliveryStatusStr = rs.getString("delivery_status");
        DeliveryStatus deliveryStatus = deliveryStatusStr !=null ? DeliveryStatus.valueOf(deliveryStatusStr) : null;
        summonsDelivery.setSummonDeliveryId(rs.getString("summons_delivery_id"));
        summonsDelivery.setTaskNumber(rs.getString("task_number"));
        summonsDelivery.setCaseId(rs.getString("case_id"));
        summonsDelivery.setTenantId(rs.getString("tenant_id"));
        summonsDelivery.setDocType(rs.getString("doc_type"));
        summonsDelivery.setDocSubType(rs.getString("doc_sub_type"));
        summonsDelivery.setPartyType(rs.getString("party_type"));
        summonsDelivery.setChannelName(ChannelName.valueOf(rs.getString("channel_name")));
        summonsDelivery.setPaymentFees(rs.getString("payment_fees"));
        summonsDelivery.setPaymentTransactionId(rs.getString("payment_transaction_id"));
        summonsDelivery.setPaymentStatus(rs.getString("payment_status"));

        summonsDelivery.setIsAcceptedByChannel(rs.getBoolean("is_accepted_by_channel"));
        summonsDelivery.setChannelAcknowledgementId(rs.getString("channel_acknowledgement_id"));

        summonsDelivery.setDeliveryRequestDate(rs.getString("delivery_request_date"));
        summonsDelivery.setDeliveryStatus(deliveryStatus);

        AdditionalFields additionalFields = new AdditionalFields();
        try {
            additionalFields = objectMapper.readValue(rs.getString("additional_fields"), AdditionalFields.class);
        } catch (JsonProcessingException e) {
            throw new SQLException(e);
        }
        summonsDelivery.setAdditionalFields(additionalFields);

        AuditDetails auditdetails = AuditDetails.builder()
                .createdBy(rs.getString("created_by"))
                .createdTime(rs.getLong("created_time"))
                .lastModifiedBy(rs.getString("last_modified_by"))
                .lastModifiedTime(rs.getLong("last_modified_time"))
                .build();
        summonsDelivery.setAuditDetails(auditdetails);

        summonsDelivery.setRowVersion(rs.getInt("row_version"));
        return summonsDelivery;
    }
}
