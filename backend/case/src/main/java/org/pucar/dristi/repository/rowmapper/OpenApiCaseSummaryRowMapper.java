package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.jetbrains.annotations.NotNull;
import org.pucar.dristi.web.OpenApiCaseSummary;
import org.pucar.dristi.web.models.StatuteSection;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


@Component
@Slf4j
public class OpenApiCaseSummaryRowMapper implements ResultSetExtractor<List<OpenApiCaseSummary>> {

    private final ObjectMapper objectMapper;

    public OpenApiCaseSummaryRowMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public List<OpenApiCaseSummary> extractData(ResultSet rs) throws SQLException, DataAccessException {

        Map<String, OpenApiCaseSummary> openApiCaseSummaryMap = new HashMap<>();
        Set<String> mappedKey = new HashSet<>();

        while (rs.next()) {
            try {
                String caseId = rs.getString("id");

                OpenApiCaseSummary openApiCaseSummary = openApiCaseSummaryMap.get(caseId);

                if (openApiCaseSummary == null) {
                    openApiCaseSummary = OpenApiCaseSummary.builder()
                            .cnrNumber(rs.getString("cnrnumber"))
                            .filingNumber(rs.getString("filingnumber"))
                            .filingDate(rs.getLong("filingdate"))
                            .registrationDate(rs.getLong("registrationdate"))
                            .registrationNumber(rs.getString("cmpnumber"))
                            .caseType(getCaseType(rs))
                            .status(getStatus(rs))
                            .subStage(rs.getString("stage") + " - " + rs.getString("substage"))
                            .build();

                    openApiCaseSummaryMap.put(caseId, openApiCaseSummary);
                }

                String statuteId = rs.getString("statute_section_id");
                if (statuteId != null && !mappedKey.contains(statuteId)) {
                    StatuteSection statuteSection = StatuteSection.builder()
                            .statute(rs.getString("statute_section_statutes"))
                            .sections(stringToList(rs.getString("statute_section_sections")))
                            .build();
                    if (openApiCaseSummary != null) {
                        openApiCaseSummary.setStatutesAndSections(Collections.singletonList(statuteSection));
                    }

                    mappedKey.add(statuteId);

                }

                String litigantId = rs.getString("litigant_id");
                if (litigantId != null && !mappedKey.contains(litigantId)) {
                    if (openApiCaseSummary != null) {
                        String litgantPartyType = rs.getString("litigant_partytype");
                        if (Objects.equals(litgantPartyType, "complainant.primary")) {
                            openApiCaseSummary.setComplainant(getNameForLitigant(rs));
                        } else if (Objects.equals(litgantPartyType, "respondent.primary")) {
                            openApiCaseSummary.setRespondent(getNameForLitigant(rs));
                        }
                    }
                    mappedKey.add(litigantId);
                }

                String representativeId = rs.getString("representing_id");
                if (representativeId != null && !mappedKey.contains(representativeId)) {
                    if (openApiCaseSummary != null) {
                        String representativeType = rs.getString("representing_partytype");
                            if (Objects.equals(representativeType, "complainant.primary")) {
                                openApiCaseSummary.setAdvocateComplainant(getNameForLitigant(rs));
                            } else if (Objects.equals(representativeType, "respondent.primary")) {
                                openApiCaseSummary.setAdvocateRespondent(getNameForLitigant(rs));
                        }
                    }
                    mappedKey.add(representativeId);
                }

                openApiCaseSummaryMap.put(caseId, openApiCaseSummary);
            } catch (Exception e) {
                log.error("Error while mapping case summary", e);
                throw new SQLException("Error while mapping case summary", e);
            }
        }

        return new ArrayList<>(openApiCaseSummaryMap.values());
    }

    public OpenApiCaseSummary.CaseTypeEnum getCaseType(ResultSet rs) {
        try {

            return OpenApiCaseSummary.CaseTypeEnum.valueOf(rs.getString("casetype"));
        } catch (Exception e) {
            log.error("Error while fetching case type from result set", e);
            return null;
        }
    }

    public OpenApiCaseSummary.StatusEnum getStatus(ResultSet rs) {
        try {
            if (Objects.equals(rs.getString("casetype"), "CMP")) {
                if (rs.getString("courtcasenumber") == null) {
                    return OpenApiCaseSummary.StatusEnum.PENDING;
                } else {
                    return OpenApiCaseSummary.StatusEnum.DISPOSED;
                }
            } else if (Objects.equals(rs.getString("casetype"), "ST")) {
                if (rs.getString("outcome") == null) {
                    return OpenApiCaseSummary.StatusEnum.PENDING;
                } else {
                    return OpenApiCaseSummary.StatusEnum.DISPOSED;
                }
            }
            else {
                if (rs.getString("courtcasenumber") == null) {
                    return OpenApiCaseSummary.StatusEnum.PENDING;
                } else {
                    return OpenApiCaseSummary.StatusEnum.DISPOSED;
                }
            }
        } catch (Exception e) {
            log.error("Error while fetching status from result set", e);
            throw new CustomException("ERROR_FETCHING_STATUS", "Error while fetching status from result set") {
            };
        }
    }

    public List<String> stringToList(String str) {
        List<String> list = new ArrayList<>();
        if (str != null) {
            StringTokenizer st = new StringTokenizer(str, ",");
            while (st.hasMoreTokens()) {
                list.add(st.nextToken());
            }
        }

        return list;
    }

    public String getNameForLitigant(@NotNull ResultSet rs) {
        String additionalDetails;
        String fullName = null;
        try {
            additionalDetails = rs.getString("litigant_additionaldetails");

            if (additionalDetails != null && !additionalDetails.isEmpty()) {
                JsonNode jsonNode = objectMapper.readTree(additionalDetails);
                if (jsonNode.has("fullName")) {
                    fullName = jsonNode.get("fullName").asText();
                }
            }
        } catch (SQLException | JsonProcessingException e) {
            throw new CustomException("ERROR_FETCHING_LITIGANT_NAME", "Error while fetching litigant name from result set");
        }

        return fullName;
    }
}