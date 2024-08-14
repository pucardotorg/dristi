package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.pucar.dristi.web.models.AdvocateClerk;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.*;

import static org.pucar.dristi.config.ServiceConstants.ROW_MAPPER_EXCEPTION;

@Component
@Slf4j
public class AdvocateClerkRowMapper implements ResultSetExtractor<List<AdvocateClerk>> {

    /** To map query result to a list of advocate clerk instance
     * @param rs
     * @return list of advocate clerk
     */
    public List<AdvocateClerk> extractData(ResultSet rs) {
        Map<String,AdvocateClerk> advocateClerkApplicationMap = new LinkedHashMap<>();
        log.info("ResultSet: {}", rs);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            while (rs.next()) {
                String uuid = rs.getString("applicationnumber");
                AdvocateClerk advocateClerkApplication = advocateClerkApplicationMap.get(uuid);

                if (advocateClerkApplication == null) {

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

                    advocateClerkApplication = AdvocateClerk.builder()
                            .applicationNumber(rs.getString("applicationnumber"))
                            .tenantId(rs.getString("tenantid"))
                            .id(UUID.fromString(rs.getString("id")))
                            .stateRegnNumber(rs.getString("stateregnnumber"))
                            .individualId(rs.getString("individualid"))
                            .isActive(rs.getBoolean("isactive"))
                            .status(rs.getString("status"))
                            .auditDetails(auditdetails)
                            .build();
                }

                PGobject pgObject = (PGobject) rs.getObject("additionaldetails");
                if(pgObject!=null)
                    advocateClerkApplication.setAdditionalDetails(objectMapper.readTree(pgObject.getValue()));

                advocateClerkApplicationMap.put(uuid, advocateClerkApplication);
            }
        } catch(CustomException e){
            throw e;
        }
        catch (Exception e){
            log.error("Error occurred while processing clerk ResultSet :: {}", e.toString());
            throw new CustomException(ROW_MAPPER_EXCEPTION,"Exception occurred while processing clerk ResultSet: "+ e.getMessage());
        }
        return new ArrayList<>(advocateClerkApplicationMap.values());
    }

    public void addDocumentToApplication(ResultSet rs, AdvocateClerk advocateClerkApplication) {
        List<Document> listDocument = new ArrayList<>();
        try {
            Document document = Document.builder()
                    .id(rs.getString("aid"))
                    .documentType(rs.getString("document_type"))
                    .fileStore(rs.getString("filestore"))
                    .documentUid(rs.getString("document_uid"))
                    .additionalDetails(rs.getObject("additional_details"))
                    .build();
            listDocument.add(document);

            advocateClerkApplication.setDocuments(listDocument);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while adding document to application :: {}", e.toString());
            throw new CustomException(ROW_MAPPER_EXCEPTION, "Exception occurred while adding document to application: " + e.getMessage());
        }
    }

}
