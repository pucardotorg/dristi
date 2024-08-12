package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.pucar.dristi.web.models.Comment;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import org.pucar.dristi.web.models.Artifact;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.*;

@Component
@Slf4j
public class EvidenceRowMapper implements ResultSetExtractor<List<Artifact>> {

    @Override
    public List<Artifact> extractData(ResultSet rs) {
        Map<String, Artifact> artifactMap = new LinkedHashMap<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            while (rs.next()) {
                String id = rs.getString("id");
                Artifact artifact = artifactMap.get(id);

                if (artifact == null) {
                    Long lastModifiedTime = rs.getLong("lastmodifiedtime");
                    if (rs.wasNull()) {
                        lastModifiedTime = null;
                    }

                    AuditDetails auditDetails = AuditDetails.builder()
                            .createdBy(rs.getString("createdby"))
                            .createdTime(rs.getLong("createdtime"))
                            .lastModifiedBy(rs.getString("lastmodifiedby"))
                            .lastModifiedTime(lastModifiedTime)
                            .build();

                    artifact = Artifact.builder()
                            .id(UUID.fromString(rs.getString("id")))
                            .tenantId(rs.getString("tenantid"))
                            .artifactNumber(rs.getString("artifactNumber"))
                            .evidenceNumber(rs.getString("evidenceNumber"))
                            .externalRefNumber(rs.getString("externalRefNumber"))
                            .caseId(rs.getString("caseId"))
                            .application(rs.getString("application"))
                            .filingNumber(rs.getString("filingNumber"))
                            .hearing(rs.getString("hearing"))
                            .order(rs.getString("orders"))
                            .mediaType(rs.getString("mediaType"))
                            .artifactType(rs.getString("artifactType"))
                            .sourceType(rs.getString("sourceType"))
                            .sourceID(rs.getString("sourceID"))
                            .sourceName(rs.getString("sourceName"))
                            .applicableTo(getObjectFromJson(rs.getString("applicableTo"), new TypeReference<List<String>>() {
                            }))
                            .comments(getObjectFromJson(rs.getString("comments"), new TypeReference<List<Comment>>() {
                            }))
                            .file(getObjectFromJson(rs.getString("file"), new TypeReference<Document>(){}))
                            .createdDate(rs.getLong("createdDate"))
                            .isActive(rs.getBoolean("isActive"))
                            .isEvidence(rs.getBoolean("isEvidence"))
                            .status(rs.getString("status"))
                            .description(rs.getString("description"))
                            .auditdetails(auditDetails)
                            .build();
                }

                PGobject artifactDetailsObject = (PGobject) rs.getObject("artifactDetails");
                if (artifactDetailsObject != null) {
                    artifact.setArtifactDetails(objectMapper.readTree(artifactDetailsObject.getValue()));
                }

                PGobject additionalDetailsObject = (PGobject) rs.getObject("additionalDetails");
                if (additionalDetailsObject != null) {
                    artifact.setAdditionalDetails(objectMapper.readTree(additionalDetailsObject.getValue()));
                }

                artifactMap.put(id, artifact);
            }
        } catch (Exception e) {
            log.error("Error occurred while processing evidence artifact ResultSet: {}", e.toString());
            throw new CustomException("ROW_MAPPER_EXCEPTION", "Error occurred while processing evidence artifact ResultSet: " + e.toString());
        }
        return new ArrayList<>(artifactMap.values());
    }
    public <T> T getObjectFromJson(String json, TypeReference<T> typeRef) {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("Converting JSON to type: {}", typeRef.getType());
        log.info("JSON content: {}", json);

        try {
            if (json == null || json.trim().isEmpty()) {
                // Handle null or empty JSON
                if (isListType(typeRef)) {
                    return (T) new ArrayList<>(); // Return an empty list for list types
                } else {
                    return objectMapper.readValue("{}", typeRef); // Return an empty object
                }
            }

            // Parse the JSON
            return objectMapper.readValue(json, typeRef);
        } catch (IOException e) {
            log.error("Failed to convert JSON to {}", typeRef.getType(), e);
            throw new CustomException("Failed to convert JSON to " + typeRef.getType(), e.getMessage());
        }
    }

    private <T> boolean isListType(TypeReference<T> typeRef) {
        // Extract the raw type from the TypeReference
        Class<?> rawClass = TypeFactory.defaultInstance().constructType(typeRef.getType()).getRawClass();
        return List.class.isAssignableFrom(rawClass);
    }
}

