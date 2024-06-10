package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.pucar.dristi.web.models.Task;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Component
@Slf4j
public class TaskRowMapper implements ResultSetExtractor<List<Task>> {
    public List<Task> extractData(ResultSet rs) {
        Map<String, Task> caseMap = new LinkedHashMap<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            while (rs.next()) {
                String uuid = rs.getString("casenumber");
                Task courtCase = caseMap.get(uuid);

                if (courtCase == null) {
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
                    courtCase = Task.builder()
                            .id(UUID.fromString(rs.getString("id")))
                            .tenantId(rs.getString("tenantid"))
                            .filingNumber(rs.getString("filingnumber"))
                            .status(rs.getString("status"))
                            .auditDetails(auditdetails)
                            .build();
                }

                PGobject pgObject = (PGobject) rs.getObject("additionalDetails");
                if(pgObject!=null)
                    courtCase.setAdditionalDetails(objectMapper.readTree(pgObject.getValue()));

                caseMap.put(uuid, courtCase);
            }
        } catch(CustomException e){
            throw e;
        } catch (Exception e){
            log.error("Error occurred while processing Case ResultSet: {}", e.getMessage());
            throw new CustomException("ROW_MAPPER_EXCEPTION","Error occurred while processing Case ResultSet: "+ e.getMessage());
        }
        return new ArrayList<>(caseMap.values());
    }

    private LocalDate stringToLocalDate(String str){
        LocalDate localDate = null;
        if(str!=null)
        try {
            DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            localDate = LocalDate.parse(str, pattern);
        } catch (DateTimeParseException e) {}

        return localDate;
    }
}
