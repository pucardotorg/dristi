package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import org.pucar.dristi.web.models.Artifact;

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
                            .applicableTo(Collections.singletonList(rs.getString("applicableTo")))
                            .createdDate(rs.getInt("createdDate"))
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
}

