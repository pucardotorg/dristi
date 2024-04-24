package org.pucar.repository.rowmapper;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.pucar.web.models.Advocate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.*;

import static org.pucar.config.ServiceConstants.ROW_MAPPER_EXCEPTION;

@Component
@Slf4j
public class AdvocateRowMapper implements ResultSetExtractor<List<Advocate>> {
    public List<Advocate> extractData(ResultSet rs) {
        Map<String, Advocate> advocateMap = new LinkedHashMap<>();

        try {
            while (rs.next()) {
                String uuid = rs.getString("applicationnumber");
                Advocate advocate = advocateMap.get(uuid);

                if (advocate == null) {
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

                    advocate = Advocate.builder()
                            .applicationNumber(rs.getString("applicationnumber"))
                            .tenantId(rs.getString("tenantid"))
                            .id(UUID.fromString(rs.getString("id")))
                            .barRegistrationNumber(rs.getString("barregistrationnumber"))
                            .organisationID(toUUID(rs.getString("organisationid")))
                            .individualId(rs.getString("individualid"))
                            .isActive(rs.getBoolean("isactive"))
                            .additionalDetails(rs.getObject("additionalDetails"))
                            .advocateType(rs.getString("advocatetype"))
                            .status(rs.getString("status"))
                            .auditDetails(auditdetails)
                            .build();
                }
                advocateMap.put(uuid, advocate);
            }
        }
        catch (Exception e){
            log.error("Error occurred while processing Advocate ResultSet: {}", e.getMessage());
            throw new CustomException(ROW_MAPPER_EXCEPTION,"Error occurred while processing Advocate ResultSet: "+ e.getMessage());
        }
        return new ArrayList<>(advocateMap.values());
    }
    private UUID toUUID(String toUuid) {
        if(toUuid == null) {
            return null;
        }
        return UUID.fromString(toUuid);
    }

}
