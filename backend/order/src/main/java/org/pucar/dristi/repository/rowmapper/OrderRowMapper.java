package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.pucar.dristi.web.models.CourtCase;
import org.pucar.dristi.web.models.Order;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Component
@Slf4j
public class OrderRowMapper implements ResultSetExtractor<List<Order>> {
    public List<Order> extractData(ResultSet rs) {
        Map<String, Order> orderMap = new LinkedHashMap<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            while (rs.next()) {
                String uuid = rs.getString("casenumber");
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
                            .hearingNumber(UUID.fromString(rs.getString("hearingnumber")))
                            .orderNumber(rs.getString("orderNumber"))
                            .filingNumber(rs.getString("filingnumber"))
                            .status(rs.getString("status"))
                            .comments(rs.getString("comments"))
                            .auditDetails(auditdetails)
                            .build();
                }

                PGobject pgObject = (PGobject) rs.getObject("additionalDetails");
                if(pgObject!=null)
                    order.setAdditionalDetails(objectMapper.readTree(pgObject.getValue()));

                orderMap.put(uuid, order);
            }
        }
        catch (Exception e){
            log.error("Error occurred while processing Case ResultSet: {}", e.getMessage());
            throw new CustomException("ROW_MAPPER_EXCEPTION","Error occurred while processing Case ResultSet: "+ e.getMessage());
        }
        return new ArrayList<>(orderMap.values());
    }
    private UUID toUUID(String toUuid) {
        if(toUuid == null) {
            return null;
        }
        return UUID.fromString(toUuid);
    }

    private LocalDate stringToLocalDate(String str){
        LocalDate localDate = null;
        if(str!=null)
        try {
            DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            localDate = LocalDate.parse(str, pattern);
        } catch (DateTimeParseException e) {}

        return localDate;
    }
}
