package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.pucar.dristi.web.models.Hearing;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.*;

import static org.pucar.dristi.config.ServiceConstants.ROW_MAPPER_EXCEPTION;

@Component
@Slf4j
public class HearingRowMapper implements ResultSetExtractor<List<Hearing>> {

    /** To map query result to a list of hearing instance
     * @param rs
     * @return list of hearing
     */
    public List<Hearing> extractData(ResultSet rs) {
        Map<String, Hearing> hearingMap = new LinkedHashMap<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            while (rs.next()) {
                String uuid = rs.getString("applicationnumber");
                Hearing hearing = hearingMap.get(uuid);

                if (hearing == null) {
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

//                    hearing = Hearing.builder()
//                            .applicationNumber(rs.getString("applicationnumber"))
//                            .tenantId(rs.getString("tenantid"))
//                            .id(UUID.fromString(rs.getString("id")))
//                            .barRegistrationNumber(rs.getString("barregistrationnumber"))
//                            .organisationID(toUUID(rs.getString("organisationid")))
//                            .individualId(rs.getString("individualid"))
//                            .isActive(rs.getBoolean("isactive"))
//                            .hearingType(rs.getString("hearingtype"))
//                            .status(rs.getString("status"))
//                            .auditDetails(auditdetails)
//                            .build();
                }

                PGobject pgObject = (PGobject) rs.getObject("additionalDetails");
                if(pgObject!=null)
//                    hearing.setAdditionalDetails(objectMapper.readTree(pgObject.getValue()));

                hearingMap.put(uuid, hearing);
            }
        }
        catch (Exception e){
            log.error("Error occurred while processing Hearing ResultSet: {}", e.getMessage());
            throw new CustomException(ROW_MAPPER_EXCEPTION,"Error occurred while processing Hearing ResultSet: "+ e.getMessage());
        }
        return new ArrayList<>(hearingMap.values());
    }
    private UUID toUUID(String toUuid) {
        if(toUuid == null) {
            return null;
        }
        return UUID.fromString(toUuid);
    }

}
