package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.pucar.dristi.web.models.StatuteSection;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.*;

@Component
@Slf4j
public class StatuteSectionRowMapper implements ResultSetExtractor<Map<UUID, StatuteSection>> {
    public Map<UUID, StatuteSection> extractData(ResultSet rs) {
        Map<UUID, StatuteSection> statuteSectionMap = new LinkedHashMap<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            while (rs.next()) {
                String orderId = rs.getString("order_id");
                UUID uuid = UUID.fromString(orderId);

                Long lastModifiedTime = rs.getLong("lastmodifiedtime");

                AuditDetails auditdetails = AuditDetails.builder()
                        .createdBy(rs.getString("createdby"))
                        .createdTime(rs.getLong("createdtime"))
                        .lastModifiedBy(rs.getString("lastmodifiedby"))
                        .lastModifiedTime(lastModifiedTime)
                        .build();
                StatuteSection statuteSection = StatuteSection.builder()
                        .id(UUID.fromString(rs.getString("id")))
                        .tenantId(rs.getString("tenantid"))
                        .sections(stringToList(rs.getString("sections")))
                        .subsections(stringToList(rs.getString("subsections")))
                        .strSubsections(rs.getString("strsubsections"))
                        .strSections(rs.getString("strsections"))
                        .statute(rs.getString("statute"))
                        .auditdetails(auditdetails)
                        .build();

                PGobject pgObject = (PGobject) rs.getObject("additionaldetails");
                if (pgObject != null)
                    statuteSection.setAdditionalDetails(objectMapper.readTree(pgObject.getValue()));

                statuteSectionMap.put(uuid, statuteSection);

            }
        } catch (Exception e) {
            log.error("Error occurred while processing Case ResultSet :: {}", e.toString());
            throw new CustomException("ROW_MAPPER_EXCEPTION", "Error occurred while processing Case ResultSet: " + e.getMessage());
        }
        return statuteSectionMap;
    }

    public List<String> stringToList(String str) {
        List<String> list = new ArrayList<>();
        if (str != null) {
            StringTokenizer st = new StringTokenizer("str", "|");
            while (st.hasMoreTokens()) {
                list.add(st.nextToken());
            }
        }

        return list;
    }

}