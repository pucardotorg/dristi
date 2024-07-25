package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.pucar.dristi.web.models.Comment;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.*;

@Component
@Slf4j
public class CommentRowMapper implements ResultSetExtractor<Map<UUID, List<Comment>>> {
    public Map<UUID, List<Comment>> extractData(ResultSet rs) {
        Map<UUID, List<Comment>> commentMap = new LinkedHashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            while (rs.next()) {
                String artifactId = rs.getString("artifactId");
                UUID uuid = UUID.fromString(artifactId);

                // Building AuditDetails
                Long lastModifiedTime = rs.getLong("lastModifiedTime");
                if (rs.wasNull()) {
                    lastModifiedTime = null;
                }
                AuditDetails auditDetails = AuditDetails.builder()
                        .createdBy(rs.getString("createdBy"))
                        .createdTime(rs.getLong("createdTime"))
                        .lastModifiedBy(rs.getString("lastModifiedBy"))
                        .lastModifiedTime(lastModifiedTime)
                        .build();

                // Building Comment
                Comment comment = Comment.builder()
                        .id(UUID.fromString(rs.getString("id")))
                        .tenantId(rs.getString("tenantId"))
                        .artifactId(rs.getString("artifactId"))
                        .individualId(rs.getString("individualId"))
                        .comment(rs.getString("comment"))
                        .isActive(rs.getBoolean("isActive"))
                        .auditdetails(auditDetails)
                        .build();

                PGobject pgObject = (PGobject) rs.getObject("additionalDetails");
                if (pgObject != null)
                    comment.setAdditionalDetails(objectMapper.readTree(pgObject.getValue()));

                if (commentMap.containsKey(uuid)) {
                    commentMap.get(uuid).add(comment);
                } else {
                    List<Comment> comments = new ArrayList<>();
                    comments.add(comment);
                    commentMap.put(uuid, comments);
                }
            }
        } catch (Exception e) {
            log.error("Error occurred while processing comment ResultSet: {}", e.getMessage());
            throw new CustomException("ROW_MAPPER_EXCEPTION", "Error occurred while processing comment ResultSet: " + e.getMessage());
        }
        return commentMap;
    }
}
