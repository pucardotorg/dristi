package org.pucar.dristi.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.querybuilder.CaseQueryBuilder;
import org.pucar.dristi.repository.rowmapper.CaseDocumentRowMapper;
import org.pucar.dristi.repository.rowmapper.CaseRowMapper;
import org.pucar.dristi.web.models.CaseCriteria;
import org.pucar.dristi.web.models.CourtCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Slf4j
@Repository
public class CaseRepository {

    @Autowired
    private CaseQueryBuilder queryBuilder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CaseRowMapper rowMapper;
    @Autowired
    private CaseDocumentRowMapper caseDocumentRowMapper;

    public List<CourtCase> getApplications(List<CaseCriteria> searchCriteria) {

        try {
            List<CourtCase> courtCaseList = new ArrayList<>();
            List<Object> preparedStmtList = new ArrayList<>();
            List<Object> preparedStmtListDoc = new ArrayList<>();
            String casesQuery = "";
            casesQuery = queryBuilder.getCasesSearchQuery(searchCriteria, preparedStmtList);
            log.info("Final advocate list query: {}", casesQuery);
            List<CourtCase> list = jdbcTemplate.query(casesQuery, preparedStmtList.toArray(), rowMapper);
            if (list != null) {
                courtCaseList.addAll(list);
            }

            List<String> ids = new ArrayList<>();
            for (CourtCase courtCase : courtCaseList) {
                ids.add(courtCase.getId().toString());
            }
            if (ids.isEmpty()) {
                return courtCaseList;
            }

            String casesDocumentQuery = "";
            casesDocumentQuery = queryBuilder.getDocumentSearchQuery(ids, preparedStmtListDoc);
            log.info("Final document query: {}", casesDocumentQuery);
            Map<UUID, List<Document>> caseDocumentMap = jdbcTemplate.query(casesDocumentQuery, preparedStmtListDoc.toArray(), caseDocumentRowMapper);
            if (caseDocumentMap != null) {
                courtCaseList.forEach(advocate -> {
                    advocate.setDocuments(caseDocumentMap.get(advocate.getId()));
                });
            }

            return courtCaseList;
        }
        catch (CustomException e){
            throw e;
        }
        catch (Exception e){
            log.error("Error while fetching advocate application list");
            throw new CustomException("ADVOCATE_SEARCH_EXCEPTION","Error while fetching advocate application list: "+e.getMessage());
        }
    }

}