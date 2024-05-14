package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.pucar.dristi.web.models.CourtCase;
import org.pucar.dristi.web.models.Witness;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import static org.pucar.dristi.config.ServiceConstants.ROW_MAPPER_EXCEPTION;

@Component
@Slf4j
public class WitnessRowMapper implements ResultSetExtractor<List<Witness>> {
    public List<Witness> extractData(ResultSet rs) {
        Map<String, Witness> witnessMap = new LinkedHashMap<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            while (rs.next()) {
                String uuid = rs.getString("id");
                Witness witness = witnessMap.get(uuid);

                if (witness == null) {
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
                    witness = witness.builder()
                            .id(UUID.fromString(rs.getString("id")))
                            .caseId(rs.getString("caseid"))
                            .filingNumber(rs.getString("filingnumber"))
                            .cnrNumber(rs.getString("cnrnumber"))
                            .individualId(rs.getString("individualid"))
                            .witnessIdentifier(rs.getString("witnessidentifier"))
                            .remarks(rs.getString("remarks"))
                            .isActive(rs.getBoolean("isactive"))
                            .auditDetails(auditdetails)
                            .build();
                }

                PGobject pgObject = (PGobject) rs.getObject("additionalDetails");
                if(pgObject!=null)
                    witness.setAdditionalDetails(objectMapper.readTree(pgObject.getValue()));

                witnessMap.put(uuid, witness);
            }
        }
        catch (Exception e){
            log.error("Error occurred while processing witness ResultSet: {}", e.getMessage());
            throw new CustomException(ROW_MAPPER_EXCEPTION,"Error occurred while processing witness ResultSet: "+ e.getMessage());
        }
        return new ArrayList<>(witnessMap.values());
    }
}
