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
import static org.pucar.dristi.config.ServiceConstants.*;

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

    @Autowired
    private RepresentingRowMapper representingRowMapper;

    public List<CourtCase> getApplications(List<CaseCriteria> searchCriteria) {
        try {
            List<CourtCase> courtCaseList = fetchCourtCases(searchCriteria);
            fetchAssociatedData(courtCaseList);
            return courtCaseList;
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error while fetching case application list");
            throw new CustomException(SEARCH_CASE_ERR, "Error while fetching case application list: " + e.getMessage());
        }
    }

    private List<CourtCase> fetchCourtCases(List<CaseCriteria> searchCriteria) {
        try {
            List<Object> preparedStmtList = new ArrayList<>();
            String casesQuery = queryBuilder.getCasesSearchQuery(searchCriteria, preparedStmtList);
            log.info(FINAL_CASE_QUERY, casesQuery);
            List<CourtCase> courtCaseList = jdbcTemplate.query(casesQuery, preparedStmtList.toArray(), rowMapper);
            return courtCaseList != null ? courtCaseList : new ArrayList<>();
        } catch (Exception e) {
            log.error(COURT_CASE_FETCH_ERROR, e.getMessage());
            return new ArrayList<>();
        }
    }


    private void fetchAssociatedData(List<CourtCase> courtCaseList) {
        for (CourtCase courtCase : courtCaseList) {
            List<String> ids = new ArrayList<>();
            ids.add(courtCase.getId().toString());
            List<Object> preparedStmtListDoc = new ArrayList<>();

            fetchLinkedCases(courtCase, ids, preparedStmtListDoc);
            fetchLitigants(courtCase, ids, preparedStmtListDoc);
            fetchStatuteSections(courtCase, ids, preparedStmtListDoc);
            fetchRepresentatives(courtCase, ids, preparedStmtListDoc);
            fetchRepresenting(courtCase, preparedStmtListDoc);
            fetchDocuments(courtCase, ids, preparedStmtListDoc);
        }
    }

    private void fetchLinkedCases(CourtCase courtCase, List<String> ids, List<Object> preparedStmtListDoc) {
        String linkedCaseQuery = queryBuilder.getLinkedCaseSearchQuery(ids, preparedStmtListDoc);
        log.info(FINAL_LINKED_CASE_QUERY, linkedCaseQuery);
        Map<UUID, List<LinkedCase>> linkedCasesMap = jdbcTemplate.query(linkedCaseQuery, preparedStmtListDoc.toArray(), linkedCaseRowMapper);
        if (linkedCasesMap != null) {
            courtCase.setLinkedCases(linkedCasesMap.get(courtCase.getId()));
        }
    }

    private void fetchLitigants(CourtCase courtCase, List<String> ids, List<Object> preparedStmtListDoc) {
        String litigantQuery = queryBuilder.getLitigantSearchQuery(ids, preparedStmtListDoc);
        log.info(FINAL_LITIGANT_QUERY, litigantQuery);
        Map<UUID, List<Party>> litigantMap = jdbcTemplate.query(litigantQuery, preparedStmtListDoc.toArray(), litigantRowMapper);
        if (litigantMap != null) {
            courtCase.setLitigants(litigantMap.get(courtCase.getId()));
        }
    }

    private void fetchStatuteSections(CourtCase courtCase, List<String> ids, List<Object> preparedStmtListDoc) {
        String statueAndSectionQuery = queryBuilder.getStatuteSectionSearchQuery(ids, preparedStmtListDoc);
        log.info(FINAL_STATUTE_QUERY, statueAndSectionQuery);
        Map<UUID, List<StatuteSection>> statuteSectionsMap = jdbcTemplate.query(statueAndSectionQuery, preparedStmtListDoc.toArray(), statuteSectionRowMapper);
        if (statuteSectionsMap != null) {
            courtCase.setStatutesAndSections(statuteSectionsMap.get(courtCase.getId()));
        }
    }

    private void fetchRepresentatives(CourtCase courtCase, List<String> ids, List<Object> preparedStmtListDoc) {
        String representativeQuery = queryBuilder.getRepresentativesSearchQuery(ids, preparedStmtListDoc);
        log.info(FINAL_REPRESENTATIVE_QUERY, representativeQuery);
        Map<UUID, List<AdvocateMapping>> representativeMap = jdbcTemplate.query(representativeQuery, preparedStmtListDoc.toArray(), representativeRowMapper);
        if (representativeMap != null) {
            courtCase.setRepresentatives(representativeMap.get(courtCase.getId()));
        }
    }

    private void fetchRepresenting(CourtCase courtCase, List<Object> preparedStmtListDoc) {
        List<String> idsRepresentive = new ArrayList<>();
        if (courtCase.getRepresentatives() != null) {
            courtCase.getRepresentatives().forEach(rep -> {
                idsRepresentive.add(rep.getId().toString());
            });
        }

        String representingQuery = queryBuilder.getRepresentingSearchQuery(idsRepresentive, preparedStmtListDoc);
        log.info(FINAL_REPRESENTATING_QUERY, representingQuery);
        Map<UUID, List<Party>> representingMap = jdbcTemplate.query(representingQuery, preparedStmtListDoc.toArray(), representingRowMapper);
        if (representingMap != null) {
            courtCase.getRepresentatives().forEach(representative -> {
                representative.setRepresenting(representingMap.get(UUID.fromString(representative.getId())));
            });
        }
    }

    private void fetchDocuments(CourtCase courtCase, List<String> ids, List<Object> preparedStmtListDoc) {
        String casesDocumentQuery = queryBuilder.getDocumentSearchQuery(ids, preparedStmtListDoc);
        log.info(FINAL_DOCUMENT_QUERY, casesDocumentQuery);
        Map<UUID, List<Document>> caseDocumentMap = jdbcTemplate.query(casesDocumentQuery, preparedStmtListDoc.toArray(), caseDocumentRowMapper);
        if (caseDocumentMap != null) {
            courtCase.setDocuments(caseDocumentMap.get(courtCase.getId()));
        }
    }
}
