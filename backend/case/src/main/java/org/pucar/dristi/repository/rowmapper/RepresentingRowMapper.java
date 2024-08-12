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
import org.pucar.dristi.web.models.Party;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RepresentingRowMapper implements ResultSetExtractor<Map<UUID, List<Party>>> {
    public Map<UUID, List<Party>> extractData(ResultSet rs) {
        Map<UUID, List<Party>> partyMap = new LinkedHashMap<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            while (rs.next()) {
                String id = rs.getString("representative_id");
                UUID uuid = UUID.fromString(id != null ? id : "00000000-0000-0000-0000-000000000000");

                Long lastModifiedTime = rs.getLong("lastmodifiedtime");

                AuditDetails auditdetails = AuditDetails.builder()
                        .createdBy(rs.getString("createdby"))
                        .createdTime(rs.getLong("createdtime"))
                        .lastModifiedBy(rs.getString("lastmodifiedby"))
                        .lastModifiedTime(lastModifiedTime)
                        .build();
                Party party = Party.builder()
                        .id(UUID.fromString(rs.getString("id")))
                        .tenantId(rs.getString("tenantid"))
                        .partyCategory(rs.getString("partycategory"))
                        .individualId(rs.getString("individualid"))
                        .organisationID(rs.getString("organisationid"))
                        .partyType(rs.getString("partytype"))
                        .isActive(rs.getBoolean("isactive"))
                        .caseId(rs.getString("case_id"))
                        .auditDetails(auditdetails)
                        .build();


                PGobject pgObject = (PGobject) rs.getObject("additionalDetails");
                if (pgObject != null)
                    party.setAdditionalDetails(objectMapper.readTree(pgObject.getValue()));

                if (partyMap.containsKey(uuid)) {
                    partyMap.get(uuid).add(party);
                } else {
                    List<Party> parties = new ArrayList<>();
                    parties.add(party);
                    partyMap.put(uuid, parties);
                }
            }
        } catch(CustomException e){
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while processing Case ResultSet :: {}", e.toString());
            throw new CustomException("ROW_MAPPER_EXCEPTION", "Exception occurred while processing Case ResultSet: " + e.getMessage());
        }
        return partyMap;
    }
}
