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
        try {
            Document document = Document.builder()
                .id(rs.getString(11))
                .documentType(rs.getString(12))
                .fileStore(rs.getString(13))
                .documentUid(rs.getString(14))
                .additionalDetails(rs.getObject(15))
                    .build();
            listDocument.add(document);

            advocateClerkApplication.setDocuments(listDocument);
        }
        catch (Exception e){
            e.printStackTrace();
        }
//        String test = rs.getString("aaid");
//        String test1 = rs.getString("adocumentyype");
//        String test2 = rs.getString("afilestore");
//        String test3 = rs.getString("documentuid");
//        System.out.println(test);


    }

}
