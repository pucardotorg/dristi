package digit.repository.rowmapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import digit.models.coremodels.AuditDetails;
import digit.web.models.ReScheduleHearing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;


@Component
@Slf4j
public class ReScheduleHearingRowMapper implements RowMapper<ReScheduleHearing> {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ReScheduleHearing mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        ReScheduleHearing reScheduleHearing = null;
        try {
            reScheduleHearing = ReScheduleHearing.builder()
                    .rescheduledRequestId(resultSet.getString("reschedule_request_id"))
                    .hearingBookingId(resultSet.getString("hearing_booking_id"))
                    .tenantId(resultSet.getString("tenant_id"))
                    .judgeId(resultSet.getString("judge_id"))
                    .caseId(resultSet.getString("case_id"))
                    .requesterId(resultSet.getString("requester_id"))
                    .reason(resultSet.getString("reason"))
                    .status(resultSet.getString("status"))
                    .suggestedDates(resultSet.getString("suggested_days") == null ? null : objectMapper.readValue(resultSet.getString("suggested_days"), new TypeReference<>() {
                    }))
                    .availableDates(resultSet.getString("available_days") == null ? null : objectMapper.readValue(resultSet.getString("available_days"), new TypeReference<>() {
                    }))
                    .litigants(resultSet.getString("litigants") == null ? null : objectMapper.readValue(resultSet.getString("litigants"), new TypeReference<>() {
                    }))
                    .representatives(resultSet.getString("representatives") == null ? null : objectMapper.readValue(resultSet.getString("representatives"), new TypeReference<>() {
                    }))
                    .auditDetails(AuditDetails.builder()
                            .createdBy(resultSet.getString("created_by"))
                            .createdTime(resultSet.getLong("created_time"))
                            .lastModifiedBy(resultSet.getString("last_modified_by"))
                            .lastModifiedTime(resultSet.getLong("last_modified_time"))
                            .build())
                    .rowVersion(resultSet.getInt("row_version")).build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return reScheduleHearing;
    }
}
