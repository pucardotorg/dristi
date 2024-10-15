package org.pucar.dristi.repository.rowmapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Slf4j
public class CaseSummaryRowMapper implements ResultSetExtractor<List<CaseSummary>> {

    private final ObjectMapper objectMapper;

    @Autowired
    public CaseSummaryRowMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public List<CaseSummary> extractData(ResultSet rs) throws SQLException, DataAccessException {


        Map<String, CaseSummary> caseMap = new HashMap<>();

        while (rs.next()) {
            String caseId = rs.getString("id");
            CaseSummary caseSummary = caseMap.get(caseId);
            if (caseSummary == null) {
                caseSummary = CaseSummary.builder()
                        .id(caseId)
                        .tenantId(rs.getString("tenantid"))
                        .caseTitle(rs.getString("casetitle"))
                        .filingDate(rs.getLong("filingdate"))
                        .statutesAndSections(null)
                        .stage(rs.getString("stage"))
                        .subStage(rs.getString("substage"))
                        .outcome(rs.getString("outcome"))
                        .litigants(new ArrayList<>())
                        .representatives(new ArrayList<>())
                        .judge(getJudge(rs))
                        .build();

                caseMap.put(caseId, caseSummary);
            }

            String statuteId = rs.getString("statute_section_id");
            if (statuteId != null) {
                StatuteSection statuteSection = StatuteSection.builder()
                        .id(UUID.fromString(rs.getString("statute_section_id")))
                        .tenantId(rs.getString("statute_section_tenantid"))
                        .sections(stringToList(rs.getString("statute_section_sections")))
                        .subsections(stringToList(rs.getString("statute_section_subsections")))
                        .statute(rs.getString("statute_section_statutes"))
                        .build();

                StringBuilder existingStatues = caseSummary.getStatutesAndSections() != null ? new StringBuilder(caseSummary.getStatutesAndSections()) : new StringBuilder();
                String statuteAndSectionsString = getStatuteAndSectionsString(existingStatues, statuteSection.getStatute(), statuteSection.getSections());
                caseSummary.setStatutesAndSections(statuteAndSectionsString);

            }

            String partyId = rs.getString("litigant_id");
            if (partyId != null) {
                PartySummary party = PartySummary.builder()
                        .partyCategory(rs.getString("litigant_partycategory"))
                        .partyType(rs.getString("litigant_partytype"))
                        .individualId(rs.getString("litigant_individualid"))
                        .individualName(getNameForLitigant(rs))
                        .organisationId(rs.getString("litigant_organisationid"))
//                        .isPartyInPerson(rs.getBoolean("isPartyInPerson"))
                        .build();
                caseSummary.getLitigants().add(party);
            }

            String representativeId = rs.getString("representative_id");
            if (representativeId != null) {
                RepresentativeSummary representative = RepresentativeSummary.builder()
                        .partyId(rs.getString("representative_case_id"))
//                        .advocateType(rs.getString("advocateType"))
                        .advocateId(rs.getString("representative_advocateid"))
                        .build();

                caseSummary.getRepresentatives().add(representative);
            }
        }

        return new ArrayList<>(caseMap.values());
    }


    //todo: this is temporary method once the db schema is updated we need to remove this table
    private String getNameForLitigant(ResultSet rs) {
        String additionalDetails ;
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
            throw new CustomException("JSON_PROCESSING_EXCEPTION", "Error processing litigant additional details") {
            };
        }

        return fullName;
    }

    private Judge getJudge(ResultSet rs) {
        return Judge.builder().build();
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


    public String getStatuteAndSectionsString(StringBuilder statueAndSections, String statute, List<String> sections) {
        if (!statueAndSections.isEmpty()) {
            statueAndSections.append(";");
        }
        if (statute != null) statueAndSections.append(statute);

        if (!sections.isEmpty()) {
            statueAndSections.append(" ");
            for (int i = 0; i < sections.size(); i++) {
                statueAndSections.append(sections.get(i));
                if (i < sections.size() - 1) {
                    statueAndSections.append(", ");
                }
            }
        }

//        if (!subsections.isEmpty()) {
//            statuteAndSectionsStringBuilder.append(" ");
//            for (int i = 0; i < subsections.size(); i++) {
//                statuteAndSectionsStringBuilder.append(subsections.get(i));
//                if (i < subsections.size() - 1) {
//                    statuteAndSectionsStringBuilder.append(", ");
//                }
//            }
//        }

        return statueAndSections.toString();
    }
}
