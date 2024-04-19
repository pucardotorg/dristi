package org.pucar.repository.rowmapper;

import org.egov.common.contract.models.AuditDetails;
import org.pucar.web.models.Advocate;
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
public class AdvocateRowMapper  implements ResultSetExtractor<List<Advocate>> {
    public List<Advocate> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<String,Advocate> advocateMap = new LinkedHashMap<>();
        System.out.println(rs);

        while (rs.next()){
            String uuid = rs.getString("applicationNumber");
            Advocate advocate = advocateMap.get(uuid);

            if(advocate == null) {

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

                advocate = Advocate.builder()
                        .applicationNumber(rs.getString("applicationNumber"))
                        .tenantId(rs.getString("tenantId"))
                        .auditDetails(auditdetails)
                        .build();
            }
            advocateMap.put(uuid, advocate);
        }
        return new ArrayList<>(advocateMap.values());
    }
}