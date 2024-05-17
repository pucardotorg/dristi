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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static org.pucar.dristi.config.ServiceConstants.ROW_MAPPER_EXCEPTION;

@Component
@Slf4j
public class ApplicationRowMapper implements ResultSetExtractor<Application> {
    public Application extractData(ResultSet rs) throws SQLException {
        Map<String, Application> applicationMap = new LinkedHashMap<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
//            while (rs.next()) {
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

                    application = Application.builder() //FIXME ADD ALL THE FIELDS
                            .applicationType(new ArrayList<>())
                            .applicationNumber(rs.getString("applicationnumber"))
                            .tenantId(rs.getString("tenantid"))
                            .id(UUID.fromString(rs.getString("id")))
                            .isActive(rs.getBoolean("isactive"))
                            .status(rs.getString("status"))
                            .auditDetails(auditdetails)
                            .build();
                }

                String additionalDetails = rs.getString("additionalDetails");
                if(additionalDetails!=null)
                    application.setAdditionalDetails(additionalDetails);

                applicationMap.put(uuid, application);
//            }
        }
        catch (Exception e){
            log.error("Error occurred while processing Application ResultSet: {}", e.getMessage());
            throw new CustomException(ROW_MAPPER_EXCEPTION,"Error occurred while processing Application ResultSet: "+ e.getMessage());
        }
        return applicationMap.get(rs.getString("id"));
    }
    private UUID toUUID(String toUuid) {
        if(toUuid == null) {
            return null;
        }
        return UUID.fromString(toUuid);
    }
}
