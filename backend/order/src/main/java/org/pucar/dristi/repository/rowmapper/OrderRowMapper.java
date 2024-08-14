package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.pucar.dristi.web.models.IssuedBy;
import org.pucar.dristi.web.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Component
@Slf4j
public class OrderRowMapper implements ResultSetExtractor<List<Order>> {

    private final ObjectMapper objectMapper;

    @Autowired
    public OrderRowMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<Order> extractData(ResultSet rs) {
        Map<String, Order> orderMap = new LinkedHashMap<>();

        try {
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
                            .linkedOrderNumber(rs.getString("linkedordernumber"))
                            .hearingNumber(rs.getString("hearingnumber"))
                            .cnrNumber(rs.getString("cnrnumber"))
                            .orderCategory(rs.getString("ordercategory"))
                            .isActive(rs.getBoolean("isactive"))
                            .orderType(rs.getString("ordertype"))
                            .createdDate(rs.getLong("createddate"))
                            .comments(rs.getString("comments"))
                            .filingNumber(rs.getString("filingnumber"))
                            .issuedBy(getObjectFromJson(rs.getString("issuedby"), new TypeReference<IssuedBy>() {}))
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

                PGobject pgObject3 = (PGobject) rs.getObject("orderDetails");
                if(pgObject3!=null)
                    order.setOrderDetails(objectMapper.readTree(pgObject3.getValue()));

                orderMap.put(uuid, order);
            }
        }
        catch (Exception e){
            log.error("Error occurred while processing order ResultSet :: {}", e.toString());
            throw new CustomException("ROW_MAPPER_EXCEPTION","Error occurred while processing order ResultSet: "+ e.getMessage());
        }
        return new ArrayList<>(orderMap.values());
    }

    public <T> T getObjectFromJson(String json, TypeReference<T> typeRef) {
        if (json == null || json.trim().isEmpty()) {
            try {
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