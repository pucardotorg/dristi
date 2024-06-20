package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.pucar.dristi.web.models.Order;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.*;

@Component
@Slf4j
public class OrderRowMapper implements ResultSetExtractor<List<Order>> {
    public List<Order> extractData(ResultSet rs) {
        Map<String, Order> orderMap = new LinkedHashMap<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            while (rs.next()) {
                String uuid = rs.getString("id");
                Order order = orderMap.get(uuid);

                if (order == null) {
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
                    order = Order.builder()
                            .id(UUID.fromString(rs.getString("id")))
                            .tenantId(rs.getString("tenantid"))
                            .orderNumber(rs.getString("ordernumber"))
                            .hearingNumber(UUID.fromString(rs.getString("hearingnumber")))
                            .cnrNumber(rs.getString("cnrnumber"))
                            .orderCategory(rs.getString("ordercategory"))
                            .isActive(rs.getBoolean("isactive"))
                            .orderType(rs.getString("ordertype"))
                            .comments(rs.getString("comments"))
                            .filingNumber(rs.getString("filingnumber"))
                            .status(rs.getString("status"))
                            .auditDetails(auditdetails)
                            .build();
                }
                PGobject pgObject1 = (PGobject) rs.getObject("applicationnumber");
                if(pgObject1!=null)
                    order.setApplicationNumber(objectMapper.readValue(pgObject1.getValue(),List.class));

                PGobject pgObject2 = (PGobject) rs.getObject("additionaldetails");
                if(pgObject2!=null)
                    order.setAdditionalDetails(objectMapper.readTree(pgObject2.getValue()));

                PGobject pgObject3 = (PGobject) rs.getObject("issuedby");
                if(pgObject3!=null)
                    order.setIssuedBy(objectMapper.readTree(pgObject3.getValue()));

                orderMap.put(uuid, order);
            }
        }
        catch (Exception e){
            log.error("Error occurred while processing order ResultSet :: {}", e.toString());
            throw new CustomException("ROW_MAPPER_EXCEPTION","Error occurred while processing order ResultSet: "+ e.getMessage());
        }
        return new ArrayList<>(orderMap.values());
    }
}