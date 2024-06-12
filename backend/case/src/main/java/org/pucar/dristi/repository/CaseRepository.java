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

    public List<CaseCriteria> getApplications(List<CaseCriteria> searchCriteria) {

        try {

            for (CaseCriteria caseCriteria : searchCriteria) {

                List<Object> preparedStmtList = new ArrayList<>();
                List<Object> preparedStmtListDoc = new ArrayList<>();
                String casesQuery = "";
                casesQuery = queryBuilder.getCasesSearchQuery(caseCriteria, preparedStmtList);
                log.info("Final case query :: {}", casesQuery);
                List<CourtCase> list = jdbcTemplate.query(casesQuery, preparedStmtList.toArray(), rowMapper);
                if (list != null) {
                    caseCriteria.setResponseList(list);
                    log.info("Case list size :: {}",list.size());
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

                for (CourtCase courtCase : caseCriteria.getResponseList()) {
                    if (courtCase.getLinkedCases() != null) {
                        courtCase.getLinkedCases().forEach(linkedCase -> {
                            idsLinkedCases.add(linkedCase.getId().toString());
                        });
                    }
                }

                for (CourtCase courtCase : caseCriteria.getResponseList()) {
                    if (courtCase.getLitigants() != null) {
                        courtCase.getLitigants().forEach(litigant -> {
                            idsLitigant.add(litigant.getId().toString());
                        });
                    }
                }

                for (CourtCase courtCase : caseCriteria.getResponseList()) {
                    if (courtCase.getRepresentatives() != null) {
                        courtCase.getRepresentatives().forEach(rep -> {
                            idsRepresentative.add(rep.getId().toString());
                        });
                    }
                }

                for (CourtCase courtCase : caseCriteria.getResponseList()) {
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
                log.info("Final linked case query :: {}", linkedCaseQuery);
                Map<UUID, List<LinkedCase>> linkedCasesMap = jdbcTemplate.query(linkedCaseQuery, preparedStmtListDoc.toArray(), linkedCaseRowMapper);
                if (linkedCasesMap != null) {
                    caseCriteria.getResponseList().forEach(courtCase -> {
                        courtCase.setLinkedCases(linkedCasesMap.get(courtCase.getId()));
                    });
                }

                String litigantQuery = "";
                preparedStmtListDoc = new ArrayList<>();
                litigantQuery = queryBuilder.getLitigantSearchQuery(ids, preparedStmtListDoc);
                log.info("Final litigant query :: {}", litigantQuery);
                Map<UUID, List<Party>> litigantMap = jdbcTemplate.query(litigantQuery, preparedStmtListDoc.toArray(), litigantRowMapper);
                if (litigantMap != null) {
                    caseCriteria.getResponseList().forEach(courtCase -> {
                        courtCase.setLitigants(litigantMap.get(courtCase.getId()));
                    });
                }

                String statueAndSectionQuery = "";
                preparedStmtListDoc = new ArrayList<>();
                statueAndSectionQuery = queryBuilder.getStatuteSectionSearchQuery(ids, preparedStmtListDoc);
                log.info("Final statute and sections query :: {}", statueAndSectionQuery);
                Map<UUID, List<StatuteSection>> statuteSectionsMap = jdbcTemplate.query(statueAndSectionQuery, preparedStmtListDoc.toArray(), statuteSectionRowMapper);
                if (statuteSectionsMap != null) {
                    caseCriteria.getResponseList().forEach(courtCase -> {
                        courtCase.setStatutesAndSections(statuteSectionsMap.get(courtCase.getId()));
                    });
                }

                String representativeQuery = "";
                preparedStmtListDoc = new ArrayList<>();
                representativeQuery = queryBuilder.getRepresentativesSearchQuery(ids, preparedStmtListDoc);
                log.info("Final representative query :: {}", representativeQuery);
                Map<UUID, List<AdvocateMapping>> representativeMap = jdbcTemplate.query(representativeQuery, preparedStmtListDoc.toArray(), representativeRowMapper);
                if (representativeMap != null) {
                    caseCriteria.getResponseList().forEach(courtCase -> {
                        courtCase.setRepresentatives(representativeMap.get(courtCase.getId()));
                    });
                }

                String representingQuery = "";
                preparedStmtListDoc = new ArrayList<>();
                representingQuery = queryBuilder.getRepresentingSearchQuery(idsRepresentative, preparedStmtListDoc);
                log.info("Final representing query :: {}", representativeQuery);
                Map<UUID, List<Party>> representingMap = jdbcTemplate.query(representingQuery, preparedStmtListDoc.toArray(), representingRowMapper);
                if (representingMap != null) {
                    caseCriteria.getResponseList().forEach(courtCase -> {
                        if(courtCase.getRepresentatives()!=null){
                            courtCase.getRepresentatives().forEach(representative -> {
                                representative.setRepresenting(representingMap.get(UUID.fromString(representative.getId())));
                            });
                        }
                    });
                }

                String casesDocumentQuery = "";
                casesDocumentQuery = queryBuilder.getDocumentSearchQuery(ids, preparedStmtListDoc);
                log.info("Final document query :: {}", casesDocumentQuery);
                Map<UUID, List<Document>> caseDocumentMap = jdbcTemplate.query(casesDocumentQuery, preparedStmtListDoc.toArray(), caseDocumentRowMapper);
                if (caseDocumentMap != null) {
                    caseCriteria.getResponseList().forEach(courtCase -> {
                        courtCase.setDocuments(caseDocumentMap.get(courtCase.getId()));
                    });
                }

                casesDocumentQuery = "";
                preparedStmtListDoc = new ArrayList<>();
                casesDocumentQuery = queryBuilder.getLitigantDocumentSearchQuery(idsLitigant, preparedStmtListDoc);
                log.info("Final document query :: {}", casesDocumentQuery);
                Map<UUID, List<Document>> caseLitigantDocumentMap = jdbcTemplate.query(casesDocumentQuery, preparedStmtListDoc.toArray(), litigantDocumentRowMapper);
                if (caseLitigantDocumentMap != null) {
                    caseCriteria.getResponseList().forEach(courtCase -> {
                        if(courtCase.getLitigants()!=null){
                            courtCase.getLitigants().forEach(litigant -> {
                                litigant.setDocuments(caseLitigantDocumentMap.get(litigant.getId()));
                            });
                        }
                    });
                }

                casesDocumentQuery = "";
                preparedStmtListDoc = new ArrayList<>();
                casesDocumentQuery = queryBuilder.getLinkedCaseDocumentSearchQuery(idsLinkedCases, preparedStmtListDoc);
                log.info("Final document query :: {}", casesDocumentQuery);
                Map<UUID, List<Document>> caseLinkedCaseDocumentMap = jdbcTemplate.query(casesDocumentQuery, preparedStmtListDoc.toArray(), linkedCaseDocumentRowMapper);
                if (caseLinkedCaseDocumentMap != null) {
                    caseCriteria.getResponseList().forEach(courtCase -> {
                        if (courtCase.getLinkedCases() != null) {
                            courtCase.getLinkedCases().forEach(linkedCase -> {
                                linkedCase.setDocuments(caseLinkedCaseDocumentMap.get(linkedCase.getId()));
                            });
                        }
                    });
                }

                casesDocumentQuery = "";
                preparedStmtListDoc = new ArrayList<>();
                casesDocumentQuery = queryBuilder.getRepresentativeDocumentSearchQuery(idsRepresentative, preparedStmtListDoc);
                log.info("Final document query :: {}", casesDocumentQuery);
                Map<UUID, List<Document>> caseRepresentiveDocumentMap = jdbcTemplate.query(casesDocumentQuery, preparedStmtListDoc.toArray(), representativeDocumentRowMapper);
                if (caseRepresentiveDocumentMap != null) {
                    caseCriteria.getResponseList().forEach(courtCase -> {
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
                log.info("Final document query :: {}", casesDocumentQuery);
                Map<UUID, List<Document>> caseRepresentingDocumentMap = jdbcTemplate.query(casesDocumentQuery, preparedStmtListDoc.toArray(), representingDocumentRowMapper);
                if (caseRepresentingDocumentMap != null) {
                    caseCriteria.getResponseList().forEach(courtCase -> {
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
            }
            return searchCriteria;
        } catch(CustomException e){
            throw e;
        } catch (Exception e) {
            log.error("Error while fetching case application list :: {}", e.toString());
            throw new CustomException(SEARCH_CASE_ERR, "Exception while fetching case application list: " + e.getMessage());
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
        }
        catch(CustomException e){
            throw e;
        } catch (Exception e) {
            log.error("Error while checking case exist :: {}", e.toString());
            throw new CustomException(SEARCH_CASE_ERR, "Custom exception while checking case exist : " + e.getMessage());
        }
    }
}