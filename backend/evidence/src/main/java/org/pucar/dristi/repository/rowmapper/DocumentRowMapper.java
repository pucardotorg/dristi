package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.*;


@Component
@Slf4j
public class DocumentRowMapper implements ResultSetExtractor<Map<UUID, Document>> {
    public Map<UUID, Document> extractData(ResultSet rs) {
        Map<UUID, Document> documentMap = new LinkedHashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            while (rs.next()) {
                String artifactId = rs.getString("artifactId");
                UUID uuid = UUID.fromString(artifactId);
                Document document = Document.builder()
                        .id(rs.getString("id"))
                        .documentType(rs.getString("documentType"))
                        .fileStore(rs.getString("filestore"))
                        .documentUid(rs.getString("documentUid"))
                        .build();

                PGobject pgObject = (PGobject) rs.getObject("additionalDetails");
                if (pgObject != null)
                    document.setAdditionalDetails(objectMapper.readTree(pgObject.getValue()));

                documentMap.put(uuid, document);
            }
        } catch (Exception e) {
            log.error("Error occurred while processing document ResultSet: {}", e.getMessage());
            throw new CustomException("ROW_MAPPER_EXCEPTION", "Error occurred while processing document ResultSet: " + e.getMessage());
        }
        return documentMap;
    }
}
