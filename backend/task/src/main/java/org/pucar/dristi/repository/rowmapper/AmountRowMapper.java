package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.pucar.dristi.web.models.Amount;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.*;

@Component
@Slf4j
public class AmountRowMapper implements ResultSetExtractor<Map<UUID, Amount>> {
    public Map<UUID, Amount> extractData(ResultSet rs) {
        Map<UUID, Amount> amountMap = new LinkedHashMap<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            while (rs.next()) {
                String id = rs.getString("task_id");
                UUID uuid = UUID.fromString(id!=null ? id : "00000000-0000-0000-0000-000000000000");

                Amount amount = Amount.builder()
                        .id(UUID.fromString(rs.getString("id")))
                        .type(rs.getString("type"))
                        .status(rs.getString("status"))
                        .amount(rs.getString("amount"))
                        .paymentRefNumber(rs.getString("paymentrefnumber"))
                        .build();

                PGobject pgObject = (PGobject) rs.getObject("additionalDetails");
                if (pgObject != null)
                    amount.setAdditionalDetails(objectMapper.readTree(pgObject.getValue()));

                amountMap.get(uuid);
            }
        } catch(CustomException e){
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while processing Task ResultSet :: {}", e.toString());
            throw new CustomException("ROW_MAPPER_EXCEPTION", "Error occurred while processing Task ResultSet: " + e.getMessage());
        }
        return amountMap;
    }

}
