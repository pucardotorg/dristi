package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.pucar.dristi.web.OpenApiCaseSummary;
import org.pucar.dristi.web.models.CaseListLineItem;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Slf4j
public class OpenApiCaseListRowMapper implements ResultSetExtractor<List<CaseListLineItem>> {

    private final ObjectMapper objectMapper;

    public OpenApiCaseListRowMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public List<CaseListLineItem> extractData(ResultSet rs) throws SQLException {

        Map<String, CaseListLineItem> openApiCaseSummaryMap = new HashMap<>();
        Set<String> mappedKey = new HashSet<>();

        while (rs.next()) {
            String caseId = rs.getString("id");
            CaseListLineItem caseListLineItem = openApiCaseSummaryMap.get(caseId);
            if (caseListLineItem == null) {
                caseListLineItem = CaseListLineItem.builder()
                        .caseNumber(getCaseNumber(rs))
                        .caseTitle(rs.getString("casetitle"))
                        .build();
                openApiCaseSummaryMap.put(caseId, caseListLineItem);
            }

        }
        return new ArrayList<>(openApiCaseSummaryMap.values());
    }

    public String getCaseNumber(ResultSet rs) throws SQLException {

        String caseType = rs.getString("casetype");
        return switch (caseType) {
            case "ST", "CC" -> rs.getString("courtcasenumber");
            case "CMP" -> rs.getString("cmpnumber");
            default -> null;
        };
    }
}
