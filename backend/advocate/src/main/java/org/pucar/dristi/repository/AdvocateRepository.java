package org.pucar.dristi.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.querybuilder.AdvocateQueryBuilder;
import org.pucar.dristi.repository.rowmapper.AdvocateDocumentRowMapper;
import org.pucar.dristi.repository.rowmapper.AdvocateRowMapper;
import org.pucar.dristi.web.models.Advocate;
import org.pucar.dristi.web.models.AdvocateSearchCriteria;
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
public class AdvocateRepository {

    private final AdvocateQueryBuilder queryBuilder;
    private final JdbcTemplate jdbcTemplate;
    private final AdvocateRowMapper rowMapper;
    private final AdvocateDocumentRowMapper advocateDocumentRowMapper;

    @Autowired
    public AdvocateRepository(AdvocateQueryBuilder queryBuilder, JdbcTemplate jdbcTemplate, AdvocateRowMapper rowMapper, AdvocateDocumentRowMapper advocateDocumentRowMapper) {
        this.queryBuilder = queryBuilder;
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
        this.advocateDocumentRowMapper = advocateDocumentRowMapper;
    }

    public List<AdvocateSearchCriteria> getApplications(List<AdvocateSearchCriteria> searchCriteria, String tenantId, Integer limit, Integer offset ) {

        try {
            for (AdvocateSearchCriteria advocateSearchCriteria : searchCriteria) {
                List<Advocate> list = performAdvocateQuery(advocateSearchCriteria, tenantId, limit, offset);
                if (list != null) {
                    advocateSearchCriteria.setResponseList(list);
                    fetchDocumentsForAdvocates(list);
                }
            }
            return searchCriteria;
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error(FETCH_ADVOCATE_EXCEPTION, e.toString());
            throw new CustomException(ADVOCATE_SEARCH_EXCEPTION, FETCH_ADVOCATE_EXCEPTION + e.getMessage());
        }
    }

    public List<Advocate> getListApplicationsByStatus(String status, String tenantId, Integer limit, Integer offset ) {

        try {
            List<Advocate> advocateList = performAdvocateQueryByStatus(status, tenantId, limit, offset);
            if (!advocateList.isEmpty()) {
                fetchDocumentsForAdvocates(advocateList);
            }
            return advocateList;
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error(FETCH_ADVOCATE_EXCEPTION, e.toString());
            throw new CustomException(ADVOCATE_SEARCH_EXCEPTION, FETCH_ADVOCATE_EXCEPTION + e.getMessage());
        }
    }

    public List<Advocate> getListApplicationsByApplicationNumber(String applicationNumber, String tenantId, Integer limit, Integer offset ) {

        try {
            List<Advocate> advocateList = performAdvocateQueryByApplicationNumber(applicationNumber, tenantId, limit, offset);
            if (!advocateList.isEmpty()) {
                fetchDocumentsForAdvocates(advocateList);
            }
            return advocateList;
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error(FETCH_ADVOCATE_EXCEPTION, e.toString());
            throw new CustomException(ADVOCATE_SEARCH_EXCEPTION, FETCH_ADVOCATE_EXCEPTION + e.getMessage());
        }
    }

    private List<Advocate> performAdvocateQuery(AdvocateSearchCriteria criteria, String tenantId, Integer limit, Integer offset) {
        List<Object> preparedStmtList = new ArrayList<>();
        String advocateQuery = queryBuilder.getAdvocateSearchQuery(criteria, preparedStmtList, tenantId, limit, offset);
        log.info(ADVOCATE_LIST_QUERY, advocateQuery);
        return jdbcTemplate.query(advocateQuery, preparedStmtList.toArray(), rowMapper);
    }

    private List<Advocate> performAdvocateQueryByStatus(String status, String tenantId, Integer limit, Integer offset) {
        List<Object> preparedStmtList = new ArrayList<>();
        String advocateQuery = queryBuilder.getAdvocateSearchQueryByStatus(status, preparedStmtList, tenantId, limit, offset);
        log.info(ADVOCATE_LIST_QUERY, advocateQuery);
        return jdbcTemplate.query(advocateQuery, preparedStmtList.toArray(), rowMapper);
    }

    private List<Advocate> performAdvocateQueryByApplicationNumber(String applicationNumber, String tenantId, Integer limit, Integer offset) {
        List<Object> preparedStmtList = new ArrayList<>();
        String advocateQuery = queryBuilder.getAdvocateSearchQueryByApplicationNumber(applicationNumber, preparedStmtList, tenantId, limit, offset);
        log.info(ADVOCATE_LIST_QUERY, advocateQuery);
        return jdbcTemplate.query(advocateQuery, preparedStmtList.toArray(), rowMapper);
    }

    private void fetchDocumentsForAdvocates(List<Advocate> advocates) {
        List<String> ids = new ArrayList<>();
        for (Advocate advocate : advocates) {
            ids.add(advocate.getId().toString());
        }
        if (!ids.isEmpty()) {
            List<Object> preparedStmtListDoc = new ArrayList<>();
            String advocateDocumentQuery = queryBuilder.getDocumentSearchQuery(ids, preparedStmtListDoc);
            log.info(DOCUMENT_LIST_QUERY, advocateDocumentQuery);
            Map<UUID, List<Document>> advocateDocumentMap = jdbcTemplate.query(advocateDocumentQuery, preparedStmtListDoc.toArray(), advocateDocumentRowMapper);
            if (advocateDocumentMap != null) {
                advocates.forEach(advocate -> advocate.setDocuments(advocateDocumentMap.get(advocate.getId())));
            }
        }
    }
}
