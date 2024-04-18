package org.pucar.repository.rowmapper;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Document;
import org.pucar.web.models.Advocate;
import org.pucar.web.models.AdvocateClerk;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Slf4j
public class AdvocateRowMapper implements ResultSetExtractor<List<Advocate>> {
    public List<Advocate> extractData(ResultSet rs) {
        Map<String, Advocate> advocateMap = new LinkedHashMap<>();

        try {
            while (rs.next()) {
                String uuid = rs.getString("applicationnumber");
                Advocate advocate = advocateMap.get(uuid);

                if (advocate == null) {
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

                    advocate = Advocate.builder()
                            .applicationNumber(rs.getString("applicationnumber"))
                            .tenantId(rs.getString("tenantid"))
                            .id(UUID.fromString(rs.getString("id")))
                            .barRegistrationNumber(rs.getString("barRegistrationNumber"))
                            .organisationID(UUID.fromString(rs.getString("organisationID")))
                            .individualId(rs.getString("individualid"))
                            .isActive(Boolean.valueOf(rs.getString("isActive")))
                            .additionalDetails(rs.getString("additionalDetails"))
                            .advocateType(rs.getString("advocatetype"))
                            .organisationID(UUID.fromString(rs.getString("organisationid")))
                            .auditDetails(auditdetails)
                            .build();
                }
                addChildrenToProperty(rs, advocate);
                advocateMap.put(uuid, advocate);
            }
        } catch (SQLException e) {
            log.error("Error occurred while processing ResultSet: {}", e.getMessage());
            throw new RuntimeException("Error occurred while processing ResultSet", e);
        }

        return new ArrayList<>(advocateMap.values());
    }

    private void addChildrenToProperty(ResultSet rs, Advocate advocate)
            throws SQLException {
        addDocumentToApplication(rs, advocate);
    }

    private void addDocumentToApplication(ResultSet rs, Advocate advocateClerkApplication) throws SQLException {
        List<Document> listDocument = new ArrayList<>();
        try {
            Document document = Document.builder()
                    .id(rs.getString("aid"))
                    .documentType(rs.getString("documenttype"))
                    .fileStore(rs.getString("filestore"))
                    .documentUid(rs.getString("documentuid"))
                    .additionalDetails(rs.getObject("docadditionaldetails"))
                    .build();
            listDocument.add(document);

            advocateClerkApplication.setDocuments(listDocument);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
