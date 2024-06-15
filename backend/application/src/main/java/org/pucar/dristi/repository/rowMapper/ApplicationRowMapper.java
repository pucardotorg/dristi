package org.pucar.dristi.repository.rowMapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.pucar.dristi.web.models.Application;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.pucar.dristi.config.ServiceConstants.ROW_MAPPER_EXCEPTION;

@Component
@Slf4j
public class ApplicationRowMapper implements ResultSetExtractor<List<Application>> {
    public List<Application> extractData(ResultSet rs) throws SQLException {
        Map<String, Application> applicationMap = new LinkedHashMap<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            while (rs.next()) {
                String uuid = rs.getString("id");
                Application application = applicationMap.get(uuid);

                if (application == null) {
                    Long lastModifiedTime = rs.getLong("lastmodifiedtime");
                    if (rs.wasNull()) {
                        lastModifiedTime = null;
                    }


                    AuditDetails auditdetails = AuditDetails.builder()
                            .createdBy(rs.getString("createdby"))
                            .createdTime(rs.getLong("createdtime"))
                            .lastModifiedBy(rs.getString("lastmodifiedby"))
                            .lastModifiedTime(lastModifiedTime)
                            .build();

                    application = Application.builder()
                            .applicationNumber(rs.getString("applicationnumber"))
                            .cnrNumber(rs.getString("cnrnumber"))
                            .caseId(rs.getString("caseid"))
                            .filingNumber(rs.getString("filingnumber"))
                            .referenceId(toUUID(rs.getString("referenceid")))
                            .createdDate(rs.getString("createddate"))
                            .createdBy(toUUID(rs.getString("applicationcreatedby")))
                            .tenantId(rs.getString("tenantid"))
                            .id(toUUID(rs.getString("id")))
                            .isActive(rs.getBoolean("isactive"))
                            .status(rs.getString("status"))
                            .comment(rs.getString("comment"))
                            .additionalDetails(rs.getString("additionaldetails"))
                            .auditDetails(auditdetails)
                            .build();
                }
                    applicationMap.put(uuid, application);
            }
        }
        catch (Exception e){
            log.error("Error occurred while processing Application ResultSet: {}", e.getMessage());
            throw new CustomException(ROW_MAPPER_EXCEPTION,"Error occurred while processing Application ResultSet: "+ e.getMessage());
        }
        return new ArrayList<>(applicationMap.values());
    }
    private UUID toUUID(String toUuid) {
        if(toUuid == null) {
            return null;
        }
        return UUID.fromString(toUuid);
    }
}
