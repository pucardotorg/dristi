package org.pucar.dristi.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.querybuilder.CaseQueryBuilder;
import org.pucar.dristi.repository.rowmapper.*;
import org.pucar.dristi.web.models.*;
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
    private DocumentRowMapper caseDocumentRowMapper;

    @Autowired
    private LinkedCaseRowMapper linkedCaseRowMapper;

    @Autowired
    private LitigantRowMapper litigantRowMapper;

    @Autowired
    private StatuteSectionRowMapper statuteSectionRowMapper;

    @Autowired
    private RepresentativeRowMapper representativeRowMapper;

    public List<CourtCase> getApplications(List<CaseCriteria> searchCriteria) {

        try {
            List<CourtCase> courtCaseList = new ArrayList<>();
            List<Object> preparedStmtList = new ArrayList<>();
            List<Object> preparedStmtListDoc = new ArrayList<>();
            String casesQuery = "";
            casesQuery = queryBuilder.getCasesSearchQuery(searchCriteria, preparedStmtList);
            log.info("Final case query: {}", casesQuery);
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
                courtCaseList.forEach(courtCase -> {
                    courtCase.setDocuments(caseDocumentMap.get(courtCase.getId()));
                });
            }

            String linkedCaseQuery = "";
            linkedCaseQuery = queryBuilder.getLinkedCaseSearchQuery(ids, preparedStmtListDoc);
            log.info("Final linked case query: {}", linkedCaseQuery);
            Map<UUID, List<LinkedCase>> linkedCasesMap = jdbcTemplate.query(linkedCaseQuery, preparedStmtListDoc.toArray(), linkedCaseRowMapper);
            if (linkedCasesMap != null) {
                courtCaseList.forEach(courtCase -> {
                    courtCase.setLinkedCases(linkedCasesMap.get(courtCase.getId()));
                });
            }

            String litigantQuery = "";
            litigantQuery = queryBuilder.getLitigantSearchQuery(ids, preparedStmtListDoc);
            log.info("Final litigant query: {}", litigantQuery);
            Map<UUID, List<Party>> litigantMap = jdbcTemplate.query(litigantQuery, preparedStmtListDoc.toArray(), litigantRowMapper);
            if (litigantMap != null) {
                courtCaseList.forEach(courtCase -> {
                    courtCase.setLitigants(litigantMap.get(courtCase.getId()));
                });
            }

            String statueAndSectionQuery = "";
            statueAndSectionQuery = queryBuilder.getStatuteSectionSearchQuery(ids, preparedStmtListDoc);
            log.info("Final statue and sections query: {}", statueAndSectionQuery);
            Map<UUID, List<StatuteSection>> statuteSectionsMap = jdbcTemplate.query(statueAndSectionQuery, preparedStmtListDoc.toArray(), statuteSectionRowMapper);
            if (statuteSectionsMap != null) {
                courtCaseList.forEach(courtCase -> {
                    courtCase.setStatutesAndSections(statuteSectionsMap.get(courtCase.getId()));
                });
            }

            String representativeQuery = "";
            representativeQuery = queryBuilder.getRepresentativesSearchQuery(ids, preparedStmtListDoc);
            log.info("Final representative query: {}", representativeQuery);
            Map<UUID, List<AdvocateMapping>> representativeMap = jdbcTemplate.query(representativeQuery, preparedStmtListDoc.toArray(), representativeRowMapper);
            if (representativeMap != null) {
                courtCaseList.forEach(courtCase -> {
                    courtCase.setRepresentatives(representativeMap.get(courtCase.getId()));
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