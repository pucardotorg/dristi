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
public class DocumentRowMapper implements ResultSetExtractor<Document> {

    @Override
    public Document extractData(ResultSet rs) {
        Document document = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            while (rs.next()) {
                String id = rs.getString("artifactId");
                if (document == null) {
                    document = Document.builder()
                            .id(rs.getString("id"))
                            .fileStore(rs.getString("fileStore"))
                            .documentUid(rs.getString("documentUid"))
                            .documentType(rs.getString("documentType"))
                            .build();
                }

                PGobject additionalDetailsObject = (PGobject) rs.getObject("additionalDetails");
                if (additionalDetailsObject != null) {
                    document.setAdditionalDetails(objectMapper.readTree(additionalDetailsObject.getValue()));
                }
            }
        } catch (Exception e) {
            log.error("Error occurred while processing evidence document ResultSet: {}", e.getMessage());
            throw new CustomException("ROW_MAPPER_EXCEPTION", "Error occurred while processing evidence document ResultSet: " + e.getMessage());
        }
        return document;
    }
}

