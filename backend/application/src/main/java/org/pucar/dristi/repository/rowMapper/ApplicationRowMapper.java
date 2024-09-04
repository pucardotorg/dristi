package org.pucar.dristi.repository.rowMapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.pucar.dristi.web.models.Application;
import org.pucar.dristi.web.models.Comment;
import org.pucar.dristi.web.models.IssuedBy;
import org.pucar.dristi.web.models.StatuteSection;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.pucar.dristi.config.ServiceConstants.JSON_PARSE_ERROR;
import static org.pucar.dristi.config.ServiceConstants.ROW_MAPPER_EXCEPTION;

@Component
@Slf4j
public class ApplicationRowMapper implements ResultSetExtractor<List<Application>> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Application> extractData(ResultSet rs) throws SQLException {
        Map<String, Application> applicationMap = new LinkedHashMap<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            while (rs.next()) {
                String uuid = rs.getString("id");
                Application application = applicationMap.get(uuid);

                if (application == null) {
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

                    application = Application.builder()
                            .applicationNumber(rs.getString("applicationnumber"))
                            .applicationType(rs.getString("applicationtype"))
                            .cnrNumber(rs.getString("cnrnumber"))
                            .caseId(rs.getString("caseid"))
                            .filingNumber(rs.getString("filingnumber"))
                            .referenceId(toUUID(rs.getString("referenceid")))
                            .createdDate(rs.getLong("createddate"))
                            .createdBy(toUUID(rs.getString("applicationcreatedby")))
                            .tenantId(rs.getString("tenantid"))
                            .reasonForApplication(rs.getString("reason_for_application"))
                            .id(toUUID(rs.getString("id")))
                            .isActive(rs.getBoolean("isactive"))
                            .status(rs.getString("status"))
                            .onBehalfOf(getObjectFromJson(rs.getString("onbehalfof"), new TypeReference<List<UUID>>() {
                            }))
                            .comment(getObjectFromJson(rs.getString("comment"), new TypeReference<List<Comment>>() {
                            }))
                            .statuteSection(getObjectFromJson(rs.getString("statuteSection"), new TypeReference<StatuteSection>(){}))
                            .issuedBy(getObjectFromJson(rs.getString("issuedby"), new TypeReference<IssuedBy>(){}))
                            .auditDetails(auditdetails)
                            .build();
                }
                PGobject pgObject1 = (PGobject) rs.getObject("additionalDetails");

                PGobject pgObject2 = (PGobject) rs.getObject("application_details");

                if(pgObject1!=null) {
                    application.setAdditionalDetails(objectMapper.readTree(pgObject1.getValue()));
                }
                if(pgObject2!=null) {
                    application.setApplicationDetails(objectMapper.readTree(pgObject2.getValue()));
                }
                applicationMap.put(uuid, application);
            }
        }
        catch (Exception e){
            log.error("Error occurred while processing Application ResultSet: {}", e.getMessage());
            throw new CustomException(ROW_MAPPER_EXCEPTION,"Error occurred while processing Application ResultSet: "+ e.getMessage());
        }
        return new ArrayList<>(applicationMap.values());
    }

    private UUID toUUID(String toUuid) {
        if(toUuid == null) {
            return null;
        }
        return UUID.fromString(toUuid);
    }

    public <T> T getObjectFromJson(String json, TypeReference<T> typeRef) {
        log.info("Converting JSON to type: {}", typeRef.getType());
        log.info("JSON content: {}", json);

        try {
            if (json == null || json.trim().isEmpty()) {
                if (isListType(typeRef)) {
                    return (T) new ArrayList<>(); // Return an empty list for list types
                } else {
                    return objectMapper.readValue("{}", typeRef); // Return an empty object for other types
                }
            }

            // Attempt to parse the JSON
            return objectMapper.readValue(json, typeRef);
        } catch (IOException e) {
            log.error("Failed to convert JSON to {}", typeRef.getType(), e);
            throw new CustomException("Failed to convert JSON to " + typeRef.getType(), e.getMessage());
        }
    }

    private <T> boolean isListType(TypeReference<T> typeRef) {
        Class<?> rawClass = TypeFactory.defaultInstance().constructType(typeRef.getType()).getRawClass();
        return List.class.isAssignableFrom(rawClass);
    }
    public List<Integer> stringToList(String str) {
        ObjectMapper objectMapper = new ObjectMapper();

        List<Integer> list = new ArrayList<>();
        try {
            if (str != null) {
                list = objectMapper.readValue(str, new TypeReference<List<Integer>>() {});
            }
        } catch (Exception e) {
            log.error("Error occurred while converting string to list: {}", e.getMessage());
            throw new CustomException(JSON_PARSE_ERROR, "Failed to parse application type from JSON: " + e.getMessage());
        }
        return list;
    }
}
