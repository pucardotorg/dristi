package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.pucar.dristi.web.models.Attendee;
import org.pucar.dristi.web.models.Hearing;
import org.pucar.dristi.web.models.PresidedBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.*;

import static org.pucar.dristi.config.ServiceConstants.ROW_MAPPER_EXCEPTION;

@Component
@Slf4j
public class HearingRowMapper implements ResultSetExtractor<List<Hearing>> {

    private final ObjectMapper objectMapper;

    @Autowired
    public HearingRowMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    /** To map query result to a list of hearing instance
     * @param rs
     * @return list of hearing
     */
    public List<Hearing> extractData(ResultSet rs) {
        Map<String, Hearing> hearingMap = new LinkedHashMap<>();

        try {
            while (rs.next()) {
                String uuid = rs.getString("id");
                Hearing hearing = hearingMap.get(uuid);

                if (hearing == null) {
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

                    hearing = Hearing.builder()
                            .tenantId(rs.getString("tenantid"))
                            .id(UUID.fromString(rs.getString("id")))
                            .hearingId(rs.getString("hearingid"))
                            .hearingType(rs.getString("hearingtype"))
                            .status(rs.getString("status"))
                            .startTime(rs.getLong("starttime"))
                            .endTime(rs.getLong("endtime"))
                            .vcLink(rs.getString("vclink"))
                            .isActive(rs.getBoolean("isactive"))
                            .notes(rs.getString("notes"))
                            .hearingType(rs.getString("hearingtype"))
                            .courtCaseNumber(rs.getString("courtcasenumber"))
                            .caseReferenceNumber(rs.getString("casereferencenumber"))
                            .cmpNumber(rs.getString("cmpNumber"))
                            .auditDetails(auditdetails)
                            .cnrNumbers(getListFromJson(rs.getString("cnrnumbers")))
                            .filingNumber(getListFromJson(rs.getString("filingnumber")))
                            .applicationNumbers(getListFromJson(rs.getString("applicationnumbers")))
                            .presidedBy(getObjectFromJson(rs.getString("presidedby"), new TypeReference<PresidedBy>() {}))
                            .attendees(getObjectFromJson(rs.getString("attendees"), new TypeReference<List<Attendee>>() {}))
                            .transcript(getListFromJson(rs.getString("transcript")))
                            .build();
                }
                PGobject pgObject = (PGobject) rs.getObject("additionalDetails");
                if(pgObject!=null)
                    hearing.setAdditionalDetails(objectMapper.readTree(pgObject.getValue()));
                hearingMap.put(uuid, hearing);
            }
        }
        catch (Exception e){
            log.error("Error occurred while processing Hearing ResultSet: {}", e.getMessage());
            throw new CustomException(ROW_MAPPER_EXCEPTION,"Error occurred while processing Hearing ResultSet: "+ e.getMessage());
        }
        return new ArrayList<>(hearingMap.values());
    }
    public List<String> getListFromJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            throw new CustomException("Failed to convert JSON to List<String>", e.getMessage());
        }
    }

    public <T> T getObjectFromJson(String json, TypeReference<T> typeRef) {
        if (json == null || json.trim().isEmpty()) {
            try {
                if (typeRef.getType().equals( new TypeReference<List<Attendee>>() {}.getType())) {
                    return objectMapper.readValue("[]", typeRef);
                }
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

}
