package com.egov.icops_integrationkerala.repository;

import com.egov.icops_integrationkerala.model.AdditionalFields;
import com.egov.icops_integrationkerala.model.DeliveryStatus;
import com.egov.icops_integrationkerala.model.IcopsTracker;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Slf4j
public class IcopsRowMapper implements RowMapper<IcopsTracker> {

    private final ObjectMapper objectMapper;

    @Autowired
    public IcopsRowMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    @Override
    public IcopsTracker mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            AdditionalFields additionalFields = new AdditionalFields();
            try {
                additionalFields = objectMapper.readValue(rs.getString("additional_details"), AdditionalFields.class);
            } catch (JsonProcessingException e) {
                throw new SQLException(e);
            }
            return IcopsTracker.builder()
                    .processNumber(rs.getString("process_number"))
                    .tenantId(rs.getString("tenant_id"))
                    .taskNumber(rs.getString("task_number"))
                    .taskType(rs.getString("task_type"))
                    .fileStoreId(rs.getString("file_store_id"))
                    .taskDetails(objectMapper.readValue(rs.getString("task_details"), Object.class))
                    .deliveryStatus(DeliveryStatus.valueOf(rs.getString("delivery_status")))
                    .remarks(rs.getString("remarks"))
                    .additionalDetails(additionalFields)
                    .rowVersion(rs.getInt("row_version"))
                    .bookingDate(rs.getString("booking_date"))
                    .receivedDate(rs.getString("received_date"))
                    .acknowledgementId(rs.getString("acknowledgement_id"))
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
