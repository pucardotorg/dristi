package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.pucar.dristi.web.models.CourtCase;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.*;

@Component
@Slf4j
public class CaseRowMapper implements ResultSetExtractor<List<CourtCase>> {
    public List<CourtCase> extractData(ResultSet rs) {
        Map<String, CourtCase> advocateMap = new LinkedHashMap<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            while (rs.next()) {
                String uuid = rs.getString("casenumber");
                CourtCase courtCase = advocateMap.get(uuid);

                if (courtCase == null) {
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
                    courtCase = CourtCase.builder()
                            .caseNumber(rs.getString("caseNumber"))
                            .tenantId(rs.getString("tenantid"))
                            .id(UUID.fromString(rs.getString("id")))
                            .caseCategory(rs.getString("casecategory"))
                            .caseDescription(rs.getString("casedescription"))
                            .courtId(rs.getString("courtid"))
                            .benchId(rs.getString("benchid"))
                            .status(rs.getString("status"))
                            .auditdetails(auditdetails)
                            .build();
                }

                PGobject pgObject = (PGobject) rs.getObject("additionalDetails");
                if(pgObject!=null)
                    courtCase.setAdditionalDetails(objectMapper.readTree(pgObject.getValue()));

                advocateMap.put(uuid, courtCase);
            }
        }
        catch (Exception e){
            log.error("Error occurred while processing Case ResultSet: {}", e.getMessage());
            throw new CustomException("ROW_MAPPER_EXCEPTION","Error occurred while processing Case ResultSet: "+ e.getMessage());
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
