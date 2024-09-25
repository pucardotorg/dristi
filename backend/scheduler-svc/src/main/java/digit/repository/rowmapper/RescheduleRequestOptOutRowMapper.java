package digit.repository.rowmapper;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import digit.models.coremodels.AuditDetails;
import digit.web.models.OptOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Component
public class RescheduleRequestOptOutRowMapper implements RowMapper<OptOut> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public OptOut mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            return OptOut.builder()
                    .id(rs.getString("id"))
                    .judgeId(rs.getString("judge_id"))
                    .caseId(rs.getString("case_id"))
                    .rescheduleRequestId(rs.getString("reschedule_request_id"))
                    .individualId(rs.getString("individual_id"))
                    .optoutDates(rs.getString("opt_out_dates") == null ? null : objectMapper.readValue(rs.getString("opt_out_dates"), new TypeReference<List<Long>>() {
                    }))
                    .rowVersion(rs.getInt("row_version"))
                    .auditDetails(AuditDetails.builder()
                            .createdBy(rs.getString("created_by"))
                            .createdTime(rs.getLong("created_time"))
                            .lastModifiedBy(rs.getString("last_modified_by"))
                            .lastModifiedTime(rs.getLong("last_modified_time"))
                            .build())
                    .tenantId(rs.getString("tenant_id"))
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }
}
