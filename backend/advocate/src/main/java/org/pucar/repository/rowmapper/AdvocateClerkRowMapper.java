package org.pucar.repository.rowmapper;

import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Document;
import org.pucar.web.models.AdvocateClerk;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class AdvocateClerkRowMapper implements ResultSetExtractor<List<AdvocateClerk>> {
    public List<AdvocateClerk> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<String,AdvocateClerk> advocateClerkApplicationMap = new LinkedHashMap<>();
        System.out.println(rs);

        while (rs.next()){
            String uuid = rs.getString("applicationnumber");
            AdvocateClerk advocateClerkApplication = advocateClerkApplicationMap.get(uuid);

            if(advocateClerkApplication == null) {

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
                        .additionalDetails(rs.getString("additionaldetails"))
                        .status(rs.getString("status"))
                        .auditDetails(auditdetails)
                        .build();
            }

            advocateClerkApplicationMap.put(uuid, advocateClerkApplication);
        }
        return new ArrayList<>(advocateClerkApplicationMap.values());
    }

    private void addChildrenToProperty(ResultSet rs, AdvocateClerk advocateClerkApplication)
            throws SQLException {
        addDocumentToApplication(rs, advocateClerkApplication);
    }

    private void addDocumentToApplication(ResultSet rs, AdvocateClerk advocateClerkApplication) throws SQLException {
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
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
