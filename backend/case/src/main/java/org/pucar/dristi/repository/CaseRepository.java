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

import static org.pucar.dristi.config.ServiceConstants.SEARCH_CASE_ERR;


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
    private LinkedCaseDocumentRowMapper linkedCaseDocumentRowMapper;

    @Autowired
    private LitigantDocumentRowMapper litigantDocumentRowMapper;

    @Autowired
    private RepresentiveDocumentRowMapper representativeDocumentRowMapper;

    @Autowired
    private RepresentingDocumentRowMapper representingDocumentRowMapper;

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
            List<String> idsLinkedCases = new ArrayList<>();
            List<String> idsLitigant = new ArrayList<>();
            List<String> idsRepresentive = new ArrayList<>();
            List<String> idsRepresenting = new ArrayList<>();
            for (CourtCase courtCase : courtCaseList) {
                ids.add(courtCase.getId().toString());
            }
            if (ids.isEmpty()) {
                return courtCaseList;
            }

            for (CourtCase courtCase : courtCaseList) {
                if (courtCase.getLinkedCases() != null) {
                    courtCase.getLinkedCases().forEach(linkedCase -> {
                        idsLinkedCases.add(linkedCase.getId().toString());
                    });
                }
            }

            for (CourtCase courtCase : courtCaseList) {
                if (courtCase.getLitigants() != null) {
                    courtCase.getLitigants().forEach(litigant -> {
                        idsLitigant.add(litigant.getId().toString());
                    });
                }
            }

            for (CourtCase courtCase : courtCaseList) {
                if (courtCase.getRepresentatives() != null) {
                    courtCase.getRepresentatives().forEach(rep -> {
                        idsRepresentive.add(rep.getId().toString());
                    });
                }
            }

            for (CourtCase courtCase : courtCaseList) {
                if (courtCase.getRepresentatives() != null) {
                    courtCase.getRepresentatives().forEach(rep -> {
                        if (rep.getRepresenting() != null) {
                            rep.getRepresenting().forEach(representing -> {
                                idsRepresenting.add(representing.getId().toString());
                            });
                        }
                    });
                }
            }

            String linkedCaseQuery = "";
            preparedStmtListDoc = new ArrayList<>();
            linkedCaseQuery = queryBuilder.getLinkedCaseSearchQuery(ids, preparedStmtListDoc);
            log.info("Final linked case query: {}", linkedCaseQuery);
            Map<UUID, List<LinkedCase>> linkedCasesMap = jdbcTemplate.query(linkedCaseQuery, preparedStmtListDoc.toArray(), linkedCaseRowMapper);
            if (linkedCasesMap != null) {
                courtCaseList.forEach(courtCase -> {
                    courtCase.setLinkedCases(linkedCasesMap.get(courtCase.getId()));
                });
            }

            String litigantQuery = "";
            preparedStmtListDoc = new ArrayList<>();
            litigantQuery = queryBuilder.getLitigantSearchQuery(ids, preparedStmtListDoc);
            log.info("Final litigant query: {}", litigantQuery);
            Map<UUID, List<Party>> litigantMap = jdbcTemplate.query(litigantQuery, preparedStmtListDoc.toArray(), litigantRowMapper);
            if (litigantMap != null) {
                courtCaseList.forEach(courtCase -> {
                    courtCase.setLitigants(litigantMap.get(courtCase.getId()));
                });
            }

            String statueAndSectionQuery = "";
            preparedStmtListDoc = new ArrayList<>();
            statueAndSectionQuery = queryBuilder.getStatuteSectionSearchQuery(ids, preparedStmtListDoc);
            log.info("Final statute and sections query: {}", statueAndSectionQuery);
            Map<UUID, List<StatuteSection>> statuteSectionsMap = jdbcTemplate.query(statueAndSectionQuery, preparedStmtListDoc.toArray(), statuteSectionRowMapper);
            if (statuteSectionsMap != null) {
                courtCaseList.forEach(courtCase -> {
                    courtCase.setStatutesAndSections(statuteSectionsMap.get(courtCase.getId()));
                });
            }

            String representativeQuery = "";
            preparedStmtListDoc = new ArrayList<>();
            representativeQuery = queryBuilder.getRepresentativesSearchQuery(ids, preparedStmtListDoc);
            log.info("Final representative query: {}", representativeQuery);
            Map<UUID, List<AdvocateMapping>> representativeMap = jdbcTemplate.query(representativeQuery, preparedStmtListDoc.toArray(), representativeRowMapper);
            if (representativeMap != null) {
                courtCaseList.forEach(courtCase -> {
                    courtCase.setRepresentatives(representativeMap.get(courtCase.getId()));
                });
            }

            String representingQuery = "";
            preparedStmtListDoc = new ArrayList<>();
            representingQuery = queryBuilder.getRepresentingSearchQuery(idsRepresentive, preparedStmtListDoc);
            log.info("Final representing query: {}", representativeQuery);
            Map<UUID, List<Party>> representingMap = jdbcTemplate.query(representingQuery, preparedStmtListDoc.toArray(), representingRowMapper);
            if (representingMap != null) {
                courtCaseList.forEach(courtCase -> {
                    courtCase.getRepresentatives().forEach(representative -> {
                        representative.setRepresenting(representingMap.get(UUID.fromString(representative.getId())));
                    });
                });
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

            casesDocumentQuery = "";
            preparedStmtListDoc = new ArrayList<>();
            casesDocumentQuery = queryBuilder.getLitigantDocumentSearchQuery(idsLitigant, preparedStmtListDoc);
            log.info("Final document query: {}", casesDocumentQuery);
            Map<UUID, List<Document>> caseLitigantDocumentMap = jdbcTemplate.query(casesDocumentQuery, preparedStmtListDoc.toArray(), litigantDocumentRowMapper);
            if (caseLitigantDocumentMap != null) {
                courtCaseList.forEach(courtCase -> {
                    courtCase.getLitigants().forEach(litigant -> {
                        litigant.setDocuments(caseLitigantDocumentMap.get(litigant.getId()));
                    });
                });
            }

            casesDocumentQuery = "";
            preparedStmtListDoc = new ArrayList<>();
            casesDocumentQuery = queryBuilder.getLinkedCaseDocumentSearchQuery(idsLinkedCases, preparedStmtListDoc);
            log.info("Final document query: {}", casesDocumentQuery);
            Map<UUID, List<Document>> caseLinkedCaseDocumentMap = jdbcTemplate.query(casesDocumentQuery, preparedStmtListDoc.toArray(), linkedCaseDocumentRowMapper);
            if (caseLinkedCaseDocumentMap != null) {
                courtCaseList.forEach(courtCase -> {
                    if (courtCase.getLinkedCases() != null) {
                        courtCase.getLinkedCases().forEach(linkedCase -> {
                            linkedCase.setDocuments(caseLinkedCaseDocumentMap.get(linkedCase.getId()));
                        });
                    }
                });
            }

            casesDocumentQuery = "";
            preparedStmtListDoc = new ArrayList<>();
            casesDocumentQuery = queryBuilder.getRepresentativeDocumentSearchQuery(idsRepresentive, preparedStmtListDoc);
            log.info("Final document query: {}", casesDocumentQuery);
            Map<UUID, List<Document>> caseRepresentiveDocumentMap = jdbcTemplate.query(casesDocumentQuery, preparedStmtListDoc.toArray(), representativeDocumentRowMapper);
            if (caseRepresentiveDocumentMap != null) {
                courtCaseList.forEach(courtCase -> {
                    if (courtCase.getRepresentatives() != null) {
                        courtCase.getRepresentatives().forEach(rep -> {
                            rep.setDocuments(caseRepresentiveDocumentMap.get(rep.getId()));
                        });
                    }
                });
            }

            casesDocumentQuery = "";
            preparedStmtListDoc = new ArrayList<>();
            casesDocumentQuery = queryBuilder.getRepresentingDocumentSearchQuery(idsRepresenting, preparedStmtListDoc);
            log.info("Final document query: {}", casesDocumentQuery);
            Map<UUID, List<Document>> caseRepresentingDocumentMap = jdbcTemplate.query(casesDocumentQuery, preparedStmtListDoc.toArray(), representingDocumentRowMapper);
            if (caseRepresentingDocumentMap != null) {
                courtCaseList.forEach(courtCase -> {
                    if (courtCase.getRepresentatives() != null) {
                        courtCase.getRepresentatives().forEach(rep -> {
                            if (rep.getRepresenting() != null) {
                                rep.getRepresenting().forEach(representing -> {
                                    representing.setDocuments(caseRepresentingDocumentMap.get(representing.getId()));
                                });
                            }
                        });
                    }
                });
            }

            return courtCaseList;
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error while fetching case application list");
            throw new CustomException(SEARCH_CASE_ERR, "Error while fetching case application list: " + e.getMessage());
        }
    }

    public List<CaseExists> checkCaseExists(List<CaseExists> caseExistsRequest) {
        try {
            for (CaseExists caseExists : caseExistsRequest) {
                if (caseExists.getCourtCaseNumber() == null && caseExists.getCnrNumber() == null && caseExists.getFilingNumber() == null) {
                    caseExists.setExists(false);
                } else {
                    String casesExistQuery = queryBuilder.checkCaseExistQuery(caseExists.getCourtCaseNumber(), caseExists.getCnrNumber(), caseExists.getFilingNumber());
                    log.info("Final case exist query: {}", casesExistQuery);
                    Integer count = jdbcTemplate.queryForObject(casesExistQuery, Integer.class);
                    caseExists.setExists(count != null && count > 0);
                }
            }
            return caseExistsRequest;
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error while checking case exist");
            throw new CustomException(SEARCH_CASE_ERR, "Custom exception while checking case exist : " + e.getMessage());
        }
    }
}