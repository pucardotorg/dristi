package org.pucar.dristi.repository.rowMapper;

import com.fasterxml.jackson.core.type.TypeReference;
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

import static org.pucar.dristi.config.ServiceConstants.ROW_MAPPER_EXCEPTION;
import static org.pucar.dristi.config.ServiceConstants.STATUTE_ROW_MAPPER_EXCEPTION;

@Component
@Slf4j
public class StatuteSectionRowMapper implements ResultSetExtractor<Map<UUID, StatuteSection>> {
    public Map<UUID, StatuteSection> extractData(ResultSet rs) {
        Map<UUID, StatuteSection> statuteSectionMap = new LinkedHashMap<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            while (rs.next()) {
                String applicationId = rs.getString("application_id");
                UUID uuid = UUID.fromString(applicationId!=null ? applicationId : "00000000-0000-0000-0000-000000000000");

                Long lastModifiedTime = rs.getLong("lastmodifiedtime");

                AuditDetails auditdetails = AuditDetails.builder()
                        .createdBy(rs.getString("createdby"))
                        .createdTime(rs.getLong("createdtime"))
                        .lastModifiedBy(rs.getString("lastmodifiedby"))
                        .lastModifiedTime(lastModifiedTime)
                        .build();
                List<String> sections = stringToList(rs.getString("sections"));
                List<String> subsections = stringToList(rs.getString("subsections"));
                StatuteSection statuteSection = StatuteSection.builder()
                        .id(UUID.fromString(rs.getString("id")))
                        .tenantId(rs.getString("tenantid"))
                        .sections(sections)
                        .subsections(subsections)
                        .strSubsections(rs.getString("strsubsections"))
                        .strSections(rs.getString("strsections"))
                        .statute(rs.getString("statute"))
                        .auditdetails(auditdetails)
                        .build();

                PGobject pgObject = (PGobject) rs.getObject("additionaldetails");
                if (pgObject != null)
                    statuteSection.setAdditionalDetails(objectMapper.readTree(pgObject.getValue()));

                if (!statuteSectionMap.containsKey(uuid) ) {
                    statuteSectionMap.put(uuid,statuteSection);
                }
            }
        } catch (Exception e) {
            log.error("Error occurred while processing Application ResultSet: {}", e.getMessage());
            throw new CustomException(STATUTE_ROW_MAPPER_EXCEPTION, "Error occurred while processing Application ResultSet: " + e.getMessage());
        }
        return statuteSectionMap;
    }
    public List<String> stringToList(String str) {
        ObjectMapper objectMapper = new ObjectMapper();

        List<String> list = new ArrayList<>();
        try {
            if (str != null) {
                list = objectMapper.readValue(str, new TypeReference<List<String>>() {});
            }
        } catch (Exception e) {
            log.error("Error occurred while converting string to list: {}", e.getMessage());
        }
        return list;
    }

}