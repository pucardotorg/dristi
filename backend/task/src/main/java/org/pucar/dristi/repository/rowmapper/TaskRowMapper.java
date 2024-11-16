package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.pucar.dristi.web.models.AssignedTo;
import org.pucar.dristi.web.models.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.*;

import static org.pucar.dristi.config.ServiceConstants.ROW_MAPPER_EXCEPTION;

@Component
@Slf4j
public class TaskRowMapper implements ResultSetExtractor<List<Task>> {

    private final ObjectMapper objectMapper;

    @Autowired
    public TaskRowMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<Task> extractData(ResultSet rs) {
        Map<String, Task> taskMap = new LinkedHashMap<>();

        try {
            while (rs.next()) {
                String uuid = rs.getString("id");
                Task task = taskMap.get(uuid);

                if (task == null) {
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

                    task = Task.builder()
                            .id(UUID.fromString(rs.getString("id")))
                            .tenantId(rs.getString("tenantid"))
                            .orderId(rs.getString("orderid")==null? null:UUID.fromString(rs.getString("orderid")))
                            .filingNumber(rs.getString("filingnumber"))
                            .taskNumber(rs.getString("tasknumber"))
                            .cnrNumber(rs.getString("cnrnumber"))
                            .createdDate(rs.getLong("createddate"))
                            .dateCloseBy(rs.getLong("datecloseby"))
                            .dateClosed(rs.getLong("dateclosed"))
                            .taskDescription(rs.getString("taskdescription"))
                            .taskDetails(objectMapper.readValue(rs.getString("taskdetails"), Object.class))
                            .taskType(rs.getString("tasktype"))
                            .status(rs.getString("status"))
                            .referenceId(rs.getString("referenceId"))
                            .state(rs.getString("state"))
                            .assignedTo(getListFromJson(rs.getString("assignedto"), new TypeReference<List<AssignedTo>>(){}))
                            .isActive(Boolean.valueOf(rs.getString("isactive")))
                            .auditDetails(auditdetails)
                            .build();
                }

                PGobject pgObject = (PGobject) rs.getObject("additionaldetails");
                if (pgObject != null)
                    task.setAdditionalDetails(objectMapper.readTree(pgObject.getValue()));

                taskMap.put(uuid, task);
            }
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while processing task ResultSet :: {}", e.toString());
            throw new CustomException(ROW_MAPPER_EXCEPTION, "Error occurred while processing Task ResultSet: " + e.getMessage());
        }
        return new ArrayList<>(taskMap.values());
    }

    public <T> T getObjectFromJson(String json, TypeReference<T> typeRef) {
        if (json == null || json.trim().isEmpty()) {
            try {
                return objectMapper.readValue("{}", typeRef); // Return an empty object of the specified type
            } catch (IOException e) {
                throw new CustomException("Failed to create an empty instance of " + typeRef.getType(), e.getMessage());
            }
        }
        try {
            return objectMapper.readValue(json, typeRef);
        } catch (Exception e) {
            throw new CustomException("Failed to convert JSON to " + typeRef.getType(), e.getMessage());
        }
    }

    public <T> List<T> getListFromJson(String jsonString, TypeReference<List<T>> typeReference) {

        if (jsonString == null || jsonString.trim().isEmpty()) {
            return Collections.emptyList(); // Return an empty list if the input is null or empty
        }
        try {
            return objectMapper.readValue(jsonString, typeReference);
        } catch (Exception e) {
            throw new CustomException("Failed to convert JSON to " + typeReference.getType(), e.getMessage());
        }
    }
}
