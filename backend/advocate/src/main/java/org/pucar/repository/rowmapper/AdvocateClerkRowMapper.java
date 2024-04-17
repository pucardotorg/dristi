package org.pucar.repository.rowmapper;

import org.egov.common.contract.models.AuditDetails;
import org.pucar.web.models.Advocate;
import org.pucar.web.models.AdvocateClerk;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class AdvocateClerkRowMapper  implements ResultSetExtractor<List<AdvocateClerk>> {
    public List<AdvocateClerk> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<String,AdvocateClerk> advocateClerkMap = new LinkedHashMap<>();
        System.out.println(rs);

        while (rs.next()){
            String uuid = rs.getString("applicationNumber");
            AdvocateClerk advocateClerk = advocateClerkMap.get(uuid);

            if(advocateClerk == null) {

                Long lastModifiedTime = rs.getLong("lastModifiedTime");
                if (rs.wasNull()) {
                    lastModifiedTime = null;
                }

                AuditDetails auditdetails = AuditDetails.builder()
                        .createdBy(rs.getString("createdBy"))
                        .createdTime(rs.getLong("createdTime"))
                        .lastModifiedBy(rs.getString("lastModifiedBy"))
                        .lastModifiedTime(lastModifiedTime)
                        .build();

                advocateClerk = AdvocateClerk.builder()
                        .applicationNumber(rs.getString("applicationNumber"))
                        .tenantId(rs.getString("tenantId"))
                        .auditDetails(auditdetails)
                        .build();
            }
            advocateClerkMap.put(uuid, advocateClerk);
        }
        return new ArrayList<>(advocateClerkMap.values());
    }
}