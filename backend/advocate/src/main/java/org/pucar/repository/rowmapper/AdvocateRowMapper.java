package org.pucar.repository.rowmapper;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.pucar.web.models.Advocate;
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
                String uuid = rs.getString("aapplicationnumber");
                Advocate advocate = advocateMap.get(uuid);

                if (advocate == null) {
                    Long lastModifiedTime = rs.getLong("alastModifiedTime");
                    if (rs.wasNull()) {
                        lastModifiedTime = null;
                    }


                    AuditDetails auditdetails = AuditDetails.builder()
                            .createdBy(rs.getString("acreatedBy"))
                            .createdTime(rs.getLong("acreatedTime"))
                            .lastModifiedBy(rs.getString("alastModifiedBy"))
                            .lastModifiedTime(lastModifiedTime)
                            .build();

                    advocate = Advocate.builder()
                            .applicationNumber(rs.getString("aapplicationnumber"))
                            .tenantId(rs.getString("atenantid"))
                            .id(UUID.fromString(rs.getString("aid")))
                            .barRegistrationNumber(rs.getString("abarRegistrationNumber"))
                            .organisationID(UUID.fromString(rs.getString("aorganisationID")))
                            .individualId(rs.getString("aindividualId"))
                            .isActive(Boolean.valueOf(rs.getString("aisActive")))
                            .additionalDetails(rs.getString("aadditionalDetails"))
                            .auditDetails(auditdetails)
                            .build();
                }
                advocateMap.put(uuid, advocate);
            }
        } catch (SQLException e) {
            log.error("Error occurred while processing ResultSet: {}", e.getMessage());
            throw new RuntimeException("Error occurred while processing ResultSet", e);
        }

        return new ArrayList<>(advocateMap.values());
    }

}
