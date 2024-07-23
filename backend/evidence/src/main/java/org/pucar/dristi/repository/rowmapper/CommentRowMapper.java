package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import org.pucar.dristi.web.models.Comment;

import java.sql.ResultSet;
import java.util.*;

@Component
@Slf4j
public class CommentRowMapper implements ResultSetExtractor<List<Comment>> {

    @Override
    public List<Comment> extractData(ResultSet rs) {
        Map<String, Comment> commentMap = new LinkedHashMap<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            while (rs.next()) {
                String id = rs.getString("id");
                Comment comment = commentMap.get(id);

                if (comment == null) {
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

                    comment = Comment.builder()
                            .id(UUID.fromString(rs.getString("id")))
                            .tenantId(rs.getString("tenantid"))
                            .artifactId(rs.getString("artifactId"))
                            .individualId(rs.getString("individualId"))
                            .comment(rs.getString("comment"))
                            .isActive(rs.getBoolean("isActive"))
                            .auditdetails(auditDetails)
                            .build();
                }

                PGobject additionalDetailsObject = (PGobject) rs.getObject("additionalDetails");
                if (additionalDetailsObject != null) {
                    comment.setAdditionalDetails(String.valueOf(objectMapper.readTree(additionalDetailsObject.getValue())));
                }

                commentMap.put(id, comment);
            }
        } catch (Exception e) {
            log.error("Error occurred while processing evidence comment ResultSet: {}", e.getMessage());
            throw new CustomException("ROW_MAPPER_EXCEPTION", "Error occurred while processing evidence comment ResultSet: " + e.getMessage());
        }
        return new ArrayList<>(commentMap.values());
    }
}

