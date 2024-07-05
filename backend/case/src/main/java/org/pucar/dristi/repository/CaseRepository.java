package org.pucar.dristi.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Document;
import org.egov.common.contract.request.RequestInfo;
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

    public static final String FINAL_DOCUMENT_QUERY = "Final document query :: {}";
    private CaseQueryBuilder queryBuilder;

    private JdbcTemplate jdbcTemplate;

    private CaseRowMapper rowMapper;

    private DocumentRowMapper caseDocumentRowMapper;

    private LinkedCaseDocumentRowMapper linkedCaseDocumentRowMapper;

    private LitigantDocumentRowMapper litigantDocumentRowMapper;

    private RepresentiveDocumentRowMapper representativeDocumentRowMapper;

    private RepresentingDocumentRowMapper representingDocumentRowMapper;

    private LinkedCaseRowMapper linkedCaseRowMapper;

    private LitigantRowMapper litigantRowMapper;

    private StatuteSectionRowMapper statuteSectionRowMapper;

    private RepresentativeRowMapper representativeRowMapper;

    private RepresentingRowMapper representingRowMapper;

    @Autowired
    public CaseRepository(CaseQueryBuilder queryBuilder, JdbcTemplate jdbcTemplate, CaseRowMapper rowMapper, DocumentRowMapper caseDocumentRowMapper, LinkedCaseDocumentRowMapper linkedCaseDocumentRowMapper, LitigantDocumentRowMapper litigantDocumentRowMapper, RepresentiveDocumentRowMapper representativeDocumentRowMapper, RepresentingDocumentRowMapper representingDocumentRowMapper, LinkedCaseRowMapper linkedCaseRowMapper, LitigantRowMapper litigantRowMapper, StatuteSectionRowMapper statuteSectionRowMapper, RepresentativeRowMapper representativeRowMapper, RepresentingRowMapper representingRowMapper) {
        this.queryBuilder = queryBuilder;
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
        this.caseDocumentRowMapper = caseDocumentRowMapper;
        this.linkedCaseDocumentRowMapper = linkedCaseDocumentRowMapper;
        this.litigantDocumentRowMapper = litigantDocumentRowMapper;
        this.representativeDocumentRowMapper = representativeDocumentRowMapper;
        this.representingDocumentRowMapper = representingDocumentRowMapper;
        this.linkedCaseRowMapper = linkedCaseRowMapper;
        this.litigantRowMapper = litigantRowMapper;
        this.statuteSectionRowMapper = statuteSectionRowMapper;
        this.representativeRowMapper = representativeRowMapper;
        this.representingRowMapper = representingRowMapper;
    }

    public List<CaseCriteria> getApplications(List<CaseCriteria> searchCriteria, RequestInfo requestInfo) {

        try {

            for (CaseCriteria caseCriteria : searchCriteria) {

                List<Object> preparedStmtList = new ArrayList<>();
                List<Object> preparedStmtListDoc = new ArrayList<>();
                String casesQuery = "";
                casesQuery = queryBuilder.getCasesSearchQuery(caseCriteria, preparedStmtList, requestInfo);
                log.info("Final case query :: {}", casesQuery);
                casesQuery = queryBuilder.addOrderByQuery(casesQuery, caseCriteria.getPagination());
                if (caseCriteria.getPagination() != null) {
                    Integer totalRecords = getTotalCount(casesQuery, preparedStmtList);
                    caseCriteria.getPagination().setTotalCount(Double.valueOf(totalRecords));
                    casesQuery = queryBuilder.addPaginationQuery(casesQuery, preparedStmtList, caseCriteria.getPagination());
                }
                List<CourtCase> list = jdbcTemplate.query(casesQuery, preparedStmtList.toArray(), rowMapper);
                if (list != null) {
                    caseCriteria.setResponseList(list);
                    log.info("Case list size :: {}", list.size());
                }

                if (caseCriteria.getDefaultFields() != null && caseCriteria.getDefaultFields()) {
                    continue;
                }

                List<String> ids = new ArrayList<>();
                List<String> idsLinkedCases = new ArrayList<>();
                List<String> idsLitigant = new ArrayList<>();
                List<String> idsRepresentative = new ArrayList<>();
                List<String> idsRepresenting = new ArrayList<>();

                for (CourtCase courtCase : caseCriteria.getResponseList()) {
                    ids.add(courtCase.getId().toString());
                }
                if (ids.isEmpty()) {
                    caseCriteria.setResponseList(new ArrayList<>());
                    continue;
                }

                extractLinkedCasesIds(caseCriteria, idsLinkedCases);

                extractLitigantIds(caseCriteria, idsLitigant);

                extractRepresentativeIds(caseCriteria, idsRepresentative);

                extractRepresentingIds(caseCriteria, idsRepresenting);

                setLinkedCases(caseCriteria, ids);

                setLitigants(caseCriteria, ids);

                setStatuteAndSections(caseCriteria, ids);

                setRepresentatives(caseCriteria, ids);

                setRepresenting(caseCriteria, idsRepresentative, preparedStmtListDoc);

                setCaseDocuments(caseCriteria, ids);
                String casesDocumentQuery;

                setLitigantDocuments(caseCriteria, idsLitigant);

                setLinkedCaseDocuments(caseCriteria, idsLinkedCases);

                setRepresentativeDocuments(caseCriteria, idsRepresentative);

                setRepresentingDocuments(caseCriteria, idsRepresenting);
            }
            return searchCriteria;
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error while fetching case application list :: {}", e.toString());
            throw new CustomException(SEARCH_CASE_ERR, "Exception while fetching case application list: " + e.getMessage());
        }
    }

    private void setRepresentingDocuments(CaseCriteria caseCriteria, List<String> idsRepresenting) {
        String casesDocumentQuery;
        List<Object> preparedStmtListDoc;
        preparedStmtListDoc = new ArrayList<>();
        casesDocumentQuery = queryBuilder.getRepresentingDocumentSearchQuery(idsRepresenting, preparedStmtListDoc);
        log.info(FINAL_DOCUMENT_QUERY, casesDocumentQuery);
        Map<UUID, List<Document>> caseRepresentingDocumentMap = jdbcTemplate.query(casesDocumentQuery, preparedStmtListDoc.toArray(), representingDocumentRowMapper);
        if (caseRepresentingDocumentMap != null) {
            caseCriteria.getResponseList().forEach(courtCase -> {
                if (courtCase.getRepresentatives() != null) {
                    courtCase.getRepresentatives().forEach(rep -> {
                        if (rep.getRepresenting() != null) {
                            rep.getRepresenting().forEach(representing -> {
                                if (representing != null) {
                                    representing.setDocuments(caseRepresentingDocumentMap.get(representing.getId()));
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    private void setRepresentativeDocuments(CaseCriteria caseCriteria, List<String> idsRepresentative) {
        String casesDocumentQuery;
        List<Object> preparedStmtListDoc;
        casesDocumentQuery = "";
        preparedStmtListDoc = new ArrayList<>();
        casesDocumentQuery = queryBuilder.getRepresentativeDocumentSearchQuery(idsRepresentative, preparedStmtListDoc);
        log.info(FINAL_DOCUMENT_QUERY, casesDocumentQuery);
        Map<UUID, List<Document>> caseRepresentiveDocumentMap = jdbcTemplate.query(casesDocumentQuery, preparedStmtListDoc.toArray(), representativeDocumentRowMapper);
        if (caseRepresentiveDocumentMap != null) {
            caseCriteria.getResponseList().forEach(courtCase -> {
                if (courtCase.getRepresentatives() != null) {
                    courtCase.getRepresentatives().forEach(rep -> {
                        if (rep != null) {
                            rep.setDocuments(caseRepresentiveDocumentMap.get(UUID.fromString(rep.getId())));
                        }
                    });
                }
            });
        }
    }

    private void setLinkedCaseDocuments(CaseCriteria caseCriteria, List<String> idsLinkedCases) {
        String casesDocumentQuery;
        List<Object> preparedStmtListDoc;
        preparedStmtListDoc = new ArrayList<>();
        casesDocumentQuery = queryBuilder.getLinkedCaseDocumentSearchQuery(idsLinkedCases, preparedStmtListDoc);
        log.info(FINAL_DOCUMENT_QUERY, casesDocumentQuery);
        Map<UUID, List<Document>> caseLinkedCaseDocumentMap = jdbcTemplate.query(casesDocumentQuery, preparedStmtListDoc.toArray(), linkedCaseDocumentRowMapper);
        if (caseLinkedCaseDocumentMap != null) {
            caseCriteria.getResponseList().forEach(courtCase -> {
                if (courtCase.getLinkedCases() != null) {
                    courtCase.getLinkedCases().forEach(linkedCase -> {
                        if (linkedCase != null) {
                            linkedCase.setDocuments(caseLinkedCaseDocumentMap.get(linkedCase.getId()));
                        }
                    });
                }
            });
        }
    }

    private void setLitigantDocuments(CaseCriteria caseCriteria, List<String> idsLitigant) {
        String casesDocumentQuery;
        List<Object> preparedStmtListDoc;
        preparedStmtListDoc = new ArrayList<>();
        casesDocumentQuery = queryBuilder.getLitigantDocumentSearchQuery(idsLitigant, preparedStmtListDoc);
        log.info(FINAL_DOCUMENT_QUERY, casesDocumentQuery);
        Map<UUID, List<Document>> caseLitigantDocumentMap = jdbcTemplate.query(casesDocumentQuery, preparedStmtListDoc.toArray(), litigantDocumentRowMapper);
        if (caseLitigantDocumentMap != null) {
            caseCriteria.getResponseList().forEach(courtCase -> {
                if (courtCase.getLitigants() != null) {
                    courtCase.getLitigants().forEach(litigant -> {
                        if (litigant != null) {
                            litigant.setDocuments(caseLitigantDocumentMap.get(litigant.getId()));
                        }
                    });
                }
            });
        }
    }

    private void setCaseDocuments(CaseCriteria caseCriteria, List<String> ids) {
        List<Object> preparedStmtListDoc;
        String casesDocumentQuery = "";
        preparedStmtListDoc = new ArrayList<>();
        casesDocumentQuery = queryBuilder.getDocumentSearchQuery(ids, preparedStmtListDoc);
        log.info("Final document query :: {}", casesDocumentQuery);
        Map<UUID, List<Document>> caseDocumentMap = jdbcTemplate.query(casesDocumentQuery, preparedStmtListDoc.toArray(), caseDocumentRowMapper);
        if (caseDocumentMap != null) {
            caseCriteria.getResponseList().forEach(courtCase -> {
                if (courtCase != null) {
                    courtCase.setDocuments(caseDocumentMap.get(courtCase.getId()));
                }
            });
        }
    }

    private void setRepresenting(CaseCriteria caseCriteria, List<String> idsRepresentative, List<Object> preparedStmtListDoc) {
        String representingQuery = "";
        representingQuery = queryBuilder.getRepresentingSearchQuery(idsRepresentative, preparedStmtListDoc);
        log.info("Final representing query :: {}", representingQuery);
        Map<UUID, List<Party>> representingMap = jdbcTemplate.query(representingQuery, preparedStmtListDoc.toArray(), representingRowMapper);
        if (representingMap != null) {
            caseCriteria.getResponseList().forEach(courtCase -> {
                if (courtCase.getRepresentatives() != null) {
                    courtCase.getRepresentatives().forEach(representative -> representative.setRepresenting(representingMap.get(UUID.fromString(representative.getId()))));
                }
            });
        }
    }

    private void setRepresentatives(CaseCriteria caseCriteria, List<String> ids) {
        List<Object> preparedStmtListDoc;
        String representativeQuery = "";
        preparedStmtListDoc = new ArrayList<>();
        representativeQuery = queryBuilder.getRepresentativesSearchQuery(ids, preparedStmtListDoc);
        log.info("Final representative query :: {}", representativeQuery);
        Map<UUID, List<AdvocateMapping>> representativeMap = jdbcTemplate.query(representativeQuery, preparedStmtListDoc.toArray(), representativeRowMapper);
        if (representativeMap != null) {
            caseCriteria.getResponseList().forEach(courtCase -> courtCase.setRepresentatives(representativeMap.get(courtCase.getId())));
        }
    }

    private void setStatuteAndSections(CaseCriteria caseCriteria, List<String> ids) {
        List<Object> preparedStmtListDoc;
        String statueAndSectionQuery = "";
        preparedStmtListDoc = new ArrayList<>();
        statueAndSectionQuery = queryBuilder.getStatuteSectionSearchQuery(ids, preparedStmtListDoc);
        log.info("Final statute and sections query :: {}", statueAndSectionQuery);
        Map<UUID, List<StatuteSection>> statuteSectionsMap = jdbcTemplate.query(statueAndSectionQuery, preparedStmtListDoc.toArray(), statuteSectionRowMapper);
        if (statuteSectionsMap != null) {
            caseCriteria.getResponseList().forEach(courtCase -> courtCase.setStatutesAndSections(statuteSectionsMap.get(courtCase.getId())));
        }
    }

    private void setLitigants(CaseCriteria caseCriteria, List<String> ids) {
        List<Object> preparedStmtListDoc;
        String litigantQuery = "";
        preparedStmtListDoc = new ArrayList<>();
        litigantQuery = queryBuilder.getLitigantSearchQuery(ids, preparedStmtListDoc);
        log.info("Final litigant query :: {}", litigantQuery);
        Map<UUID, List<Party>> litigantMap = jdbcTemplate.query(litigantQuery, preparedStmtListDoc.toArray(), litigantRowMapper);
        if (litigantMap != null) {
            caseCriteria.getResponseList().forEach(courtCase -> courtCase.setLitigants(litigantMap.get(courtCase.getId())));
        }
    }

    private void setLinkedCases(CaseCriteria caseCriteria, List<String> ids) {
        List<Object> preparedStmtListDoc;
        String linkedCaseQuery = "";
        preparedStmtListDoc = new ArrayList<>();
        linkedCaseQuery = queryBuilder.getLinkedCaseSearchQuery(ids, preparedStmtListDoc);
        log.info("Final linked case query :: {}", linkedCaseQuery);
        Map<UUID, List<LinkedCase>> linkedCasesMap = jdbcTemplate.query(linkedCaseQuery, preparedStmtListDoc.toArray(), linkedCaseRowMapper);
        if (linkedCasesMap != null) {
            caseCriteria.getResponseList().forEach(courtCase -> courtCase.setLinkedCases(linkedCasesMap.get(courtCase.getId())));
        }
    }

    private static void extractRepresentingIds(CaseCriteria caseCriteria, List<String> idsRepresenting) {
        for (CourtCase courtCase : caseCriteria.getResponseList()) {
            if (courtCase.getRepresentatives() != null) {
                courtCase.getRepresentatives().forEach(rep -> {
                    if (rep.getRepresenting() != null) {
                        rep.getRepresenting().forEach(representing -> idsRepresenting.add(representing.getId().toString()));
                    }
                });
            }
        }
    }

    private static void extractRepresentativeIds(CaseCriteria caseCriteria, List<String> idsRepresentative) {
        for (CourtCase courtCase : caseCriteria.getResponseList()) {
            if (courtCase.getRepresentatives() != null) {
                courtCase.getRepresentatives().forEach(rep -> idsRepresentative.add(rep.getId()));
            }
        }
    }

    private static void extractLitigantIds(CaseCriteria caseCriteria, List<String> idsLitigant) {
        for (CourtCase courtCase : caseCriteria.getResponseList()) {
            if (courtCase.getLitigants() != null) {
                courtCase.getLitigants().forEach(litigant -> idsLitigant.add(litigant.getId().toString()));
            }
        }
    }

    private static void extractLinkedCasesIds(CaseCriteria caseCriteria, List<String> idsLinkedCases) {
        for (CourtCase courtCase : caseCriteria.getResponseList()) {
            if (courtCase.getLinkedCases() != null) {
                courtCase.getLinkedCases().forEach(linkedCase -> idsLinkedCases.add(linkedCase.getId().toString()));
            }
        }
    }

    public List<CaseExists> checkCaseExists(List<CaseExists> caseExistsRequest) {
        try {
            for (CaseExists caseExists : caseExistsRequest) {
                if (
                        (caseExists.getCaseId() == null || caseExists.getCaseId().isEmpty()) &&
                                (caseExists.getCourtCaseNumber() == null || caseExists.getCourtCaseNumber().isEmpty()) &&
                                (caseExists.getCnrNumber() == null || caseExists.getCnrNumber().isEmpty()) &&
                                (caseExists.getFilingNumber() == null || caseExists.getFilingNumber().isEmpty())
                ) {
                    caseExists.setExists(false);
                } else {
                    String casesExistQuery = queryBuilder.checkCaseExistQuery(caseExists.getCaseId(), caseExists.getCourtCaseNumber(), caseExists.getCnrNumber(), caseExists.getFilingNumber());
                    log.info("Final case exist query :: {}", casesExistQuery);
                    Integer count = jdbcTemplate.queryForObject(casesExistQuery, Integer.class);
                    caseExists.setExists(count != null && count > 0);
                }
            }
            return caseExistsRequest;
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error while checking case exist :: {}", e.toString());
            throw new CustomException(SEARCH_CASE_ERR, "Custom exception while checking case exist : " + e.getMessage());
        }
    }

    public Integer getTotalCount(String baseQuery, List<Object> preparedStmtList) {
        String countQuery = queryBuilder.getTotalCountQuery(baseQuery);
        log.info("Final count query :: {}", countQuery);
        return jdbcTemplate.queryForObject(countQuery, preparedStmtList.toArray(), Integer.class);
    }
}