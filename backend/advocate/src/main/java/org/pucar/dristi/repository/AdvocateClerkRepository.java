package org.pucar.dristi.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.querybuilder.AdvocateClerkQueryBuilder;
import org.pucar.dristi.repository.rowmapper.AdvocateClerkDocumentRowMapper;
import org.pucar.dristi.repository.rowmapper.AdvocateClerkRowMapper;
import org.pucar.dristi.web.models.AdvocateClerk;
import org.pucar.dristi.web.models.AdvocateClerkSearchCriteria;
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
public class AdvocateClerkRepository {

    private final AdvocateClerkQueryBuilder queryBuilder;
    private final JdbcTemplate jdbcTemplate;
    private final AdvocateClerkRowMapper rowMapper;
    private final AdvocateClerkDocumentRowMapper documentRowMapper;

    @Autowired
    public AdvocateClerkRepository(AdvocateClerkQueryBuilder queryBuilder, JdbcTemplate jdbcTemplate, AdvocateClerkRowMapper rowMapper, AdvocateClerkDocumentRowMapper documentRowMapper) {
        this.queryBuilder = queryBuilder;
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
        this.documentRowMapper = documentRowMapper;
    }

    public List<AdvocateClerk> getClerks(List<AdvocateClerkSearchCriteria> searchCriteria, String tenantId, Integer limit, Integer offset) {
        try {
            List<AdvocateClerk> advocateClerkList = new ArrayList<>();
            for (AdvocateClerkSearchCriteria advocateSearchCriteria : searchCriteria) {
                List<Object> preparedStmtList = new ArrayList<>();
                List<Integer> preparedStmtArgList = new ArrayList<>();
                String query = queryBuilder.getAdvocateClerkSearchQuery(advocateSearchCriteria, preparedStmtList, preparedStmtArgList,tenantId, limit, offset);
                log.info(FINAL_QUERY, query);
                if(preparedStmtList.size()!=preparedStmtArgList.size()){
                    log.info("Arg size :: {}, and ArgType size :: {}", preparedStmtList.size(),preparedStmtArgList.size());
                    throw new CustomException(ADVOCATE_CLERK_SEARCH_EXCEPTION, "Arg and ArgType size mismatch");
                }
                List<AdvocateClerk> processedList = jdbcTemplate.query(query, preparedStmtList.toArray(), preparedStmtArgList.stream().mapToInt(Integer::intValue).toArray(), rowMapper);
                if (processedList != null) {
                    advocateSearchCriteria.setResponseList(processedList);
                    fetchDocumentsForAdvocateClerks(processedList);
                }
                advocateClerkList.addAll(processedList);
            }
            return advocateClerkList;
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error(FETCH_ADVOCATE_CLERK_EXCEPTION, e.toString());
            throw new CustomException(ADVOCATE_CLERK_SEARCH_EXCEPTION, FETCH_ADVOCATE_CLERK_EXCEPTION + e.getMessage());
        }
    }

    public List<AdvocateClerk> getApplicationsByStatus(String status, String tenantId, Integer limit, Integer offset) {
        try {
            List<Object> preparedStmtList = new ArrayList<>();
            List<Integer> preparedStmtArgList = new ArrayList<>();
            String query = queryBuilder.getAdvocateClerkSearchQueryByStatus(status, preparedStmtList,preparedStmtArgList, tenantId, limit, offset);
            log.info(FINAL_QUERY, query);
            if(preparedStmtList.size()!=preparedStmtArgList.size()){
                log.info("Search by status Arg size :: {}, and ArgType size :: {}", preparedStmtList.size(),preparedStmtArgList.size());
                throw new CustomException(ADVOCATE_CLERK_SEARCH_EXCEPTION, "Search by status Arg and ArgType size mismatch");
            }
            List<AdvocateClerk> advocateClerkList = jdbcTemplate.query(query, preparedStmtList.toArray(),preparedStmtArgList.stream().mapToInt(Integer::intValue).toArray(), rowMapper);
            if (!advocateClerkList.isEmpty()) {
                fetchDocumentsForAdvocateClerks(advocateClerkList);
            }
            return advocateClerkList;
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error(FETCH_ADVOCATE_CLERK_EXCEPTION, e.toString());
            throw new CustomException(ADVOCATE_CLERK_SEARCH_EXCEPTION, FETCH_ADVOCATE_CLERK_EXCEPTION + e.getMessage());
        }
    }

    public List<AdvocateClerk> getApplicationsByAppNumber(String applicationNumber, String tenantId, Integer limit, Integer offset) {
        try {
            List<Object> preparedStmtList = new ArrayList<>();
            List<Integer> preparedStmtArgList = new ArrayList<>();
            String query = queryBuilder.getAdvocateClerkSearchQueryByAppNumber(applicationNumber, preparedStmtList, preparedStmtArgList,tenantId, limit, offset);
            log.info(FINAL_QUERY, query);
            if(preparedStmtList.size()!=preparedStmtArgList.size()){
                log.info("Search by App num Arg size :: {}, and ArgType size :: {}", preparedStmtList.size(),preparedStmtArgList.size());
                throw new CustomException(ADVOCATE_CLERK_SEARCH_EXCEPTION, "Search by App num Arg and ArgType size mismatch");
            }
            List<AdvocateClerk> advocateClerkList = jdbcTemplate.query(query, preparedStmtList.toArray(), preparedStmtArgList.stream().mapToInt(Integer::intValue).toArray(), rowMapper);
            if (!advocateClerkList.isEmpty()) {
                fetchDocumentsForAdvocateClerks(advocateClerkList);
            }
            return advocateClerkList;
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error(FETCH_ADVOCATE_CLERK_EXCEPTION, e.toString());
            throw new CustomException(ADVOCATE_CLERK_SEARCH_EXCEPTION, FETCH_ADVOCATE_CLERK_EXCEPTION + e.getMessage());
        }
    }

    private void fetchDocumentsForAdvocateClerks(List<AdvocateClerk> advocateClerkList) {
        List<String> ids = new ArrayList<>();
        for (AdvocateClerk advocate : advocateClerkList) {
            ids.add(advocate.getId().toString());
        }
        if (!ids.isEmpty()) {
            List<Object> preparedStmtListDoc = new ArrayList<>();
            List<Integer> preparedStmtArgListDoc = new ArrayList<>();
            String advocateDocumentQuery = queryBuilder.getDocumentSearchQuery(ids, preparedStmtListDoc,preparedStmtArgListDoc);
            log.info(FINAL_QUERY_DOCUMENT, advocateDocumentQuery);
            if(preparedStmtListDoc.size()!=preparedStmtArgListDoc.size()){
                log.info("Doc search Arg size :: {}, and ArgType size :: {}", preparedStmtListDoc.size(),preparedStmtArgListDoc.size());
                throw new CustomException(ADVOCATE_CLERK_SEARCH_EXCEPTION, "Doc search Arg and ArgType size mismatch");
            }
            Map<UUID, List<Document>> advocateDocumentMap = jdbcTemplate.query(advocateDocumentQuery, preparedStmtListDoc.toArray(), preparedStmtArgListDoc.stream().mapToInt(Integer::intValue).toArray(),documentRowMapper);
            if (advocateDocumentMap != null) {
                advocateClerkList.forEach(advocate -> advocate.setDocuments(advocateDocumentMap.get(advocate.getId())));
            }
        }
    }
}