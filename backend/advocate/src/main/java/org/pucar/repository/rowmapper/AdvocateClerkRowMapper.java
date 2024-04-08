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
            String uuid = rs.getString("aapplicationnumber");
            AdvocateClerk advocateClerkApplication = advocateClerkApplicationMap.get(uuid);

            if(advocateClerkApplication == null) {

                Long lastModifiedTime = rs.getLong("alastmodifiedtime");
                if (rs.wasNull()) {
                    lastModifiedTime = null;
                }

                AuditDetails auditdetails = AuditDetails.builder()
                        .createdBy(rs.getString("acreatedby"))
                        .createdTime(rs.getLong("acreatedtime"))
                        .lastModifiedBy(rs.getString("alastmodifiedby"))
                        .lastModifiedTime(lastModifiedTime)
                        .build();

                advocateClerkApplication = AdvocateClerk.builder()
                        .applicationNumber(rs.getString("aapplicationnumber"))
                        .tenantId(rs.getString("atenantid"))
                        .id(UUID.fromString(rs.getString("aid")))
                        .stateRegnNumber(rs.getString("stateregnnumber"))
                        .individualId(rs.getString("aindividualid"))
                        .isActive(rs.getBoolean("aisactive"))
                        .additionalDetails(rs.getString("aadditionaldetails"))
                        .auditDetails(auditdetails)
                        .build();
            }
            addChildrenToProperty(rs, advocateClerkApplication);
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
        Document document = Document.builder()
                .id(rs.getString("aid"))
                .documentType(rs.getString("documentyype"))
                .fileStore(rs.getString("filestore"))
                .documentUid(rs.getString("documentuid"))
                .additionalDetails(rs.getDouble("additionaldetails"))
                .build();
        listDocument.add(document);

        advocateClerkApplication.setDocuments(listDocument);

    }

}
