package digit.repository.rowmapper;

import digit.models.coremodels.AuditDetails;
import digit.web.models.ScheduleHearing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class HearingRowMapper implements RowMapper<ScheduleHearing> {
    @Override
    public ScheduleHearing mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        ScheduleHearing hearing = ScheduleHearing.builder()
                .description(resultSet.getString("description"))
                .hearingBookingId(resultSet.getString("hearing_booking_id"))
                .tenantId(resultSet.getString("tenant_id"))
                .courtId(resultSet.getString("court_id"))
                .judgeId(resultSet.getString("judge_id"))
                .hearingType(resultSet.getString("hearing_type"))
                .caseId(resultSet.getString("case_id"))
                .title(resultSet.getString("title"))
                .status(resultSet.getString("status"))
                .hearingDate(resultSet.getLong("hearing_date"))
                .startTime(Long.parseLong(resultSet.getString("start_time")))
                .endTime(Long.parseLong(resultSet.getString("end_time")))
                .rescheduleRequestId(resultSet.getString("reschedule_request_id"))
                .auditDetails(AuditDetails.builder()
                        .createdBy(resultSet.getString("created_by"))
                        .createdTime(resultSet.getLong("created_time"))
                        .lastModifiedBy(resultSet.getString("last_modified_by"))
                        .lastModifiedTime(resultSet.getLong("last_modified_time"))
                        .build())
                .rowVersion(resultSet.getInt("row_version")).build();
        return hearing;
    }
}
