package org.pucar.dristi.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.common.contract.models.Document;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.querybuilder.CaseQueryBuilder;
import org.pucar.dristi.repository.rowmapper.CaseRowMapper;
import org.pucar.dristi.repository.rowmapper.DocumentRowMapper;
import org.pucar.dristi.repository.rowmapper.LinkedCaseDocumentRowMapper;
import org.pucar.dristi.repository.rowmapper.LinkedCaseRowMapper;
import org.pucar.dristi.repository.rowmapper.LitigantDocumentRowMapper;
import org.pucar.dristi.repository.rowmapper.LitigantRowMapper;
import org.pucar.dristi.repository.rowmapper.RepresentativeRowMapper;
import org.pucar.dristi.repository.rowmapper.RepresentingDocumentRowMapper;
import org.pucar.dristi.repository.rowmapper.RepresentingRowMapper;
import org.pucar.dristi.repository.rowmapper.RepresentiveDocumentRowMapper;
import org.pucar.dristi.repository.rowmapper.StatuteSectionRowMapper;
import org.pucar.dristi.web.models.AdvocateMapping;
import org.pucar.dristi.web.models.CaseCriteria;
import org.pucar.dristi.web.models.CaseExists;
import org.pucar.dristi.web.models.CourtCase;
import org.pucar.dristi.web.models.LinkedCase;
import org.pucar.dristi.web.models.Party;
import org.pucar.dristi.web.models.StatuteSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

import static org.pucar.dristi.config.ServiceConstants.*;


@Slf4j
@Repository
public class CaseRepository {

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

    public List<CaseCriteria> getCases(List<CaseCriteria> searchCriteria, RequestInfo requestInfo) {

        try {

            for (CaseCriteria caseCriteria : searchCriteria) {

                List<Object> preparedStmtList = new ArrayList<>();
                List<Object> preparedStmtListDoc = new ArrayList<>();

                List<Integer> preparedStmtArgList = new ArrayList<>();

                String casesQuery = "";
                casesQuery = queryBuilder.getCasesSearchQuery(caseCriteria, preparedStmtList, preparedStmtArgList, requestInfo);
                casesQuery = queryBuilder.addOrderByQuery(casesQuery, caseCriteria.getPagination());
                log.info("Final case query :: {}", casesQuery);
                if (caseCriteria.getPagination() != null) {
                    Integer totalRecords = getTotalCount(casesQuery, preparedStmtList);
                    caseCriteria.getPagination().setTotalCount(Double.valueOf(totalRecords));
                    casesQuery = queryBuilder.addPaginationQuery(casesQuery, preparedStmtList, caseCriteria.getPagination(),preparedStmtArgList);
                }
                if(preparedStmtList.size()!=preparedStmtArgList.size()){
                    log.info("Arg size :: {}, and ArgType size :: {}", preparedStmtList.size(),preparedStmtArgList.size());
                    throw new CustomException(CASE_SEARCH_QUERY_EXCEPTION, "Arg and ArgType size mismatch ");
                }
                List<CourtCase> list = jdbcTemplate.query(casesQuery, preparedStmtList.toArray(), preparedStmtArgList.stream().mapToInt(Integer::intValue).toArray() ,rowMapper);
                if (list != null) {
                    caseCriteria.setResponseList(list);
                    log.info("Case list size :: {}", list.size());
                }

                if (caseCriteria.getDefaultFields() != null && caseCriteria.getDefaultFields()) {
                    continue;
                }

                List<String> ids = new ArrayList<>();

                for (CourtCase courtCase : caseCriteria.getResponseList()) {
                    ids.add(courtCase.getId().toString());
                }
                if (ids.isEmpty()) {
                    caseCriteria.setResponseList(new ArrayList<>());
                    continue;
                }
                enrichCaseCriteria(caseCriteria,ids,preparedStmtListDoc);
            }
            return searchCriteria;
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error while fetching case application list :: {}", e.toString());
            throw new CustomException(SEARCH_CASE_ERR, "Exception while fetching case application list: " + e.getMessage());
        }
    }

    private void enrichCaseCriteria(CaseCriteria caseCriteria, List<String> ids, List<Object> preparedStmtListDoc){
        List<String> idsLinkedCases = new ArrayList<>();
        List<String> idsLitigant = new ArrayList<>();
        List<String> idsRepresentative = new ArrayList<>();
        List<String> idsRepresenting = new ArrayList<>();

        setLinkedCases(caseCriteria, ids);

        extractLinkedCasesIds(caseCriteria, idsLinkedCases);

        setLitigants(caseCriteria, ids);

        extractLitigantIds(caseCriteria, idsLitigant);

        setRepresentatives(caseCriteria, ids);

        extractRepresentativeIds(caseCriteria, idsRepresentative);

        if (!idsRepresentative.isEmpty())
            setRepresenting(caseCriteria, idsRepresentative, preparedStmtListDoc);

        extractRepresentingIds(caseCriteria, idsRepresenting);

        setStatuteAndSections(caseCriteria, ids);

        setCaseDocuments(caseCriteria, ids);

        if (!idsLitigant.isEmpty())
            setLitigantDocuments(caseCriteria, idsLitigant);

        if (!idsLinkedCases.isEmpty())
            setLinkedCaseDocuments(caseCriteria, idsLinkedCases);

        if (!idsRepresentative.isEmpty())
            setRepresentativeDocuments(caseCriteria, idsRepresentative);

        if (!idsRepresenting.isEmpty())
            setRepresentingDocuments(caseCriteria, idsRepresenting);
    }

    private void setRepresentingDocuments(CaseCriteria caseCriteria, List<String> idsRepresenting) {
        String representingDocumentQuery;
        List<Object> preparedStmtListDoc;
        List<Integer> preparedStmtArgList = new ArrayList<>();

        preparedStmtListDoc = new ArrayList<>();
        representingDocumentQuery = queryBuilder.getRepresentingDocumentSearchQuery(idsRepresenting, preparedStmtListDoc, preparedStmtArgList);
        log.info("Final representing document query :: {}", representingDocumentQuery);
        Map<UUID, List<Document>> caseRepresentingDocumentMap = jdbcTemplate.query(representingDocumentQuery, preparedStmtListDoc.toArray(), preparedStmtArgList.stream().mapToInt(Integer::intValue).toArray(), representingDocumentRowMapper);

        List<CourtCase> responseList = caseCriteria.getResponseList();
        if(responseList.isEmpty())
            return;
        if (caseRepresentingDocumentMap != null) {
            responseList.forEach(courtCase -> {
                setRepresentingDoc(courtCase,caseRepresentingDocumentMap);
            });
        }
    }

    private void setRepresentingDoc(CourtCase courtCase, Map<UUID, List<Document>> caseRepresentingDocumentMap){
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
    }

    private void setRepresentativeDocuments(CaseCriteria caseCriteria, List<String> idsRepresentative) {
        String representativeDocumentQuery;
        List<Object> preparedStmtListDoc;
        List<Integer> preparedStmtArgList = new ArrayList<>();

        preparedStmtListDoc = new ArrayList<>();
        representativeDocumentQuery = queryBuilder.getRepresentativeDocumentSearchQuery(idsRepresentative, preparedStmtListDoc, preparedStmtArgList);
        log.info("Final representative document query :: {}", representativeDocumentQuery);
        Map<UUID, List<Document>> caseRepresentiveDocumentMap = jdbcTemplate.query(representativeDocumentQuery, preparedStmtListDoc.toArray(),preparedStmtArgList.stream().mapToInt(Integer::intValue).toArray(), representativeDocumentRowMapper);
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
        String linkedCaseDocumentQuery;
        List<Object> preparedStmtListDoc;
        List<Integer> preparedStmtArgList = new ArrayList<>();

        preparedStmtListDoc = new ArrayList<>();
        linkedCaseDocumentQuery = queryBuilder.getLinkedCaseDocumentSearchQuery(idsLinkedCases, preparedStmtListDoc, preparedStmtArgList);
        log.info("Final linked case document query :: {}", linkedCaseDocumentQuery);
        Map<UUID, List<Document>> caseLinkedCaseDocumentMap = jdbcTemplate.query(linkedCaseDocumentQuery, preparedStmtListDoc.toArray(), preparedStmtArgList.stream().mapToInt(Integer::intValue).toArray(),linkedCaseDocumentRowMapper);
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
        String litigantDocumentQuery;
        List<Object> preparedStmtListDoc;
        preparedStmtListDoc = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        litigantDocumentQuery = queryBuilder.getLitigantDocumentSearchQuery(idsLitigant, preparedStmtListDoc, preparedStmtArgList);
        log.info("Final litigant document query :: {}", litigantDocumentQuery);
        Map<UUID, List<Document>> caseLitigantDocumentMap = jdbcTemplate.query(litigantDocumentQuery, preparedStmtListDoc.toArray(), preparedStmtArgList.stream().mapToInt(Integer::intValue).toArray(),litigantDocumentRowMapper);
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
        List<Integer> preparedStmtArgList = new ArrayList<>();

        casesDocumentQuery = queryBuilder.getDocumentSearchQuery(ids, preparedStmtListDoc, preparedStmtArgList);
        log.info("Final case document query :: {}", casesDocumentQuery);
        Map<UUID, List<Document>> caseDocumentMap = jdbcTemplate.query(casesDocumentQuery, preparedStmtListDoc.toArray(), preparedStmtArgList.stream().mapToInt(Integer::intValue).toArray(), caseDocumentRowMapper);
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
        List<Integer> preparedStmtArgList = new ArrayList<>();

        representingQuery = queryBuilder.getRepresentingSearchQuery(idsRepresentative, preparedStmtListDoc, preparedStmtArgList);
        log.info("Final representing query :: {}", representingQuery);
        Map<UUID, List<Party>> representingMap = jdbcTemplate.query(representingQuery, preparedStmtListDoc.toArray(),preparedStmtArgList.stream().mapToInt(Integer::intValue).toArray(), representingRowMapper);
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
        List<Integer> preparedStmtArgList = new ArrayList<>();

        representativeQuery = queryBuilder.getRepresentativesSearchQuery(ids, preparedStmtListDoc, preparedStmtArgList);
        log.info("Final representative query :: {}", representativeQuery);
        Map<UUID, List<AdvocateMapping>> representativeMap = jdbcTemplate.query(representativeQuery, preparedStmtListDoc.toArray(),preparedStmtArgList.stream().mapToInt(Integer::intValue).toArray(), representativeRowMapper);
        if (representativeMap != null) {
            caseCriteria.getResponseList().forEach(courtCase -> courtCase.setRepresentatives(representativeMap.get(courtCase.getId())));
        }
    }

    private void setStatuteAndSections(CaseCriteria caseCriteria, List<String> ids) {
        List<Object> preparedStmtListDoc;
        String statueAndSectionQuery = "";
        preparedStmtListDoc = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        statueAndSectionQuery = queryBuilder.getStatuteSectionSearchQuery(ids, preparedStmtListDoc, preparedStmtArgList);
        log.info("Final statute and sections query :: {}", statueAndSectionQuery);
        Map<UUID, List<StatuteSection>> statuteSectionsMap = jdbcTemplate.query(statueAndSectionQuery, preparedStmtListDoc.toArray(), preparedStmtArgList.stream().mapToInt(Integer::intValue).toArray(),statuteSectionRowMapper);
        if (statuteSectionsMap != null) {
            caseCriteria.getResponseList().forEach(courtCase -> courtCase.setStatutesAndSections(statuteSectionsMap.get(courtCase.getId())));
        }
    }

    private void setLitigants(CaseCriteria caseCriteria, List<String> ids) {
        List<Object> preparedStmtListDoc;
        String litigantQuery = "";
        preparedStmtListDoc = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        litigantQuery = queryBuilder.getLitigantSearchQuery(ids, preparedStmtListDoc, preparedStmtArgList);
        log.info("Final litigant query :: {}", litigantQuery);
        Map<UUID, List<Party>> litigantMap = jdbcTemplate.query(litigantQuery, preparedStmtListDoc.toArray(), preparedStmtArgList.stream().mapToInt(Integer::intValue).toArray(), litigantRowMapper);
        if (litigantMap != null) {
            caseCriteria.getResponseList().forEach(courtCase -> courtCase.setLitigants(litigantMap.get(courtCase.getId())));
        }
    }

    private void setLinkedCases(CaseCriteria caseCriteria, List<String> ids) {
        List<Object> preparedStmtListDoc;
        String linkedCaseQuery = "";
        preparedStmtListDoc = new ArrayList<>();
        List<Integer> preparedStmtArgList = new ArrayList<>();

        linkedCaseQuery = queryBuilder.getLinkedCaseSearchQuery(ids, preparedStmtListDoc, preparedStmtArgList);
        log.info("Final linked case query :: {}", linkedCaseQuery);
        Map<UUID, List<LinkedCase>> linkedCasesMap = jdbcTemplate.query(linkedCaseQuery, preparedStmtListDoc.toArray(), preparedStmtArgList.stream().mapToInt(Integer::intValue).toArray(),linkedCaseRowMapper);
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
                    List<Object> preparedStmtList = new ArrayList<>();
                    List<Integer> preparedStmtListArgs = new ArrayList<>();
                    String casesExistQuery = queryBuilder.checkCaseExistQuery(caseExists, preparedStmtList, preparedStmtListArgs);
                    log.info("Final case exist query :: {}", casesExistQuery);
                    Integer count = jdbcTemplate.queryForObject(casesExistQuery, preparedStmtList.toArray(), preparedStmtListArgs.stream().mapToInt(Integer::intValue).toArray(), Integer.class);
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