package org.pucar.dristi.repository.rowmapper;

import static org.pucar.dristi.config.ServiceConstants.ROW_MAPPER_EXCEPTION;

import java.io.IOException;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.pucar.dristi.web.models.CourtCase;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CaseRowMapper implements ResultSetExtractor<List<CourtCase>> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<CourtCase> extractData(ResultSet rs) {
        Map<String, CourtCase> caseMap = new LinkedHashMap<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            while (rs.next()) {
                String uuid = rs.getString("id");
                CourtCase courtCase = caseMap.get(uuid);

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
                    courtCase = CourtCase.builder()
                            .id(UUID.fromString(rs.getString("id")))
                            .tenantId(rs.getString("tenantid"))
                            .resolutionMechanism(rs.getString("resolutionmechanism"))
                            .caseTitle(rs.getString("casetitle"))
                            .caseDescription(rs.getString("casedescription"))
                            .filingNumber(rs.getString("filingnumber"))
                            .caseNumber(rs.getString("caseNumber"))
                            .cnrNumber(rs.getString("cnrnumber"))
                            .courtCaseNumber(rs.getString("courtcaseNumber"))
                            .accessCode(rs.getString("accesscode"))
                            .outcome(rs.getString("outcome"))
                            .caseType(rs.getString("casetype"))
                            .courtId(rs.getString("courtid"))
                            .benchId(rs.getString("benchid"))
                            .cmpNumber(rs.getString("cmpnumber"))
                            .judgeId(rs.getString("judgeid"))
                            .stage(rs.getString("stage"))
                            .isActive(rs.getBoolean("isactive"))
                            .substage(rs.getString("substage"))
                            .filingDate(rs.getLong("filingdate"))
                            .judgementDate(rs.getLong("judgementdate"))
                            .registrationDate(rs.getLong("registrationdate"))
                            .caseCategory(rs.getString("casecategory"))
                            .natureOfPleading(rs.getString("natureofpleading"))
                            .status(rs.getString("status"))
                            .remarks(rs.getString("remarks"))
                            .auditdetails(auditdetails)
                            .build();
                }

                PGobject pgObject = (PGobject) rs.getObject("additionalDetails");
                if (pgObject != null)
                    courtCase.setAdditionalDetails(objectMapper.readTree(pgObject.getValue()));
                PGobject caseDetailsObject = (PGobject) rs.getObject("casedetails");
                if (caseDetailsObject != null)
                    courtCase.setCaseDetails(objectMapper.readTree(caseDetailsObject.getValue()));

                caseMap.put(uuid, courtCase);
            }
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while processing Case ResultSet :: {}", e.toString());
            throw new CustomException(ROW_MAPPER_EXCEPTION, "Exception occurred while processing Case ResultSet: " + e.getMessage());
        }
        return new ArrayList<>(caseMap.values());
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
    private LocalDate stringToLocalDate(String str) {
        LocalDate localDate = null;
        try {
            if (str != null) {
                DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                localDate = LocalDate.parse(str, pattern);
            }
        } catch (DateTimeParseException e) {
            log.error("Date parsing failed for input: {}", str, e);
            throw new CustomException("DATE_PARSING_FAILED", "Failed to parse date: " + str);
        }
        return localDate;
    }
}
