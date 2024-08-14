package org.pucar.dristi.repository.rowmapper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.pucar.dristi.web.models.LinkedCase;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LinkedCaseRowMapper implements ResultSetExtractor<Map<UUID, List<LinkedCase>>> {

    public Map<UUID, List<LinkedCase>> extractData(ResultSet rs) {

        Map<UUID, List<LinkedCase>> linkedCaseMap = new LinkedHashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            while (rs.next()) {
                String id = rs.getString("case_id");
                UUID uuid = UUID.fromString(id != null ? id : "00000000-0000-0000-0000-000000000000");

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

                LinkedCase linkedCase = LinkedCase.builder()
                        .id(UUID.fromString(rs.getString("id")))
                        .relationshipType(rs.getString("relationshiptype"))
                        .caseNumber(rs.getString("casenumbers"))
                        .isActive(rs.getBoolean("isactive"))
                        .auditdetails(auditdetails)
                        .build();

                PGobject pgObject = (PGobject) rs.getObject("additionalDetails");
                if (pgObject != null)
                    linkedCase.setAdditionalDetails(objectMapper.readTree(pgObject.getValue()));

                if (linkedCaseMap.containsKey(uuid)) {
                    linkedCaseMap.get(uuid).add(linkedCase);
                } else {
                    List<LinkedCase> linkedCaseList = new ArrayList<>();
                    linkedCaseList.add(linkedCase);
                    linkedCaseMap.put(uuid, linkedCaseList);
                }

            }
        } catch(CustomException e){
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while processing Case ResultSet :: {}", e.toString());
            throw new CustomException("ROW_MAPPER_EXCEPTION", "Exception occurred while processing Case ResultSet: " + e.getMessage());
        }
        return linkedCaseMap;
    }
}
